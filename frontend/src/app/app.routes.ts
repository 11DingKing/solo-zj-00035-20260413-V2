import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list.component';
import { OrderListComponent } from './components/order-list.component';
import { CreateOrderComponent } from './components/create-order.component';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'orders', component: OrderListComponent },
  { path: 'create-order', component: CreateOrderComponent },
];
