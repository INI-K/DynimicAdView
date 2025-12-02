package com.inik.dynamicadview.presentation.composable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType
import com.inik.dynamicadview.presentation.theme.DynamicAdViewTheme

/**
 * 광고 섹션 뷰 Composable
 * 콘텐츠 타입에 따라 이미지 또는 동영상을 표시
 */
@Composable
fun AdSectionView(
    section: AdSection,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (section.contentType) {
            ContentType.IMAGE -> {
                ImageSection(url = section.contentUrl)
            }

            ContentType.VIDEO -> {
                VideoSection(url = section.contentUrl)
            }

            ContentType.IMAGE_SLIDER -> {
                ImageSliderSection(
                    imageUrls = section.imageUrls,
                    delayMs = section.sliderDelayMs
                )
            }

            ContentType.EMPTY -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "빈 영역",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * 이미지 섹션
 * 이미지를 늘리지 않고 원본 비율 유지
 */
@Composable
private fun ImageSection(url: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "Ad Image",
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * 이미지 슬라이더 섹션
 * 여러 이미지를 자동으로 전환하며 표시
 *
 * @param imageUrls 슬라이더에 표시할 이미지 URL 리스트
 * @param delayMs 이미지 전환 딜레이 (밀리초)
 */
@Composable
private fun ImageSliderSection(imageUrls: List<String>, delayMs: Long = 1000L) {
    if (imageUrls.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "이미지 없음",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
        return
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // XML 레이아웃에서 ImageSlider inflate
            val view = LayoutInflater.from(context)
                .inflate(com.inik.dynamicadview.R.layout.layout_image_slider, null)
            val imageSlider =
                view.findViewById<ImageSlider>(com.inik.dynamicadview.R.id.image_slider)

            imageSlider.apply {
                // 이미지 URL을 SlideModel로 변환 (원본 비율 유지)
                val slideModels = imageUrls.map { url ->
                    SlideModel(url, scaleType = ScaleTypes.CENTER_INSIDE)
                }
                setImageList(slideModels)

                // 지정된 딜레이로 자동 슬라이드
                startSliding(delayMs)
            }

            view
        },
        update = { view ->
            // 이미지 리스트가 변경되면 업데이트
            val imageSlider =
                view.findViewById<ImageSlider>(com.inik.dynamicadview.R.id.image_slider)
            val slideModels = imageUrls.map { url ->
                SlideModel(url, scaleType = ScaleTypes.CENTER_INSIDE)
            }
            imageSlider.setImageList(slideModels)
        }
    )
}

/**
 * 동영상 섹션
 * key를 사용하여 URL이 변경될 때만 재생성되도록 최적화
 */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoSection(url: String) {
    val context = LocalContext.current

    val exoPlayer = remember(url) {
        ExoPlayer.Builder(context)
            .setVideoScalingMode(androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
            }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            (android.view.LayoutInflater.from(ctx)
                .inflate(
                    com.inik.dynamicadview.R.layout.view_texture_player,
                    FrameLayout(ctx),
                    false
                ) as PlayerView).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                keepScreenOn = false
                player = exoPlayer
            }
        },
        update = { view ->
            if (view.player !== exoPlayer) {
                view.player = exoPlayer
            }
        }
    )
}

// =========================== 프리뷰 ===========================

/**
 * 이미지 섹션 프리뷰
 * 단일 이미지를 원본 비율로 표시
 */
@Preview(name = "이미지 섹션", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ImageSectionPreview() {
    DynamicAdViewTheme {
        AdSectionView(
            section = AdSection(
                contentType = ContentType.IMAGE,
                contentUrl = "https://picsum.photos/seed/image1/1024/760",
                weight = 1f
            )
        )
    }
}

/**
 * 동영상 섹션 프리뷰
 * 실제 프리뷰에서는 동영상 플레이스홀더 표시
 */
@Preview(name = "동영상 섹션", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun VideoSectionPreview() {
    DynamicAdViewTheme {
        // 프리뷰에서는 동영상 대신 플레이스홀더 표시
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "▶ 동영상 재생 영역",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

/**
 * 이미지 슬라이더 섹션 프리뷰
 * 5개 이미지를 1초마다 자동 전환 (dots 없음)
 */
@Preview(name = "이미지 슬라이더", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ImageSliderSectionPreview() {
    DynamicAdViewTheme {
        AdSectionView(
            section = AdSection(
                contentType = ContentType.IMAGE_SLIDER,
                contentUrl = "",
                imageUrls = listOf(
                    "https://picsum.photos/seed/slider1/1024/760",
                    "https://picsum.photos/seed/slider2/1024/760",
                    "https://picsum.photos/seed/slider3/1024/760",
                    "https://picsum.photos/seed/slider4/1024/760",
                    "https://picsum.photos/seed/slider5/1024/760"
                ),
                weight = 1f,
                sliderDelayMs = 1000L
            )
        )
    }
}

/**
 * 빈 영역 프리뷰
 */
@Preview(name = "빈 영역", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun EmptySectionPreview() {
    DynamicAdViewTheme {
        AdSectionView(
            section = AdSection(
                contentType = ContentType.EMPTY,
                contentUrl = "",
                weight = 1f
            )
        )
    }
}

/**
 * 전체 화면 프리뷰
 * 상단(이미지 슬라이더) - 중단(동영상) - 하단(이미지)
 */
@Preview(name = "전체 화면", showBackground = true, showSystemUi = true)
@Composable
private fun FullScreenPreview() {
    DynamicAdViewTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 - 이미지 슬라이더
            Box(modifier = Modifier.weight(1f)) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE_SLIDER,
                        contentUrl = "",
                        imageUrls = listOf(
                            "https://picsum.photos/seed/top1/1024/760",
                            "https://picsum.photos/seed/top2/1024/760",
                            "https://picsum.photos/seed/top3/1024/760"
                        ),
                        weight = 1f,
                        sliderDelayMs = 1000L
                    )
                )
            }

            // 구분선
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.05f)
                    .background(Color.Red.copy(alpha = 0.7f))
            )

            // 중단 - 동영상 플레이스홀더
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "▶ 동영상",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // 구분선
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.05f)
                    .background(Color.Red.copy(alpha = 0.7f))
            )

            // 하단 - 이미지 슬라이더
            Box(modifier = Modifier.weight(1f)) {
                AdSectionView(
                    section = AdSection(
                        contentType = ContentType.IMAGE_SLIDER,
                        contentUrl = "",
                        imageUrls = listOf(
                            "https://picsum.photos/seed/bottom1/1024/760",
                            "https://picsum.photos/seed/bottom2/1024/760",
                            "https://picsum.photos/seed/bottom3/1024/760"
                        ),
                        weight = 1f,
                        sliderDelayMs = 1200L
                    )
                )
            }
        }
    }
}
