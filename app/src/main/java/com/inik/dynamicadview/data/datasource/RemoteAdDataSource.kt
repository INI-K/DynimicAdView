package com.inik.dynamicadview.data.datasource

import com.inik.dynamicadview.data.dto.AdConfigDto
import com.inik.dynamicadview.data.dto.AdContentDto
import com.inik.dynamicadview.data.dto.AdSectionDto
import com.inik.dynamicadview.data.dto.AdWeightDto
import kotlinx.coroutines.delay

/**
 * 원격 광고 데이터 소스 (API 통신 시뮬레이션)
 * 실제 프로젝트에서는 Retrofit, OkHttp 등을 사용하여 구현
 */
class RemoteAdDataSource {

    /**
     * 서버에서 광고 콘텐츠 가져오기 (시뮬레이션)
     *
     * 사용 예시:
     * ```
     * val content = remoteDataSource.fetchAdContent("campaign_001")
     * ```
     */
    suspend fun fetchAdContent(campaignId: String): AdContentDto {
        // API 호출 시뮬레이션
        delay(500)

        return AdContentDto(
            topSection = AdSectionDto(
                contentType = "IMAGE",
                contentUrl = "https://picsum.photos/seed/$campaignId-top/1024/760",
                weight = 1f
            ),
            middleSection = AdSectionDto(
                contentType = "VIDEO",
                contentUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                weight = 2f
            ),
            bottomSection = AdSectionDto(
                contentType = "IMAGE",
                contentUrl = "https://picsum.photos/seed/$campaignId-bottom/1024/760",
                weight = 1f
            )
        )
    }

    /**
     * 서버로 사용자의 가중치 설정 전송 (분석용)
     *
     * 사용 예시:
     * ```
     * remoteDataSource.uploadUserPreference("user_123", "ad_001", weightDto)
     * ```
     */
    suspend fun uploadUserPreference(
        userId: String,
        adId: String,
        weights: AdWeightDto
    ): Boolean {
        // API 호출 시뮬레이션
        delay(300)

        // 로그 출력 (실제로는 서버로 전송)
        println("광고 조절 데이터 전송: userId=$userId, adId=$adId, weights=$weights")

        return true
    }

    /**
     * 다양한 광고 프리셋 가져오기
     */
    suspend fun fetchAdPresets(): List<AdContentDto> {
        delay(300)

        return listOf(
            // 프리셋 1: 기본 (이미지-동영상-이미지)
            AdContentDto(
                topSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset1-top/1024/760",
                    1f
                ),
                middleSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                    1f
                ),
                bottomSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset1-bottom/1024/760",
                    1f
                )
            ),
            // 프리셋 2: 전체 동영상
            AdContentDto(
                topSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                    0.5f
                ),
                middleSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    3f
                ),
                bottomSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                    0.5f
                )
            ),
            // 프리셋 3: 전체 이미지
            AdContentDto(
                topSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset3-top/1024/760",
                    1f
                ),
                middleSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset3-middle/1024/760",
                    2f
                ),
                bottomSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset3-bottom/1024/760",
                    1f
                )
            ),
            // 프리셋 4: 동영상 샌드위치 (동영상-이미지-동영상)
            AdContentDto(
                topSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                    2f
                ),
                middleSection = AdSectionDto(
                    "IMAGE",
                    "https://picsum.photos/seed/preset4-banner/1024/300",
                    0.5f
                ),
                bottomSection = AdSectionDto(
                    "VIDEO",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                    2f
                )
            )
        )
    }
}
