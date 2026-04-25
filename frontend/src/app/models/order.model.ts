import { OrderItem } from './order-item.model';

export interface Order {
  id?: number;
  orderNumber: string;
  username: string;
  orderItems: OrderItem[];
  totalAmount: number;
  status: OrderStatus;
  createdAt: string;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  CANCELLED = 'CANCELLED',
  COMPLETED = 'COMPLETED'
}

export interface OrderPage {
  content: Order[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface CreateOrderRequest {
  username: string;
  items: OrderItemRequest[];
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export const OrderStatusLabels: Record<OrderStatus, string> = {
  [OrderStatus.PENDING]: '待支付',
  [OrderStatus.PAID]: '已支付',
  [OrderStatus.CANCELLED]: '已取消',
  [OrderStatus.COMPLETED]: '已完成'
};

export const OrderStatusColors: Record<OrderStatus, string> = {
  [OrderStatus.PENDING]: '#ffc107',
  [OrderStatus.PAID]: '#28a745',
  [OrderStatus.CANCELLED]: '#dc3545',
  [OrderStatus.COMPLETED]: '#17a2b8'
};
