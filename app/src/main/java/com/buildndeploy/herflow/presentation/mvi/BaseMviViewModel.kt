package com.buildndeploy.herflow.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<Intent : MviIntent, State : MviState, Effect : MviEffect>(
    initialState: State
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun dispatchIntent(intent: Intent) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected suspend fun postEffect(effect: Effect) {
        _effect.send(effect)
    }

    protected abstract suspend fun handleIntent(intent: Intent)
}
