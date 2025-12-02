package com.inik.dynamicadview.di

import android.content.Context
import com.google.gson.Gson
import com.inik.dynamicadview.data.datasource.AdDataSource
import com.inik.dynamicadview.data.datasource.LocalAdDataSource
import com.inik.dynamicadview.data.datasource.RemoteAdDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
     * Gson 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    /**
     * 로컬 광고 데이터 소스 제공 (JSON 파일)
     */
    @Provides
    @Singleton
    fun provideLocalAdDataSource(
        @ApplicationContext context: Context,
        gson: Gson
    ): LocalAdDataSource {
        return LocalAdDataSource(context, gson)
    }

    /**
     * 광고 데이터 소스 제공 (메모리 캐시)
     */
    @Provides
    @Singleton
    fun provideAdDataSource(
        localAdDataSource: LocalAdDataSource
    ): AdDataSource {
        return AdDataSource(localAdDataSource)
    }

    /**
     * 원격 광고 데이터 소스 제공 (나중에 실제 네트워크 통신으로 교체)
     */
    @Provides
    @Singleton
    fun provideRemoteAdDataSource(): RemoteAdDataSource {
        return RemoteAdDataSource()
    }
}
