package com.inik.dynamicadview.di

import com.inik.dynamicadview.data.datasource.AdDataSource
import com.inik.dynamicadview.data.repository.AdRepositoryImpl
import com.inik.dynamicadview.domain.repository.AdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository 의존성 주입 모듈
 * Domain Layer의 Repository 인터페이스에 대한 구현체 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * AdRepository 제공
     * Domain Layer의 인터페이스를 Data Layer의 구현체와 연결
     */
    @Provides
    @Singleton
    fun provideAdRepository(
        dataSource: AdDataSource
    ): AdRepository {
        return AdRepositoryImpl(dataSource)
    }
}
