package com.inik.dynamicadview.domain.repository

import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.entity.AdWeight

/**
 * 광고 데이터 저장소 인터페이스
 */
interface AdRepository {
    /**
     * 광고 콘텐츠 가져오기
     */
    suspend fun getAdContent(): AdContent

    /**
     * 광고 콘텐츠 업데이트
     * 각 섹션의 타입과 URL 변경 가능
     */
    suspend fun updateAdContent(adContent: AdContent)

    /**
     * 광고 가중치 업데이트
     */
    suspend fun updateAdWeight(adWeight: AdWeight)

    /**
     * 현재 광고 가중치 가져오기
     */
    suspend fun getCurrentAdWeight(): AdWeight
}
