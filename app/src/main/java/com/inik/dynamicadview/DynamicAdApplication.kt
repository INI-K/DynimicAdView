package com.inik.dynamicadview

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application 클래스
 * Hilt 의존성 주입을 위한 진입점
 */
@HiltAndroidApp
class DynamicAdApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 앱 초기화 로직
    }
}
