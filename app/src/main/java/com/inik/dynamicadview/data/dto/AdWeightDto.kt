package com.inik.dynamicadview.data.dto

import com.inik.dynamicadview.domain.entity.AdWeight

/**
 * 광고 가중치 DTO
 * 가중치 정보를 저장하거나 전송할 때 사용
 */
data class AdWeightDto(
    val topWeight: Float,
    val middleWeight: Float,
    val bottomWeight: Float
) {
    /**
     * DTO를 도메인 엔티티로 변환
     */
    fun toEntity(): AdWeight {
        return AdWeight(
            topWeight = topWeight,
            middleWeight = middleWeight,
            bottomWeight = bottomWeight
        )
    }

    companion object {
        /**
         * 도메인 엔티티를 DTO로 변환
         */
        fun fromEntity(adWeight: AdWeight): AdWeightDto {
            return AdWeightDto(
                topWeight = adWeight.topWeight,
                middleWeight = adWeight.middleWeight,
                bottomWeight = adWeight.bottomWeight
            )
        }
    }
}
