package com.inik.dynamicadview.data.dto

/**
 * 광고 설정 DTO
 * 서버에서 받아온 광고 설정을 파싱하거나 서버로 전송할 때 사용
 *
 * 사용 예시:
 * ```
 * val config = AdConfigDto(
 *     adId = "ad_001",
 *     topSection = SectionConfig(ContentType.IMAGE, "https://example.com/top.jpg"),
 *     middleSection = SectionConfig(ContentType.VIDEO, "https://example.com/video.mp4"),
 *     bottomSection = SectionConfig(ContentType.IMAGE, "https://example.com/bottom.jpg"),
 *     initialWeights = WeightConfig(1f, 2f, 1f),
 *     enableUserResize = true
 * )
 * ```
 */
data class AdConfigDto(
    // 광고 ID
    val adId: String,

    // 각 섹션 설정
    val topSection: SectionConfig,
    val middleSection: SectionConfig,
    val bottomSection: SectionConfig,

    // 초기 가중치 설정
    val initialWeights: WeightConfig = WeightConfig(),

    // 사용자 크기 조절 허용 여부
    val enableUserResize: Boolean = true,

    // 최소/최대 가중치 제한
    val minWeight: Float = 0.1f,
    val maxWeight: Float = 5f,

    // 자동 저장 여부
    val autoSave: Boolean = true
) {
    /**
     * 섹션 설정
     */
    data class SectionConfig(
        val type: String,  // "IMAGE", "VIDEO", "IMAGE_SLIDER"
        val url: String,
        val imageUrls: List<String> = emptyList(),  // IMAGE_SLIDER 타입용
        val sliderDelayMs: Long = 1000L  // 이미지 슬라이더 전환 딜레이
    )

    /**
     * 가중치 설정
     */
    data class WeightConfig(
        val top: Float = 1f,
        val middle: Float = 1f,
        val bottom: Float = 1f
    )

    /**
     * AdContentDto로 변환
     */
    fun toAdContentDto(): AdContentDto {
        return AdContentDto(
            topSection = AdSectionDto(
                contentType = topSection.type,
                contentUrl = topSection.url,
                weight = initialWeights.top,
                imageUrls = topSection.imageUrls,
                sliderDelayMs = topSection.sliderDelayMs
            ),
            middleSection = AdSectionDto(
                contentType = middleSection.type,
                contentUrl = middleSection.url,
                weight = initialWeights.middle,
                imageUrls = middleSection.imageUrls,
                sliderDelayMs = middleSection.sliderDelayMs
            ),
            bottomSection = AdSectionDto(
                contentType = bottomSection.type,
                contentUrl = bottomSection.url,
                weight = initialWeights.bottom,
                imageUrls = bottomSection.imageUrls,
                sliderDelayMs = bottomSection.sliderDelayMs
            )
        )
    }
}
