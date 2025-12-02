package com.inik.dynamicadview.domain.entity

/**
 * 광고 섹션
 * 각 영역(상단/중단/하단)의 콘텐츠 정보
 */
data class AdSection(
    val contentType: ContentType,
    val contentUrl: String,
    val weight: Float = 1f
)
