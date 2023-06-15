package com.example.unscrablenew.model

data class Letter(
    val letter: Char = ' '
) {
    override fun toString(): String {
        return letter.toString()
    }
}
