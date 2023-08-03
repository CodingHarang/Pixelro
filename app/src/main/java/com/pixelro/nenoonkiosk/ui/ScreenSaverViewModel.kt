package com.pixelro.nenoonkiosk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.harang.data.repository.ScreenSaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenSaverViewModel @Inject constructor(
    private val screenSaverRepository: ScreenSaverRepository
) : ViewModel() {

    fun setMediaItem(exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            val videoURI = screenSaverRepository.getScreenSaverVideoURI()
            exoPlayer.setMediaItem(MediaItem.fromUri(videoURI))
            exoPlayer.prepare()
            exoPlayer.playWhenReady
        }
    }
}