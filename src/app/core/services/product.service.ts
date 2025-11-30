import { Injectable } from '@angular/core';
import { Firestore, collection, collectionData, doc, addDoc, updateDoc, deleteDoc, docData } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private productsCollection = collection(this.firestore, 'products');

    constructor(private firestore: Firestore) { }

    /**
     * Obtiene todos los productos en tiempo real
     */
    getProducts(): Observable<Product[]> {
        return collectionData(this.productsCollection, { idField: 'id' }) as Observable<Product[]>;
    }

    /**
     * Obtiene un producto por ID
     */
    getProduct(id: string): Observable<Product> {
        const productDoc = doc(this.firestore, `products/${id}`);
        return docData(productDoc, { idField: 'id' }) as Observable<Product>;
    }

    /**
     * Añade un nuevo producto
     */
    async addProduct(product: Product): Promise<any> {
        try {
            const result = await addDoc(this.productsCollection, product);
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
