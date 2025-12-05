import { Injectable } from '@angular/core';
import { Firestore, collection, doc, addDoc, updateDoc, deleteDoc, onSnapshot } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    constructor(private firestore: Firestore) { }

    /**
     * Obtiene todos los productos en tiempo real
     */
    getProducts(): Observable<Product[]> {
        return new Observable(observer => {
            const productsRef = collection(this.firestore, 'products');
            const unsubscribe = onSnapshot(productsRef,
                (snapshot) => {
                    const products: Product[] = [];
                    snapshot.forEach((doc) => {
                        products.push({ id: doc.id, ...doc.data() } as Product);
                    });
                    observer.next(products);
                },
                (error) => {
                    console.error('Error in getProducts:', error);
                    observer.error(error);
                }
            );
            return () => unsubscribe();
        });
    }

    /**
     * Obtiene un producto por ID
     */
    getProduct(id: string): Observable<Product> {
        return new Observable(observer => {
            const productDoc = doc(this.firestore, `products/${id}`);
            const unsubscribe = onSnapshot(productDoc,
                (snapshot) => {
                    if (snapshot.exists()) {
                        observer.next({ id: snapshot.id, ...snapshot.data() } as Product);
                    } else {
                        observer.error(new Error('Product not found'));
                    }
                },
                (error) => {
                    observer.error(error);
                }
            );
            return () => unsubscribe();
        });
    }

    /**
     * Añade un nuevo producto
     */
    async addProduct(product: Product): Promise<any> {
        try {
            const productsRef = collection(this.firestore, 'products');
            const result = await addDoc(productsRef, product);
            return result;
        } catch (error) {
            throw error;
        }
    }

    /**
     * Actualiza un producto existente
     */
    async updateProduct(id: string, product: Partial<Product>): Promise<void> {
        try {
            const productDoc = doc(this.firestore, `products/${id}`);
            await updateDoc(productDoc, product);
        } catch (error) {
            throw error;
        }
    }

    /**
     * Elimina un producto
     */
    async deleteProduct(id: string): Promise<void> {
        try {
            const productDoc = doc(this.firestore, `products/${id}`);
            await deleteDoc(productDoc);
        } catch (error) {
            throw error;
        }
    }

    /**
     * Actualiza la cantidad de un producto
     */
    async updateQuantity(id: string, quantity: number): Promise<void> {
        try {
            const productDoc = doc(this.firestore, `products/${id}`);
            await updateDoc(productDoc, { quantity });
        } catch (error) {
            throw error;
        }
    }

    /**
     * Incrementa la cantidad de un producto en 1
     */
    async incrementQuantity(product: Product): Promise<void> {
        if (product.id) {
            await this.updateQuantity(product.id, product.quantity + 1);
        }
    }

    /**
     * Decrementa la cantidad de un producto en 1
     */
    async decrementQuantity(product: Product): Promise<void> {
        if (product.id && product.quantity > 0) {
            await this.updateQuantity(product.id, product.quantity - 1);
        }
    }
}
