package com.pixelro.nenoonkiosk.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.harang.data.repository.ScreenSaverRepository
import com.pixelro.nenoonkiosk.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@HiltViewModel
class ScreenSaverViewModel @Inject constructor(
    private val screenSaverRepository: ScreenSaverRepository
) : ViewModel() {

    fun setMediaItem(isSignedIn: Boolean, exoPlayer: ExoPlayer) {

        viewModelScope.launch {
            if (isSignedIn) {
                val videoURI = screenSaverRepository.getScreenSaverVideoURI()
                exoPlayer.setMediaItem(MediaItem.fromUri(videoURI))
            } else {
                exoPlayer.setMediaItem(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.ad_main)))
            }
            exoPlayer.prepare()
            exoPlayer.playWhenReady
        }
    }
}