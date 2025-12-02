package com.inik.dynamicadview.data.datasource

import com.inik.dynamicadview.data.dto.AdContentDto
import com.inik.dynamicadview.data.dto.AdSectionDto
import com.inik.dynamicadview.data.dto.AdWeightDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 광고 데이터 소스
 * LocalAdDataSource에서 초기 데이터를 로드하고 메모리에 캐시
 */
@Singleton
class AdDataSource @Inject constructor(
    private val localAdDataSource: LocalAdDataSource
) {

    // 현재 콘텐츠 구성 (메모리 캐시)
    // LocalAdDataSource에서 JSON을 읽어서 초기화
    private var currentContent: AdContentDto = localAdDataSource.getAdContent()

    // 현재 저장된 가중치 (메모리 캐시)
    // LocalAdDataSource에서 초기 가중치 로드
    private var currentWeight: AdWeightDto = run {
        val (top, middle, bottom) = localAdDataSource.getInitialWeights()
        AdWeightDto(
            topWeight = top,
            middleWeight = middle,
            bottomWeight = bottom
        )
    }

    /**
     * 광고 콘텐츠 가져오기
     * 실제 환경에서는 API 호출 또는 로컬 DB 조회
     */
    fun getAdContent(): AdContentDto {
        // 현재 가중치를 적용한 콘텐츠 반환
        return currentContent.copy(
            topSection = currentContent.topSection.copy(weight = currentWeight.topWeight),
            middleSection = currentContent.middleSection.copy(weight = currentWeight.middleWeight),
            bottomSection = currentContent.bottomSection.copy(weight = currentWeight.bottomWeight)
        )
    }

    /**
     * 광고 콘텐츠 업데이트
     * 각 섹션의 타입과 URL 변경 가능
     */
    fun updateAdContent(content: AdContentDto) {
        currentContent = content
    }

    /**
     * 광고 가중치 업데이트
     */
    fun updateAdWeight(adWeight: AdWeightDto) {
        currentWeight = adWeight
    }

    /**
     * 현재 가중치 가져오기
     */
    fun getCurrentAdWeight(): AdWeightDto {
        return currentWeight
    }
}
