package com.inik.dynamicadview.data.repository

import com.inik.dynamicadview.data.datasource.AdDataSource
import com.inik.dynamicadview.data.dto.AdContentDto
import com.inik.dynamicadview.data.dto.AdWeightDto
import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.entity.AdWeight
import com.inik.dynamicadview.domain.repository.AdRepository

/**
 * 광고 저장소 구현체
 * Domain Layer의 Repository 인터페이스 구현
 */
class AdRepositoryImpl(
    private val dataSource: AdDataSource
) : AdRepository {

    override suspend fun getAdContent(): AdContent {
        return dataSource.getAdContent().toEntity()
    }

    override suspend fun updateAdContent(adContent: AdContent) {
        val dto = AdContentDto.fromEntity(adContent)
        dataSource.updateAdContent(dto)
    }

    override suspend fun updateAdWeight(adWeight: AdWeight) {
        val dto = AdWeightDto.fromEntity(adWeight)
        dataSource.updateAdWeight(dto)
    }

    override suspend fun getCurrentAdWeight(): AdWeight {
        return dataSource.getCurrentAdWeight().toEntity()
    }
}
