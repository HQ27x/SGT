import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
    IonContent, IonHeader, IonTitle, IonToolbar, IonCard, IonCardHeader,
    IonCardTitle, IonCardContent, IonItem, IonLabel, IonButton, IonButtons,
    IonBackButton, AlertController, ToastController
} from '@ionic/angular/standalone';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss'],
    imports: [
        CommonModule, IonContent, IonHeader, IonTitle, IonToolbar, IonCard,
        IonCardHeader, IonCardTitle, IonCardContent, IonItem, IonLabel,
        IonButton, IonButtons, IonBackButton
    ]
})
export class ProfileComponent implements OnInit {
    userEmail: string = '';

    constructor(
        private authService: AuthService,
        private router: Router,
        private alertController: AlertController,
        private toastController: ToastController
    ) { }

    ngOnInit() {
        const user = this.authService.getCurrentUser();
        this.userEmail = user?.email || 'No disponible';
    }

    async resetPassword() {
        const alert = await this.alertController.create({
            header: 'Restablecer Contraseña',
            message: `Se enviará un correo de restablecimiento a ${this.userEmail}`,
            buttons: [
                {
                    text: 'Cancelar',
                    role: 'cancel'
                },
                {
                    text: 'Enviar',
                    handler: async () => {
                        try {
                            await this.authService.resetPassword(this.userEmail);
                            await this.showToast('Correo de restablecimiento enviado', 'success');
                        } catch (error) {
                            await this.showToast('Error al enviar correo', 'danger');
                        }
                    }
                }
            ]
        });

        await alert.present();
    }

    async logout() {
        const alert = await this.alertController.create({
            header: 'Cerrar Sesión',
            message: '¿Estás seguro de que deseas cerrar sesión?',
            buttons: [
                {
                    text: 'Cancelar',
                    role: 'cancel'
                },
                {
                    text: 'Cerrar Sesión',
                    handler: async () => {
                        try {
                            await this.authService.logout();
                            this.router.navigate(['/login']);
                        } catch (error) {
                            await this.showToast('Error al cerrar sesión', 'danger');
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
