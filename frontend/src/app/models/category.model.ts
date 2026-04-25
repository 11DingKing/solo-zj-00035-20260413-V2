export interface Category {
    id?: number;
    name: string;
    parentId?: number;
    parentName?: string;
    children?: Category[];
    productCount?: number;
}

export interface CategoryStatistics {
    categoryId: number;
    categoryName: string;
    productCount: number;
}
