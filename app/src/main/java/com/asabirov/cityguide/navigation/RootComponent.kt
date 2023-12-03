package com.asabirov.cityguide.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.asabirov.search.presentation.navigation.PlaceDetailsComponent
import com.asabirov.search.presentation.navigation.SearchComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SearchScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            is Configuration.SearchScreen -> Child.SearchScreen(
                SearchComponent(
                    componentContext = context,
                    onNavigateToPlaceDetails = { placeId ->
                        navigation.pushNew(Configuration.PlaceDetailsScreen(placeId))
                    }
                )
            )
            is Configuration.PlaceDetailsScreen-> Child.PlaceDetailsScreen(
                PlaceDetailsComponent(
                    placeId = config.placeId,
                    componentContext = context
                )
            )
        }
    }

    sealed class Child {
        data class SearchScreen(val component: SearchComponent): Child()
        data class PlaceDetailsScreen(val component: PlaceDetailsComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object SearchScreen: Configuration()

        @Serializable
        data class PlaceDetailsScreen(val placeId: String): Configuration()
    }
}