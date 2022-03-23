package jp.chikaharu11.instant_drumpad_tr808

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.PlaybackParams
import android.net.Uri


class LoopMediaPlayer(context: Context, resId: Uri) {
    private var mContext: Context? = null
    private var mResId: Uri? = null
    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null
    private var count2 = 5
    private var bpm2 = 10
    private var count2f = 0.5f
    private var bpm2f = 1.0f

    private fun createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(mContext, mResId)
        mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
        mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
        setVolume(count2f,count2f)
        speed(bpm2f)
    }

    private val onCompletionListener =
        OnCompletionListener { mediaPlayer ->
            mediaPlayer.release()
            mCurrentPlayer = mNextPlayer
            createNextMediaPlayer()
        }

    @get:Throws(IllegalStateException::class)
    val isPlaying: Boolean
        get() = mCurrentPlayer!!.isPlaying

    private fun setVolume(leftVolume: Float, rightVolume: Float) {
        mCurrentPlayer!!.setVolume(leftVolume, rightVolume)
    }

    fun volumePlus() {
        if (count2 < 10) {
            count2 += 1
        }
        count2f = count2/10.0f
        setVolume(count2f, count2f)
        println(count2f)
    }

    fun volumeMinus() {
        if (count2 > 1) {
            count2 -= 1
        }
        count2f = count2/10.0f
        setVolume(count2f,count2f)
        println(count2f)
    }

    private fun speed(tempo : Float) {
        val params = PlaybackParams()
        params.speed = tempo
        mCurrentPlayer!!.playbackParams = params
    }

    fun speedUp() {
        if (bpm2 < 60) {
            bpm2 += 1
        }
        bpm2f = bpm2/10.0f
        speed(bpm2f)
        println(bpm2f)
    }

    fun speedDown() {
        if (bpm2 > 1) {
            bpm2 -= 1
        }
        bpm2f = bpm2/10.0f
        speed(bpm2f)
        println(bpm2f)
    }

    @Throws(IllegalStateException::class)
    fun start() {
        mCurrentPlayer!!.start()
    }

    @Throws(IllegalStateException::class)
    fun stop() {
        mCurrentPlayer!!.stop()
        mCurrentPlayer!!.prepare()
    }

    @Throws(IllegalStateException::class)
    fun pause() {
        mCurrentPlayer!!.pause()
    }

    fun release() {
        mCurrentPlayer!!.release()
        mNextPlayer!!.release()
    }

    fun reset() {
        mCurrentPlayer!!.reset()
        mNextPlayer!!.reset()
    }

    companion object {
        fun create(context: Context, resId: Uri): LoopMediaPlayer {
            return LoopMediaPlayer(context, resId)
        }
    }

    init {
        mContext = context
        mResId = resId
        mCurrentPlayer = MediaPlayer.create(mContext, mResId)
        createNextMediaPlayer()
    }
}