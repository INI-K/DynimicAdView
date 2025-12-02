package com.inik.dynamicadview.di

import com.inik.dynamicadview.data.datasource.AdDataSource
import com.inik.dynamicadview.data.datasource.RemoteAdDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataSource 의존성 주입 모듈
 * 로컬 및 원격 데이터 소스 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    /**
     * 로컬 광고 데이터 소스 제공
     */
    @Provides
    @Singleton
    fun provideAdDataSource(): AdDataSource {
        return AdDataSource()
    }

    /**
     * 원격 광고 데이터 소스 제공
     */
    @Provides
    @Singleton
    fun provideRemoteAdDataSource(): RemoteAdDataSource {
        return RemoteAdDataSource()
    }
}
