import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {
    IonContent, IonHeader, IonTitle, IonToolbar, IonItem, IonLabel,
    IonInput, IonSelect, IonSelectOption, IonButton, IonButtons,
    IonBackButton, IonSpinner, ToastController
} from '@ionic/angular/standalone';
import { ProductService } from '../../../core/services/product.service';
import { CategoryService } from '../../../core/services/category.service';
import { Product } from '../../../core/models/product.model';
import { Category } from '../../../core/models/category.model';

@Component({
    selector: 'app-product-edit',
    templateUrl: './product-edit.component.html',
    styleUrls: ['./product-edit.component.scss'],
    imports: [
        CommonModule, FormsModule, IonContent, IonHeader, IonTitle, IonToolbar,
        IonItem, IonLabel, IonInput, IonSelect, IonSelectOption, IonButton,
        IonButtons, IonBackButton, IonSpinner
    ]
})
export class ProductEditComponent implements OnInit {
    productId: string = '';
    product: Product = {
        name: '',
        category: '',
        quantity: 0,
        price: 0,
        barcode: ''
    };
    categories: Category[] = [];
    loading: boolean = false;
    loadingData: boolean = true;

    constructor(
        private productService: ProductService,
        private categoryService: CategoryService,
        private router: Router,
        private route: ActivatedRoute,
        private toastController: ToastController
    ) { }

    ngOnInit() {
        this.productId = this.route.snapshot.paramMap.get('id') || '';
        this.loadCategories();
        this.loadProduct();
    }

    loadCategories() {
        this.categoryService.getCategories().subscribe({
            next: (categories: Category[]) => {
                this.categories = categories;
            },
            error: (error: any) => {
                console.error('Error loading categories:', error);
            }
        });
    }

    loadProduct() {
        if (this.productId) {
            this.productService.getProduct(this.productId).subscribe({
                next: (product: Product) => {
                    this.product = product;
                    this.loadingData = false;
                },
                error: (error: any) => {
                    console.error('Error loading product:', error);
                    this.loadingData = false;
                }
            });
        }
    }

    async onSubmit() {
        if (!this.product.name || !this.product.category || this.product.quantity < 0 || this.product.price < 0) {
            await this.showToast('Por favor, completa todos los campos obligatorios', 'warning');
            return;
        }

        this.loading = true;

        try {
            await this.productService.updateProduct(this.productId, this.product);
            await this.showToast('Producto actualizado exitosamente', 'success');
            this.router.navigate(['/products']);
        } catch (error: any) {
            await this.showToast('Error al actualizar producto', 'danger');
        } finally {
            this.loading = false;
        }
    }

    cancel() {
        this.router.navigate(['/products']);
    }

    async showToast(message: string, color: string = 'success') {
        const toast = await this.toastController.create({
            message,
            duration: 2000,
            color,
            position: 'bottom'
        });
        await toast.present();
    }
}
