package com.example.unscrablenew.viewmodels

sealed class Errors {
    object NoSelectedLetters: Errors()
    object WrongWord: Errors()
    object GameOver: Errors()
}
