package com.asabirov.core.utils.event

sealed interface UiEvent {
    data object Success : UiEvent
    data object NavigateUp : UiEvent
    data class ShowSnackbar(val message: UiText) : UiEvent
}
