import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
    IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader,
    IonCardTitle, IonCardContent, IonList, IonItem, IonLabel, IonBadge,
    IonSearchbar, IonFab, IonFabButton, IonIcon, IonButtons, IonButton,
    IonMenuButton, IonGrid, IonRow, IonCol, IonSpinner
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { add, search, personCircle, cube, layers, folder } from 'ionicons/icons';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../core/models/product.model';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [
        CommonModule, FormsModule, IonContent, IonHeader, IonTitle, IonToolbar,
        IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonList, IonItem,
        IonLabel, IonBadge, IonSearchbar, IonFab, IonFabButton, IonIcon,
        IonButtons, IonButton, IonMenuButton, IonGrid, IonRow, IonCol, IonSpinner
    ]
})
export class DashboardComponent implements OnInit {
    @ViewChild('pieChartCanvas') pieChartCanvas!: ElementRef;

    products: Product[] = [];
    filteredProducts: Product[] = [];
    totalProducts: number = 0;
    totalUnits: number = 0;
    searchTerm: string = '';
    loading: boolean = true;
    pieChart: Chart | null = null;

    constructor(
        public productService: ProductService,
        private router: Router
    ) {
        addIcons({ add, search, personCircle, cube, layers, folder });
    }

    ngOnInit() {
        this.loadProducts();
    }

    loadProducts() {
        this.productService.getProducts().subscribe({
            next: (products: Product[]) => {
                this.products = products;
                this.filteredProducts = products;
                this.updateStatistics();
                this.updatePieChart();
                this.loading = false;
            },
            error: (error: any) => {
                console.error('Error loading products:', error);
                this.loading = false;
            }
        });
    }

    updateStatistics() {
        this.totalProducts = this.products.length;
        this.totalUnits = this.products.reduce((sum, product) => sum + product.quantity, 0);
    }

    updatePieChart() {
        // Esperar a que el canvas esté disponible
        setTimeout(() => {
            if (!this.pieChartCanvas) return;

            // Destruir gráfico anterior si existe
            if (this.pieChart) {
                this.pieChart.destroy();
            }

            // Agrupar productos por categoría
            const categoryMap = new Map<string, number>();
            this.products.forEach(product => {
                const category = product.category || 'Sin Categoría';
                categoryMap.set(category, (categoryMap.get(category) || 0) + 1);
            });

            const labels = Array.from(categoryMap.keys());
            const data = Array.from(categoryMap.values());
            const colors = this.generateColors(labels.length);

            const config: ChartConfiguration = {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        backgroundColor: colors,
                        borderWidth: 2,
                        borderColor: '#ffffff'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                padding: 15,
                                font: {
                                    size: 12
                                }
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: (context: any) => {
                                    const label = context.label || '';
                                    const value = context.parsed || 0;
                                    const total = (context.dataset.data as number[]).reduce((a, b) => a + b, 0);
                                    const percentage = ((value / total) * 100).toFixed(1);
                                    return `${label}: ${value} (${percentage}%)`;
                                }
                            }
                        }
                    }
                }
            };

            this.pieChart = new Chart(this.pieChartCanvas.nativeElement, config);
        }, 100);
    }

    generateColors(count: number): string[] {
        const colors = [
            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
            '#FF9F40', '#FF6384', '#C9CBCF', '#4BC0C0', '#FF6384'
        ];
        return colors.slice(0, count);
    }

    filterProducts(event: any) {
        const searchTerm = event.target.value?.toLowerCase() || '';

        if (!searchTerm) {
            this.filteredProducts = this.products;
        } else {
            this.filteredProducts = this.products.filter(product =>
                product.name.toLowerCase().includes(searchTerm) ||
                product.barcode?.includes(searchTerm)
            );
        }
    }

    isLowStock(quantity: number): boolean {
        return quantity <= 5;
    }

    goToManageProducts() {
        this.router.navigate(['/products']);
    }

    goToAddProduct() {
        this.router.navigate(['/products/add']);
    }

    goToProfile() {
        this.router.navigate(['/profile']);
    }

    goToCategories() {
        this.router.navigate(['/categories']);
    }
}
