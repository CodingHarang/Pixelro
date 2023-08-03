package com.pixelro.nenoonkiosk.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.harang.data.repository.ScreenSaverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

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
                exoPlayer.setMediaItem(MediaItem.fromUri(Uri.fromFile(File("/storage/emulated/0/Download/ad1.mp4"))))
            }
            exoPlayer.prepare()
            exoPlayer.playWhenReady
        }
    }
}