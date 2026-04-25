import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProductService } from '../services/product.service';
import { OrderService } from '../services/order.service';
import { Product } from '../models/product.model';
import { CreateOrderRequest, OrderItemRequest } from '../models/order.model';

@Component({
  selector: 'app-create-order',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './create-order.component.html',
  styleUrl: './create-order.component.scss'
})
export class CreateOrderComponent implements OnInit {
  products = signal<Product[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  
  username = signal('');
  selectedItems = signal<Map<number, { product: Product; quantity: number }>>(new Map());

  totalAmount = computed(() => {
    let total = 0;
    this.selectedItems().forEach((item) => {
      total += item.product.price * item.quantity;
    });
    return total;
  });

  constructor(
    private productService: ProductService,
    private orderService: OrderService
  ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading.set(true);
    this.error.set(null);
    this.productService.getProducts().subscribe({
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

  addToOrder(product: Product) {
    if (product.quantity <= 0) {
      return;
    }
    
    const currentItems = this.selectedItems();
    const newItems = new Map(currentItems);
    
    if (newItems.has(product.id!)) {
      const existing = newItems.get(product.id!)!;
      if (existing.quantity < product.quantity) {
        newItems.set(product.id!, { ...existing, quantity: existing.quantity + 1 });
      }
    } else {
      newItems.set(product.id!, { product, quantity: 1 });
    }
    
    this.selectedItems.set(newItems);
  }

  removeFromOrder(productId: number) {
    const currentItems = this.selectedItems();
    const newItems = new Map(currentItems);
    newItems.delete(productId);
    this.selectedItems.set(newItems);
  }

  updateQuantity(productId: number, quantity: number) {
    const currentItems = this.selectedItems();
    const item = currentItems.get(productId);
    
    if (!item) return;
    
    const newQuantity = Math.max(1, Math.min(quantity, item.product.quantity));
    const newItems = new Map(currentItems);
    newItems.set(productId, { ...item, quantity: newQuantity });
    this.selectedItems.set(newItems);
  }

  createOrder() {
    if (!this.username().trim()) {
      this.error.set('请输入用户名');
      return;
    }
    
    if (this.selectedItems().size === 0) {
      this.error.set('请至少选择一个商品');
      return;
    }
    
    const items: OrderItemRequest[] = [];
    this.selectedItems().forEach((item) => {
      items.push({
        productId: item.product.id!,
        quantity: item.quantity
      });
    });
    
    const request: CreateOrderRequest = {
      username: this.username().trim(),
      items: items
    };
    
    this.loading.set(true);
    this.error.set(null);
    this.successMessage.set(null);
    
    this.orderService.createOrder(request).subscribe({
      next: (order) => {
        this.successMessage.set(`订单创建成功！订单号: ${order.orderNumber}`);
        this.username.set('');
        this.selectedItems.set(new Map());
        this.loadProducts();
        this.loading.set(false);
      },
      error: (err) => {
        const errorMessage = err.error?.message || '创建订单失败';
        this.error.set(errorMessage);
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  getSelectedItemsArray() {
    return Array.from(this.selectedItems().values());
  }
}
