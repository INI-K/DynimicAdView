package com.inik.dynamicadview.util

import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType

/**
 * 광고 프리셋 모음
 * 다양한 광고 레이아웃 조합 예제
 */
object AdPresets {

    /**
     * 기본 프리셋: 이미지슬라이더-동영상-이미지슬라이더
     */
    val DEFAULT = AdContent(
        topSection = AdSection(
            contentType = ContentType.IMAGE_SLIDER,
            contentUrl = "",
            imageUrls = listOf(
                "https://picsum.photos/seed/slider1/1024/760",
                "https://picsum.photos/seed/slider2/1024/760",
                "https://picsum.photos/seed/slider3/1024/760",
                "https://picsum.photos/seed/slider4/1024/760",
                "https://picsum.photos/seed/slider5/1024/760"
            ),
            weight = 1f,
            sliderDelayMs = 1000L // 상단: 1초
        ),
        middleSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            weight = 1f
        ),
        bottomSection = AdSection(
            contentType = ContentType.IMAGE_SLIDER,
            contentUrl = "",
            imageUrls = listOf(
                "https://picsum.photos/seed/bottom1/1024/760",
                "https://picsum.photos/seed/bottom2/1024/760",
                "https://picsum.photos/seed/bottom3/1024/760",
                "https://picsum.photos/seed/bottom4/1024/760",
                "https://picsum.photos/seed/bottom5/1024/760"
            ),
            weight = 1f,
            sliderDelayMs = 1200L // 하단: 1.2초
        )
    )

    /**
     * 동영상 중심: 동영상-동영상-동영상
     * 모든 영역이 동영상인 몰입형 광고
     */
    val ALL_VIDEO = AdContent(
        topSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            weight = 0.5f
        ),
        middleSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            weight = 3f
        ),
        bottomSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            weight = 0.5f
        )
    )

    /**
     * 이미지 갤러리: 이미지-이미지-이미지
     * 모든 영역이 이미지인 정적 광고
     */
    val ALL_IMAGE = AdContent(
        topSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/gallery1/1024/760",
            weight = 1f
        ),
        middleSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/gallery2/1024/760",
            weight = 2f
        ),
        bottomSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/gallery3/1024/760",
            weight = 1f
        )
    )

    /**
     * 동영상 샌드위치: 동영상-이미지-동영상
     * 중간에 이미지 배너, 상하단은 동영상
     */
    val VIDEO_SANDWICH = AdContent(
        topSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            weight = 2f
        ),
        middleSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/banner/1024/300",
            weight = 0.5f
        ),
        bottomSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            weight = 2f
        )
    )

    /**
     * 메인 히어로: 동영상-이미지-이미지
     * 상단 큰 동영상과 하단 작은 이미지들
     */
    val HERO_VIDEO = AdContent(
        topSection = AdSection(
            contentType = ContentType.VIDEO,
            contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            weight = 3f
        ),
        middleSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/sub1/1024/760",
            weight = 1f
        ),
        bottomSection = AdSection(
            contentType = ContentType.IMAGE,
            contentUrl = "https://picsum.photos/seed/sub2/1024/760",
            weight = 1f
        )
    )

    /**
     * 모든 프리셋 목록
     */
    val ALL_PRESETS = mapOf(
        "기본 (이미지-동영상-이미지)" to DEFAULT,
        "전체 동영상" to ALL_VIDEO,
        "전체 이미지" to ALL_IMAGE,
        "동영상 샌드위치" to VIDEO_SANDWICH,
        "히어로 동영상" to HERO_VIDEO
    )
}
