package com.electrofire.playpkm.ui.ViewModels.games

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import com.electrofire.playpkm.Domain.normalizar
import com.electrofire.playpkm.ui.ViewModels.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

enum class LetterColor {
    GREEN, YELLOW, RED, UNUSED
}

sealed class PokewordleGameState {
    object Playing : PokewordleGameState()
    object Won : PokewordleGameState()
    object Lost : PokewordleGameState()
}

@HiltViewModel
class PokewordleViewModel @Inject constructor(
    private val repo: PokemonApiRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<PokemonApi>>(UIState.Loading)
    val state: StateFlow<UIState<PokemonApi>> = _state

    var targetWord by mutableStateOf("")
        private set

    var wordLength by mutableIntStateOf(5)
        private set

    var guesses = mutableStateOf(listOf<MutableList<Char>>())
        private set

    var guessColors = mutableStateOf(listOf<MutableList<LetterColor>>())
        private set

    var currentRow by mutableIntStateOf(0)
        private set

    var currentCol by mutableIntStateOf(0)
        private set

    var gameStatus by mutableStateOf<PokewordleGameState>(PokewordleGameState.Playing)
        private set

    var keyboardColors = mutableStateOf(mutableMapOf<Char, LetterColor>())
        private set

    init {
        loadGame()
    }

    fun loadGame() {
        viewModelScope.launch {
            _state.value = UIState.Loading
            try {
                var pokemon: PokemonApi? = null
                var cleanName = ""
                while (pokemon == null || cleanName.length !in 3..7) {
                    pokemon = repo.obtenerPokemonRandom()
                    if (pokemon != null) {
                        cleanName = pokemon.name
                            .normalizar()
                            .replace("-", "")
                            .replace(" ", "")
                            .uppercase()
                    }
                }

                targetWord = cleanName
                wordLength = cleanName.length

                guesses.value = List(5) { MutableList(wordLength) { ' ' } }
                guessColors.value = List(5) { MutableList(wordLength) { LetterColor.UNUSED } }
                currentRow = 0
                currentCol = 0
                gameStatus = PokewordleGameState.Playing
                keyboardColors.value = mutableMapOf()

                _state.value = UIState.Success(pokemon)
            } catch (e: IOException) {
                _state.value = UIState.Error("Sin conexión a internet.")
            } catch (e: Exception) {
                Log.e("POKEWORDLE_VM", "Error: ${e.message}")
                _state.value = UIState.Error("Error inesperado al cargar el juego.")
            }
        }
    }

    fun appendLetter(char: Char) {
        if (gameStatus !is PokewordleGameState.Playing) return
        if (currentCol < wordLength) {
            val updated = guesses.value.map { it.toMutableList() }
            updated[currentRow][currentCol] = char
            guesses.value = updated
            currentCol++
        }
    }

    fun deleteLetter() {
        if (gameStatus !is PokewordleGameState.Playing) return
        if (currentCol > 0) {
            currentCol--
            val updated = guesses.value.map { it.toMutableList() }
            updated[currentRow][currentCol] = ' '
            guesses.value = updated
        }
    }

    fun deleteAll() {
        if (gameStatus !is PokewordleGameState.Playing) return
        val updated = guesses.value.map { it.toMutableList() }
        for (i in 0 until wordLength) {
            updated[currentRow][i] = ' '
        }
        guesses.value = updated
        currentCol = 0
    }

    fun confirmGuess() {
        if (gameStatus !is PokewordleGameState.Playing) return
        if (currentCol < wordLength) return

        val currentGuessStr = guesses.value[currentRow].joinToString("")
        val targetChars = targetWord.toMutableList()
        val guessChars = currentGuessStr.toMutableList()
        val rowColors = MutableList(wordLength) { LetterColor.RED }
        val newKeyboardColors = keyboardColors.value.toMutableMap()

        // 1. Green
        for (i in 0 until wordLength) {
            if (guessChars[i] == targetChars[i]) {
                rowColors[i] = LetterColor.GREEN
                newKeyboardColors[guessChars[i]] = LetterColor.GREEN
                targetChars[i] = '*'
                guessChars[i] = '#'
            }
        }

        // 2. Yellow
        for (i in 0 until wordLength) {
            if (guessChars[i] != '#') {
                val index = targetChars.indexOf(guessChars[i])
                if (index != -1) {
                    rowColors[i] = LetterColor.YELLOW
                    if (newKeyboardColors[guessChars[i]] != LetterColor.GREEN) {
                        newKeyboardColors[guessChars[i]] = LetterColor.YELLOW
                    }
                    targetChars[index] = '*'
                } else {
                    rowColors[i] = LetterColor.RED
                    if (newKeyboardColors[guessChars[i]] == null || newKeyboardColors[guessChars[i]] == LetterColor.UNUSED) {
                        newKeyboardColors[guessChars[i]] = LetterColor.RED
                    }
                }
            }
        }

        keyboardColors.value = newKeyboardColors
        val colorsUpdated = guessColors.value.map { it.toMutableList() }.toMutableList()
        colorsUpdated[currentRow] = rowColors
        guessColors.value = colorsUpdated

        if (currentGuessStr == targetWord) {
            gameStatus = PokewordleGameState.Won
        } else if (currentRow < 4) {
            currentRow++
            currentCol = 0
        } else {
            gameStatus = PokewordleGameState.Lost
        }
    }
}
