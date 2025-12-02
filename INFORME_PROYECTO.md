# Informe del Proyecto — Farmacia Medicitas (Android)

## 1. Resumen
La aplicación móvil “Farmacia Medicitas” replica el e‑commerce farmacéutico de la plataforma web en Android. Permite explorar catálogo, buscar productos con sugerencias, ver detalle, gestionar carrito y realizar pagos con Stripe PaymentSheet. Integra historial/detalle de órdenes, perfil del usuario autenticado y la sección “Ubícanos y contáctanos” con mapa y tarjeta de contacto. Se añaden notificaciones locales (pedido confirmado, ofertas) y push (FCM) para comunicación de eventos clave.

## 2. Objetivos
- Replicar capacidades esenciales del e‑commerce web en Android.
- Mantener arquitectura limpia (MVVM, DI con Hilt), UI declarativa (Jetpack Compose) y buenas prácticas de seguridad.
- Integrar servicios externos (Stripe, Firebase, Google Maps) de forma segura y comprobable.

## 3. Tecnologías y Arquitectura
- Lenguaje y UI: Kotlin, Jetpack Compose, Material 3.
- Arquitectura: MVVM con `StateFlow` y `viewModelScope`.
- Inyección de dependencias: Hilt (módulos de red y repositorios).
- Red: Retrofit + OkHttp (`AuthInterceptor` Bearer y `JwtAuthenticator` para refresh).
- Persistencia: Room para carrito (DAO/Entities, `Flow`).
- Servicios externos: Stripe PaymentSheet (Android SDK), Firebase Analytics y Cloud Messaging, Google Maps Compose.
- Navegación: Navigation Compose + accompanist con transiciones horizontales.
- Configuración segura de claves:
  - Stripe `publishable_key` en `BuildConfig`.
  - Google Maps API Key mediante `manifestPlaceholders` leyendo `local.properties`.
  - `google-services.json` en módulo `app` para Firebase.

## 4. Conexión con el Backend
- Backend: Django REST Framework, base `api/`.
- Endpoints consumidos:
  - Productos: `GET ecommerce/products/` (paginado, búsqueda), `GET ecommerce/categories/`.
  - Carrito: `POST ecommerce/cart/items/`, `POST ecommerce/cart/clear/`.
  - Órdenes y checkout: `POST ecommerce/checkout/stripe/`, `POST ecommerce/stripe/confirm/`.
  - Usuario: `GET users/me/`.
- Interceptor y autenticador:
  - `Authorization: Bearer <access>` en cada request autenticado.
  - Refresh automático en 401 mediante `JwtAuthenticator`.
- Sincronización de carrito antes del pago: limpieza remota y reenvío desde Room para evitar acumulaciones.

## 5. Módulos Implementados
- Catálogo y Búsqueda:
  - Lista paginada, chips de categorías, buscador con panel de sugerencias (loader + resultados clicables).
- Detalle de Producto:
  - Imagen y datos; animación de “zoom” en la tarjeta previa a navegación.
- Carrito:
  - Persistencia local en Room, aumentar/disminuir cantidad, cálculo de total.
- Checkout:
  - Envío (mock), Pago (Stripe PaymentSheet), Confirmación con idempotencia en backend y notificación local.
- Órdenes:
  - Historial con encabezado a color y tarjetas a ancho completo; detalle con items y totales.
- Perfil:
  - Datos del usuario desde `users/me` (nombre y correo), accesos a órdenes, direcciones y métodos de pago.
- Ubícanos y Contáctanos:
  - Google Maps con marcadores de sedes y tarjeta de contacto (logo, teléfono, correo, horario). Solicitud de permisos automática.
- Notificaciones:
  - Locales: “Pedido confirmado” y “Ofertas” (limitadas por sesión).
  - Push (FCM): receptor de payloads tipo `ORDER_CONFIRMED` y `OFFER`.

## 6. Diseño de Interfaz
- Top bar fijo con buscador estilizado y sugerencias.
- Bottom bar persistente en vistas principales (Dashboard, Búsqueda, Perfil, Ubícanos).
- Drawer con identidad del usuario (nombre + correo) y accesos.
- Skeleton shimmer en carouseles y listados mientras cargan.
- Transiciones horizontales entre pantallas.

## 7. Pruebas y Verificación
- Funcionales:
  - Catálogo y categorías: respuestas 200 y renderizado correcto.
  - Búsqueda: sugerencias y filtro, sin bloqueos.
  - Carrito: cantidades y total consistentes; sincronización remota antes del checkout.
  - Stripe: PaymentSheet presentado; confirmación y notificación posterior.
  - Órdenes: historial y detalle cargan correctamente.
  - Ubicación: permisos automáticos y carga de mapa con pines.
  - Notificaciones: locales emitidas en eventos; push recibidas con payloads dirigidos.
- Logs:
  - OkHttp con trazas de endpoints (200/JSON).
  - Ajustes aplicados en ciclo de vida (PaymentSheet en `MainActivity`) y renderer de Maps.
- Emulador y dispositivo físico: verificación de funcionalidades esenciales.

## 8. Resultados
- Flujo de compra estable y coherente con el backend.
- UI consistente y adaptable, con componentes reutilizables.
- Integraciones externas funcionales (Stripe, Firebase, Maps).
- Notificaciones operativas (locales y push), control de spam en ofertas.

## 9. Conclusiones
La solución cumple los objetivos del proyecto y se alinea al marco teórico del curso. La arquitectura MVVM con Compose y Hilt facilita mantenimiento y escalabilidad; las integraciones con Stripe, Firebase y Maps se realizaron siguiendo buenas prácticas y manejo seguro de claves. Existen bases para ampliar funcionalidad (direcciones avanzadas, segmentación de notificaciones, pruebas UI instrumentadas y optimización de rendimiento).

## 10. Trabajo Futuro
- Integrar Places API para autocompletar direcciones y selección de punto.
- Persistir tokens FCM por usuario en backend y enviar notificaciones segmentadas.
- Añadir Espresso UI tests para flujos principales.
- Optimizar rendimiento en listados con paging y cache híbrido (Room + red).

## 11. Publicación y Evidencias Multimedia
- Repositorio del proyecto: [URL del repositorio](https://github.com/usuario/farmacia-medicitas-android)
- Video demostrativo: [URL del video](https://www.youtube.com/watch?v=XXXXXXXXXXX)
- Instrucciones de ejecución:
  - Clonar el repositorio y abrir en Android Studio Arctic Fox o superior.
  - Crear `local.properties` con `MAPS_API_KEY=...`.
  - Colocar `google-services.json` en `app/` para Firebase.
  - Ejecutar en emulador/dispositivo con conexión a `BASE_URL` del backend.
