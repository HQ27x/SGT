import { Injectable } from '@angular/core';
import { Auth, signInWithEmailAndPassword, createUserWithEmailAndPassword, signOut, sendPasswordResetEmail, User, user, authState } from '@angular/fire/auth';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    user$: Observable<User | null>;

    constructor(private auth: Auth) {
        this.user$ = authState(this.auth);
    }

    /**
     * Inicia sesión con email y contraseña
     */
    async login(email: string, password: string): Promise<any> {
        try {
            const result = await signInWithEmailAndPassword(this.auth, email, password);
            return result;
        } catch (error) {
            throw error;
        }
    }

    /**
     * Registra un nuevo usuario
     */
    async register(email: string, password: string): Promise<any> {
        try {
            const result = await createUserWithEmailAndPassword(this.auth, email, password);
            return result;
        } catch (error) {
            throw error;
        }
    }

    /**
     * Cierra la sesión del usuario actual
     */
    async logout(): Promise<void> {
        try {
            await signOut(this.auth);
        } catch (error) {
            throw error;
        }
    }

    /**
     * Envía un correo de restablecimiento de contraseña
     */
    async resetPassword(email: string): Promise<void> {
        try {
            await sendPasswordResetEmail(this.auth, email);
        } catch (error) {
            throw error;
        }
    }

    /**
     * Obtiene el usuario actual
     */
    getCurrentUser(): User | null {
        return this.auth.currentUser;
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    isAuthenticated(): boolean {
        return this.auth.currentUser !== null;
    }
}
