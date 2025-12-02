package com.inik.dynamicadview.domain.entity

/**
 * 광고 섹션
 * 각 영역(상단/중단/하단)의 콘텐츠 정보
 *
 * @property contentType 콘텐츠 타입
 * @property contentUrl 단일 이미지 또는 동영상 URL (IMAGE, VIDEO 타입용)
 * @property weight 섹션 가중치
 * @property imageUrls 이미지 슬라이더용 이미지 URL 리스트 (IMAGE_SLIDER 타입용)
 * @property sliderDelayMs 이미지 슬라이더 전환 딜레이 (밀리초, 기본값 1000ms)
 */
data class AdSection(
    val contentType: ContentType,
    val contentUrl: String,
    val weight: Float = 1f,
    val imageUrls: List<String> = emptyList(),
    val sliderDelayMs: Long = 1000L
)
