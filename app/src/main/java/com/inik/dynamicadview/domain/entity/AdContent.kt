package com.inik.dynamicadview.domain.entity

/**
 * 광고 콘텐츠 엔티티
 * 각 광고 영역에 표시될 콘텐츠 정보
 * 각 섹션은 이미지 또는 동영상이 될 수 있음
 */
data class AdContent(
    val topSection: AdSection,
    val middleSection: AdSection,
    val bottomSection: AdSection
) {
    companion object {
        /**
         * 기본 광고 콘텐츠 생성 (하위 호환성)
         */
        fun createDefault(
            topImageUrl: String = "https://picsum.photos/1024/760",
            middleVideoUrl: String = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            bottomImageUrl: String = "https://picsum.photos/1024/760"
        ): AdContent {
            return AdContent(
                topSection = AdSection(ContentType.IMAGE, topImageUrl, 1f),
                middleSection = AdSection(ContentType.VIDEO, middleVideoUrl, 1f),
                bottomSection = AdSection(ContentType.IMAGE, bottomImageUrl, 1f)
            )
        }
    }
}
