import { Injectable } from '@angular/core';
import { Firestore, collection, doc, addDoc, updateDoc, deleteDoc, onSnapshot } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { Category } from '../models/category.model';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {
    constructor(private firestore: Firestore) { }

    /**
     * Obtiene todas las categorías en tiempo real
     */
    getCategories(): Observable<Category[]> {
        return new Observable(observer => {
            const categoriesRef = collection(this.firestore, 'categories');
            const unsubscribe = onSnapshot(categoriesRef,
                (snapshot) => {
                    const categories: Category[] = [];
                    snapshot.forEach((doc) => {
                        categories.push({ id: doc.id, ...doc.data() } as Category);
                    });
                    observer.next(categories);
                },
                (error) => {
                    console.error('Error in getCategories:', error);
                    observer.error(error);
                }
            );
            return () => unsubscribe();
        });
    }

    /**
     * Obtiene una categoría por ID
     */
    getCategory(id: string): Observable<Category> {
        return new Observable(observer => {
            const categoryDoc = doc(this.firestore, `categories/${id}`);
            const unsubscribe = onSnapshot(categoryDoc,
                (snapshot) => {
                    if (snapshot.exists()) {
                        observer.next({ id: snapshot.id, ...snapshot.data() } as Category);
                    } else {
                        observer.error(new Error('Category not found'));
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
     * Añade una nueva categoría
     */
    async addCategory(category: Category): Promise<any> {
        try {
            console.log('Attempting to add category:', category);
            const categoryData = {
                name: category.name,
                createdAt: new Date().toISOString()
            };
            const categoriesRef = collection(this.firestore, 'categories');
            const result = await addDoc(categoriesRef, categoryData);
            console.log('Category added successfully:', result.id);
            return result;
        } catch (error) {
            console.error('Error adding category:', error);
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
