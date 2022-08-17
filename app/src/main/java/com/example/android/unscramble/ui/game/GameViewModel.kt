package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    // For Talkback: service will read it character by character
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    private val _currentFreeLetters = MutableLiveData(mutableListOf<Char>())
    val currentFreeLetters: LiveData<MutableList<Char>>
        get() = _currentFreeLetters

    private val _currentSelectedLetters = MutableLiveData(mutableListOf<Char>())
    val currentSelectedLetters: LiveData<MutableList<Char>>
        get() = _currentSelectedLetters

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord  = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    fun addNewSelectedLetter(newLetter: Char) {
        _currentSelectedLetters.value?.add(newLetter)
        _currentSelectedLetters.value = currentSelectedLetters.value
        Log.d("lol", "i added $newLetter inside ViewModel to CSL, my size: ${_currentSelectedLetters.value?.size}")
    }

    fun addNewFreeLetter(newLetter: Char) {
        _currentFreeLetters.value?.add(newLetter)
        _currentFreeLetters.value = currentFreeLetters.value
        Log.d("lol", "i added $newLetter inside ViewModel to NFL, my size: ${_currentFreeLetters.value?.size}")
    }

    fun addAllNewFreeLetters(allNewLetters: MutableList<Char>) {
        deleteAllFreeLetters()
        _currentFreeLetters.value?.addAll(allNewLetters)
    }

    fun deleteAllSelectedLetters() {
        _currentSelectedLetters.value?.clear()
    }

    fun deleteAllFreeLetters() {
        _currentFreeLetters.value?.clear()
    }

    init {
        getNextWord()
    }
}