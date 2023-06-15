package com.example.unscrablenew.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import com.example.unscrablenew.data.englishWords
import com.example.unscrablenew.data.russianWords
import com.example.unscrablenew.model.GameUiState
import com.example.unscrablenew.model.Letter
import com.example.unscrablenew.util.Constants.MAX_NO_OF_WORDS
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(scrambledWord = "sss"))
    val uiState = _uiState.asStateFlow()

    private val _errorStream = MutableStateFlow(ErrorState())
    val errorStream = _errorStream.asStateFlow()
    private var state: ErrorState
        get() = _errorStream.value
        set(newState) {
            _errorStream.update { newState }
        }

    private val word: MutableStateFlow<String?> = MutableStateFlow(null)
    private val usedWords: MutableList<String> = mutableListOf()

    init {
        setNewWord()
        inflateFreeLetters()
        clearSelectedLetters()
    }

    fun handleGameEvent(gameEvent: GameEvent) {
        when(gameEvent) {
            is GameEvent.Submit -> { submit() }
            is GameEvent.Skip -> { skip() }
            is GameEvent.Revert -> { revert() }
            is GameEvent.Restart -> { restart() }
            is GameEvent.OnFreeLetterClick -> { onFreeLetterClick(gameEvent.letter) }
            is GameEvent.OnSelectedLetterClick -> { onSelectedLetterClick(gameEvent.letter) }
        }
    }

    private fun submit() {
        val playerWord = _uiState.value.selectedLetters.joinToString("")

        if (playerWord.isEmpty()) {
            state = state.copy(error = triggered(Errors.NoSelectedLetters))
        } else if (isWordCorrect(playerWord)) {
            increaseScore(playerWord.length)

            if (isGameOver(_uiState.value.wordCounter + 1)) {
                state = state.copy(error = triggered(Errors.GameOver))
            } else {
                increaseWordCounter()
                setNewWord()
                inflateFreeLetters()
                clearSelectedLetters()
            }
        } else {
            state = state.copy(error = triggered(Errors.WrongWord))
        }
    }

    private fun skip() {
        if (isGameOver(_uiState.value.wordCounter)) {
            state = state.copy(error = triggered(Errors.GameOver))
        } else {
            setNewWord()
            increaseWordCounter()
            inflateFreeLetters()
            clearSelectedLetters()
        }
    }

    private fun revert() {
        inflateFreeLetters()
        clearSelectedLetters()
    }

    private fun restart() {
        _uiState.value = GameUiState()
        setNewWord()
        clearSelectedLetters()
        inflateFreeLetters()
    }

    private fun onFreeLetterClick(letter: Letter) {
        _uiState.value.freeLetters.remove(letter)
        _uiState.value.selectedLetters.add(letter)
    }

    private fun onSelectedLetterClick(letter: Letter) {
        _uiState.value.selectedLetters.remove(letter)
        _uiState.value.freeLetters.add(letter)
    }

    private fun inflateFreeLetters() {
        // Don't ask me how it works, that's a fucking magic...
        _uiState.value = _uiState.value.copy(
            freeLetters = mutableStateListOf(
                *_uiState.value.scrambledWord
                    .toCharArray()
                    .map { Letter(it) }
                    .toTypedArray()
            )
        )
    }

    private fun clearSelectedLetters() {
        _uiState.value = _uiState.value.copy(
            selectedLetters = mutableStateListOf()
        )
    }

    private fun setNewWord() {
        word.value = getWord()
        val scrambledWord = getScrambledWord(word.value.toString())

        if (usedWords.contains(word.value)) {
            setNewWord()
        } else {
            _uiState.value = _uiState.value.copy(scrambledWord = scrambledWord)
            usedWords.add(word.value ?: "")
        }
    }

    private fun getWord(): String {
        return if (Locale.current.region == "RU") {
            russianWords.random()
        } else {
            englishWords.random()
        }
    }

    private fun getScrambledWord(word: String): String {
        val scrambledWord = word.toCharArray().apply { shuffle() }

        // Shuffle word if it does not distinct from original
        while (scrambledWord.toString() == word) {
            scrambledWord.shuffle()
        }

        return String(scrambledWord)
    }

    private fun isWordCorrect(playerWord: String): Boolean {
        if (playerWord == word.value) {
            return true
        }
        return false
    }

    private fun increaseWordCounter() {
        _uiState.value = _uiState.value.copy(
            wordCounter = _uiState.value.wordCounter + 1
        )
    }

    private fun increaseScore(wordLength: Int) {
        _uiState.value = _uiState.value.copy(
            score = _uiState.value.score + wordLength
        )
    }

    private fun isGameOver(currentScore: Int): Boolean {
        return currentScore >= MAX_NO_OF_WORDS
    }

    fun onConsumedError() {
        state = state.copy(error = consumed())
    }
}