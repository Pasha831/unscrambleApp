package com.example.unscrablenew.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class GameUiState(
    val scrambledWord: String = "",
    val wordCounter: Int = 1,
    val score: Int = 0,
    val freeLetters: SnapshotStateList<Letter> = mutableStateListOf(),
    val selectedLetters: SnapshotStateList<Letter> = mutableStateListOf(),
    val isGameOver: Boolean = false
)
