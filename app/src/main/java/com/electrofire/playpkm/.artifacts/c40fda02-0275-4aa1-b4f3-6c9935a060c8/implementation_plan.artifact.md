# Implementation Plan - Nationality Dropdown and Flag Overlay

Implement a nationality selection system using a `ExposedDropdownMenu` and display the corresponding flag over the user's Pokemon insignia in various screens.

## Proposed Changes

### Data Layer

#### [MODIFY] [UserData.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/Data/UserData.kt)
- Add `nationality: String? = null` to the `UserData` data class.

#### [MODIFY] [HomeStatsViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/ViewModels/main/HomeStatsViewModel.kt)
- Add `registrarNationality(nationality: String)` function.
- Update `guardarStats` and `cargarStats` to persist and retrieve the `nationality` field from Firestore.

### UI Components

#### [NEW] [NationalityUtils.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/NationalityUtils.kt)
- Define a data class `Nationality` with name and flag resource ID.
- Provide a list of all available nationalities based on the provided drawables.

#### [NEW] [NationalityDropdown.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/NationalityDropdown.kt)
- Create a reusable `NationalityDropdown` component using `ExposedDropdownMenuBox`.

#### [NEW] [PokemonWithFlag.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/PokemonWithFlag.kt)
- Create a reusable component that overlays a flag on top of a Pokemon image in a `Box`.

### Screens

#### [MODIFY] [NewUserScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/auth/NewUserScreen.kt)
- Integrate `NationalityDropdown` into the registration form.
- Update the preview to use `PokemonWithFlag`.
- Save nationality on confirm.

#### [MODIFY] [UserScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/main/UserScreen.kt)
- Use `PokemonWithFlag` for the main profile picture.
- Add `NationalityDropdown` inside the Settings dialog.

#### [MODIFY] [RankingList.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/RankingList.kt)
- Update `PodiumItem` and `RankingUserItem` to use `PokemonWithFlag` for user avatars.

## Verification Plan

### Manual Verification
- Open `NewUserScreen`, select a nationality, and complete registration.
- Verify the flag appears on the user's insignia in `UserScreen`.
- Open settings in `UserScreen`, change nationality, and verify it updates.
- Check `RankingScreen` to ensure other users (if they have nationality) display their flags correctly.
