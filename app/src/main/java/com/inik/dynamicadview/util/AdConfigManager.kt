package com.inik.dynamicadview.util

import com.inik.dynamicadview.data.dto.AdConfigDto
import com.inik.dynamicadview.data.dto.AdContentDto
import com.inik.dynamicadview.data.dto.AdWeightDto

/**
 * 광고 설정 관리 유틸리티
 * DTO를 활용하여 광고 설정을 쉽게 관리할 수 있는 헬퍼 클래스
 */
object AdConfigManager {

    /**
     * JSON 형식의 광고 설정 파싱 시뮬레이션
     * 실제로는 Gson, Moshi, kotlinx.serialization 등을 사용
     *
     * 사용 예시:
     * ```
     * val jsonString = """
     * {
     *   "adId": "ad_001",
     *   "topImageUrl": "https://example.com/top.jpg",
     *   ...
     * }
     * """
     * val config = AdConfigManager.parseConfig(jsonString)
     * ```
     */
    fun createSampleConfig(
        adId: String = "sample_ad",
        videoUrl: String = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        topWeight: Float = 1f,
        middleWeight: Float = 1f,
        bottomWeight: Float = 1f
    ): AdConfigDto {
        return AdConfigDto(
            adId = adId,
            topSection = AdConfigDto.SectionConfig(
                "IMAGE",
                "https://picsum.photos/seed/${adId}_top/1024/760"
            ),
            middleSection = AdConfigDto.SectionConfig("VIDEO", videoUrl),
            bottomSection = AdConfigDto.SectionConfig(
                "IMAGE",
                "https://picsum.photos/seed/${adId}_bottom/1024/760"
            ),
            initialWeights = AdConfigDto.WeightConfig(
                top = topWeight,
                middle = middleWeight,
                bottom = bottomWeight
            )
        )
    }

    /**
     * 광고 설정 유효성 검사
     */
    fun validateConfig(config: AdConfigDto): Boolean {
        return config.topSection.url.isNotBlank() &&
                config.middleSection.url.isNotBlank() &&
                config.bottomSection.url.isNotBlank() &&
                config.initialWeights.top >= config.minWeight &&
                config.initialWeights.middle >= config.minWeight &&
                config.initialWeights.bottom >= config.minWeight
    }

    /**
     * 가중치를 퍼센트로 변환
     */
    fun weightToPercentage(weight: AdWeightDto): Triple<Int, Int, Int> {
        val total = weight.topWeight + weight.middleWeight + weight.bottomWeight
        return Triple(
            ((weight.topWeight / total) * 100).toInt(),
            ((weight.middleWeight / total) * 100).toInt(),
            ((weight.bottomWeight / total) * 100).toInt()
        )
    }

    /**
     * 퍼센트를 가중치로 변환
     */
    fun percentageToWeight(
        topPercent: Int,
        middlePercent: Int,
        bottomPercent: Int
    ): AdWeightDto {
        val total = topPercent + middlePercent + bottomPercent
        val normalizer = 3f / total // 기본 합계 3이 되도록

        return AdWeightDto(
            topWeight = topPercent * normalizer,
            middleWeight = middlePercent * normalizer,
            bottomWeight = bottomPercent * normalizer
        )
    }

    /**
     * 프리셋 목록
     */
    val presets = mapOf(
        "균등 분할" to AdConfigDto.WeightConfig(1f, 1f, 1f),
        "동영상 중심" to AdConfigDto.WeightConfig(0.5f, 3f, 0.5f),
        "이미지 중심" to AdConfigDto.WeightConfig(2f, 0.5f, 2f),
        "상단 강조" to AdConfigDto.WeightConfig(3f, 1f, 1f),
        "하단 강조" to AdConfigDto.WeightConfig(1f, 1f, 3f)
    )
}
