package com.inik.dynamicadview.domain.usecase

import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.repository.AdRepository

/**
 * 광고 콘텐츠 가져오기 유스케이스
 */
class GetAdContentUseCase(private val repository: AdRepository) {

    suspend operator fun invoke(): AdContent {
        return repository.getAdContent()
    }
}
