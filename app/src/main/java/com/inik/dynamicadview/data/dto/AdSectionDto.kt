package com.inik.dynamicadview.data.dto

import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType

/**
 * 광고 섹션 DTO
 */
data class AdSectionDto(
    val contentType: String,
    val contentUrl: String,
    val weight: Float = 1f,
    val imageUrls: List<String> = emptyList(),
    val sliderDelayMs: Long = 1000L
) {
    /**
     * DTO를 도메인 엔티티로 변환
     */
    fun toEntity(): AdSection {
        return AdSection(
            contentType = ContentType.valueOf(contentType.uppercase()),
            contentUrl = contentUrl,
            weight = weight,
            imageUrls = imageUrls,
            sliderDelayMs = sliderDelayMs
        )
    }

    companion object {
        /**
         * 도메인 엔티티를 DTO로 변환
         */
        fun fromEntity(section: AdSection): AdSectionDto {
            return AdSectionDto(
                contentType = section.contentType.name,
                contentUrl = section.contentUrl,
                weight = section.weight,
                imageUrls = section.imageUrls,
                sliderDelayMs = section.sliderDelayMs
            )
        }
    }
}
