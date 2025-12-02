package com.inik.dynamicadview.presentation.composable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import com.inik.dynamicadview.domain.entity.AdSection
import com.inik.dynamicadview.domain.entity.ContentType

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

            ContentType.EMPTY -> {
                // 빈 영역
            }
        }
    }
}

/**
 * 이미지 섹션
 */
@Composable
private fun ImageSection(url: String) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = "Ad Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
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
