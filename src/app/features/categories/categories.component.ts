import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
    IonContent, IonHeader, IonTitle, IonToolbar, IonList, IonItem,
    IonLabel, IonButton, IonIcon, IonButtons, IonBackButton,
    IonSpinner, AlertController, ToastController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { add, create, trash, folder } from 'ionicons/icons';
import { CategoryService } from '../../core/services/category.service';
import { Category } from '../../core/models/category.model';

@Component({
    selector: 'app-categories',
    templateUrl: './categories.component.html',
    styleUrls: ['./categories.component.scss'],
    imports: [
        CommonModule, IonContent, IonHeader, IonTitle, IonToolbar, IonList,
        IonItem, IonLabel, IonButton, IonIcon, IonButtons, IonBackButton, IonSpinner
    ]
})
export class CategoriesComponent implements OnInit {
    categories: Category[] = [];
    loading: boolean = true;

    constructor(
        private categoryService: CategoryService,
        private router: Router,
        private alertController: AlertController,
        private toastController: ToastController
    ) {
        addIcons({ add, create, trash, folder });
    }

    ngOnInit() {
        this.loadCategories();
    }

    loadCategories() {
        this.categoryService.getCategories().subscribe({
            next: (categories: Category[]) => {
                this.categories = categories;
                this.loading = false;
            },
            error: (error: any) => {
                console.error('Error loading categories:', error);
                this.loading = false;
            }
        });
    }

    async addCategory() {
        const alert = await this.alertController.create({
            header: 'Nueva Categoría',
            inputs: [
                {
                    name: 'name',
                    type: 'text',
                    placeholder: 'Nombre de la categoría'
                }
            ],
            buttons: [
                {
                    text: 'Cancelar',
                    role: 'cancel'
                },
                {
                    text: 'Añadir',
                    handler: async (data: any) => {
                        if (data.name && data.name.trim()) {
                            try {
                                await this.categoryService.addCategory({ name: data.name.trim() });
                                await this.showToast('Categoría añadida exitosamente', 'success');
                            } catch (error: any) {
                                await this.showToast('Error al añadir categoría', 'danger');
                            }
                        } else {
                            await this.showToast('El nombre no puede estar vacío', 'warning');
                            return false;
                        }
                        return true;
                    }
                }
            ]
        });

        await alert.present();
    }

    async editCategory(category: Category) {
        const alert = await this.alertController.create({
            header: 'Editar Categoría',
            inputs: [
                {
                    name: 'name',
                    type: 'text',
                    value: category.name,
                    placeholder: 'Nombre de la categoría'
                }
            ],
            buttons: [
                {
                    text: 'Cancelar',
                    role: 'cancel'
                },
                {
                    text: 'Guardar',
                    handler: async (data: any) => {
                        if (data.name && data.name.trim() && category.id) {
                            try {
                                await this.categoryService.updateCategory(category.id, { name: data.name.trim() });
                                await this.showToast('Categoría actualizada exitosamente', 'success');
                            } catch (error: any) {
                                await this.showToast('Error al actualizar categoría', 'danger');
                            }
                        } else {
                            await this.showToast('El nombre no puede estar vacío', 'warning');
                            return false;
                        }
                        return true;
                    }
                }
            ]
        });

        await alert.present();
    }

    async deleteCategory(category: Category) {
        const alert = await this.alertController.create({
            header: 'Confirmar Eliminación',
            message: `¿Estás seguro de que deseas eliminar la categoría "${category.name}"?`,
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
                            if (category.id) {
                                await this.categoryService.deleteCategory(category.id);
                                await this.showToast('Categoría eliminada exitosamente', 'success');
                            }
                        } catch (error: any) {
                            await this.showToast('Error al eliminar categoría', 'danger');
                        }
                    }
                }
            ]
        });

        await alert.present();
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
