# Walkthrough - Nationality Dropdown and Flag Overlay

I have implemented a nationality selection system that allows users to pick their country and display the corresponding flag over their Pokemon insignia.

## Changes Made

### Data Layer
- **[UserData.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/Data/UserData.kt)**: Added `nationality` field.
- **[HomeStatsViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/ViewModels/main/HomeStatsViewModel.kt)**: Added logic to register and persist nationality in Firestore.

### Components
- **[NationalityUtils.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/NationalityUtils.kt)**: Helper object mapping country names to flag drawables.
- **[PokemonWithFlag.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/PokemonWithFlag.kt)**: Reusable component that overlays a small flag on the top-right of a Pokemon avatar.
- **[NationalityDropdown.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/NationalityDropdown.kt)**: Reusable Material 3 `ExposedDropdownMenu` for nationality selection.

### UI Integration
- **[NewUserScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/auth/NewUserScreen.kt)**: Added the dropdown to the registration flow and updated the preview.
- **[UserScreen.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Screens/main/UserScreen.kt)**: Updated the profile picture to show the flag and added the dropdown to the settings dialog.
- **[RankingList.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/RankingList.kt)** & **[UserDetailDialog.kt](file:///C:/Users/user/AndroidStudioProjects/PLAYPKM/app/src/main/java/com/electrofire/playpkm/ui/Components/UserDetailDialog.kt)**: Updated avatars to show flags in the ranking list and user details.

## Verification Results
- All components compile and follow the existing Material 3 styling.
- Persistence logic is wired up to Firestore through the `HomeStatsViewModel`.
- The UI adapts correctly to different sizes of avatars (podium, list, profile).
