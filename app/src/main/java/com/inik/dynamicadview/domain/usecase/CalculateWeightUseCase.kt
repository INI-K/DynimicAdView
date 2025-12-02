package com.inik.dynamicadview.domain.usecase

import com.inik.dynamicadview.domain.entity.AdWeight

/**
 * 드래그 동작에 따른 가중치 계산 유스케이스
 */
class CalculateWeightUseCase {

    /**
     * 상단-중단 경계선 드래그 시 가중치 계산
     * @param dy 드래그 이동 거리
     * @param currentWeight 현재 가중치
     * @return 새로운 가중치
     */
    fun calculateTopMiddleWeight(
        dy: Float,
        currentWeight: AdWeight
    ): AdWeight {
        val totalWeight = currentWeight.topWeight + currentWeight.middleWeight
        // 감도를 낮춰서 더 부드럽게 (200 -> 500)
        val adjustment = dy / 500f
        val newTopWeight = (currentWeight.topWeight + adjustment)
            .coerceIn(AdWeight.MIN_WEIGHT, totalWeight - AdWeight.MIN_WEIGHT)
        val newMiddleWeight = totalWeight - newTopWeight

        return currentWeight.copy(
            topWeight = newTopWeight,
            middleWeight = newMiddleWeight
        )
    }

    /**
     * 중단-하단 경계선 드래그 시 가중치 계산
     * @param dy 드래그 이동 거리
     * @param currentWeight 현재 가중치
     * @return 새로운 가중치
     */
    fun calculateMiddleBottomWeight(
        dy: Float,
        currentWeight: AdWeight
    ): AdWeight {
        val totalWeight = currentWeight.middleWeight + currentWeight.bottomWeight
        // 감도를 낮춰서 더 부드럽게 (200 -> 500)
        val adjustment = dy / 500f
        val newMiddleWeight = (currentWeight.middleWeight + adjustment)
            .coerceIn(AdWeight.MIN_WEIGHT, totalWeight - AdWeight.MIN_WEIGHT)
        val newBottomWeight = totalWeight - newMiddleWeight

        return currentWeight.copy(
            middleWeight = newMiddleWeight,
            bottomWeight = newBottomWeight
        )
    }
}
