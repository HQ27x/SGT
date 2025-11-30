import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonItem, IonLabel, IonInput, IonButton, IonText, IonSpinner } from '@ionic/angular/standalone';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    imports: [CommonModule, FormsModule, IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonItem, IonLabel, IonInput, IonButton, IonText, IonSpinner]
})
export class LoginComponent {
    email: string = '';
    password: string = '';
    loading: boolean = false;
    errorMessage: string = '';

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
        // Verificar si ya está autenticado
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/dashboard']);
        }
    }

    async onLogin() {
        if (!this.email || !this.password) {
            this.errorMessage = 'Por favor, introduce correo y contraseña';
            return;
        }

        this.loading = true;
        this.errorMessage = '';

        try {
            await this.authService.login(this.email, this.password);
            this.router.navigate(['/dashboard']);
        } catch (error: any) {
            this.errorMessage = this.getErrorMessage(error.code);
        } finally {
            this.loading = false;
        }
    }

    goToRegister() {
        this.router.navigate(['/register']);
    }

    private getErrorMessage(errorCode: string): string {
        switch (errorCode) {
            case 'auth/invalid-email':
                return 'Correo electrónico inválido';
            case 'auth/user-not-found':
                return 'Usuario no encontrado';
            case 'auth/wrong-password':
                return 'Contraseña incorrecta';
            case 'auth/invalid-credential':
                return 'Credenciales inválidas';
            default:
                return 'Error al iniciar sesión. Intenta de nuevo.';
        }
    }
}
