package com.asabirov.search.domain.di

import com.asabirov.search.domain.repository.SearchRepository
import com.asabirov.search.domain.use_case.SearchByText
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
            searchByText = SearchByText(repository = repository)
        )
    }
}