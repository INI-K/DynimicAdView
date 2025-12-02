package com.inik.dynamicadview.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType
import com.inik.dynamicadview.presentation.MainViewModel
import com.inik.dynamicadview.presentation.theme.DynamicAdViewTheme

/**
 * 동적 광고 화면
 * 3개의 섹션과 드래그 가능한 경계선으로 구성
 * 각 섹션은 key로 식별하여 불필요한 recomposition 방지
 * Edge-to-edge 지원 (시스템바 인셋 적용)
 */
@Composable
fun DynamicAdScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 섹션 - key로 섹션 고정, 상태바 인셋 적용
        androidx.compose.runtime.key("top_section_${uiState.topSection.contentUrl}") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(uiState.topWeight)
                    .windowInsetsPadding(WindowInsets.statusBars)
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

        // 하단 섹션 - key로 섹션 고정, 네비게이션바 인셋 적용
        androidx.compose.runtime.key("bottom_section_${uiState.bottomSection.contentUrl}") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(uiState.bottomWeight)
                    .windowInsetsPadding(WindowInsets.navigationBars)
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

// =========================== 프리뷰 ===========================

/**
 * 동적 광고 화면 프리뷰 (상태 저장 버전)
 * 실제 드래그 동작을 테스트할 수 있는 상태 저장 프리뷰
 */
@Preview(name = "동적 광고 화면", showBackground = true, showSystemUi = true)
@Composable
private fun DynamicAdScreenPreview() {
    var topWeight by remember { mutableFloatStateOf(1f) }
    var middleWeight by remember { mutableFloatStateOf(1f) }
    var bottomWeight by remember { mutableFloatStateOf(1f) }

    DynamicAdViewTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(topWeight)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE,
                        contentUrl = "https://picsum.photos/800/600?random=1",
                        weight = topWeight
                    )
                )
            }

            // 첫 번째 경계선
            DraggableDivider(
                onDragStart = { },
                onDrag = { deltaY ->
                    val total = topWeight + middleWeight
                    val adjustment = deltaY / 100f
                    topWeight = (topWeight + adjustment).coerceIn(0.2f, total - 0.2f)
                    middleWeight = total - topWeight
                },
                onDragEnd = { }
            )

            // 중단 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(middleWeight)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE,
                        contentUrl = "https://picsum.photos/800/600?random=2",
                        weight = middleWeight
                    )
                )
            }

            // 두 번째 경계선
            DraggableDivider(
                onDragStart = { },
                onDrag = { deltaY ->
                    val total = middleWeight + bottomWeight
                    val adjustment = deltaY / 100f
                    middleWeight = (middleWeight + adjustment).coerceIn(0.2f, total - 0.2f)
                    bottomWeight = total - middleWeight
                },
                onDragEnd = { }
            )

            // 하단 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(bottomWeight)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE,
                        contentUrl = "https://picsum.photos/800/600?random=3",
                        weight = bottomWeight
                    )
                )
            }
        }
    }
}

/**
 * 정적 레이아웃 프리뷰
 * 기본 1:1:1 비율
 */
@Preview(name = "정적 레이아웃 (1:1:1)", showBackground = true)
@Composable
private fun StaticLayoutPreview() {
    DynamicAdViewTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE,
                        contentUrl = "https://picsum.photos/800/400",
                        weight = 1f
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.Red.copy(alpha = 0.7f))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.EMPTY,
                        contentUrl = "",
                        weight = 1f
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.Red.copy(alpha = 0.7f))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE,
                        contentUrl = "https://picsum.photos/800/600",
                        weight = 1f
                    )
                )
            }
        }
    }
}
