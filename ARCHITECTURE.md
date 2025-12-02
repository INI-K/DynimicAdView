# DynamicAdView - 클린 아키텍처 구조

## 📋 프로젝트 개요

**DynamicAdView**는 사용자가 실시간으로 광고 영역의 크기를 조절할 수 있는 인터랙티브 광고 플랫폼입니다.
MVVM 패턴과 클린 아키텍처를 적용하여 유지보수성과 확장성을 높였습니다.

## 🏗 아키텍처 구조

```
┌─────────────────────────────────────────────┐
│         Presentation Layer                   │
│  - MainActivity                              │
│  - MainViewModel                             │
│  - ViewModelFactory                          │
│  - BindingAdapter                            │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         Domain Layer                         │
│  - Entity (AdContent, AdWeight)              │
│  - UseCase (GetAdContent, UpdateAdWeight,    │
│             CalculateWeight)                 │
│  - Repository Interface                      │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         Data Layer                           │
│  - DTO (AdContentDto, AdWeightDto,           │
│         AdConfigDto)                         │
│  - Repository Implementation                 │
│  - DataSource (Local, Remote)                │
└─────────────────────────────────────────────┘
```

## 📁 디렉토리 구조

```
app/src/main/java/com/inik/dynamicadview/
├── presentation/              # Presentation Layer
│   ├── MainViewModel.kt      # MVVM ViewModel
│   └── ViewModelFactory.kt   # ViewModel 팩토리
│
├── domain/                    # Domain Layer (비즈니스 로직)
│   ├── entity/               # 도메인 엔티티
│   │   ├── AdContent.kt     # 광고 콘텐츠 엔티티
│   │   └── AdWeight.kt      # 가중치 엔티티
│   ├── usecase/              # 유스케이스 (비즈니스 규칙)
│   │   ├── GetAdContentUseCase.kt
│   │   ├── UpdateAdWeightUseCase.kt
│   │   └── CalculateWeightUseCase.kt
│   └── repository/           # Repository 인터페이스
│       └── AdRepository.kt
│
├── data/                      # Data Layer (데이터 처리)
│   ├── dto/                  # Data Transfer Object
│   │   ├── AdContentDto.kt  # 광고 콘텐츠 DTO
│   │   ├── AdWeightDto.kt   # 가중치 DTO
│   │   └── AdConfigDto.kt   # 광고 설정 DTO ⭐
│   ├── datasource/           # 데이터 소스
│   │   ├── AdDataSource.kt  # 로컬 데이터 소스
│   │   └── RemoteAdDataSource.kt  # 원격 데이터 소스
│   └── repository/           # Repository 구현체
│       └── AdRepositoryImpl.kt
│
├── di/                        # Dependency Injection
│   └── DependencyInjection.kt
│
├── util/                      # 유틸리티
│   └── AdConfigManager.kt    # 광고 설정 관리 헬퍼
│
├── MainActivity.kt            # 메인 액티비티
└── BindingAdapter.kt          # Data Binding 어댑터
```

## 🎯 핵심 기능

### 1. 실시간 크기 조절 ✅

사용자가 빨간색 경계선(Divider)을 드래그하면 **실시간으로** 각 광고 영역의 크기가 변경됩니다.

**동작 원리:**

```kotlin
// MainViewModel.kt
fun onDividerTouch(event: MotionEvent, dividerIndex: Int): Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            // 드래그 시작 시 초기값 저장
            initialY = event.rawY
            initialWeight = AdWeight(...)
        }
        MotionEvent.ACTION_MOVE -> {
            // 드래그 중 실시간으로 가중치 계산 및 UI 업데이트
            val dy = event.rawY - initialY
            val newWeight = calculateWeightUseCase.calculate(dy, currentWeight)
            
            // LiveData 업데이트 → 즉시 UI 반영
            _topWeight.value = newWeight.topWeight
            _middleWeight.value = newWeight.middleWeight
            _bottomWeight.value = newWeight.bottomWeight
        }
        MotionEvent.ACTION_UP -> {
            // 드래그 종료 시 가중치 저장
            saveAdWeight(finalWeight)
        }
    }
    return true
}
```

**Data Binding으로 자동 UI 업데이트:**

```xml
<!-- activity_main.xml -->
<ImageView
    android:layout_height="0dp"
    app:layout_weight="@{viewModel.topWeight}" />
```

### 2. MVVM 클린 아키텍처 ✅

**관심사의 분리(Separation of Concerns):**

- **Presentation Layer**: UI 로직만 담당
- **Domain Layer**: 비즈니스 로직 (플랫폼 독립적)
- **Data Layer**: 데이터 소스 처리

**장점:**

- 테스트 용이성 향상
- 유지보수 편리
- 확장성 증대
- 코드 재사용성 증가

### 3. DTO (Data Transfer Object) ✅

다양한 DTO를 제공하여 광고 설정을 쉽게 관리할 수 있습니다.

#### AdContentDto

```kotlin
// 기본 광고 콘텐츠
val content = AdContentDto(
    topImageUrl = "https://example.com/top.jpg",
    middleVideoUrl = "https://example.com/video.mp4",
    bottomImageUrl = "https://example.com/bottom.jpg",
    topWeight = 1f,
    middleWeight = 2f,
    bottomWeight = 1f
)
```

#### AdWeightDto

```kotlin
// 가중치만 별도로 관리
val weight = AdWeightDto(
    topWeight = 1f,
    middleWeight = 2f,
    bottomWeight = 1f
)

// 퍼센트로 변환
val (topPercent, middlePercent, bottomPercent) = 
    AdConfigManager.weightToPercentage(weight)
// 결과: (25%, 50%, 25%)
```

#### AdConfigDto ⭐ (가장 강력한 DTO)

```kotlin
// 완전한 광고 설정
val config = AdConfigDto(
    adId = "campaign_001",
    topImageUrl = "https://example.com/top.jpg",
    middleVideoUrl = "https://example.com/video.mp4",
    bottomImageUrl = "https://example.com/bottom.jpg",
    initialWeights = AdConfigDto.WeightConfig(
        top = 1f,
        middle = 2f,  // 동영상 영역 2배 크기
        bottom = 1f
    ),
    enableUserResize = true,  // 사용자 조절 허용
    minWeight = 0.1f,
    maxWeight = 5f,
    autoSave = true
)
```

## 💡 DTO 사용 예시

### 예시 1: 프리셋 적용

```kotlin
// AdConfigManager의 프리셋 사용
val presets = AdConfigManager.presets

// "동영상 중심" 프리셋 적용
val videoFocusConfig = AdConfigManager.createSampleConfig(
    adId = "video_campaign",
    topWeight = 0.5f,
    middleWeight = 3f,  // 동영상을 크게
    bottomWeight = 0.5f
)
```

### 예시 2: 원격 데이터 가져오기

```kotlin
// RemoteAdDataSource 사용
val remoteSource = RemoteAdDataSource()

// 서버에서 광고 설정 가져오기
val config = remoteSource.fetchAdConfig("campaign_001")

// DTO → Entity 변환
val adContent = config.toAdContentDto().toEntity()
```

### 예시 3: 사용자 선호도 전송

```kotlin
// 사용자가 조절한 가중치를 서버로 전송 (분석용)
val userWeight = AdWeightDto(
    topWeight = viewModel.topWeight.value!!,
    middleWeight = viewModel.middleWeight.value!!,
    bottomWeight = viewModel.bottomWeight.value!!
)

remoteSource.uploadUserPreference(
    userId = "user_123",
    adId = "campaign_001",
    weights = userWeight
)
```

### 예시 4: 가중치 검증

```kotlin
// 설정 유효성 검사
val isValid = AdConfigManager.validateConfig(config)

if (isValid) {
    // 광고 로드
    loadAd(config)
} else {
    // 에러 처리
    showError("잘못된 광고 설정입니다")
}
```

## 🔄 데이터 흐름

```
User Interaction (드래그)
        ↓
MainActivity → MainViewModel.onDividerTouch()
        ↓
CalculateWeightUseCase (가중치 계산)
        ↓
LiveData 업데이트 (실시간 UI 반영) ⚡
        ↓
UpdateAdWeightUseCase → Repository → DataSource
        ↓
데이터 저장/전송
```

## 🎨 사용 기술 스택

- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **UI**: Data Binding, LiveData
- **Video**: ExoPlayer 2.18.1
- **Image**: Coil 2.4.0
- **Async**: Coroutines
- **Dependency Injection**: Manual DI (향후 Hilt 적용 가능)

## ✅ 확인 완료 사항

1. ✅ **실시간 크기 조절 기능** - 드래그 시 즉시 UI 업데이트
2. ✅ **MVVM 아키텍처** - ViewModel, LiveData 활용
3. ✅ **클린 아키텍처** - Domain, Data, Presentation Layer 분리
4. ✅ **DTO 구현** - AdContentDto, AdWeightDto, AdConfigDto
5. ✅ **유스케이스 패턴** - 비즈니스 로직 캡슐화
6. ✅ **Repository 패턴** - 데이터 소스 추상화
7. ✅ **빌드 성공** - APK 생성 완료

## 🚀 향후 개선 가능 사항

1. **Hilt/Koin** - 의존성 주입 라이브러리 도입
2. **Room Database** - 로컬 데이터 영구 저장
3. **Retrofit** - API 통신 구현
4. **Unit Test** - 각 Layer별 테스트 코드 작성
5. **UI Test** - Espresso를 활용한 UI 테스트
6. **애니메이션** - 드래그 시 부드러운 애니메이션 추가
7. **다크모드** - 테마 지원

## 📝 라이선스

이 프로젝트는 실시간 광고 영역 조절 시스템의 데모 구현입니다.
