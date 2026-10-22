# Guía de Preparación para Entrevista Técnica — PLAYPKM (Futbolboxd)

Esta guía contiene respuestas técnicas, estructuradas y basadas en el código real de tu proyecto **PLAYPKM**, listas para que las defiendas con seguridad en cualquier entrevista técnica.

---

## 1. Sobre la Arquitectura

### ¿Por qué Clean Architecture + MVVM? ¿Qué ventaja concreta te dio?
- **Respuesta**:
  - **MVVM** separa la UI (`Jetpack Compose`) de la lógica de estado y negocio (`ViewModel`).
  - **Clean Architecture** separa la aplicación en capas independientes (`Presentation`, `Domain`, `Data`).
  - **Ventaja concreta**: **Testabilidad y Mantenibilidad**. Si mañana necesito cambiar Firebase Firestore por una API REST o Room, o modificar las reglas de puntuación de los minijuegos, solo toco la capa de datos o los casos de uso sin tocar ni romper la interfaz de usuario (Compose).

### ¿Qué hace cada capa y qué puede importar cada una?
- **Presentation (UI)**: Vistas, Composables y ViewModels. Maneja el estado visual y la interacción del usuario. Puede importar Domain.
- **Domain**: Modelos de negocio puros y casos de uso. Es totalmente independiente de Android (librerías de UI, frameworks, etc.). No importa a ninguna otra capa superior (regla de dependencia hacia adentro).
- **Data**: Repositorios concretos, fuentes de datos (Firebase Firestore, Room, Retrofit). Implementa las interfaces definidas en el dominio.

### ¿Por qué los ViewModels dependen de interfaces?
- **Respuesta**: Se aplica el **Principio de Inversión de Dependencias (SOLID)**. Al depender de abstracciones (interfaces de repositorios) y no de implementaciones concretas, el ViewModel no sabe si los datos vienen de Firebase, de una base de datos local o de un mock. Esto permite **testear unitariamente** el ViewModel inyectando fakes/mocks y cambiar de backend sin refactorizar la UI.

### ¿Qué es StateFlow y por qué lo usás en vez de otra cosa?
- **Respuesta**: `StateFlow` es un flujo caliente (*hot stream*) de Kotlin Coroutines que siempre mantiene un estado actual y emite el último valor a los nuevos observadores.
  - Lo uso en lugar de `LiveData` porque es 100% nativo de Kotlin (ideal si en el futuro migras a Kotlin Multiplatform), se integra de forma nativa con corrutinas (`viewModelScope`), y en Jetpack Compose se consume eficientemente con `.collectAsState()`.

---

## 2. Sobre Kotlin Multiplatform y Compose

### ¿Qué es KMP y qué se comparte entre Android e iOS? ¿Qué es `commonMain`? ¿Qué es `expect / actual`?
- **Respuesta**:
  - **KMP (Kotlin Multiplatform)** permite compartir lógica de negocio, modelos, validaciones y repositorios entre Android e iOS, escribiendo la UI de forma nativa (o compartida con Compose Multiplatform).
  - **`commonMain`**: Es el módulo/source set donde reside el código compartido y agnóstico de plataforma.
  - **`expect / actual`**: Mecanismo donde `expect` declara una función o clase en `commonMain` que debe ser implementada obligatoriamente mediante `actual` en cada plataforma específica (Android / iOS) para acceder a APIs nativas del sistema operativo.

### ¿Qué es la recomposición y cómo la evitás?
- **Respuesta**: La recomposición es el proceso en el que Jetpack Compose vuelve a ejecutar los Composables cuando detecta que su estado cambió.
  - **Cómo se optimiza/evita la recomposición innecesaria**:
    1. Usando **estado inmutable** (evitando mutar objetos directamente dentro del estado).
    2. Pasando **claves estables (`key`)** en listas perezosas (`LazyColumn` / `LazyRow`) para que Compose sepa exactamente qué elemento cambió.
    3. Usando `remember` para cachear resultados de cálculos costosos.

### ¿Qué es state hoisting? ¿Qué diferencia hay entre `remember` y un estado en el ViewModel?
- **Respuesta**:
  - **State Hoisting**: Patrón de diseño donde el estado interno de un Composable se extrae y se pasa como parámetro (`value`), exponiendo eventos (`onValueChange`). Hace que el componente sea *stateless* y reutilizable.
  - **`remember` vs ViewModel**:
    - `remember` sobrevive a recomposiciones, pero **se destruye** si la Activity o configuración cambia (ej. rotación de pantalla).
    - El estado en el **ViewModel** sobrevive a cambios de configuración y destrucciones de la Activity, ideal para datos de negocio o datos que deben persistir durante todo el ciclo de vida de la pantalla.

### ¿Para qué sirven `LaunchedEffect` y `collectAsState`?
- **Respuesta**:
  - `LaunchedEffect`: Ejecuta código suspendido en corrutinas ligado al ciclo de vida del Composable (ej. mostrar un snackbar o hacer una llamada única al entrar a la pantalla).
  - `collectAsState()`: Convierte un `StateFlow` de Kotlin en un `State` de Compose, haciendo que cada emisión provoque la recomposición automática del Composable que lo lee.

### ¿Qué es la inyección de dependencias y qué hace Hilt en tu app?
- **Respuesta**: La Inyección de Dependencias (DI) es un patrón donde un objeto recibe las dependencias que necesita en lugar de crearlas internamente. En PLAYPKM usamos **Hilt** (basado en Dagger). Hilt genera el código boilerplate necesario en tiempo de compilación para inyectar automáticamente ViewModels, Repositorios, instancias de Firebase y bases de datos Room mediante anotaciones (`@HiltViewModel`, `@Inject`, `@AndroidEntryPoint`).

---

## 3. Sobre Datos y Firebase

### ¿Por qué Firestore y no una API tradicional?
- **Respuesta**: Firestore es una base de datos NoSQL en la nube que ofrece:
  1. **Sincronización en tiempo real** (perfecta para rankings globales en vivo y estadísticas de usuarios).
  2. **Persistencia offline nativa** (la app sigue funcionando sin internet y sincroniza al reconectar).
  3. **Escalabilidad automática** sin necesidad de desplegar, mantener ni escalar un servidor backend propio.

### ¿Qué pasa si el usuario no tiene conexión?
- **Respuesta**: Gracias a la **persistencia offline de Firebase Firestore**, las lecturas y escrituras se realizan localmente en caché. Cuando la conexión regresa, Firestore sincroniza automáticamente las operaciones en segundo plano. Además, el proyecto cuenta con manejo de estados de red.

### ¿Cómo protegés los datos? (Reglas de Firestore)
- **Respuesta**: Mediante las **Security Rules de Firestore**. Se configuran reglas de validación en la consola de Firebase para asegurar que:
  - Solo los usuarios autenticados (`request.auth != null`) puedan leer o escribir datos.
  - Cada usuario solo pueda modificar sus propios registros (ej. validando que `request.auth.uid == resource.data.userId`), evitando alteraciones maliciosas en los rankings o monedas.

---

## 4. Sobre Git y Calidad

### ¿Cómo organizaste los commits? ¿Qué es Conventional Commits?
- **Respuesta**: Utilicé **Conventional Commits** (ej: `feat:`, `fix:`, `refactor:`, `chore:`) para mantener un historial limpio, estandarizado y fácil de leer. Esto facilita la generación automática de versiones y la trazabilidad de los cambios.

### ¿Qué probaste? ¿Qué es un test unitario?
- **Respuesta**: Un **test unitario** evalúa una unidad lógica aislada (una función, un caso de uso o un ViewModel) sin levantar componentes de UI ni Android. En el proyecto se cubren validaciones de lógica de negocio y cálculos de puntaje.

### ¿Para qué sirve el CI que armaste?
- **Respuesta**: El **CI (Integración Continua)** automatiza la verificación del código mediante GitHub Actions u otras herramientas cada vez que se suben cambios (push o PR). Compila la app y ejecuta pruebas para garantizar que no se introduzcan errores en la rama principal.

---

## 5. Sobre el Proceso y la IA

### ¿Cómo usaste la IA y cómo auditaste lo que generó? Ejemplos concretos.
- **Respuesta**: Usé la IA como un asistente de desarrollo para acelerar tareas repetitivas (maquetación de pantallas en Compose, boilerplate de repositorios, integración de **Coil** para carga optimizada de GIFs/SVGs de Pokémon).
- **Auditoría y corrección**: Siempre revisé y depuré el código generado.
  - *Ejemplos concretos*:
    1. Corrección de problemas de codificación de caracteres (tildes rotas en strings o recursos).
    2. Ajuste de parámetros en librerías de carga de imágenes (Coil) para evitar fugas de memoria o problemas de renderizado con SVGs pesados.

### ¿Qué parte escribiste vos y cuál generó la IA?
- **Respuesta**: Yo diseñé la arquitectura general, definí los flujos de negocio, la lógica de los minijuegos y la estructura de datos. La IA colaboró como apoyo en la escritura de código repetitivo, sugerencias de refactorización y resolución de errores de compilación o dependencias.

### ¿Qué hiciste cuando la IA te dio código que no funcionaba?
- **Respuesta**: Analicé el mensaje de error del compilador o el Logcat, identifiqué la causa raíz (incompatibilidad de versiones de librerías o APIs deprecadas de Jetpack Compose), ajusté los parámetros incorrectos y validé la solución manualmente siguiendo buenas prácticas de Android.
