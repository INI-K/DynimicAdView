package com.inik.dynamicadview.domain.usecase

import com.inik.dynamicadview.domain.entity.AdWeight
import com.inik.dynamicadview.domain.repository.AdRepository

/**
 * 광고 가중치 업데이트 유스케이스
 * 사용자가 드래그로 영역 크기를 조절할 때 사용
 */
class UpdateAdWeightUseCase(private val repository: AdRepository) {

    suspend operator fun invoke(adWeight: AdWeight) {
        repository.updateAdWeight(adWeight)
    }
}
