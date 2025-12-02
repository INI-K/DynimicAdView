# DynamicAdView - 클린 아키텍처 구조

## 📋 프로젝트 개요

**DynamicAdView**는 사용자가 실시간으로 영역 크기를 조절할 수 있는 동적 광고 뷰 시스템입니다.

### 핵심 기능

- ✅ 3개 영역(상단/중단/하단)으로 구성된 광고 레이아웃
- ✅ 드래그로 실시간 영역 크기 조절
- ✅ 각 영역에 **이미지** 또는 **동영상** 표시 가능
- ✅ 콘텐츠 타입을 동적으로 변경 가능
- ✅ Jetpack Compose UI
- ✅ Material Design 3

## 🏗️ 아키텍처

클린 아키텍처 + MVVM 패턴 적용

```
app/src/main/java/com/inik/dynamicadview/
├── data/                          # Data Layer
│   ├── datasource/               # 데이터 소스
│   │   ├── AdDataSource.kt       # 로컬 데이터 소스
│   │   └── RemoteAdDataSource.kt # 원격 데이터 소스 (시뮬레이션)
│   ├── dto/                      # Data Transfer Objects
│   │   ├── AdContentDto.kt
│   │   ├── AdSectionDto.kt
│   │   ├── AdWeightDto.kt
│   │   └── AdConfigDto.kt
│   └── repository/               # Repository 구현
│       └── AdRepositoryImpl.kt
│
├── domain/                        # Domain Layer
│   ├── entity/                   # 도메인 엔티티
│   │   ├── AdContent.kt
│   │   ├── AdSection.kt
│   │   ├── AdWeight.kt
│   │   └── ContentType.kt
│   ├── repository/               # Repository 인터페이스
│   │   └── AdRepository.kt
│   └── usecase/                  # 비즈니스 로직
│       ├── GetAdContentUseCase.kt
│       ├── UpdateAdContentUseCase.kt
│       ├── UpdateAdWeightUseCase.kt
│       └── CalculateWeightUseCase.kt
│
├── presentation/                  # Presentation Layer
│   ├── MainViewModel.kt          # ViewModel (StateFlow)
│   ├── composable/               # Composable UI
│   │   ├── DynamicAdScreen.kt
│   │   └── AdSectionView.kt
│   └── theme/                    # Material3 테마
│       ├── Theme.kt
│       └── Type.kt
│
├── di/                           # Dependency Injection
│   ├── DataSourceModule.kt
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
│
├── util/                         # 유틸리티
│   ├── AdPresets.kt             # 광고 프리셋
│   ├── AdContentBuilder.kt      # Builder 패턴
│   └── AdConfigManager.kt       # 설정 관리
│
├── DynamicAdApplication.kt       # Application 클래스
└── MainActivity.kt               # 메인 액티비티
```

## 🔧 기술 스택

### Core

- **언어**: Kotlin 1.9.22
- **UI**: Jetpack Compose + Material3
- **아키텍처**: Clean Architecture + MVVM
- **의존성 주입**: Hilt (KSP)
- **비동기**: Coroutines + StateFlow

### Libraries

- **이미지 로딩**: Coil Compose 2.5.0
- **동영상 재생**: ExoPlayer (Media3)
- **ViewModel**: Lifecycle ViewModel Compose
- **Navigation**: Hilt Navigation Compose

## 📱 사용 방법

### 1. 기본 사용

```kotlin
@Composable
fun MyScreen() {
    DynamicAdScreen()
}
```

### 2. 콘텐츠 변경

```kotlin
// ViewModel에서 섹션 콘텐츠 변경
viewModel.updateSectionContent(
    position = 0,  // 0: 상단, 1: 중단, 2: 하단
    contentType = ContentType.VIDEO,
    url = "https://example.com/video.mp4"
)
```

### 3. 프리셋 사용

```kotlin
// AdPresets에서 미리 정의된 레이아웃 사용
val allVideo = AdPresets.ALL_VIDEO
val allImage = AdPresets.ALL_IMAGE
val videoSandwich = AdPresets.VIDEO_SANDWICH
```

### 4. Builder 패턴으로 생성

```kotlin
val content = AdContentBuilder()
    .topImage("https://example.com/top.jpg", weight = 1f)
    .middleVideo("https://example.com/video.mp4", weight = 2f)
    .bottomImage("https://example.com/bottom.jpg", weight = 1f)
    .build()
```

## 🎯 클린 아키텍처 레이어

### Domain Layer (핵심 비즈니스 로직)

- Android/Framework에 독립적
- 비즈니스 규칙과 엔티티 정의
- UseCase로 비즈니스 로직 캡슐화

### Data Layer (데이터 관리)

- Repository 인터페이스 구현
- DTO를 통한 데이터 전송
- DataSource로 데이터 소스 추상화

### Presentation Layer (UI)

- Jetpack Compose UI
- StateFlow로 상태 관리
- ViewModel이 Domain Layer에만 의존

## 🔄 데이터 흐름

```
UI (Compose)
    ↕ StateFlow
ViewModel
    ↕ suspend fun
UseCase
    ↕ suspend fun
Repository (Interface)
    ↕ DTO
RepositoryImpl
    ↕ DTO
DataSource
```

## 📦 DTO (Data Transfer Object)

### AdContentDto

광고 콘텐츠 전송 객체

```kotlin
AdContentDto(
    topSection = AdSectionDto("IMAGE", "url", 1f),
    middleSection = AdSectionDto("VIDEO", "url", 1f),
    bottomSection = AdSectionDto("IMAGE", "url", 1f)
)
```

### AdSectionDto

개별 섹션 정보

```kotlin
AdSectionDto(
    contentType = "IMAGE" or "VIDEO",
    contentUrl = "https://...",
    weight = 1.0f
)
```

### AdWeightDto

가중치 정보

```kotlin
AdWeightDto(
    topWeight = 1f,
    middleWeight = 2f,
    bottomWeight = 1f
)
```

## 🎨 UI 컴포넌트

### DynamicAdScreen

메인 화면 Composable

- 3개의 광고 섹션
- 2개의 드래그 가능한 경계선
- 실시간 크기 조절

### AdSectionView

개별 섹션 표시

- ContentType에 따라 Image 또는 Video 표시
- Coil Compose로 이미지 로딩
- ExoPlayer로 동영상 재생

## 🧪 확장 가능성

### 새로운 콘텐츠 타입 추가

1. `ContentType` enum에 타입 추가
2. `AdSectionView`에 새로운 타입 처리 로직 추가
3. DTO 업데이트

### 새로운 데이터 소스 추가

1. DataSource 클래스 생성
2. `DataSourceModule`에 Provider 추가
3. Repository 수정

### 분석 기능 추가

1. `UpdateAdWeightUseCase`에 분석 로직 추가
2. `RemoteAdDataSource`에 전송 로직 구현

## 📝 의존성 주입 (Hilt)

### Modules

- **DataSourceModule**: 데이터 소스 제공
- **RepositoryModule**: Repository 구현체 제공
- **UseCaseModule**: UseCase 제공

### Scopes

- **Singleton**: DataSource, Repository
- **ViewModelScoped**: UseCase

## 🚀 빌드

```bash
./gradlew assembleDebug
```

## 📄 라이센스

이 프로젝트는 클린 아키텍처 학습 및 참고용입니다.
