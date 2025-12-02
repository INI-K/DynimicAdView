package com.inik.dynamicadview.di

import com.inik.dynamicadview.domain.repository.AdRepository
import com.inik.dynamicadview.domain.usecase.CalculateWeightUseCase
import com.inik.dynamicadview.domain.usecase.GetAdContentUseCase
import com.inik.dynamicadview.domain.usecase.UpdateAdWeightUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * UseCase 의존성 주입 모듈
 * 비즈니스 로직을 담당하는 UseCase 제공
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    /**
     * 광고 콘텐츠 가져오기 UseCase 제공
     */
    @Provides
    @ViewModelScoped
    fun provideGetAdContentUseCase(
        repository: AdRepository
    ): GetAdContentUseCase {
        return GetAdContentUseCase(repository)
    }

    /**
     * 광고 가중치 업데이트 UseCase 제공
     */
    @Provides
    @ViewModelScoped
    fun provideUpdateAdWeightUseCase(
        repository: AdRepository
    ): UpdateAdWeightUseCase {
        return UpdateAdWeightUseCase(repository)
    }

    /**
     * 광고 콘텐츠 변경 UseCase 제공
     */
    @Provides
    @ViewModelScoped
    fun provideUpdateAdContentUseCase(
        repository: AdRepository
    ): com.inik.dynamicadview.domain.usecase.UpdateAdContentUseCase {
        return com.inik.dynamicadview.domain.usecase.UpdateAdContentUseCase(repository)
    }

    /**
     * 가중치 계산 UseCase 제공
     * Repository에 의존하지 않는 순수 계산 로직
     */
    @Provides
    @ViewModelScoped
    fun provideCalculateWeightUseCase(): CalculateWeightUseCase {
        return CalculateWeightUseCase()
    }
}
