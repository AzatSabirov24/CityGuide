package com.asabirov.search.domain.di

import com.asabirov.search.domain.repository.SearchRepository
import com.asabirov.search.domain.use_case.PlaceDetails
import com.asabirov.search.domain.use_case.SearchMorePlacesForMap
import com.asabirov.search.domain.use_case.SearchPlacesPaginated
import com.asabirov.search.domain.use_case.SearchUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SearchDomainModule {

    @ViewModelScoped
    @Provides
    fun provideSearchUseCases(
        repository: SearchRepository
    ): SearchUseCases {
        return SearchUseCases(
            searchPlacesPaginated = SearchPlacesPaginated(repository = repository),
            searchMorePlacesForMap = SearchMorePlacesForMap(repository = repository),
            placeDetails = PlaceDetails(repository = repository)
        )
    }
}