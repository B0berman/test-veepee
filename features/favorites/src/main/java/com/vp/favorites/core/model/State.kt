package com.vp.favorites.core.model

sealed class State {

    object Loading : State()
    object Error : State()
    object Success : State()

}