package com.inik.dynamicadview.data.dto

import com.inik.dynamicadview.domain.entity.AdContent

/**
 * 광고 콘텐츠 DTO (Data Transfer Object)
 * API 또는 로컬 데이터 소스와의 통신에 사용
 */
data class AdContentDto(
    val topSection: AdSectionDto,
    val middleSection: AdSectionDto,
    val bottomSection: AdSectionDto
) {
    /**
     * DTO를 도메인 엔티티로 변환
     */
    fun toEntity(): AdContent {
        return AdContent(
            topSection = topSection.toEntity(),
            middleSection = middleSection.toEntity(),
            bottomSection = bottomSection.toEntity()
        )
    }

    companion object {
        /**
         * 도메인 엔티티를 DTO로 변환
         */
        fun fromEntity(adContent: AdContent): AdContentDto {
            return AdContentDto(
                topSection = AdSectionDto.fromEntity(adContent.topSection),
                middleSection = AdSectionDto.fromEntity(adContent.middleSection),
                bottomSection = AdSectionDto.fromEntity(adContent.bottomSection)
            )
        }
    }
}
