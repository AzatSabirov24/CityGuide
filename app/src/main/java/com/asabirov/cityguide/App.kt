package com.asabirov.cityguide

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.asabirov.cityguide.navigation.RootComponent
import com.asabirov.search.presentation.place_details.screen.PlaceDetailsScreen
import com.asabirov.search.presentation.search.screen.SearchScreen

@Composable
fun App(root: RootComponent) {
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()
        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when(val instance = child.instance) {
                is RootComponent.Child.SearchScreen-> SearchScreen(instance.component)
                is RootComponent.Child.PlaceDetailsScreen -> PlaceDetailsScreen(instance.component.text, instance.component)
            }
        }
    }
}