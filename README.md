SGT - Sistema de Gestión de Inventario
1. Introducción y Objetivo del Proyecto
SGT es una Aplicación Web (SPA - Single Page Application) desarrollada en Angular, diseñada para ofrecer una solución completa, moderna y multiplataforma a la gestión de inventarios. El objetivo principal es proporcionar una herramienta intuitiva, rápida y eficiente que permita a los usuarios controlar su stock de productos en tiempo real desde cualquier dispositivo con navegador web.

La aplicación se ha construido utilizando Angular (TypeScript), aprovechando la robustez y modularidad del framework, y se apoya en los servicios de Firebase para la autenticación de usuarios y la base de datos en la nube.

2. Tecnologías y Arquitectura
Framework Frontend: Angular (basado en TypeScript).

Base de Datos: Cloud Firestore (NoSQL, escalable y en tiempo real).

Autenticación: Firebase Authentication (gestión segura de sesiones).

Estilos y UI: HTML5, SCSS/CSS y diseño responsivo (adaptable a móviles y escritorio).

Gestión de Estado y Datos: Uso de Servicios (Services) e inyección de dependencias para la comunicación entre componentes y la API de Firebase.

Rutas: Angular Router para una navegación fluida sin recargas de página.


Shutterstock
3. Módulos y Funcionalidades Principales
La aplicación está estructurada en módulos y componentes reutilizables que cubren el ciclo completo de la gestión de un inventario.

Módulo de Autenticación
Componentes de Login y Registro: Formularios reactivos que validan las entradas del usuario y se comunican con Firebase Auth.

Guards (Protección de Rutas): Implementación de CanActivate para asegurar que solo los usuarios autenticados puedan acceder al Dashboard y a la gestión de productos. Si no hay sesión activa, redirige al Login.

Módulo del Dashboard (Vista Principal)
DashboardComponent: Centro de control visual de la aplicación.

Estadísticas en Tiempo Real: Tarjetas informativas que muestran el total de productos y el total de unidades, sincronizadas en vivo mediante Observables.

Alertas Visuales: Lógica condicional en la vista para resaltar filas o tarjetas en rojo cuando el stock es igual o menor a 5 unidades.

Búsqueda Dinámica: Pipe personalizado o función de filtrado para buscar productos por nombre instantáneamente sin recargar la lista.

Módulo de Gestión de Productos
CRUD Completo: Interfaz para Crear, Leer, Actualizar y Eliminar productos.

Tablas de Datos: Visualización clara del inventario.

Acciones Rápidas:

Control de Stock: Botones interactivos para incrementar o decrementar unidades directamente desde la lista.

Edición Modal/Formulario: Navegación a una vista de edición o uso de modales para modificar detalles del producto.

Eliminación Segura: Implementación de alertas de confirmación antes de borrar un registro de la base de datos.

Módulo de Gestión de Categorías
Administración Centralizada: Interfaz dedicada para crear y gestionar las categorías.

Integración en Formularios: Al crear un producto (AddProductComponent), las categorías se cargan dinámicamente desde Firebase en un elemento <select> (lista desplegable), asegurando la integridad de los datos.

Módulo de Perfil de Usuario
ProfileComponent: Visualización de la información del usuario actual.

Gestión de Cuenta: Opción para cerrar sesión y disparar el envío de correo para restablecimiento de contraseña mediante la API de Firebase.

4. Grupo de Desarrollo
Este proyecto ha sido desarrollado por el Grupo N5, integrado por:

Huayta Palma, Nelson Enrrique (orcid.org/0000-0002-0076-6693)

Dueñas Guzman, Carlos Delpiero (orcid.org/0009-0000-1567-1542)

Remigio Ramirez, George Neffer (orcid.org/0000-0002-7269-5927)

Rojas Flores, Miguel Sebastian (orcid.org/0000-0002-6287-5369)
