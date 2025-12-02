package com.inik.dynamicadview.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.inik.dynamicadview.data.dto.AdConfigDto
import com.inik.dynamicadview.data.dto.AdContentDto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 로컬 광고 데이터 소스
 * assets 폴더의 JSON 파일에서 광고 설정을 읽어옴
 * 네트워크 레이어 대신 Dummy 데이터로 사용
 */
@Singleton
class LocalAdDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    // JSON 파일명
    private val jsonFileName = "ad_config.json"

    /**
     * assets 폴더에서 광고 설정 읽어오기
     */
    fun loadAdConfig(): AdConfigDto {
        return try {
            context.assets.open(jsonFileName).use { inputStream ->
                val reader = InputStreamReader(inputStream)
                gson.fromJson(reader, AdConfigDto::class.java)
            }
        } catch (e: Exception) {
            // JSON 읽기 실패 시 기본값 반환
            e.printStackTrace()
            getDefaultConfig()
        }
    }

    /**
     * 광고 콘텐츠 가져오기
     */
    fun getAdContent(): AdContentDto {
        val config = loadAdConfig()
        return config.toAdContentDto()
    }

    /**
     * 광고 설정에서 초기 가중치 가져오기
     */
    fun getInitialWeights(): Triple<Float, Float, Float> {
        val config = loadAdConfig()
        return Triple(
            config.initialWeights.top,
            config.initialWeights.middle,
            config.initialWeights.bottom
        )
    }

    /**
     * 기본 설정 반환 (폴백)
     */
    private fun getDefaultConfig(): AdConfigDto {
        return AdConfigDto(
            adId = "default",
            topSection = AdConfigDto.SectionConfig(
                type = "IMAGE",
                url = "https://picsum.photos/seed/default-top/1024/760"
            ),
            middleSection = AdConfigDto.SectionConfig(
                type = "VIDEO",
                url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            ),
            bottomSection = AdConfigDto.SectionConfig(
                type = "IMAGE",
                url = "https://picsum.photos/seed/default-bottom/1024/760"
            )
        )
    }
}
