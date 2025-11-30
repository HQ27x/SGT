import { Injectable } from '@angular/core';
import { Firestore, collection, collectionData, doc, addDoc, updateDoc, deleteDoc, docData } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    private categoriesCollection = collection(this.firestore, 'categories');

    constructor(private firestore: Firestore) { }

    /**
     * Obtiene todas las categorías en tiempo real
     */
    getCategories(): Observable<Category[]> {
        return collectionData(this.categoriesCollection, { idField: 'id' }) as Observable<Category[]>;
    }

    /**
     * Obtiene una categoría por ID
     */
    getCategory(id: string): Observable<Category> {
        const categoryDoc = doc(this.firestore, `categories/${id}`);
        return docData(categoryDoc, { idField: 'id' }) as Observable<Category>;
    }

    /**
     * Añade una nueva categoría
     */
    async addCategory(category: Category): Promise<any> {
        try {
            const result = await addDoc(this.categoriesCollection, { name: category.name });
            return result;
        } catch (error) {
            throw error;
        }
    }

    /**
     * Actualiza una categoría existente
     */
    async updateCategory(id: string, category: Partial<Category>): Promise<void> {
        try {
            const categoryDoc = doc(this.firestore, `categories/${id}`);
            await updateDoc(categoryDoc, category);
        } catch (error) {
            throw error;
        }
    }

    /**
     * Elimina una categoría
     */
    async deleteCategory(id: string): Promise<void> {
        try {
            const categoryDoc = doc(this.firestore, `categories/${id}`);
            await deleteDoc(categoryDoc);
        } catch (error) {
            throw error;
        }
    }
}
