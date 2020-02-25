package com.lab.sports.utils

data class AppNetworkState (var state: State, var value: String?){
    enum class State {
        LOADING,
        LOADED,
        FAILED,
        NAVIGATE
    }
}
