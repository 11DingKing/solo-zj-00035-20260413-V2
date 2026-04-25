/** 
 * FRONTEND CRUD INTERFACE STRUCTURE
 * 
 * Files created for the Product Manager CRUD interface:
 * 
 * 1. Models
 *    └── src/app/models/product.model.ts
 *        - Defines the Product interface with: id, name, description, price, quantity
 * 
 * 2. Services
 *    └── src/app/services/product.service.ts
 *        - HTTP service for API communication
 *        - Methods: getProducts(), getProductById(), createProduct(), updateProduct(), deleteProduct()
 *        - Base URL: http://localhost:8080/api/products
 * 
 * 3. Components
 *    └── src/app/components/product-list.component.ts
 *        - Main component using Angular Signals for reactive state management
 *        - Signals used: products, loading, error, selectedProduct, showForm, formData
 *        - Features:
 *          * Load and display products in a table
 *          * Create new products via modal form
 *          * Edit existing products
 *          * Delete products with confirmation
 *          * Validation of form inputs
 *    
 *    └── src/app/components/product-list.component.html
 *        - Responsive table view of products
 *        - Modal form for create/edit operations
 *        - Error messages and loading states
 *        - CRUD action buttons
 * 
 *    └── src/app/components/product-list.component.scss
 *        - Complete styling for the interface
 *        - Mobile responsive design
 *        - Professional UI with hover effects and animations
 * 
 * 4. Updated App Files
 *    ├── src/app/app.ts - Updated to use ProductListComponent
 *    ├── src/app/app.config.ts - Added provideHttpClient() for HTTP support
 *    └── src/app/app.html - Simplified to use <app-product-list></app-product-list>
 * 
 * FEATURES:
 * - Signal-based reactive state management (Angular 21+)
 * - Full CRUD operations with API integration
 * - Modal form for adding/editing products
 * - Responsive design (desktop and mobile)
 * - Error handling and loading states
 * - Input validation
 * - Confirmation dialog for deletions
 * - Professional UI with smooth animations
 * 
 * USAGE:
 * The interface is ready to use. Make sure your backend API is running on http://localhost:8080
 * and has the /api/products endpoints implemented.
 */
