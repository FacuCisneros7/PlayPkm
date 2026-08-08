# Optimización de Lecturas en Firestore

Este plan tiene como objetivo reducir drásticamente el consumo de lecturas en Firestore, optimizando la gestión de intentos de juego, la carga de avatares y el acceso a los rankings, manteniendo la restricción de "una vez al día".

## User Review Required

> [!IMPORTANT]
> Para eliminar las lecturas de la colección `ImagenesDePerfil`, moveremos los avatares a una lista estática en el código. Si planeas añadir avatares frecuentemente sin actualizar la app, esta opción podría no ser ideal, pero es la que más ahorra dinero.

## Proposed Changes

### 1. Centralización de Intentos de Juego (`GameAttempts`)

Actualmente, cada vez que el usuario hace clic en un juego en el Home, se dispara una lectura `Source.SERVER`. Esto genera un "goteo" constante de lecturas.

#### [MODIFY] [HomeStatsViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/ViewModels/HomeStatsViewModel.kt)
- Añadir un estado `gameAttempts` de tipo `GameAttempts?`.
- Cargar los intentos una sola vez dentro de `cargarStats()`.
- Implementar una función `verificarAccesoJuego(gameId: String, onResult: (Boolean) -> Unit)` que use los datos ya cargados en memoria y los compare con la hora del servidor.

#### [MODIFY] [GameAttemptsRepository.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/Data/Repository/GameAttemptsRepository.kt)
- Refactorizar para que solo se encargue de la **escritura** (marcar juego como jugado) y de la lectura inicial (sin forzar `Source.SERVER`).
- Eliminar la lógica redundante de comprobación en cada clic.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/HomeScreen.kt)
- Cambiar las llamadas directas al `repo` por consultas al `statsViewModel`.

---

### 2. Optimización de Avatares

Eliminaremos las lecturas a la colección `ImagenesDePerfil`.

#### [NEW] [AvatarData.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/Data/AvatarData.kt)
- Crear un objeto estático con la lista de URLs y nombres de los Pokémon disponibles como avatares.

#### [MODIFY] [ChoiseImage.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/ChoiseImage.kt)
- Eliminar el `LaunchedEffect` que consulta Firestore y usar la lista estática de `AvatarData`.

---

### 3. Optimización de Rankings

#### [MODIFY] [RankingViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/ViewModels/RankingViewModel.kt) (y otros rankings)
- Aumentar el tiempo de caché de 30 minutos a 2 horas para evitar lecturas innecesarias si el usuario entra y sale de la pantalla de ranking.

## Verification Plan

### Manual Verification
1. **Intentos**: Entrar al Home, verificar que solo se hace una lectura inicial de `attempts`. Jugar un juego y verificar que el botón se deshabilita localmente sin disparar otra lectura de verificación.
2. **Avatares**: Abrir la selección de imagen y comprobar que aparecen instantáneamente sin rastro de actividad de red hacia Firestore.
3. **Rankings**: Navegar entre pestañas de ranking y verificar que no se refrescan si no ha pasado el tiempo estipulado.
