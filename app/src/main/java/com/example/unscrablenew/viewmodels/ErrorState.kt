package com.example.unscrablenew.viewmodels

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ErrorState(
    val error: StateEventWithContent<Errors> = consumed()
)
