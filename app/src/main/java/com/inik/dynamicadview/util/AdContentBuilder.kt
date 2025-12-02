package com.inik.dynamicadview.util

import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType

/**
 * 광고 콘텐츠 빌더
 * 프로그래밍 방식으로 광고 콘텐츠를 쉽게 생성
 *
 * 사용 예시:
 * ```
 * val content = AdContentBuilder()
 *     .topImage("https://example.com/top.jpg", 1f)
 *     .middleVideo("https://example.com/video.mp4", 2f)
 *     .bottomImage("https://example.com/bottom.jpg", 1f)
 *     .build()
 *
 * // 또는
 * val content = AdContentBuilder()
 *     .section(0, ContentType.VIDEO, "https://...", 1f)
 *     .section(1, ContentType.IMAGE, "https://...", 2f)
 *     .section(2, ContentType.VIDEO, "https://...", 1f)
 *     .build()
 * ```
 */
class AdContentBuilder {

    private var topSection: AdSection? = null
    private var middleSection: AdSection? = null
    private var bottomSection: AdSection? = null

    /**
     * 상단 이미지 설정
     */
    fun topImage(url: String, weight: Float = 1f): AdContentBuilder {
        topSection = AdSection(ContentType.IMAGE, url, weight)
        return this
    }

    /**
     * 상단 동영상 설정
     */
    fun topVideo(url: String, weight: Float = 1f): AdContentBuilder {
        topSection = AdSection(ContentType.VIDEO, url, weight)
        return this
    }

    /**
     * 중단 이미지 설정
     */
    fun middleImage(url: String, weight: Float = 1f): AdContentBuilder {
        middleSection = AdSection(ContentType.IMAGE, url, weight)
        return this
    }

    /**
     * 중단 동영상 설정
     */
    fun middleVideo(url: String, weight: Float = 1f): AdContentBuilder {
        middleSection = AdSection(ContentType.VIDEO, url, weight)
        return this
    }

    /**
     * 하단 이미지 설정
     */
    fun bottomImage(url: String, weight: Float = 1f): AdContentBuilder {
        bottomSection = AdSection(ContentType.IMAGE, url, weight)
        return this
    }

    /**
     * 하단 동영상 설정
     */
    fun bottomVideo(url: String, weight: Float = 1f): AdContentBuilder {
        bottomSection = AdSection(ContentType.VIDEO, url, weight)
        return this
    }

    /**
     * 인덱스로 섹션 설정
     * @param position 0: 상단, 1: 중단, 2: 하단
     * @param contentType 콘텐츠 타입
     * @param url 콘텐츠 URL
     * @param weight 가중치
     */
    fun section(
        position: Int,
        contentType: ContentType,
        url: String,
        weight: Float = 1f
    ): AdContentBuilder {
        val section = AdSection(contentType, url, weight)
        when (position) {
            0 -> topSection = section
            1 -> middleSection = section
            2 -> bottomSection = section
        }
        return this
    }

    /**
     * AdContent 생성
     */
    fun build(): AdContent {
        return AdContent(
            topSection = topSection ?: AdSection(
                ContentType.IMAGE,
                "https://picsum.photos/1024/760",
                1f
            ),
            middleSection = middleSection ?: AdSection(
                ContentType.VIDEO,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                1f
            ),
            bottomSection = bottomSection ?: AdSection(
                ContentType.IMAGE,
                "https://picsum.photos/1024/760",
                1f
            )
        )
    }
}
