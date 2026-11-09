# Plan de Implementación: Nuevo Minijuego "Pokewordle"

Crear un nuevo minijuego inspirado en Wordle adaptado a Pokémon ("Pokewordle"), con selección de Pokémon de hasta 7 letras, cuadrícula de intentos (5 filas x N columnas), teclado virtual personalizado en pantalla, validación de colores (Verde, Amarillo, Rojo) y diálogo de victoria/derrota.

## User Review Required

> [!NOTE]
> - El juego seleccionará aleatoriamente un Pokémon cuyo nombre tenga **7 letras o menos** (ej. Pikachu, Charmander, Ditto, Mew, etc.).
> - El teclado incluirá las letras del abecedario y el guión (`-`).
> - Se registrarán victorias y derrotas en `HomeStatsViewModel` al finalizar.

## Proposed Changes

### Navegación y Rutas

#### [MODIFY] [Screen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Navegation/Screen.kt)
- Añadir `object PokewordleGame : Screen("pokewordle_game")`.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/MainActivity.kt)
- Registrar la ruta `Screen.PokewordleGame.route` en el `NavHost`.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/main/HomeScreen.kt)
- Añadir el botón de acceso al minijuego Pokewordle en los retos libres (o diarios).

### ViewModel y Lógica del Juego

#### [NEW] [PokewordleViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/ViewModels/games/PokewordleViewModel.kt)
- Obtener Pokémon aleatorio de longitud $\le 7$ desde el repositorio.
- Gestionar matriz de intentos (5 filas, N columnas), fila actual, columna actual, y estado de letras en el teclado.
- Lógica de validación de palabras (Verde: letra y posición correctas; Amarillo: en la palabra pero otra posición; Rojo: no está en la palabra).

### Interfaz de Gráficos y Pantalla

#### [NEW] [PokewordleGame.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/games/PokewordleGame.kt)
- Cuadrícula de casillas con colores dinámicos.
- Teclado virtual personalizado (Letras + `-`).
- Botones "Borrar todo", "Borrar" y "Confirmar".
- Diálogo de victoria/derrota (`GameResultDialog`) y registro en `HomeStatsViewModel`.

## Verification Plan

### Automated Tests
- Compilación exitosa del proyecto (`app:assembleDebug`).

### Manual Verification
- Desplegar la app, entrar al minijuego Pokewordle, escribir letras con el teclado virtual, probar confirmación con colores verde/amarillo/rojo, y verificar comportamiento al ganar o perder tras 5 intentos.
