import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
    IonContent, IonHeader, IonTitle, IonToolbar, IonList, IonItem,
    IonLabel, IonButton, IonIcon, IonFab, IonFabButton, IonSpinner,
    IonButtons, IonBackButton, AlertController, ToastController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { add, remove, create, trash } from 'ionicons/icons';
import { ProductService } from '../../../core/services/product.service';
import { Product } from '../../../core/models/product.model';

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.scss'],
    imports: [
        CommonModule, IonContent, IonHeader, IonTitle, IonToolbar, IonList,
        IonItem, IonLabel, IonButton, IonIcon, IonFab, IonFabButton, IonSpinner,
        IonButtons, IonBackButton
    ]
})
export class ProductListComponent implements OnInit {
    products: Product[] = [];
    loading: boolean = true;

    constructor(
        private productService: ProductService,
        private router: Router,
        private alertController: AlertController,
        private toastController: ToastController
    ) {
        addIcons({ add, remove, create, trash });
    }

    ngOnInit() {
        this.loadProducts();
    }

    loadProducts() {
        this.productService.getProducts().subscribe({
            next: (products: Product[]) => {
                this.products = products;
                this.loading = false;
            },
            error: (error: any) => {
                console.error('Error loading products:', error);
                this.loading = false;
            }
        });
    }

    async incrementQuantity(product: Product) {
        try {
            await this.productService.incrementQuantity(product);
            await this.showToast('Cantidad incrementada');
        } catch (error: any) {
            await this.showToast('Error al actualizar cantidad', 'danger');
        }
    }

    async decrementQuantity(product: Product) {
        try {
            await this.productService.decrementQuantity(product);
            await this.showToast('Cantidad decrementada');
        } catch (error: any) {
            await this.showToast('Error al actualizar cantidad', 'danger');
        }
    }

    editProduct(product: Product) {
        this.router.navigate(['/products/edit', product.id]);
    }

    async deleteProduct(product: Product) {
        const alert = await this.alertController.create({
            header: 'Confirmar Eliminación',
            message: `¿Estás seguro de que deseas eliminar "${product.name}"?`,
            buttons: [
                {
                    text: 'Cancelar',
                    role: 'cancel'
                },
                {
                    text: 'Eliminar',
                    role: 'destructive',
                    handler: async () => {
                        try {
                            if (product.id) {
                                await this.productService.deleteProduct(product.id);
                                await this.showToast('Producto eliminado exitosamente', 'success');
                            }
                        } catch (error: any) {
                            await this.showToast('Error al eliminar producto', 'danger');
                        }
                    }
                }
            ]
        });

        await alert.present();
    }

    goToAddProduct() {
        this.router.navigate(['/products/add']);
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
