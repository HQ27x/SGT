# SGT - Sistema de Gestión de Inventario

## 1. Introducción y Objetivo del Proyecto

**SGT** es una aplicación Android nativa diseñada para ofrecer una solución **completa y moderna** a la gestión de inventarios. El objetivo principal es proporcionar una herramienta **intuitiva, rápida y eficiente** que permita a los usuarios controlar su stock de productos en tiempo real, desde cualquier lugar.

La aplicación se ha construido utilizando **Kotlin**, el lenguaje oficial para el desarrollo de Android, y se apoya en los servicios de **Firebase** para la autenticación de usuarios y la base de datos en tiempo real.

---

## 2. Tecnologías y Arquitectura

*   **Lenguaje de Programación:** Kotlin.
*   **Base de Datos:** Cloud Firestore (para un almacenamiento de datos NoSQL, escalable y en tiempo real).
*   **Autenticación:** Firebase Authentication (para la gestión segura de usuarios por correo y contraseña).
*   **Interfaz de Usuario (UI):** XML con Material Design 3, para un look & feel moderno y coherente.
*   **Componentes de Android Jetpack:** Se han utilizado componentes clave como `RecyclerView` para la creación de listas eficientes, `ConstraintLayout` y `DrawerLayout` para diseños flexibles y menús laterales, y `BottomNavigationView` para una navegación principal intuitiva.

---

## 3. Módulos y Funcionalidades Principales

La aplicación está estructurada en varios módulos interconectados que cubren el ciclo completo de la gestión de un inventario.

#### **Módulo de Autenticación**
*   **`Login.kt`**: Pantalla de inicio de sesión. Verifica las credenciales del usuario con Firebase Auth.
*   **`registro.kt`**: (Funcionalidad base) Permite a nuevos usuarios crear una cuenta.
*   **Sesión Persistente:** La aplicación recuerda al usuario si ya ha iniciado sesión, llevándolo directamente al Dashboard.

#### **Módulo del Dashboard (Pantalla Principal)**
*   **`DashboardActivity.kt`**: Es el centro de control de la aplicación. Muestra un resumen del estado del inventario.
*   **Estadísticas en Tiempo Real:** Calcula y muestra el **número total de productos** y la **suma total de unidades** en stock, actualizándose automáticamente con cada cambio en la base de datos.
*   **Alertas de Stock Bajo:** Los productos con 5 o menos unidades se resaltan en color rojo para una fácil identificación.
*   **Búsqueda Rápida:** Permite filtrar la lista de productos por nombre en tiempo real.

#### **Módulo de Gestión de Productos**
*   **`ManageProductsActivity.kt`**: Pantalla donde el usuario puede realizar todas las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos.
*   **Acciones Rápidas:**
    *   **Añadir/Restar Cantidad:** Botones `+` y `-` para modificar el stock rápidamente.
    *   **Editar:** Abre un formulario para modificar los detalles del producto.
    *   **Eliminar:** Borra un producto del inventario, con un diálogo de confirmación para evitar errores.

#### **Módulo de Gestión de Categorías**
*   **`ManageCategoriesActivity.kt`**: Permite al usuario **añadir, editar y eliminar categorías**, lo que centraliza la gestión y evita errores de escritura al registrar nuevos productos.
*   **`AddProductActivity.kt`**: En esta pantalla, en lugar de un campo de texto, se utiliza un **Spinner (lista desplegable)** que se carga con las categorías existentes, haciendo el proceso de añadir productos más rápido y robusto.

#### **Módulo de Perfil de Usuario**
*   **`ProfileActivity.kt`**: Pantalla accesible desde el ícono de perfil. Muestra el correo electrónico del usuario y permite solicitar un **restablecimiento de contraseña** a través de un correo electrónico enviado por Firebase.

---

## 4. Grupo de Desarrollo

Este proyecto ha sido desarrollado por el **Grupo N5**, integrado por:

*   **Huayta Palma, Nelson Enrrique** ([orcid.org/0000-0002-0076-6693](https://orcid.org/0000-0002-0076-6693))
*   **Dueñas Guzman, Carlos Delpiero** ([orcid.org/0009-0000-1567-1542](https://orcid.org/0009-0000-1567-1542))
*   **Remigio Ramirez, George Neffer** ([orcid.org/0000-0002-7269-5927](https://orcid.org/0000-0002-7269-5927))
*   **Rojas Flores, Miguel Sebastian** ([orcid.org/0000-0002-6287-5369](https://orcid.org/0000-0002-6287-5369))
