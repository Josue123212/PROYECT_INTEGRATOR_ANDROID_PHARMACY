# Replicación de Farmacia Web en Android (Marco Teórico 15 Semanas)

## Arquitectura Fuente (Web)
- Backend: Django REST Framework con endpoints bajo `api/`.
- Dominios clave: `ecommerce` (productos, carrito, órdenes), `users` (auth), `notifications`.
- Ofertas: cálculo server-side de `sale_active` y ventana `valid_from/valid_until`.
- Referencias:
  - Productos: `FARMACIA-WEB/backend/apps/ecommerce/serializers.py:52–60`.
  - Precio y oferta: `FARMACIA-WEB/backend/apps/ecommerce/serializers.py:11–44`.
  - Carrito (API): `FARMACIA-WEB/backend/apps/ecommerce/views.py:70–112`.
  - Checkout/órdenes: `FARMACIA-WEB/backend/apps/ecommerce/views.py:287–333`.
  - Auth actual: `users/auth/login`, `users/auth/refresh`, `users/me` (ya usados en la app).

## Stack del Front Android (Alineado al Marco)
- UI: Jetpack Compose + Material 3.
- Arquitectura: MVVM con Hilt DI y `StateFlow`.
- Networking: Retrofit + OkHttp (`AuthInterceptor` + `JwtAuthenticator`).
- Persistencia: Room (DAO/Entities) para cache local y carrito.
- Concurrencia: Coroutines (`Dispatchers.IO`, `viewModelScope`).
- Servicios externos: Firebase (FCM/Auth) en Semanas 12–13.
- Pruebas: Espresso UI (Semana 15).

## Mapeo de Endpoints Web → Android
- Base: `BuildConfig.BASE_URL + "api/"` (ya configurado en `NetworkModule`).
- Productos:
  - `GET ecommerce/products/` paginado.
  - `GET ecommerce/products/{id}/` detalle.
- Carrito:
  - `GET ecommerce/cart/` lista del carrito.
  - `POST ecommerce/cart/items/` agregar ítem.
  - `POST ecommerce/cart/clear/` vaciar carrito.
- Órdenes (fase 1, mock):
  - `POST ecommerce/checkout/` crea orden confirmada (según backend)
- Auth:
  - `POST users/auth/login/`, `POST users/auth/refresh/`, `GET users/me/`.

## Cambios Necesarios en FARMACIA-APP
- ApiService (Networking)
  - Añadir endpoints de carrito y checkout:
    - `getCart()`, `addCartItem(body)`, `clearCart()` [hecho]
    - `checkout()` (fase siguiente, opcional Semana 9–10)
- DTOs (Serialización)
  - Crear `AddItemRequest`, `CartItemDto`, `CartDto` [hecho]
  - Verificar `ProductDto` refleje `price.sale_active` para UI de oferta.
- Repositorios
  - `RemoteProductRepository`: mantener paginación y errores.
  - `HybridProductRepository` (sugerido):
    - Leer de Room (`ProductDao`) y refrescar con Retrofit.
    - Escribir en cache tras cada fetch.
  - `CartRepositoryImpl`:
    - Room como fuente local (ya).
    - Sincronizar con API si hay token (`add`, `clear`) [hecho].
    - Extender `increase/decrease` para API si se requiere.
- Categorías (Alineación Front)
  - ApiService: `GET ecommerce/categories/` [hecho]
  - DTO/Modelo: `CategoryDto` y `Category` [hecho]
  - Repositorio: `CategoryRepository` + `RemoteCategoryRepository` [hecho]
  - ViewModel: `CatalogViewModel` mantiene `categories` y `selectedCategoryId`, y filtra productos por categoría [hecho]
  - UI: `CatalogScreen` muestra chips “Todas” + categorías y llama `onSelectCategory` [hecho]
- ViewModels
  - `CatalogViewModel`: estados (`isLoading`, `error`) y paginación ya implementados.
  - `CartViewModel`: acciones integradas (local + remoto si autenticado) [hecho].
 - UI Compose
  - `ProductCard`: badge “En oferta” y precio promocional con tachado del base [hecho].
  - Pantallas: Home/Catálogo, Detalle, Carrito.
  - Estados de carga/error consistentes.
- Persistencia (Room)
  - Definir `ProductEntity`, `ProductDao` y migraciones para cache.
  - Mantener `CartDao` y entidades actuales.
- Seguridad
  - Confirmar `AuthInterceptor` y `JwtAuthenticator` activos (ya).
  - Redactar header `Authorization` en logs (ya).
- Servicios Externos (Semanas 12–13)
  - Integrar FCM para notificaciones de confirmación de orden.
  - Registrar token FCM en backend si se habilita endpoint.
- WebView (Semana 14)
  - Pantalla con contenido web útil (landing/ayuda) dentro de la app.
- Pruebas (Semana 15)
  - Espresso: flujo catálogo→detalle→carrito→checkout (mock).
  - Instrumentadas para `TokenManager` y navegación básica.

## Detalle de Implementación Sugerida (Paso a Paso)
1. Networking y DTOs
   - ApiService: rutas y firmas de carrito [hecho].
   - Añadir `checkout()` [hecho].
2. Cache Offline de Productos (Room)
   - Crear `ProductEntity` con campos de `ProductDto`.
   - `ProductDao`: `insertAll`, `observeAll`, `getById`.
   - `HybridProductRepository`: primero cache, luego red remoto y persistir.
3. Carrito Sincronizado
   - Extender `increase/decrease` a API opcional (mantener dentro de marco teórico si se requiere).
   - `CartViewModel`: acciones y estado `items` con Flow de Room.
4. UI Compose
   - Componentes reutilizables (cards, badges, modales de cantidad).
   - Estados de carga/error accesibles.
5. Seguridad
   - Mantener refresh automático ante 401 (`JwtAuthenticator`).
   - Almacenamiento seguro (EncryptedSharedPreferences opcional).
6. Servicios Externos
   - FCM básico para notificaciones locales/push (Semanas 12–13).
7. Pruebas
   - Espresso para validar pantallas y flujos principales.

## Consideraciones de Oferta
- La app solo consume `sale_active` desde backend (no controla expiración).
- Mostrar fecha fin con `price.sale_ends_at` si se desea.

## Referencias Clave en Web
- `FARMACIA-WEB/backend/apps/ecommerce/serializers.py:11–44` (precio y oferta).
- `FARMACIA-WEB/backend/apps/ecommerce/views.py:70–112` (carrito API).
- `FARMACIA-WEB/backend/apps/ecommerce/views.py:287–333` (checkout/órdenes).

## Alcance y Marco
- Todo se mantiene dentro de: Kotlin/Compose, MVVM, Room, Retrofit, Coroutines, Firebase y Espresso.
- No se introduce tecnología fuera del temario.
