
package com.play.a360magic

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.video.spherical.SphericalGLSurfaceView
import com.play.a360magic.databinding.VideoPlayerLayoutBinding
import java.lang.Exception


class PlayerViewActivity : AppCompatActivity() , Player.Listener {

    private lateinit var binding : VideoPlayerLayoutBinding
    private var player: SimpleExoPlayer? = null
    private  var url:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideoPlayerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (binding.playerView.videoSurfaceView as SphericalGLSurfaceView)
            .setDefaultStereoMode(C.STEREO_MODE_STEREO_MESH)
        setListeners()
    }

    override
    fun onPlayerError(error: PlaybackException) {
        val cause = error.cause
        try {
            Toast.makeText(this@PlayerViewActivity, cause?.message, Toast.LENGTH_LONG).show()
        }catch (e:Exception){e.printStackTrace()}
    }
    private fun setListeners(){

        binding.urlUpdateButton.setOnClickListener { onBackPressed() }

    }
    override fun onStart() {
        super.onStart()
        if (isSupportedMultiWindow()) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((isSupportedMultiWindow() || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isSupportedMultiWindow()) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isSupportedMultiWindow()) {
            releasePlayer()
        }
    }

    private fun isSupportedMultiWindow() = Build.VERSION.SDK_INT > 23

    private fun buildMediaSource(source: String): MediaItem {
//        val dataSourceFactory = DefaultDataSourceFactory(this, "javiermarsicano-VR-app")
//        // Create a media source using the supplied URI
//        return ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(uri)
        return when (PublicFunctions.getMimeType(source)) {

            PublicValues.KEY_MP4 -> buildMediaItemMP4(source)
            PublicValues.KEY_M3U8 -> buildMediaHLS(source)
            PublicValues.KEY_MP3 -> buildMediaItemMP4(source)

            else -> buildMediaGlobal(source)
        }


    }

    private fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(this).build()
        }
        url = intent.getStringExtra("Url")

//        val uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-1/360/congo.mp4")
//        val uri = Uri.parse("https://dash.akamaized.net/dash264/TestCases/9a/qualcomm/1/MultiRate.mpd")
//        val mediaSource = buildMediaSource("https://6a23-141-24-49-108.ngrok.io/manifest.mpd")
//        val mediaSource = buildMediaSource("https://storage.googleapis.com/exoplayer-test-media-1/360/congo.mp4")
        val mediaSource = buildMediaSource(url!!)
        player?.setMediaItem(mediaSource)
//        player?.addListener(this)

        binding.playerView.player = player
        binding.playerView.onResume()
    }

    private fun releasePlayer() {
        binding.playerView.onPause()
        player?.release()
        player = null
    }











    private fun buildMediaItemMP4(
        source: String

    ): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_MP4)
//            .setLicenseRequestHeaders(extraHeaders)
            .build()
    }

    private fun buildMediaHLS(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
//            .setLicenseRequestHeaders(extraHeaders)
            .build()
    }

    private fun buildMediaDash(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
            .setMimeType(MimeTypes.APPLICATION_MPD)
//            .setLicenseRequestHeaders(extraHeaders)
            .build()
    }

    private fun buildMediaGlobal(source: String): MediaItem {
        return MediaItem.Builder()
            .setUri(source)
//            .setLicenseRequestHeaders(extraHeaders)
            .build()
    }

}

