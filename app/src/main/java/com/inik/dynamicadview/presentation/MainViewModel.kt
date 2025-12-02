package com.inik.dynamicadview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.AdWeight
import com.inik.dynamicadview.domain.entity.ContentType
import com.inik.dynamicadview.domain.usecase.CalculateWeightUseCase
import com.inik.dynamicadview.domain.usecase.GetAdContentUseCase
import com.inik.dynamicadview.domain.usecase.UpdateAdContentUseCase
import com.inik.dynamicadview.domain.usecase.UpdateAdWeightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 메인 화면 ViewModel
 * 클린 아키텍처 MVVM 패턴 + Hilt 의존성 주입
 * Compose용 StateFlow 사용
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAdContentUseCase: GetAdContentUseCase,
    private val updateAdContentUseCase: UpdateAdContentUseCase,
    private val updateAdWeightUseCase: UpdateAdWeightUseCase,
    private val calculateWeightUseCase: CalculateWeightUseCase
) : ViewModel() {

    // UI 상태
    private val _uiState = MutableStateFlow(AdUiState())
    val uiState: StateFlow<AdUiState> = _uiState.asStateFlow()

    // 드래그 시작 시 저장할 값들
    private var accumulatedDragDy = 0f
    private var initialWeight: AdWeight? = null

    init {
        loadAdContent()
    }

    /**
     * 광고 콘텐츠 로드
     */
    private fun loadAdContent() {
        viewModelScope.launch {
            try {
                val adContent = getAdContentUseCase()

                _uiState.value = _uiState.value.copy(
                    topSection = adContent.topSection,
                    middleSection = adContent.middleSection,
                    bottomSection = adContent.bottomSection,
                    topWeight = adContent.topSection.weight,
                    middleWeight = adContent.middleSection.weight,
                    bottomWeight = adContent.bottomSection.weight,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    /**
     * 특정 섹션의 콘텐츠 타입 변경
     * @param position 0: 상단, 1: 중단, 2: 하단
     * @param contentType 새로운 콘텐츠 타입
     * @param url 새로운 URL
     */
    fun updateSectionContent(position: Int, contentType: ContentType, url: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val currentTop = currentState.topSection
                val currentMiddle = currentState.middleSection
                val currentBottom = currentState.bottomSection

                val newContent = when (position) {
                    0 -> com.inik.dynamicadview.domain.entity.AdContent(
                        topSection = currentTop.copy(contentType = contentType, contentUrl = url),
                        middleSection = currentMiddle,
                        bottomSection = currentBottom
                    )

                    1 -> com.inik.dynamicadview.domain.entity.AdContent(
                        topSection = currentTop,
                        middleSection = currentMiddle.copy(
                            contentType = contentType,
                            contentUrl = url
                        ),
                        bottomSection = currentBottom
                    )

                    2 -> com.inik.dynamicadview.domain.entity.AdContent(
                        topSection = currentTop,
                        middleSection = currentMiddle,
                        bottomSection = currentBottom.copy(
                            contentType = contentType,
                            contentUrl = url
                        )
                    )

                    else -> return@launch
                }

                updateAdContentUseCase(newContent)
                loadAdContent()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * 드래그 시작
     */
    fun onDragStart() {
        accumulatedDragDy = 0f
        initialWeight = AdWeight(
            topWeight = _uiState.value.topWeight,
            middleWeight = _uiState.value.middleWeight,
            bottomWeight = _uiState.value.bottomWeight
        )
    }

    /**
     * 드래그 중
     * @param deltaY 이번 이벤트에서 이동한 Y 거리
     * @param dividerIndex 경계선 인덱스 (1: 상단-중단, 2: 중단-하단)
     */
    fun onDrag(deltaY: Float, dividerIndex: Int) {
        val currentWeight = initialWeight ?: return
        accumulatedDragDy += deltaY

        val newWeight = when (dividerIndex) {
            1 -> calculateWeightUseCase.calculateTopMiddleWeight(accumulatedDragDy, currentWeight)
            2 -> calculateWeightUseCase.calculateMiddleBottomWeight(
                accumulatedDragDy,
                currentWeight
            )
            else -> return
        }

        _uiState.value = _uiState.value.copy(
            topWeight = newWeight.topWeight,
            middleWeight = newWeight.middleWeight,
            bottomWeight = newWeight.bottomWeight
        )
    }

    /**
     * 드래그 종료
     */
    fun onDragEnd() {
        val finalWeight = AdWeight(
            topWeight = _uiState.value.topWeight,
            middleWeight = _uiState.value.middleWeight,
            bottomWeight = _uiState.value.bottomWeight
        )
        accumulatedDragDy = 0f
        initialWeight = null
        saveAdWeight(finalWeight)
    }

    /**
     * 가중치 저장
     */
    private fun saveAdWeight(adWeight: AdWeight) {
        viewModelScope.launch {
            try {
                updateAdWeightUseCase(adWeight)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * 가중치 초기화
     */
    fun resetWeights() {
        val resetWeight = AdWeight(
            topWeight = AdWeight.DEFAULT_WEIGHT,
            middleWeight = AdWeight.DEFAULT_WEIGHT,
            bottomWeight = AdWeight.DEFAULT_WEIGHT
        )

        _uiState.value = _uiState.value.copy(
            topWeight = AdWeight.DEFAULT_WEIGHT,
            middleWeight = AdWeight.DEFAULT_WEIGHT,
            bottomWeight = AdWeight.DEFAULT_WEIGHT
        )

        saveAdWeight(resetWeight)
    }
}

/**
 * UI 상태 데이터 클래스
 */
data class AdUiState(
    val topSection: AdSection = AdSection(ContentType.IMAGE, "", 1f),
    val middleSection: AdSection = AdSection(ContentType.VIDEO, "", 1f),
    val bottomSection: AdSection = AdSection(ContentType.IMAGE, "", 1f),
    val topWeight: Float = 1f,
    val middleWeight: Float = 1f,
    val bottomWeight: Float = 1f,
    val isLoading: Boolean = true,
    val error: String? = null
)
