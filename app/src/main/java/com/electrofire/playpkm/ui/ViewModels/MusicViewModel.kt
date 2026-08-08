package com.electrofire.playpkm.ui.ViewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.electrofire.playpkm.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private val maxVolume = 0.1f

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    init {

        val tracks = listOf(
            R.raw.themetwocindery,
            R.raw.themeonecindery,
            R.raw.themethreecindery,
            R.raw.themefourcindery,
            R.raw.themefivecindery,
            R.raw.themesixcindery
        )

        tracks.forEach { resId ->
            val mediaItem = MediaItem.fromUri(
                Uri.parse("android.resource://${context.packageName}/$resId")
            )
            player.addMediaItem(mediaItem)
        }

        player.repeatMode = Player.REPEAT_MODE_ALL

        player.shuffleModeEnabled = true
        val randomIndex = (tracks.indices).random()
        player.seekTo(randomIndex, 0)

        player.volume = maxVolume
        player.prepare()
        play()
    }

    fun play() {
        if (!player.isPlaying) {
            player.play()
            _isPlaying.value = true
        }
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        player.volume = if (_isMuted.value) 0f else maxVolume
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
