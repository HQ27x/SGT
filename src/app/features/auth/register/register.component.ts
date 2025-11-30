import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonItem, IonLabel, IonInput, IonButton, IonText, IonSpinner, IonBackButton, IonButtons } from '@ionic/angular/standalone';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss'],
    imports: [CommonModule, FormsModule, IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonItem, IonLabel, IonInput, IonButton, IonText, IonSpinner, IonBackButton, IonButtons]
})
export class RegisterComponent {
    email: string = '';
    password: string = '';
    confirmPassword: string = '';
    loading: boolean = false;
    errorMessage: string = '';

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    async onRegister() {
        if (!this.email || !this.password || !this.confirmPassword) {
            this.errorMessage = 'Por favor, completa todos los campos';
            return;
        }

        if (this.password !== this.confirmPassword) {
            this.errorMessage = 'Las contraseñas no coinciden';
            return;
        }

        if (this.password.length < 6) {
            this.errorMessage = 'La contraseña debe tener al menos 6 caracteres';
            return;
        }

        this.loading = true;
        this.errorMessage = '';

        try {
            await this.authService.register(this.email, this.password);
            this.router.navigate(['/dashboard']);
        } catch (error: any) {
            this.errorMessage = this.getErrorMessage(error.code);
        } finally {
            this.loading = false;
        }
    }

    goToLogin() {
        this.router.navigate(['/login']);
    }

    private getErrorMessage(errorCode: string): string {
        switch (errorCode) {
            case 'auth/email-already-in-use':
                return 'Este correo ya está registrado';
            case 'auth/invalid-email':
                return 'Correo electrónico inválido';
            case 'auth/weak-password':
                return 'La contraseña es muy débil';
            default:
                return 'Error al crear la cuenta. Intenta de nuevo.';
        }
    }
}
