import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../services/product.service';
import { CategoryService } from '../services/category.service';
import { Product } from '../models/product.model';
import { Category, CategoryStatistics } from '../models/category.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent implements OnInit {
  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  categoriesFlat = signal<Category[]>([]);
  categoryStatistics = signal<CategoryStatistics[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  selectedCategoryId = signal<number | null>(null);
  showCategoryDropdown = signal(false);
  showCategoryManagement = signal(false);

  selectedProduct = signal<Product | null>(null);
  showForm = signal(false);

  formData = signal<Product>({
    name: '',
    description: '',
    price: 0,
    quantity: 0,
  });

  categoryFormData = signal<Category>({
    name: '',
    parentId: undefined,
  });
  editingCategory = signal<Category | null>(null);

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
  ) {}

  ngOnInit() {
    this.loadCategories();
    this.loadProducts();
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories.set(data);
      },
      error: (err) => {
        console.error('Error loading categories', err);
      },
    });

    this.categoryService.getCategoriesFlat().subscribe({
      next: (data) => {
        this.categoriesFlat.set(data);
      },
      error: (err) => {
        console.error('Error loading flat categories', err);
      },
    });

    this.loadCategoryStatistics();
  }

  loadCategoryStatistics() {
    this.categoryService.getCategoryStatistics().subscribe({
      next: (data) => {
        this.categoryStatistics.set(data);
      },
      error: (err) => {
        console.error('Error loading category statistics', err);
      },
    });
  }

  loadProducts() {
    this.loading.set(true);
    this.error.set(null);
    this.productService.getProducts(this.selectedCategoryId() ?? undefined).subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error loading products');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  selectCategory(categoryId: number | null) {
    this.selectedCategoryId.set(categoryId);
    this.showCategoryDropdown.set(false);
    this.loadProducts();
  }

  getCategoryName(categoryId?: number): string {
    if (!categoryId) return '';
    const cat = this.categoriesFlat().find((c) => c.id === categoryId);
    return cat ? cat.name : '';
  }

  getProductCountForCategory(categoryId: number): number {
    const stat = this.categoryStatistics().find((s) => s.categoryId === categoryId);
    return stat ? stat.productCount : 0;
  }

  openForm(product?: Product) {
    if (product) {
      this.selectedProduct.set(product);
      this.formData.set({ ...product });
    } else {
      this.selectedProduct.set(null);
      this.formData.set({
        name: '',
        description: '',
        price: 0,
        quantity: 0,
      });
    }
    this.showForm.set(true);
  }

  closeForm() {
    this.showForm.set(false);
    this.selectedProduct.set(null);
  }

  saveProduct() {
    const data = this.formData();

    if (!data.name || data.price < 0 || data.quantity < 0) {
      this.error.set('Please fill in all fields correctly');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    if (this.selectedProduct()?.id) {
      this.productService.updateProduct(this.selectedProduct()!.id!, data).subscribe({
        next: () => {
          this.loadProducts();
          this.loadCategoryStatistics();
          this.closeForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Error updating product');
          this.loading.set(false);
          console.error(err);
        },
      });
    } else {
      this.productService.createProduct(data).subscribe({
        next: () => {
          this.loadProducts();
          this.loadCategoryStatistics();
          this.closeForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Error creating product');
          this.loading.set(false);
          console.error(err);
        },
      });
    }
  }

  deleteProduct(id?: number) {
    if (!id) return;

    if (confirm('Are you sure you want to delete this product?')) {
      this.loading.set(true);
      this.error.set(null);
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts();
          this.loadCategoryStatistics();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Error deleting product');
          this.loading.set(false);
          console.error(err);
        },
      });
    }
  }

  updateFormData(field: keyof Product, value: any) {
    const current = this.formData();
    this.formData.set({
      ...current,
      [field]: field === 'price' || field === 'quantity' ? parseFloat(value) : value,
    });
  }

  updateFormDataCategoryId(value: string) {
    const current = this.formData();
    this.formData.set({
      ...current,
      categoryId: value ? +value : undefined,
    });
  }

  toggleCategoryManagement() {
    this.showCategoryManagement.set(!this.showCategoryManagement());
    if (this.showCategoryManagement()) {
      this.resetCategoryForm();
    }
  }

  resetCategoryForm() {
    this.categoryFormData.set({
      name: '',
      parentId: undefined,
    });
    this.editingCategory.set(null);
  }

  openCategoryEdit(category: Category) {
    this.editingCategory.set(category);
    this.categoryFormData.set({
      name: category.name,
      parentId: category.parentId,
    });
  }

  saveCategory() {
    const data = this.categoryFormData();
    if (!data.name) {
      this.error.set('Category name is required');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    if (this.editingCategory()?.id) {
      this.categoryService.updateCategory(this.editingCategory()!.id!, data).subscribe({
        next: () => {
          this.loadCategories();
          this.resetCategoryForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Error updating category');
          this.loading.set(false);
          console.error(err);
        },
      });
    } else {
      this.categoryService.createCategory(data).subscribe({
        next: () => {
          this.loadCategories();
          this.resetCategoryForm();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Error creating category. Maximum 2 levels allowed.');
          this.loading.set(false);
          console.error(err);
        },
      });
    }
  }

  deleteCategory(id?: number) {
    if (!id) return;

    if (confirm('Are you sure you want to delete this category?')) {
      this.loading.set(true);
      this.error.set(null);
      this.categoryService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories();
          this.loadProducts();
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set('Cannot delete category: it has products or child categories');
          this.loading.set(false);
          console.error(err);
        },
      });
    }
  }

  getParentCategories(): Category[] {
    return this.categoriesFlat().filter((c) => !c.parentId);
  }

  isChildCategory(category: Category): boolean {
    return !!category.parentId;
  }

  updateCategoryFormName(name: string) {
    const current = this.categoryFormData();
    this.categoryFormData.set({
      ...current,
      name: name,
    });
  }

  updateCategoryFormParentId(value: string) {
    const current = this.categoryFormData();
    this.categoryFormData.set({
      ...current,
      parentId: value ? +value : undefined,
    });
  }
}
