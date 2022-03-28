package com.dekola.githublogin.di

import com.dekola.githublogin.api.GithubSearchService
import com.dekola.githublogin.data.repository.GithubSearchRepository
import com.dekola.githublogin.data.repository.IGithubSearchRepository
import com.dekola.githublogin.ui.GithubSearchViewModel
import com.dekola.githublogin.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GithubSearchModule {

    @Provides
    fun provideViewModel(
        repository: IGithubSearchRepository,
        networkConnectivity: NetworkConnectivity,
    ): GithubSearchViewModel {
        return GithubSearchViewModel(repository, networkConnectivity)
    }

    @Provides
    fun provideRepository(
        ioDispatcher: CoroutineDispatcher,
        githubSearchService: GithubSearchService,
    ): IGithubSearchRepository {
        return GithubSearchRepository(ioDispatcher, githubSearchService)
    }

    @Provides
    @Singleton
    fun provideGithubSearchService(retrofit: Retrofit): GithubSearchService =
        retrofit.create(GithubSearchService::class.java)

}
