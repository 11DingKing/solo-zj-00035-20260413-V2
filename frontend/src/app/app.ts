import { Component } from '@angular/core';
import { ProductListComponent } from './components/product-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ProductListComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App { }
