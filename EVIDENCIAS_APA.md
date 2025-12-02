# Farmacia Medicitas: Documento técnico (formato APA)

## Portada
- Título: Farmacia Medicitas — Implementación Android alineada al marco teórico del curso
- Autor: Estudiante de Ingeniería de Sistemas
- Institución: [Nombre de la institución]
- Curso: Desarrollo de Aplicaciones Móviles
- Fecha: 02 de diciembre de 2025

## Resumen
Se documenta la replicación de un e‑commerce farmacéutico en Android, alineado con el proyecto web (Django/DRF). La solución emplea Kotlin, Jetpack Compose y MVVM, con integración de autenticación JWT, catálogo y carrito persistente (Room), pagos con Stripe PaymentSheet, notificaciones locales/push mediante Firebase Cloud Messaging, y servicios de ubicación con Google Maps Compose. El trabajo se articula según el “Avance por semanas” y cumple la rúbrica de evaluación en planificación, arquitectura, consumo de APIs, servicios externos, diseño, presentación y funcionalidad. Se describen metodología, implementación, resultados, evidencias, discusión y conclusiones.

Palabras clave: Android, Jetpack Compose, MVVM, Retrofit, Room, Stripe, Firebase, Google Maps, notificaciones, e‑commerce.

## Introducción
El objetivo es trasladar a Android las capacidades clave del sistema web “Farmacia Medicitas”, garantizando un flujo de compra robusto (catálogo → detalle → carrito → checkout), vistas de usuario y pedidos, servicios de ubicación, y mecanismos de notificación. El proyecto sigue una arquitectura moderna (MVVM + Compose + Hilt) y buenas prácticas de seguridad y UX.

## Objetivos y alcance
- Objetivos específicos:
  - Implementar catálogo y búsqueda con sugerencias en tiempo real.
  - Gestionar carrito con persistencia local y sincronización remota en checkout.
  - Integrar pagos seguros con Stripe PaymentSheet.
  - Mostrar historial/detalle de pedidos y perfil del usuario autenticado.
  - Habilitar mapa de ubicación con permiso runtime.
  - Activar notificaciones locales y push (FCM) para eventos clave.
- Alcance:
  - Funcionalidades esenciales de e‑commerce, UI en Compose, arquitectura MVVM, servicios externos (Stripe, Firebase, Maps).
  - No incluye pasarela propia ni almacenamiento de tarjetas; se delega a Stripe.

## Marco teórico y alineación con la rúbrica
- Planificación y organización: Documentos `PLAN_REPLICACION_WEB_ANDROID.md`, `AVANCE_SEMANAS.md` y esta evidencia.
- Arquitectura MVVM: ViewModels con `StateFlow`, DI con Hilt, separación de capas.
- Base de datos / APIs: Room para carrito; Retrofit/OkHttp contra DRF (productos, categorías, carrito, órdenes, checkout, `users/me`).
- Servicios externos: Stripe PaymentSheet, Firebase Analytics/FCM, Google Maps.
- Diseño y usabilidad: Jetpack Compose, Material 3, barras fija superior/inferior, drawer.
- Presentación: textos en español, componentes reutilizables y consistentes.
- Funcionalidad: flujo de compra completo, perfil, pedidos, notificaciones y mapa.

## Vinculación con “Avance por semanas”
- Semana 1–3: Setup Android Studio, Compose, base de pantallas.
- Semana 4–6: Navegación, menús y barras; transiciones de arrastre (accompanist).
- Semana 7: Room y flujo de carrito; acciones de cantidad.
- Semana 8–9: Retrofit/DRF; catálogo, categorías, carrito y checkout.
- Semana 10: Corrutinas y `StateFlow` en ViewModels.
- Semana 11: Localización y Google Maps Compose; permisos runtime.
- Semana 12–13: Notificaciones locales y FCM; analytics.
- Semana 14: WebView informativa.
- Semana 15: Base de pruebas UI.

## Metodología y arquitectura
- Patrón MVVM: UI en Compose, estado observable con `StateFlow`, lógica en ViewModels.
- DI con Hilt: módulos de red (Retrofit/OkHttp), repositorios y DAOs.
- Red y seguridad: `AuthInterceptor` (Bearer) y `JwtAuthenticator` (refresh automático en 401), manejo de errores y tiempos.
- Persistencia: Room para `CartItem`; flujo continuo con `Flow` y suma de totales.
- Servicios externos:
  - Stripe: PaymentSheet, confirmación backend, idempotencia en confirm.
  - Maps: API Key gestionada en `local.properties`, `MapProperties` con `isMyLocationEnabled` y `MapUiSettings`.
  - Firebase: Analytics y Messaging; receptor `AppMessagingService` para payloads de datos.
- Navegación y UX: `AnimatedNavHost` con transiciones horizontales; barra inferior persistente en vistas principales; topBar fija con buscador.

## Implementación
- Catálogo y búsqueda:
  - Lista paginada; buscador con `TextField` y panel de sugerencias (spinner + lista clicable).
- ProductCard:
  - Animación de “zoom” breve en imagen previa a la navegación a detalle.
- Carrito:
  - Persistencia local, acciones aumentar/disminuir, total calculado; sincronización remota antes de checkout.
- Checkout:
  - Envío (mock), pago con PaymentSheet y resumen del pedido (items, subtotales, total); confirmación limpia carrito.
- Perfil y pedidos:
  - Perfil muestra nombre real/correo desde `users/me`; historial con encabezado a color y cards full‑width; detalle con items y totales.
- Ubicación:
  - Mapa con botón “Mi ubicación” y solicitud de permiso; cámara centrada en coordenadas iniciales.
- Notificaciones:
  - Locales: pedido confirmado y ofertas (limitadas por sesión).
  - Push: FCM con tipos `ORDER_CONFIRMED` y `OFFER`.

## Configuración del entorno
- Claves y seguridad:
  - Stripe publishable key en `BuildConfig` (no se guarda el secreto).
  - Maps API Key en `local.properties` y `manifestPlaceholders`.
  - `google-services.json` en `app/` para Firebase.
- Gradle:
  - Plugin Google Services y BoM de Firebase.
  - Dependencias: Retrofit/OkHttp, Room, Hilt, Compose Material3, accompanist, Stripe, Maps Compose, FCM.

## Resultados y evidencias (capturas sugeridas)
1. Bienvenida (tema aplicado).
2. Dashboard con header fijo, buscador estilizado y sugerencias visibles.
3. Drawer con nombre y correo del usuario autenticado.
4. Skeleton shimmer en ofertas durante carga.
5. Búsqueda con loader y lista de sugerencias.
6. Animación de zoom al tocar imagen en ProductCard.
7. Carrito mostrando incrementos/decrementos y total.
8. Checkout Pago: método de pago (Stripe), resumen y PaymentSheet.
9. Confirmación y notificación local “Pedido confirmado”.
10. Historial de compras con encabezado y cards a ancho completo.
11. Ubicación: mapa con “Mi ubicación” activa.
12. Notificación FCM en background (payload `ORDER_CONFIRMED` / `OFFER`).

## Evaluación frente a la rúbrica
- Planificación y organización: Documentos claros, rutas y módulos uniformes.
- Arquitectura MVVM: Separación responsabilidades; pruebas simplificadas.
- Base de datos/APIs: Uso eficiente de Room y Retrofit; manejo de errores y tiempos.
- Servicios externos: Stripe, Firebase y Maps configurados correctamente.
- Diseño y usabilidad: Material Design consistente, accesos claros, transiciones fluidas.
- Presentación: documentación clara; evidencia suficiente con capturas.
- Funcionalidad general: flujo robusto sin errores críticos; buen rendimiento.

## Discusión
La combinación de Compose y MVVM permitió una construcción declarativa y reactiva de la UI. Stripe resolvió el pago con menor fricción y menor manejo directo de PCI. FCM aportó comunicación efectiva incluso en background. La integración de Maps fortaleció el componente de localización, y el uso apropiado de claves y placeholders evitó exposición de secretos.

## Conclusiones
El proyecto cumple el marco teórico y la rúbrica, acercándose a un producto listo para ampliaciones: direcciones avanzadas (Places), segmentación de ofertas vía FCM, pruebas de UI instrumentadas y optimizaciones de rendimiento. La arquitectura escogida facilita mantenimiento y escalabilidad.

## Referencias
- Android Developers. (s. f.). Jetpack Compose: Material Design. https://developer.android.com/jetpack/compose
- Stripe. (s. f.). Stripe Android SDK: PaymentSheet. https://docs.stripe.com/payments/accept-a-payment?platform=android
- Google. (s. f.). Maps Compose. https://developers.google.com/maps/documentation/android-sdk/renderer/compose
- Firebase. (s. f.). Add Firebase to your Android project. https://firebase.google.com/docs/android/setup
- Django REST Framework. (s. f.). API Guide. https://www.django-rest-framework.org/api-guide/
