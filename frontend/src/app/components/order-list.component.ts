import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { OrderService } from '../services/order.service';
import { Order, OrderStatus, OrderStatusLabels, OrderStatusColors, OrderPage } from '../models/order.model';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.scss'
})
export class OrderListComponent implements OnInit {
  orders = signal<Order[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  selectedStatus = signal<OrderStatus | null>(null);
  currentPage = signal(0);
  pageSize = signal(10);
  totalPages = signal(0);
  totalElements = signal(0);
  showDetailModal = signal(false);
  selectedOrder = signal<Order | null>(null);

  readonly OrderStatus = OrderStatus;
  readonly statuses = [null, OrderStatus.PENDING, OrderStatus.PAID, OrderStatus.CANCELLED, OrderStatus.COMPLETED];
  readonly statusLabels = OrderStatusLabels;
  readonly statusColors = OrderStatusColors;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.loading.set(true);
    this.error.set(null);
    this.orderService.getOrders(this.selectedStatus() ?? undefined, this.currentPage(), this.pageSize()).subscribe({
      next: (data: OrderPage) => {
        this.orders.set(data.content);
        this.totalPages.set(data.totalPages);
        this.totalElements.set(data.totalElements);
        this.currentPage.set(data.number);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Error loading orders');
        this.loading.set(false);
        console.error(err);
      },
    });
  }

  selectStatus(status: OrderStatus | null) {
    this.selectedStatus.set(status);
    this.currentPage.set(0);
    this.loadOrders();
  }

  getStatusLabel(status: OrderStatus): string {
    return OrderStatusLabels[status];
  }

  getStatusColor(status: OrderStatus): string {
    return OrderStatusColors[status];
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  openDetail(order: Order) {
    this.selectedOrder.set(order);
    this.showDetailModal.set(true);
  }

  closeDetail() {
    this.showDetailModal.set(false);
    this.selectedOrder.set(null);
  }

  cancelOrder(order: Order) {
    if (order.status !== OrderStatus.PENDING) {
      alert('只有待支付的订单可以取消');
      return;
    }
    
    if (confirm('确定要取消这个订单吗？')) {
      this.loading.set(true);
      this.orderService.cancelOrder(order.id!).subscribe({
        next: () => {
          this.loadOrders();
        },
        error: (err) => {
          this.error.set('取消订单失败');
          this.loading.set(false);
          console.error(err);
        },
      });
    }
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.loadOrders();
    }
  }
}
