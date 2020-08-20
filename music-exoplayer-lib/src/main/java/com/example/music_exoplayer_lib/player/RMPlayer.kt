package com.example.music_exoplayer_lib.player

import android.content.Context
import android.net.Uri
import com.example.music_exoplayer_lib.iinterface.AutioControlListener
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 *
 * @des:
 * @data: 8/19/20 11:11 AM
 * @Version: 1.0.0
 */
class RMPlayer constructor(var context: Context) {
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    val RADIO_URL = "http://kastos.cdnstream.com/1345_32"
    private lateinit var listener: AutioControlListener

    init {
        initializePlayer()
        initListener()
    }

    fun setOnAutioControlListener(listener: AutioControlListener?) {
        this.listener = listener!!
    }
    fun initializePlayer() {

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)

        dataSourceFactory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, "exoPlayerSample"))

        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(RADIO_URL))


        with(simpleExoPlayer) {
            prepare(mediaSource)
            playWhenReady = true
        }
    }

    private fun initListener() {
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

            }
            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {

            }

            override fun onLoadingChanged(isLoading: Boolean) {

            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

            }

            override fun onRepeatModeChanged(repeatMode: Int) {

            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

            }

            override fun onPlayerError(error: ExoPlaybackException?) {
            }

            override fun onPositionDiscontinuity(reason: Int) {

            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            }

            override fun onSeekProcessed() {
            }

        })

    }
}