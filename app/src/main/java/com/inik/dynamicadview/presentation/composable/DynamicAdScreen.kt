package com.inik.dynamicadview.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inik.dynamicadview.presentation.MainViewModel

/**
 * 동적 광고 화면
 * 3개의 섹션과 드래그 가능한 경계선으로 구성
 * 각 섹션은 key로 식별하여 불필요한 recomposition 방지
 */
@Composable
fun DynamicAdScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 섹션 - key로 섹션 고정
        androidx.compose.runtime.key("top_section_${uiState.topSection.contentUrl}") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(uiState.topWeight)
            ) {
                AdSectionView(section = uiState.topSection)
            }
        }

        // 첫 번째 경계선 (드래그 가능)
        DraggableDivider(
            onDragStart = { viewModel.onDragStart() },
            onDrag = { deltaY -> viewModel.onDrag(deltaY, 1) },
            onDragEnd = { viewModel.onDragEnd() }
        )

        // 중단 섹션 - key로 섹션 고정
        androidx.compose.runtime.key("middle_section_${uiState.middleSection.contentUrl}") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(uiState.middleWeight)
            ) {
                AdSectionView(section = uiState.middleSection)
            }
        }

        // 두 번째 경계선 (드래그 가능)
        DraggableDivider(
            onDragStart = { viewModel.onDragStart() },
            onDrag = { deltaY -> viewModel.onDrag(deltaY, 2) },
            onDragEnd = { viewModel.onDragEnd() }
        )

        // 하단 섹션 - key로 섹션 고정
        androidx.compose.runtime.key("bottom_section_${uiState.bottomSection.contentUrl}") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(uiState.bottomWeight)
            ) {
                AdSectionView(section = uiState.bottomSection)
            }
        }
    }
}

/**
 * 드래그 가능한 경계선
 * 터치 영역을 넓게 하여 사용성 향상
 */
@Composable
private fun DraggableDivider(
    onDragStart: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(Color.Red.copy(alpha = 0.7f))
            .pointerInput(Unit) {
                val densityScale = density
                detectDragGestures(
                    onDragStart = {
                        onDragStart()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (densityScale != 0f) {
                            onDrag((dragAmount.y / densityScale) * DRAG_SENSITIVITY_MULTIPLIER)
                        }
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDragCancel = {
                        onDragEnd()
                    }
                )
            }
    )
}

private const val DRAG_SENSITIVITY_MULTIPLIER = 1.6f
