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
import { add, search, personCircle, cube, layers, folder, moon, sunny } from 'ionicons/icons';
import { ProductService } from '../../core/services/product.service';
import { CategoryService } from '../../core/services/category.service';
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
    @ViewChild('categoryChartCanvas') categoryChartCanvas!: ElementRef;
    @ViewChild('stockChartCanvas') stockChartCanvas!: ElementRef;

    products: Product[] = [];
    filteredProducts: Product[] = [];
    categories: any[] = [];
    totalProducts: number = 0;
    totalCategories: number = 0;
    totalUnits: number = 0;
    searchTerm: string = '';
    loading: boolean = true;
    categoryChart: Chart | null = null;
    stockChart: Chart | null = null;
    isDarkMode: boolean = false;

    constructor(
        public productService: ProductService,
        private categoryService: CategoryService,
        private router: Router
    ) {
        addIcons({ add, search, personCircle, cube, layers, folder, moon, sunny });
        // Load dark mode preference
        const savedDarkMode = localStorage.getItem('darkMode');
        this.isDarkMode = savedDarkMode === 'true';
        this.applyDarkMode();
    }

    ngOnInit() {
        this.loadProducts();
        this.loadCategories();
    }

    loadProducts() {
        this.productService.getProducts().subscribe({
            next: (products: Product[]) => {
                this.products = products;
                this.filteredProducts = products;
                this.updateStatistics();
                this.updateCharts();
                this.loading = false;
            },
            error: (error: any) => {
                console.error('Error loading products:', error);
                this.loading = false;
            }
        });
    }

    loadCategories() {
        this.categoryService.getCategories().subscribe({
            next: (categories) => {
                this.categories = categories;
                this.totalCategories = categories.length;
                this.updateCharts();
            },
            error: (error) => {
                console.error('Error loading categories:', error);
            }
        });
    }

    updateStatistics() {
        this.totalProducts = this.products.length;
        this.totalUnits = this.products.reduce((sum, product) => sum + product.quantity, 0);
    }

    updateCharts() {
        setTimeout(() => {
            this.createCategoryChart();
            this.createStockChart();
        }, 100);
    }

    createCategoryChart() {
        if (!this.categoryChartCanvas) return;

        // Destroy previous chart
        if (this.categoryChart) {
            this.categoryChart.destroy();
        }

        // Group products by category
        const categoryMap = new Map<string, number>();
        this.products.forEach(product => {
            const category = product.category || 'Sin Categoría';
            categoryMap.set(category, (categoryMap.get(category) || 0) + 1);
        });

        const labels = Array.from(categoryMap.keys());
        const data = Array.from(categoryMap.values());

        // Diverse vibrant colors for each category
        const colors = [
            '#7C3AED', // Purple
            '#14B8A6', // Teal
            '#F59E0B', // Amber
            '#EF4444', // Red
            '#10B981', // Green
            '#3B82F6', // Blue
            '#EC4899', // Pink
            '#8B5CF6', // Violet
            '#F97316', // Orange
            '#06B6D4', // Cyan
            '#84CC16', // Lime
            '#6366F1', // Indigo
            '#D946EF', // Fuchsia
            '#0EA5E9', // Sky
            '#22C55E'  // Emerald
        ];

        const config: any = {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: colors.slice(0, labels.length),
                    borderWidth: 3,
                    borderColor: this.isDarkMode ? '#1F2937' : '#FFFFFF',
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                cutout: '65%',
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 15,
                            font: { size: 12, weight: 'bold' },
                            color: this.isDarkMode ? '#F9FAFB' : '#000000',
                            usePointStyle: true,
                            pointStyle: 'circle'
                        }
                    },
                    tooltip: {
                        backgroundColor: this.isDarkMode ? '#374151' : '#FFFFFF',
                        titleColor: this.isDarkMode ? '#F9FAFB' : '#000000',
                        bodyColor: this.isDarkMode ? '#F9FAFB' : '#000000',
                        borderColor: this.isDarkMode ? '#4B5563' : '#E5E7EB',
                        borderWidth: 1,
                        padding: 12,
                        boxPadding: 6,
                        usePointStyle: true,
                        callbacks: {
                            label: (context: any) => {
                                const label = context.label || '';
                                const value = context.parsed || 0;
                                const total = (context.dataset.data as number[]).reduce((a, b) => a + b, 0);
                                const percentage = ((value / total) * 100).toFixed(1);
                                return ` ${label}: ${value} productos (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        };

        this.categoryChart = new Chart(this.categoryChartCanvas.nativeElement, config);
    }

    createStockChart() {
        if (!this.stockChartCanvas) return;

        // Destroy previous chart
        if (this.stockChart) {
            this.stockChart.destroy();
        }

        // Calculate stock status
        const normalStock = this.products.filter(p => p.quantity > 5).length;
        const lowStock = this.products.filter(p => p.quantity <= 5).length;

        const config: any = {
            type: 'pie',
            data: {
                labels: ['Stock Normal', 'Stock Bajo'],
                datasets: [{
                    data: [normalStock, lowStock],
                    backgroundColor: ['#10B981', '#EF4444'],
                    borderWidth: 3,
                    borderColor: this.isDarkMode ? '#1F2937' : '#FFFFFF',
                    hoverOffset: 10
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
                            font: { size: 12, weight: 'bold' },
                            color: this.isDarkMode ? '#F9FAFB' : '#000000',
                            usePointStyle: true,
                            pointStyle: 'circle'
                        }
                    },
                    tooltip: {
                        backgroundColor: this.isDarkMode ? '#374151' : '#FFFFFF',
                        titleColor: this.isDarkMode ? '#F9FAFB' : '#000000',
                        bodyColor: this.isDarkMode ? '#F9FAFB' : '#000000',
                        borderColor: this.isDarkMode ? '#4B5563' : '#E5E7EB',
                        borderWidth: 1,
                        padding: 12,
                        boxPadding: 6,
                        usePointStyle: true,
                        callbacks: {
                            label: (context: any) => {
                                const label = context.label || '';
                                const value = context.parsed || 0;
                                const total = (context.dataset.data as number[]).reduce((a, b) => a + b, 0);
                                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : '0';
                                return ` ${label}: ${value} productos (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        };

        this.stockChart = new Chart(this.stockChartCanvas.nativeElement, config);
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

    toggleDarkMode() {
        this.isDarkMode = !this.isDarkMode;
        localStorage.setItem('darkMode', this.isDarkMode.toString());
        this.applyDarkMode();
    }

    private applyDarkMode() {
        if (this.isDarkMode) {
            document.body.classList.add('dark');
        } else {
            document.body.classList.remove('dark');
        }
    }
}
