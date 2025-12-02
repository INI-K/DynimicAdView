package com.inik.dynamicadview.domain.usecase

import com.inik.dynamicadview.domain.entity.AdContent
import com.inik.dynamicadview.domain.repository.AdRepository

/**
 * 광고 콘텐츠 업데이트 유스케이스
 * 각 섹션의 타입과 콘텐츠를 변경할 수 있음
 */
class UpdateAdContentUseCase(private val repository: AdRepository) {

    suspend operator fun invoke(adContent: AdContent) {
        // 여기서 비즈니스 로직 검증 가능
        // 예: URL 유효성 검사, 콘텐츠 타입 제한 등
        repository.updateAdContent(adContent)
    }
}
