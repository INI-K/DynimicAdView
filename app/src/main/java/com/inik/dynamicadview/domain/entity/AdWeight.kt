package com.inik.dynamicadview.domain.entity

/**
 * 광고 영역 가중치 엔티티
 * 각 영역의 높이 비율을 나타냄
 */
data class AdWeight(
    val topWeight: Float,
    val middleWeight: Float,
    val bottomWeight: Float
) {
    companion object {
        const val MIN_WEIGHT = 0.1f
        const val DEFAULT_WEIGHT = 1f
    }
}
