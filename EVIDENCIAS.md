# Farmacia Medicitas — Evidencias del Proyecto

## Resumen breve del proyecto
- Aplicación Android para e‑commerce farmacéutico que replica funcionalidades clave del proyecto web (Django/DRF).
- Stack: Kotlin + Jetpack Compose + Material 3, MVVM con Hilt, Retrofit/OkHttp, Room (carrito), Stripe PaymentSheet, Google Maps Compose, Firebase (Analytics/FCM).
- Objetivo: flujo de compra completo (catálogo → detalle → carrito → checkout), perfil y pedidos, notificaciones locales/push, y ubicación.

## Cumplimiento de la rúbrica (AVANCE_SEMANAS.md)
- Planificación y organización
  - Documentación y mapeo Web→Android en `PLAN_REPLICACION_WEB_ANDROID.md` y este documento.
  - Estructura de módulos y paquetes clara; navegación organizada con rutas (`NavRoutes`).
- Arquitectura MVVM
  - ViewModels con `StateFlow` y `viewModelScope` (Catalog, Cart, Orders, Checkout, Auth).
  - Inyección con Hilt, separación UI/estado/servicios.
- Base de datos local / Consumo de APIs
  - Room para carrito (persistencia y `Flow` de items).
  - Retrofit/OkHttp con interceptor JWT y autenticación/refresh.
  - Endpoints DRF: productos, categorías, carrito, órdenes, checkout/stripe.
- Uso de servicios externos
  - Stripe PaymentSheet (pago, confirmación, idempotencia backend).
  - Google Maps Compose (pantalla de ubicación); API Key por `local.properties`.
  - Firebase Analytics + FCM (push) y notificaciones locales.
- Diseño y usabilidad (Material Design)
  - Compose, topBar fijo, navbar inferior fija, drawer, cards y skeleton shimmer.
  - Búsqueda con sugerencias y panel dinámico.
- Presentación
  - UI limpia, textos localizados en español, secciones y flujos coherentes.
- Funcionalidad general
  - Flujo completo de compra, perfil con datos del usuario, historial y detalle de pedidos, ubicación, notificaciones.

## Vinculación con “Avance por semanas”
- Semana 1–3: Setup Android Studio, Compose y diseño base (Home, Search, Profile).
- Semana 4–6: Navegación (NavHost, top/bottom bars), menús/drawer.
- Semana 7: Room para carrito; acciones aumentar/disminuir y `Flow` de items.
- Semana 8–9: Retrofit/DRF; productos, categorías, carrito, órdenes, checkout.
- Semana 10: Corrutinas en repositorios/ViewModels; `StateFlow` y carga.
- Semana 11: Localización y permisos; Google Maps Compose.
- Semana 12–13: Notificaciones locales (pedido/ofertas) y FCM; Analytics.
- Semana 14: WebView informativa (mock de ayuda).
- Semana 15: Base para pruebas UI (navegación y estados listos).

## Funcionalidades clave entregadas
- Home (dashboard):
  - Header fijo con buscador, sugerencias y accesos (notificaciones/mock, pedidos).
  - Drawer con datos del usuario (nombre y correo) y opciones.
  - Ofertas y categorías, con skeleton shimmer mientras carga.
- Búsqueda:
  - Campo estilizado y panel de sugerencias con loader; lista de resultados.
- Checkout:
  - Envío (mock), Pago (Stripe PaymentSheet) y Confirmación.
  - Resumen del pedido (items, cantidades, subtotales, total) y pago seguro.
- Perfil:
  - Nombre real y correo desde `users/me`; opciones (pedidos, direcciones, pagos).
  - Barra inferior presente (excepto carrito), coherente en vistas principales.
- Órdenes:
  - Historial con cards full‑width y header con color de la app.
  - Detalle con items y totales.
- Ubicación:
  - Google Map con botón “Mi ubicación” y permiso en runtime.
- Notificaciones:
  - Pedido confirmado (local).
  - Ofertas (local, limitadas y sin spam).
  - FCM: receptor configurado para `ORDER_CONFIRMED` y `OFFER`.

## Evidencias (capturas sugeridas)
1. Pantalla de bienvenida (fondo y botones con colores de tema).
2. Dashboard con:
   - Header fijo, buscador limpio y sugerencias desplegables.
   - Drawer abierto mostrando nombre y correo del usuario.
   - Navbar inferior fija visible.
3. Skeleton shimmer (ofertas) durante carga.
4. Búsqueda mostrando loader y luego sugerencias.
5. ProductCard con animación de zoom al tocar imagen (antes de abrir detalle).
6. Carrito con botones de cantidad funcionando.
7. Checkout — Paso Pago:
   - Método de pago (Stripe) y Resumen del pedido (full‑width), Total y “Pagar ahora”.
   - Modal de PaymentSheet visible.
8. Confirmación del pedido y notificación local posterior.
9. Historial de compras con encabezado coloreado y cards a todo el ancho.
10. Perfil mostrando nombre real y correo; opciones funcionales.
11. Ubicación — mapa con botón “Mi ubicación” y punto actual (si el permiso está otorgado).
12. Notificación FCM recibida en background (mensaje de prueba desde Firebase Console).

## Notas técnicas de implementación (resumen)
- Stripe: `PaymentSheet` en `MainActivity` con callback; confirmación vía backend.
- Auth: `users/me` con `AuthInterceptor` y `JwtAuthenticator` (refresh en 401).
- Persistencia: Room para carrito; sincronización remota al checkout.
- Maps: API Key en `local.properties`, meta‑data en Manifest, Compose Map y permisos.
- Firebase: Google Services plugin, BoM, Analytics y Messaging; servicio `AppMessagingService` para payloads `ORDER_CONFIRMED`/`OFFER`.

## Recolección de evidencias
- Usa el emulador o dispositivo físico y toma capturas en cada paso listado.
- Para FCM, envía mensajes desde Firebase Console con payloads de datos:
  - `{"type":"ORDER_CONFIRMED","total":"80.00"}`
  - `{"type":"OFFER","name":"Paracetamol 500mg","price":"12.50"}`
- Para Stripe, utiliza credenciales de prueba y registra la notificación local.

---
Este documento resume el alcance y mapea la implementación al marco teórico del curso, aportando capturas sugeridas como evidencia.
