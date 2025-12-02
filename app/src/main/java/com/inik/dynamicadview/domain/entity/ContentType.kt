package com.inik.dynamicadview.domain.entity

/**
 * 콘텐츠 타입
 * 각 광고 영역에 표시될 콘텐츠의 종류
 */
enum class ContentType {
    /** 이미지 콘텐츠 */
    IMAGE,

    /** 동영상 콘텐츠 */
    VIDEO,

    /** 빈 영역 (배경색만) */
    EMPTY
}
