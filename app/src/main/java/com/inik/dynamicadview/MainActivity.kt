package com.inik.dynamicadview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.inik.dynamicadview.presentation.composable.DynamicAdScreen
import com.inik.dynamicadview.presentation.theme.DynamicAdViewTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 메인 액티비티
 * 동적 광고 뷰를 표시하고 실시간 크기 조절 기능 제공
 * Hilt를 통한 의존성 주입
 * Jetpack Compose UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DynamicAdViewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DynamicAdScreen()
                }
            }
        }
    }
}
