# SGT - Sistema de Gestión de Inventario

## 1. Introducción y Objetivo del Proyecto

**SGT** es una **Aplicación Web (SPA - Single Page Application)** desarrollada en **Angular**, diseñada para ofrecer una solución **completa, moderna y multiplataforma** a la gestión de inventarios. El objetivo principal es proporcionar una herramienta **intuitiva, rápida y eficiente** que permita a los usuarios controlar su stock de productos en tiempo real desde cualquier dispositivo con un navegador web.

La aplicación se ha construido utilizando **Angular (TypeScript)**, aprovechando la arquitectura basada en componentes, y se apoya en los servicios de **Firebase** para la autenticación de usuarios y el almacenamiento de datos en la nube.

---

## 2. Tecnologías y Arquitectura

* **Framework Frontend:** Angular (TypeScript).
* **Base de Datos:** Cloud Firestore (NoSQL, escalable y en tiempo real).
* **Autenticación:** Firebase Authentication (gestión segura de sesiones).
* **Diseño UI/UX:** HTML5, SCSS y diseño responsivo (adaptable a móviles y escritorio).
* **Gestión de Estado y Flujo de Datos:** Uso de **Servicios (Services)** e inyección de dependencias para la comunicación entre componentes.
* **Navegación:** Angular Router para una navegación fluida entre vistas sin recargar la página.

---

## 3. Módulos y Funcionalidades Principales

La aplicación está estructurada en módulos y componentes reutilizables que cubren el ciclo completo de la gestión de un inventario.

#### **Módulo de Autenticación**
* **Login y Registro:** Formularios reactivos que gestionan la entrada del usuario y validan credenciales contra Firebase Auth.
* **Seguridad (Guards):** Implementación de `CanActivate` para proteger las rutas privadas. Si un usuario no está autenticado, el sistema lo redirige automáticamente al Login.

#### **Módulo del Dashboard (Vista Principal)**
* **`DashboardComponent`:** Centro de control visual de la aplicación.
* **Estadísticas en Tiempo Real:** Tarjetas informativas que muestran el **total de productos** y el **total de unidades**, sincronizadas en vivo mediante Observables.
* **Alertas Visuales:** Lógica condicional (`*ngIf` / `[class]`) para resaltar filas o tarjetas en rojo cuando el stock de un producto es crítico (5 unidades o menos).
* **Búsqueda Dinámica:** Filtro en tiempo real para localizar productos por nombre instantáneamente.

#### **Módulo de Gestión de Productos**
* **CRUD Completo:** Interfaz para Crear, Leer, Actualizar y Eliminar productos.
* **Acciones Rápidas en Tabla:**
    * **Control de Stock:** Botones interactivos para incrementar (+) o decrementar (-) unidades directamente desde la lista de inventario.
    * **Edición:** Navegación o modales para modificar los detalles del producto.
    * **Eliminación Segura:** Alertas de confirmación antes de borrar registros de la base de datos.

#### **Módulo de Gestión de Categorías**
* **Administración Centralizada:** Interfaz dedicada para crear y gestionar las categorías del negocio.
* **Integración Dinámica:** Al crear un nuevo producto, el formulario carga automáticamente las categorías existentes desde Firebase en un selector (`<select>`), asegurando la coherencia de los datos.

#### **Módulo de Perfil de Usuario**
* **`ProfileComponent`:** Visualización de la información del usuario autenticado.
* **Gestión de Cuenta:** Funcionalidad para cerrar sesión de forma segura y opción para solicitar el restablecimiento de contraseña vía correo electrónico.

---

## 4. Instalación y Ejecución Local

Para ejecutar este proyecto en tu entorno local, asegúrate de tener instalado **Node.js** y **Angular CLI**.

1.  **Clonar el repositorio:**
    ```bash
    git clone <URL_DE_TU_REPOSITORIO>
    ```
2.  **Instalar dependencias:**
    ```bash
    npm install
    ```
3.  **Configurar Firebase:**
    Asegúrate de tener tu archivo de configuración de Firebase en `src/environments/environment.ts`.
4.  **Ejecutar el servidor de desarrollo:**
    ```bash
    ng serve
    ```
    Navega a `http://localhost:4200/`.

---

## 5. Grupo de Desarrollo

Este proyecto ha sido desarrollado por el **Grupo N5**, integrado por:

* **Huayta Palma, Nelson Enrrique** ([orcid.org/0000-0002-0076-6693](https://orcid.org/0000-0002-0076-6693))
* **Dueñas Guzman, Carlos Delpiero** ([orcid.org/0009-0000-1567-1542](https://orcid.org/0009-0000-1567-1542))
* **Remigio Ramirez, George Neffer** ([orcid.org/0000-0002-7269-5927](https://orcid.org/0000-0002-7269-5927))
* **Rojas Flores, Miguel Sebastian** ([orcid.org/0000-0002-6287-5369](https://orcid.org/0000-0002-6287-5369))
