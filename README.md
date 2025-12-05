# SGT - Sistema de Gestión de Tienda

## 📱 Descripción del Proyecto

SGT es una aplicación moderna desarrollada en **Angular** e **Ionic** para la gestión eficiente de inventarios. Diseñada para ser intuitiva y visualmente atractiva, permite a los usuarios administrar productos y categorías con facilidad, ofreciendo un panel de control con estadísticas en tiempo real.

### 🚀 Características Principales

- **Gestión de Inventario:** CRUD completo (Crear, Leer, Actualizar, Eliminar) para Productos y Categorías.
- **Dashboard Interactivo:** Gráficos visuales (Chart.js) para analizar el stock y la distribución de productos por categoría.
- **Autenticación Segura:** Sistema de Login y Registro integrado con Firebase Authentication.
- **Modo Oscuro:** Interfaz adaptable con soporte para temas claro y oscuro.
- **Multiplataforma:** Diseño responsivo que funciona en web y dispositivos móviles (Android/iOS) gracias a Capacitor.
- **Base de Datos en Tiempo Real:** Sincronización instantánea de datos utilizando Firebase Firestore.

## 🛠️ Tecnologías Utilizadas

- **Frontend:** Angular 18+, Ionic Framework 8
- **Backend:** Firebase (Authentication, Firestore)
- **Gráficos:** Chart.js
- **Móvil:** Capacitor (para generación de APK)
- **Contenedorización:** Docker

---

## 🐳 Guía de Ejecución con Docker

Este proyecto está contenerizado para facilitar su ejecución sin necesidad de instalar dependencias locales como Node.js o Angular CLI.

### Requisitos
- Tener instalado **Docker Desktop**.

### Pasos para Ejecutar

1.  **Clonar o descargar** este repositorio.
2.  Abrir una terminal en la carpeta raíz del proyecto.
3.  Ejecutar el siguiente comando:

    ```bash
    docker-compose up
    ```

4.  Esperar a que termine la instalación y compilación (la primera vez puede tardar unos minutos).
5.  Abrir el navegador en: **[http://localhost:4200](http://localhost:4200)**

### Comandos Útiles

- **Detener la aplicación:** `Ctrl + C` en la terminal.
- **Reconstruir (si hay cambios en dependencias):** `docker-compose up --build`
- **Limpiar contenedores:** `docker-compose down`

---

## 👥 Grupo de Desarrollo

Este proyecto ha sido desarrollado por el **Grupo N5**, integrado por:

- **Huayta Palma, Nelson Enrrique** ([orcid.org/0000-0002-0076-6693](https://orcid.org/0000-0002-0076-6693))
- **Dueñas Guzman, Carlos Delpiero** ([orcid.org/0009-0000-1567-1542](https://orcid.org/0009-0000-1567-1542))
- **Remigio Ramirez, George Neffer** ([orcid.org/0000-0002-7269-5927](https://orcid.org/0000-0002-7269-5927))
- **Rojas Flores, Miguel Sebastian** ([orcid.org/0000-0002-6287-5369](https://orcid.org/0000-0002-6287-5369))
