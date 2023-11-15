package com.asabirov.core.utils.coroutine_scope

import androidx.compose.runtime.RememberObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive


class ActiveCoroutineHandler(val scope: CoroutineScope) : RememberObserver {

    init {
        println("qqq ActiveCoroutineScopeHandler->init")
    }

    override fun onRemembered() {
        println("qqq ActiveCoroutineScopeHandler->onRemembered -> ${scope.isActive}")

    }

    override fun onForgotten() {
        println("qqq ActiveCoroutineScopeHandler->onForgotten -> ${scope.isActive}")
    }

    override fun onAbandoned() {
        println("qqq ActiveCoroutineScopeHandler->onAbandoned -> ${scope.isActive}")
    }
}