package com.example.unscrablenew.viewmodels

import com.example.unscrablenew.model.Letter

sealed class GameEvent {
    object Skip: GameEvent()
    object Submit: GameEvent()
    object Revert: GameEvent()
    object Restart: GameEvent()
    class OnFreeLetterClick(val letter: Letter): GameEvent()
    class OnSelectedLetterClick(val letter: Letter): GameEvent()
}
