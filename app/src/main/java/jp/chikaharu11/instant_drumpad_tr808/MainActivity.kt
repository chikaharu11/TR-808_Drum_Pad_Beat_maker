package jp.chikaharu11.instant_drumpad_tr808

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import jp.chikaharu11.instant_drumpad_tr808.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.hypot
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), CustomAdapterListener {

    private var mRewardedAd: RewardedAd? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var adViewContainer: FrameLayout
    private lateinit var admobmAdView: AdView

    private var mpDuration = 320
    private var mpDuration2 = 625
    private var mpDuration3 = 1294
    private var mpDuration4 = 1033
    private var mpDuration5 = 1465
    private var mpDuration6 = 1072
    private var mpDuration7 = 794
    private var mpDuration8 = 1065
    private var mpDuration9 = 1065
    private var mpDuration10 = 1137
    private var mpDuration11 = 773
    private var mpDuration12 = 1070
    private var mpDuration13 = 1050
    private var mpDuration14 = 608
    private var mpDuration15 = 55
    private var noteDuration = 1000.toLong()
    private var noteDurationConst = 1000.toLong()

    private var actionTitle = "BEAT 3 BPM110"
    private var padText1 = "clsd_hi_hat_01"
    private var padText2 = "open_hi_hat_01"
    private var padText3 = "tr_8_cymbal_01"
    private var padText4 = "snare_drum_05"
    private var padText5 = "low_tom_01"
    private var padText6 = "snare_drum_01"
    private var padText7 = "tr_909_bass_drum_02"
    private var padText8 = "mid_tom_01"
    private var padText9 = "bass_drum_short_01"
    private var padText10 = "claves_02"
    private var padText11 = "high_tom_01"
    private var padText12 = "high_conga_01"
    private var padText13 = "tr_909_cymbal_01"
    private var padText14 = "tr_909_cymbal_02"
    private var padText15 = "clap_01"

    private var count = 5
    private var bpm = 10

    private var soundPoolVolume = 0.5f
    private var soundPoolTempo = 1.0f
    private var soundPoolVolume2 = 0.5f
    private var soundPoolTempo2 = 1.0f
    private var soundPoolVolume3 = 0.5f
    private var soundPoolTempo3 = 1.0f
    private var soundPoolVolume4 = 0.5f
    private var soundPoolTempo4 = 1.0f
    private var soundPoolVolume5 = 0.5f
    private var soundPoolTempo5 = 1.0f
    private var soundPoolVolume6 = 0.5f
    private var soundPoolTempo6 = 1.0f
    private var soundPoolVolume7 = 0.5f
    private var soundPoolTempo7 = 1.0f
    private var soundPoolVolume8 = 0.5f
    private var soundPoolTempo8 = 1.0f
    private var soundPoolVolume9 = 0.5f
    private var soundPoolTempo9 = 1.0f
    private var soundPoolVolume10 = 0.5f
    private var soundPoolTempo10 = 1.0f
    private var soundPoolVolume11 = 0.5f
    private var soundPoolTempo11 = 1.0f
    private var soundPoolVolume12 = 0.5f
    private var soundPoolTempo12 = 1.0f
    private var soundPoolVolume13 = 0.5f
    private var soundPoolTempo13 = 1.0f
    private var soundPoolVolume14 = 0.5f
    private var soundPoolTempo14 = 1.0f
    private var soundPoolVolume15 = 0.5f
    private var soundPoolTempo15 = 1.0f

    private var spvF = 5
    private var sptF = 10
    private var spvF2 = 5
    private var sptF2 = 10
    private var spvF3 = 5
    private var sptF3 = 10
    private var spvF4 = 5
    private var sptF4 = 10
    private var spvF5 = 5
    private var sptF5 = 10
    private var spvF6 = 5
    private var sptF6 = 10
    private var spvF7 = 5
    private var sptF7 = 10
    private var spvF8 = 5
    private var sptF8 = 10
    private var spvF9 = 5
    private var sptF9 = 10
    private var spvF10 = 5
    private var sptF10 = 10
    private var spvF11 = 5
    private var sptF11 = 10
    private var spvF12 = 5
    private var sptF12 = 10
    private var spvF13 = 5
    private var sptF13 = 10
    private var spvF14 = 5
    private var sptF14 = 10
    private var spvF15 = 5
    private var sptF15 = 10

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 41
        private const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 42
    }

    @SuppressLint("Range")
    fun selectEX() {
        if (!isReadExternalStoragePermissionGranted()) {
            requestReadExternalStoragePermission()
        } else {
            tSoundList.clear()
            val audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor = contentResolver.query(audioUri, null, null, null, null)
            cursor!!.moveToFirst()
            val path: Array<String?> = arrayOfNulls(cursor.count)
            for (i in path.indices) {
                path[i] = cursor.getString(cursor.getColumnIndex("_data"))
                tSoundList.add(SoundList(path[i].toString()))
                cursor.moveToNext()
            }

            cursor.close()
        }
    }

    private lateinit var soundPool: SoundPool

    private lateinit var mp: MediaPlayer

    private lateinit var getmpDuration: MediaPlayer

    private lateinit var lmp: LoopMediaPlayer

    private lateinit var aCustomAdapter: CustomAdapter
    private lateinit var bCustomAdapter: CustomAdapter
    private lateinit var cCustomAdapter: CustomAdapter
    private lateinit var dCustomAdapter: CustomAdapter
    private lateinit var eCustomAdapter: CustomAdapter
    private lateinit var fCustomAdapter: CustomAdapter
    private lateinit var gCustomAdapter: CustomAdapter
    private lateinit var hCustomAdapter: CustomAdapter
    private lateinit var iCustomAdapter: CustomAdapter
    private lateinit var jCustomAdapter: CustomAdapter
    private lateinit var kCustomAdapter: CustomAdapter
    private lateinit var lCustomAdapter: CustomAdapter

    private lateinit var nCustomAdapter: CustomAdapter
    private lateinit var oCustomAdapter: CustomAdapter
    private lateinit var pCustomAdapter: CustomAdapter
    private lateinit var qCustomAdapter: CustomAdapter
    private lateinit var rCustomAdapter: CustomAdapter

    private lateinit var sCustomAdapter: CustomAdapter
    private lateinit var tCustomAdapter: CustomAdapter

    private lateinit var aSoundList: MutableList<SoundList>
    private lateinit var bSoundList: MutableList<SoundList>
    private lateinit var cSoundList: MutableList<SoundList>
    private lateinit var dSoundList: MutableList<SoundList>
    private lateinit var eSoundList: MutableList<SoundList>
    private lateinit var fSoundList: MutableList<SoundList>
    private lateinit var gSoundList: MutableList<SoundList>
    private lateinit var hSoundList: MutableList<SoundList>
    private lateinit var iSoundList: MutableList<SoundList>
    private lateinit var jSoundList: MutableList<SoundList>
    private lateinit var kSoundList: MutableList<SoundList>
    private lateinit var lSoundList: MutableList<SoundList>

    private lateinit var nSoundList: MutableList<SoundList>
    private lateinit var oSoundList: MutableList<SoundList>
    private lateinit var pSoundList: MutableList<SoundList>
    private lateinit var qSoundList: MutableList<SoundList>
    private lateinit var rSoundList: MutableList<SoundList>

    private lateinit var sSoundList: MutableList<SoundList>
    private lateinit var tSoundList: MutableList<SoundList>

    private lateinit var mRealm: Realm

    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0
    private var sound5 = 0
    private var sound6 = 0
    private var sound7 = 0
    private var sound8 = 0
    private var sound9 = 0
    private var sound10 = 0
    private var sound11 = 0
    private var sound12 = 0
    private var sound13 = 0
    private var sound14 = 0
    private var sound15 = 0
    private var sound16 = 0
    private var sound17 = 0

    private var paste = 0

    private var buttonA = 0
    private var buttonB = 0

    private var adCheck = 0

    private var padCheck = 53

    private var colorCheck = 1

    private var beatCheck = ""

    private var noteCount = 0

    private var sequencerCount = 0

    private var sequencerSize = 0

    private var sequencerMaxSize = 0

    private var sequencerBpm: Long = 110

    val handler = Handler(Looper.getMainLooper())

    private var a1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var a16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var b16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var c16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var d16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    private var e16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)

    var timer: Timer? = null

    private var se1 by Delegates.observable(0) { _, _, _ ->
        soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
    }
    private var se2 by Delegates.observable(0) { _, _, _ ->
        soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
    }
    private var se3 by Delegates.observable(0) { _, _, _ ->
        soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
    }
    private var se4 by Delegates.observable(0) { _, _, _ ->
        soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
    }
    private var se5 by Delegates.observable(0) { _, _, _ ->
        soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
    }

    private val runnable = object: Runnable{
        override fun run () {
            handler.postDelayed(this, noteDuration)
            noteCount++
            when {
                noteCount == 1 && beatCheck == "reggaeton_1_bpm90" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.reggaeton1_1, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   1/2"
                }
                noteCount == 2 && beatCheck == "reggaeton_1_bpm90" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.reggaeton1_2, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   2/2"
                    noteCount = 0
                }
                noteCount == 1 && beatCheck == "beat_1" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat1_1, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   1/2"
                }
                noteCount == 2 && beatCheck == "beat_1" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat1_2, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   2/2"
                    noteCount = 0
                }
                noteCount == 1 && beatCheck == "beat_2" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat2_1, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   1/2"
                }
                noteCount == 2 && beatCheck == "beat_2" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat2_2, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   2/2"
                    noteCount = 0
                }
                noteCount == 1 && beatCheck == "beat_3" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat3_1, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   1/2"
                }
                noteCount == 2 && beatCheck == "beat_3" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat3_2, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   2/2"
                    noteCount = 0
                }
                noteCount == 1 && beatCheck == "beat_9" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat9_1, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   1/2"
                }
                noteCount == 2 && beatCheck == "beat_9" -> {
                    binding.notes.setImageDrawable(getDrawable(resources, R.drawable.beat9_2, null))
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase() + "   2/2"
                    noteCount = 0
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sequencerPlay() {
            runBlocking {
                val job1 = launch {
                    soundPool.play(sound17, 1.0f, 1.0f, 0, 0, 1.0f)
                    soundPool.play(sound17, 1.0f, 1.0f, 0, 0, 1.0f)
                    soundPool.play(sound17, 1.0f, 1.0f, 0, 0, 1.0f)
                    delay(100)
                }
                job1.join()
            }
        sequencerSize = 0
        timer = Timer()
        timer!!.scheduleAtFixedRate(0, 15000/sequencerBpm) {
            sequencerCount++
            when (sequencerCount) {
                1 -> {
                    GlobalScope.launch { if (a1[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b1[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c1[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d1[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e1[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number16).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                2 -> {
                    GlobalScope.launch { if (a2[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b2[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c2[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d2[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e2[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number2).setBackgroundColor(Color.parseColor("#FFFFFF"))

                }
                3 -> {
                    GlobalScope.launch { if (a3[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b3[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c3[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d3[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e3[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number2).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number3).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                4 -> {
                    GlobalScope.launch { if (a4[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b4[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c4[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d4[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e4[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number3).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number4).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                5 -> {
                    GlobalScope.launch { if (a5[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b5[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c5[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d5[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e5[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number4).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number5).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                6 -> {
                    GlobalScope.launch { if (a6[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b6[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c6[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d6[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e6[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number5).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number6).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                7 -> {
                    GlobalScope.launch { if (a7[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b7[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c7[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d7[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e7[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number6).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number7).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                8 -> {
                    GlobalScope.launch { if (a8[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b8[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c8[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d8[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e8[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number7).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number8).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                9 -> {
                    GlobalScope.launch { if (a9[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b9[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c9[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d9[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e9[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number8).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number9).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                10 -> {
                    GlobalScope.launch { if (a10[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b10[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c10[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d10[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e10[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number9).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number10).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                11 -> {
                    GlobalScope.launch { if (a11[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b11[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c11[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d11[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e11[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number10).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number11).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                12 -> {
                    GlobalScope.launch { if (a12[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b12[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c12[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d12[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e12[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number11).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number12).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                13 -> {
                    GlobalScope.launch { if (a13[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b13[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c13[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d13[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e13[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number12).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number13).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                14 -> {
                    GlobalScope.launch { if (a14[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b14[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c14[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d14[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e14[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number13).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number14).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                15 -> {
                    GlobalScope.launch { if (a15[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b15[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c15[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d15[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e15[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number14).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number15).setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                16 -> {
                    GlobalScope.launch { if (a16[sequencerSize] == 1) { se1++ } }
                    GlobalScope.launch { if (b16[sequencerSize] == 1) { se2++ } }
                    GlobalScope.launch { if (c16[sequencerSize] == 1) { se3++ } }
                    GlobalScope.launch { if (d16[sequencerSize] == 1) { se4++ } }
                    GlobalScope.launch { if (e16[sequencerSize] == 1) { se5++ } }
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number15).setBackgroundColor(Color.parseColor("#5A5A66"))
                    findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number16).setBackgroundColor(Color.parseColor("#FFFFFF"))
                    sequencerCount = 0
                    if (sequencerSize == sequencerMaxSize) {
                        sequencerSize = 0
                        changeSequence()
                    } else {
                        sequencerSize++
                        changeSequence()
                    }
                }
            }
        }
    }

    private fun sequencerStop() {
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_view).findViewById<TextView>(R.id.number16).setBackgroundColor(Color.parseColor("#5A5A66"))
        sequencerCount = 0
        timer?.cancel()
        timer = null
    }

    private fun resetSequence() {
        a1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        a16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        b16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        c16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        d16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e1 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e2 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e3 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e4 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e5 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e6 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e7 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e8 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e9 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e10 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e11 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e12 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e13 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e14 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e15 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        e16 = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        resetColor()
    }

    private fun resetColor() {
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
    }

    private fun changeSequence() {
        resetColor()
        if ( a1[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a2[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a3[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a4[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a5[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a6[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a7[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a8[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a9[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a10[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a11[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a12[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a13[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a14[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a15[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( a16[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#d03933")) }
        if ( b1[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b2[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b3[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b4[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b5[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b6[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b7[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b8[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b9[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b10[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b11[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b12[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b13[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b14[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b15[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( b16[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#e98e2f")) }
        if ( c1[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c2[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c3[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c4[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c5[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c6[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c7[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c8[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c9[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c10[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c11[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c12[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c13[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c14[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c15[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( c16[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#dfd441")) }
        if ( d1[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d2[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d3[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d4[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d5[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d6[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d7[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d8[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d9[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d10[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d11[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d12[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d13[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d14[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d15[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( d16[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#e9e8e7")) }
        if ( e1[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e2[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e3[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e4[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e5[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e6[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e7[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e8[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e9[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e10[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e11[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e12[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e13[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e14[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e15[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#ffffff")) }
        if ( e16[sequencerSize] == 1 ) { findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#ffffff")) }
        }

    private fun hiphopSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 80
        a1[0] = 1
        a5[0] = 1
        a9[0] = 1
        a13[0] = 1
        b5[0] = 1
        b13[0] = 1
        c1[0] = 1
        c3[0] = 1
        c11[0] = 1
    }

    private fun reggaetonSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 1
        sequencerSize = 0
        sequencerBpm = 90
        a1[0] = 1
        a5[0] = 1
        a9[0] = 1
        a13[0] = 1
        b4[0] = 1
        b7[0] = 1
        b12[0] = 1
        b15[0] = 1
        c4[0] = 1
        c7[0] = 1
        c12[0] = 1
        c15[0] = 1

        a1[1] = 1
        a5[1] = 1
        a9[1] = 1
        a13[1] = 1
        b4[1] = 1
        b7[1] = 1
        b10[1] = 1
        b12[1] = 1
        b15[1] = 1
        c4[1] = 1
        c7[1] = 1
        c10[1] = 1
        c12[1] = 1
        c15[1] = 1
    }

    private fun electronicaSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 90
        a1[0] = 1
        a2[0] = 1
        a3[0] = 1
        a4[0] = 1
        a5[0] = 1
        a6[0] = 1
        a7[0] = 1
        a8[0] = 1
        a9[0] = 1
        a10[0] = 1
        a11[0] = 1
        a12[0] = 1
        a13[0] = 1
        a14[0] = 1
        a15[0] = 1
        a16[0] = 1
        b13[0] = 1
        c1[0] = 1
        c7[0] = 1
        c12[0] = 1
        d4[0] = 1
    }

    private fun dubstepSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 140
        a1[0] = 1
        a3[0] = 1
        a4[0] = 1
        a7[0] = 1
        a11[0] = 1
        a14[0] = 1
        b9[0] = 1
        b16[0] = 1
        c1[0] = 1
        c4[0] = 1
        c7[0] = 1
        c15[0] = 1
        d1[0] = 1
    }

    private fun houseSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 130
        a1[0] = 1
        a5[0] = 1
        a9[0] = 1
        a13[0] = 1
        b3[0] = 1
        b7[0] = 1
        b11[0] = 1
        b15[0] = 1
        c4[0] = 1
        c7[0] = 1
        c10[0] = 1
        c15[0] = 1
        d1[0] = 1
        d5[0] = 1
        d9[0] = 1
        d13[0] = 1
        e5[0] = 1
    }

    private fun discoSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 110
        a1[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a13[0] = 1
        b3[0] = 1
        b11[0] = 1
        b15[0] = 1
        c5[0] = 1
        c8[0] = 1
        c16[0] = 1
        d1[0] = 1
        d7[0] = 1
        d13[0] = 1
        e5[0] = 1
        e11[0] = 1
        e13[0] = 1
    }

    private fun technoSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 110
        a1[0] = 1
        a2[0] = 1
        a5[0] = 1
        a6[0] = 1
        a9[0] = 1
        a10[0] = 1
        a13[0] = 1
        a14[0] = 1
        b3[0] = 1
        b7[0] = 1
        b11[0] = 1
        b15[0] = 1
        c5[0] = 1
        c13[0] = 1
        c16[0] = 1
        d1[0] = 1
        d5[0] = 1
        d9[0] = 1
        d13[0] = 1
        e5[0] = 1
    }

    private fun eurobeatSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 130
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a11[0] = 1
        a13[0] = 1
        a15[0] = 1
        b2[0] = 1
        b3[0] = 1
        b7[0] = 1
        b8[0] = 1
        b10[0] = 1
        b11[0] = 1
        b14[0] = 1
        b15[0] = 1
        b16[0] = 1
        c5[0] = 1
        c13[0] = 1
        d1[0] = 1
        d5[0] = 1
        d9[0] = 1
        d13[0] = 1
    }

    private fun twostepSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 100
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a10[0] = 1
        a11[0] = 1
        a13[0] = 1
        b15[0] = 1
        c5[0] = 1
        c13[0] = 1
        d1[0] = 1
        d7[0] = 1
        d11[0] = 1
        d16[0] = 1
        e1[0] = 1
        e3[0] = 1
        e5[0] = 1
        e6[0] = 1
        e7[0] = 1
        e9[0] = 1
        e11[0] = 1
        e13[0] = 1
        e14[0] = 1
        e15[0] = 1
    }

    private fun drumnbassSequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 170
        a1[0] = 1
        a3[0] = 1
        a6[0] = 1
        a8[0] = 1
        a15[0] = 1
        b5[0] = 1
        b9[0] = 1
        b11[0] = 1
        b12[0] = 1
        b15[0] = 1
        c7[0] = 1
        c13[0] = 1
        c15[0] = 1
        c16[0] = 1
        d1[0] = 1
        d4[0] = 1
        d5[0] = 1
        d8[0] = 1
        d10[0] = 1
        d11[0] = 1
        d14[0] = 1
    }

    private fun beat1Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 1
        sequencerSize = 0
        sequencerBpm = 120
        a1[0] = 1
        a9[0] = 1
        b5[0] = 1
        b13[0] = 1

        a1[1] = 1
        a7[1] = 1
        a9[1] = 1
        b5[1] = 1
        b13[1] = 1
    }

    private fun beat2Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 1
        sequencerSize = 0
        sequencerBpm = 120
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a8[0] = 1
        a10[0] = 1
        a12[0] = 1
        a13[0] = 1
        b5[0] = 1
        b6[0] = 1
        b8[0] = 1
        b10[0] = 1
        b12[0] = 1
        b13[0] = 1
        b15[0] = 1
        c5[0] = 1
        c8[0] = 1
        c10[0] = 1
        c13[0] = 1

        a1[1] = 1
        a3[1] = 1
        a5[1] = 1
        a8[1] = 1
        a10[1] = 1
        a12[1] = 1
        a13[1] = 1
        b5[1] = 1
        b6[1] = 1
        b8[1] = 1
        b10[1] = 1
        b12[1] = 1
        b13[1] = 1
        b15[1] = 1
        c5[1] = 1
        c6[1] = 1
        c8[1] = 1
        c10[1] = 1
        c12[1] = 1
        c13[1] = 1
    }

    private fun beat3Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 1
        sequencerSize = 0
        sequencerBpm = 100
        a1[0] = 1
        a3[0] = 1
        a7[0] = 1
        a11[0] = 1
        a14[0] = 1
        b5[0] = 1
        b8[0] = 1
        b10[0] = 1
        b13[0] = 1
        d1[0] = 1
        d5[0] = 1
        d9[0] = 1
        d13[0] = 1
        e14[0] = 1

        a1[1] = 1
        a3[1] = 1
        a7[1] = 1
        a11[1] = 1
        a14[1] = 1
        b5[1] = 1
        b8[1] = 1
        b10[1] = 1
        b12[1] = 1
        b13[1] = 1
        b15[1] = 1
        c4[1] = 1
        c9[1] = 1
        c13[1] = 1
        d1[1] = 1
        d5[1] = 1
        d9[1] = 1
        d11[1] = 1
        d13[1] = 1
        e6[1] = 1
        e10[1] = 1
        e14[1] = 1
    }

    private fun beat5Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 110
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a11[0] = 1
        a13[0] = 1
        a15[0] = 1
        b5[0] = 1
        b8[0] = 1
        b10[0] = 1
        b13[0] = 1
        c1[0] = 1
        c11[0] = 1
    }

    private fun beat6Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 100
        a1[0] = 1
        a4[0] = 1
        a11[0] = 1
        a16[0] = 1
        b5[0] = 1
        b10[0] = 1
        b13[0] = 1
        c11[0] = 1
        c13[0] = 1
    }

    private fun beat7Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 90
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a11[0] = 1
        a13[0] = 1
        a15[0] = 1
        b5[0] = 1
        b13[0] = 1
        c1[0] = 1
        c3[0] = 1
        c11[0] = 1
        c12[0] = 1
        c16[0] = 1
    }

    private fun beat8Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 0
        sequencerSize = 0
        sequencerBpm = 100
        a1[0] = 1
        a3[0] = 1
        a5[0] = 1
        a7[0] = 1
        a9[0] = 1
        a11[0] = 1
        a13[0] = 1
        a15[0] = 1
        b5[0] = 1
        b13[0] = 1
        c1[0] = 1
        c3[0] = 1
        c4[0] = 1
        c11[0] = 1
        c16[0] = 1
        d11[0] = 1
    }

    private fun beat9Sequence() {
        sequencerStop()
        resetSequence()
        sequencerMaxSize = 1
        sequencerSize = 0
        sequencerBpm = 120
        a1[0] = 1
        a7[0] = 1
        a11[0] = 1
        b5[0] = 1
        b13[0] = 1

        a1[1] = 1
        a7[1] = 1
        a9[1] = 1
        a11[1] = 1
        b5[1] = 1
        b8[1] = 1
        b10[1] = 1
        b13[1] = 1
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "Range", "CutPasteId", "ShowToast",
        "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .apply { setContentView(this.root) }

        setSupportActionBar(findViewById(R.id.toolbar_main))

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            Log.d("MainActivity", "Current token: $token")
        })

        stickyImmersiveMode()
        initAdMob()
        loadAdMob()
        loadRewardedAd()

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
        mRealm = Realm.getInstance(realmConfig)

        if (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad != null) {
            padText1 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad.toString())
            padText2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad2.toString())
            padText3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad3.toString())
            padText4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad4.toString())
            padText5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad5.toString())
            padText6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad6.toString())
            padText7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad7.toString())
            padText8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad8.toString())
            padText9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad9.toString())
            padText10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad10.toString())
            padText11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad11.toString())
            padText12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad12.toString())
            padText13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad13.toString())
            padText14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad14.toString())
            padText15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad15.toString())
            padCheck = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.check!!)
            colorCheck = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.c_check!!)
            when (padCheck) {
                53 -> {
                    x53()
                }
                43 -> {
                    x43()
                }
                33 -> {
                    x33()
                }
                52 -> {
                    x52()
                }
                42 -> {
                    x42()
                }
                32 -> {
                    x32()
                }
                22 -> {
                    x22()
                }
                21 -> {
                    x21()
                }
                51 -> {
                    x51()
                }
                41 -> {
                    x41()
                }
                31 -> {
                    x31()
                }
            }
        }

        binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView2.textView.text = padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView3.textView.text = padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView5.textView.text = padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView6.textView.text = padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView8.textView.text = padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView9.textView.text = padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView11.textView.text = padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView12.textView.text = padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView14.textView.text = padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView15.textView.text = padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()

        if (colorCheck == 1) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
            } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
            }
        } else {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
            } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
            }
        }

        val orientation = resources.configuration.orientation

        val tuning = arrayOf(
            "Change Pad Sounds",
            "Random Pad Sounds",
            "Change Pad Colors",
            "Save Pad Settings",
            "Load Pad Settings",
            "Adjusting Sounds",
            "Lock Settings",
            "Hide banner Ads",
            "EXIT",
            "5x3","5x2","5x1",
            "4x3","4x2","4x1",
            "3x3","3x2","3x1",
            "2x2","2x1"
        )
        val tuning2 = arrayOf(
            "Change to Play Mode",
            "Random Pad Sounds",
            "Change Pad Colors",
            "Save Pad Settings",
            "Load Pad Settings",
            "Adjusting Sounds",
            "Lock Settings",
            "Hide banner Ads",
            "EXIT",
            "5x3","5x2","5x1",
            "4x3","4x2","4x1",
            "3x3","3x2","3x1",
            "2x2","2x1"
        )
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown, tuning)
        val adapterA = ArrayAdapter(this, R.layout.custom_spinner_dropdown, tuning2)
        val gridView: GridView = findViewById(R.id.grid_view)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { adapterView, _, position, _ ->
            when(adapterView.getItemAtPosition(position)) {
                "Change Pad Sounds" -> {
                    sequencerCount = 0
                    paste = 1
                    binding.toolbarMain.setBackgroundColor(Color.parseColor("#FFA630"))
                    Toast.makeText(applicationContext, R.string.change, Toast.LENGTH_LONG).show()
                    gridView.visibility = View.INVISIBLE
                    gridView.adapter = adapterA
                    adapterA.notifyDataSetChanged()
                }
                "Change to Play Mode" -> {
                    paste = 0
                    binding.toolbarMain.setBackgroundColor(Color.parseColor("#5A5A66"))
                    Toast.makeText(applicationContext, R.string.change2, Toast.LENGTH_LONG).show()
                    gridView.visibility = View.INVISIBLE
                    gridView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                "Change Pad Colors" -> {
                    if (colorCheck == 0) {
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            colorCheck = 1
                        }
                        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                            findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                            findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                            findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                            findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                            colorCheck = 1
                        }
                    } else {
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            colorCheck = 0
                        }
                        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                            colorCheck = 0
                        }
                    }
                    gridView.visibility = View.INVISIBLE
                }
                "Random Pad Sounds" -> {
                    padText1 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText2 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText3 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText4 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText5 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText6 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText7 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText8 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText9 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText10 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText11 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText12 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText13 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText14 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    padText15 = (aSoundList+bSoundList+cSoundList+dSoundList+eSoundList+fSoundList+gSoundList+hSoundList+iSoundList+jSoundList+kSoundList+lSoundList).random().name.replace(".ogg","")
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView2.textView.text = padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView3.textView.text = padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView5.textView.text = padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView6.textView.text = padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView8.textView.text = padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView9.textView.text = padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView11.textView.text = padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView12.textView.text = padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView14.textView.text = padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView15.textView.text = padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound2 = soundPool.load(assets.openFd("$padText2.ogg"), 1)
                    sound3 = soundPool.load(assets.openFd("$padText3.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound5 = soundPool.load(assets.openFd("$padText5.ogg"), 1)
                    sound6 = soundPool.load(assets.openFd("$padText6.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound8 = soundPool.load(assets.openFd("$padText8.ogg"), 1)
                    sound9 = soundPool.load(assets.openFd("$padText9.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound11 = soundPool.load(assets.openFd("$padText11.ogg"), 1)
                    sound12 = soundPool.load(assets.openFd("$padText12.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    sound14 = soundPool.load(assets.openFd("$padText14.ogg"), 1)
                    sound15 = soundPool.load(assets.openFd("$padText15.ogg"), 1)
                    gridView.visibility = View.INVISIBLE
                }
                "Save Pad Settings" -> {
                    if (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad == null) {
                        create()
                        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        val snackBar = Snackbar.make(findViewById(R.id.snack_space) , R.string.Saved, Snackbar.LENGTH_LONG)
                        val snackTextView: TextView = snackBar.view.findViewById(R.id.snackbar_text)
                        snackTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        snackBar.setDuration(2000).show()
                        Handler().postDelayed({
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            val manager = ReviewManagerFactory.create(this)
                            val request = manager.requestReviewFlow()
                            request.addOnCompleteListener { task: Task<ReviewInfo?> ->
                                when {
                                    task.isSuccessful -> {
                                        val reviewInfo = task.result
                                        val flow = manager.launchReviewFlow(this, reviewInfo)
                                        flow.addOnCompleteListener {
                                            stickyImmersiveMode()
                                        }
                                    }
                                    else -> {
                                        stickyImmersiveMode()
                                    }
                                }
                            }
                        }, 2000)
                    } else {
                        update()
                        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        val snackBar = Snackbar.make(findViewById(R.id.snack_space) , R.string.Saved, Snackbar.LENGTH_LONG)
                        val snackTextView: TextView = snackBar.view.findViewById(R.id.snackbar_text)
                        snackTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        snackBar.setDuration(2000).show()
                        Handler().postDelayed({
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }, 2000)
                    }
                    gridView.visibility = View.INVISIBLE
                }
                "Load Pad Settings" -> {
                    read()
                    if (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad != null) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        val snackBar2 = Snackbar.make(findViewById(R.id.snack_space) , R.string.Loaded, Snackbar.LENGTH_LONG)
                        val snackTextView2: TextView = snackBar2.view.findViewById(R.id.snackbar_text)
                        snackTextView2.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        snackBar2.setDuration(2000).show()
                        Handler().postDelayed({
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }, 2000)
                    }
                    gridView.visibility = View.INVISIBLE
                }
                "Adjusting Sounds" -> {
                    binding.view.visibility = View.VISIBLE
                    gridView.visibility = View.INVISIBLE
                }
                "Lock Settings" -> {
                    paste = 0
                    menuSwitchLock = false
                    invalidateOptionsMenu()
                    binding.toolbarMain.setBackgroundColor(Color.parseColor("#5A5A66"))
                    Toast.makeText(applicationContext, R.string.lock, Toast.LENGTH_LONG).show()
                    gridView.visibility = View.INVISIBLE
                    gridView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                "Hide banner Ads" -> {
                    if (adCheck == 0) {
                        AlertDialog.Builder(this)
                            .setOnCancelListener {
                                stickyImmersiveMode()
                            }
                            .setTitle(R.string.menu5a)
                            .setMessage(R.string.menu5b)
                            .setPositiveButton("YES") { _, _ ->
                                showRewardAd()
                            }
                            .setNegativeButton("NO") { _, _ ->
                                stickyImmersiveMode()
                            }
                            .show()
                    } else if (adCheck == 1){
                        AlertDialog.Builder(this)
                            .setOnCancelListener {
                                stickyImmersiveMode()
                            }
                            .setTitle(R.string.menu5c)
                            .setPositiveButton("OK") { _, _ ->
                                stickyImmersiveMode()
                            }
                            .show()
                    }
                }
                "EXIT" -> {
                    AlertDialog.Builder(this)
                        .setOnCancelListener {
                            stickyImmersiveMode()
                        }
                        .setTitle(R.string.menu6)
                        .setPositiveButton("YES") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("NO") { _, _ ->
                            stickyImmersiveMode()
                        }
                        .show()
                }
                "5x3" -> {
                    x53()
                }
                "4x3" -> {
                    x43()
                }
                "3x3" -> {
                    x33()
                }
                "5x2" -> {
                    x52()
                }
                "4x2" -> {
                    x42()
                }
                "3x2" -> {
                    x32()
                }
                "2x2" -> {
                    x22()
                }
                "2x1" -> {
                    x21()
                }
                "5x1" -> {
                    x51()
                }
                "4x1" -> {
                    x41()
                }
                "3x1" -> {
                    x31()
                }
            }
        }

        val choose = arrayOf(
            "HIPHOP 1 BPM80",
            "REGGAETON 1 BPM90",
            "ELECTRONICA 1 BPM90",
            "DUBSTEP 1 BPM140",
            "HOUSE 1 BPM130",
            "DISCO 1 BPM110",
            "TECHNO 1 BPM110",
            "EUROBEAT 1 BPM130",
            "2 STEP 1 BPM100",
            "DRUM'N'BASS 1 BPM170",
            "BEAT 1 BPM120",
            "BEAT 2 BPM120",
            "BEAT 3 BPM110",
            "BEAT 4 BPM100",
            "BEAT 5 BPM90",
            "BEAT 6 BPM100",
            "BEAT 7 BPM120",
            "BEAT 8 BPM100"
            )
        val adapter2 = ArrayAdapter(this, R.layout.custom_spinner_dropdown, choose)
        val gridView2: GridView = findViewById(R.id.grid_view_choose)
        val soundListView = findViewById<ListView>(R.id.list_view)
        gridView2.adapter = adapter2

        gridView2.setOnItemClickListener { adapterView, _, position, _ ->
            when (adapterView.getItemAtPosition(position)) {
                "HIPHOP 1 BPM80" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "tr_8_clsd_hi_hat_02"
                    padText4 = "snare_drum_11"
                    padText7 = "tr_909_bass_drum_02"
                    actionTitle = "hiphop_1_bpm80"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    hiphopSequence()
                    changeSequence()
                    beatCheck = "hiphop_1_bpm80"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "HIPHOP 1 BPM80"
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "REGGAETON 1 BPM90" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "bass_drum_short_08"
                    padText4 = "snare_drum_05"
                    padText7 = "clap_08"
                    actionTitle = "reggaeton_1_bpm90"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    reggaetonSequence()
                    changeSequence()
                    beatCheck = "reggaeton_1_bpm90"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "REGGAETON 1 BPM90"
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "ELECTRONICA 1 BPM90" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_01"
                    padText4 = "snare_drum_14"
                    padText7 = "bass_drum_short_01"
                    padText10 = "clap_01"
                    actionTitle = "electronica_1_bpm90"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    electronicaSequence()
                    changeSequence()
                    beatCheck = "electronica_1_bpm90"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "ELECTRONICA 1 BPM90"
                    x41()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "DUBSTEP 1 BPM140" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_03"
                    padText4 = "tr_8_snare_drum_03"
                    padText7 = "tr_909_bass_drum_01"
                    padText10 = "clap_05"
                    actionTitle = "dubstep_1_bpm140"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    dubstepSequence()
                    changeSequence()
                    beatCheck = "dubstep_1_bpm140"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "DUBSTEP 1 BPM140"
                    x41()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "HOUSE 1 BPM130" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "tr_909_clsd_hi_hat_02"
                    padText4 = "tr_909_open_hi_hat_01"
                    padText7 = "snare_drum_14"
                    padText10 = "bass_drum_short_08"
                    padText13 = "clap_01"
                    actionTitle = "house_1_bpm130"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    beatCheck = "house_1_bpm130"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "HOUSE 1 BPM130"
                    houseSequence()
                    changeSequence()
                    x51()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.VISIBLE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "DISCO 1 BPM110" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_03"
                    padText4 = "open_hi_hat_11"
                    padText7 = "snare_drum_11"
                    padText10 = "bass_drum_long_08"
                    padText13 = "clap_08"
                    actionTitle = "disco_1_bpm110"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    discoSequence()
                    changeSequence()
                    beatCheck = "disco_1_bpm110"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "DISCO 1 BPM110"
                    x51()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.VISIBLE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "TECHNO 1 BPM110" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_03"
                    padText4 = "open_hi_hat_04"
                    padText7 = "tr_8_snare_drum_03"
                    padText10 = "tr_909_bass_drum_01"
                    padText13 = "clap_01"
                    actionTitle = "techno_1_bpm110"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    technoSequence()
                    changeSequence()
                    beatCheck = "techno_1_bpm110"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "TECHNO 1 BPM110"
                    x51()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.VISIBLE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "EUROBEAT 1 BPM130" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_09"
                    padText4 = "maracas_03"
                    padText7 = "snare_drum_11"
                    padText10 = "bass_drum_long_08"
                    actionTitle = "eurobeat_1_bpm130"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    eurobeatSequence()
                    changeSequence()
                    beatCheck = "eurobeat_1_bpm130"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "EUROBEAT 1 BPM130"
                    x41()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "2 STEP 1 BPM100" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "tr_909_clsd_hi_hat_02"
                    padText4 = "tr_909_open_hi_hat_01"
                    padText7 = "snare_drum_02"
                    padText10 = "tr_909_bass_drum_02"
                    padText13 = "tr_909_clap"
                    actionTitle = "two_step_1_bpm100"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    twostepSequence()
                    changeSequence()
                    beatCheck = "two_step_1_bpm100"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "2 STEP 1 BPM100"
                    x51()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.VISIBLE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "DRUM'N'BASS 1 BPM170" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_01"
                    padText4 = "tr_909_open_hi_hat_01"
                    padText7 = "tr_8_snare_drum_04"
                    padText10 = "bass_drum_short_08"
                    actionTitle = "drum_n_bass_1_bpm170"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    drumnbassSequence()
                    changeSequence()
                    beatCheck = "drum_n_bass_1_bpm170"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = "DRUM'N'BASS 1 BPM170"
                    x41()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 1 BPM120" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "bass_drum_short_01"
                    padText4 = "snare_drum_01"
                    actionTitle = "beat_1_bpm120"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    beat1Sequence()
                    changeSequence()
                    beatCheck = "beat_1"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x21()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 7 BPM120" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "bass_drum_short_08"
                    padText4 = "low_tom_06"
                    padText7 = "maracas_03"
                    actionTitle = "beat_7_bpm120"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    beat2Sequence()
                    changeSequence()
                    beatCheck = "beat_2"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 8 BPM100" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clap_01"
                    padText4 = "mid_tom_01"
                    padText7 = "claves_05"
                    padText10 = "clsd_hi_hat_09"
                    padText13 = "high_conga_08"
                    actionTitle = "beat_8_bpm100"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
                    beat3Sequence()
                    changeSequence()
                    beatCheck = "beat_3"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x51()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.VISIBLE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 3 BPM110" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_01"
                    padText4 = "snare_drum_05"
                    padText7 = "tr_909_bass_drum_02"
                    actionTitle = "beat_3_bpm110"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    beat5Sequence()
                    changeSequence()
                    beatCheck = "beat_5"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 4 BPM100" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "tr_909_bass_drum_02"
                    padText4 = "tr_8_snare_drum_03"
                    padText7 = "open_hi_hat_08"
                    actionTitle = "beat_4_bpm100"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    beat6Sequence()
                    changeSequence()
                    beatCheck = "beat_6"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 5 BPM90" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "clsd_hi_hat_03"
                    padText4 = "snare_drum_02"
                    padText7 = "bass_drum_short_01"
                    actionTitle = "beat_5_bpm90"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    beat7Sequence()
                    changeSequence()
                    beatCheck = "beat_7"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x31()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 6 BPM100" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "tr_909_clsd_hi_hat_01"
                    padText4 = "snare_drum_15"
                    padText7 = "tr_909_bass_drum_03"
                    padText10 = "tr_8_open_hi_hat_03"
                    actionTitle = "beat_6_bpm100"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
                    sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
                    beat8Sequence()
                    changeSequence()
                    beatCheck = "beat_8"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x41()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.VISIBLE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
                "BEAT 2 BPM120" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    padText1 = "bass_drum_short_01"
                    padText4 = "snare_drum_01"
                    actionTitle = "beat_2_bpm120"
                    binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
                    sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
                    beat9Sequence()
                    changeSequence()
                    beatCheck = "beat_9"
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    supportActionBar?.title = actionTitle.replace("_"," ").uppercase()
                    x21()
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        findViewById<View>(R.id.sequencer_list3).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list4).visibility = View.GONE
                        findViewById<View>(R.id.sequencer_list5).visibility = View.GONE
                        binding.sequencerView.visibility = View.VISIBLE
                        binding.notes.visibility = View.VISIBLE
                    }
                    gridView2.visibility = View.INVISIBLE
                }
            }
        }


        aSoundList = arrayListOf(
                SoundList("bass_drum_long_01.ogg"),
                SoundList("bass_drum_long_02.ogg"),
                SoundList("bass_drum_long_03.ogg"),
                SoundList("bass_drum_long_04.ogg"),
                SoundList("bass_drum_long_05.ogg"),
                SoundList("bass_drum_long_06.ogg"),
                SoundList("bass_drum_long_07.ogg"),
                SoundList("bass_drum_long_08.ogg"),
                SoundList("bass_drum_long_09.ogg"),
                SoundList("bass_drum_long_10.ogg"),
                SoundList("bass_drum_long_11.ogg"),
                SoundList("bass_drum_long_12.ogg"),
                SoundList("bass_drum_long_13.ogg"),
                SoundList("bass_drum_long_14.ogg"),
                SoundList("bass_drum_long_15.ogg"),
                SoundList("bass_drum_long_16.ogg"),
                SoundList("bass_drum_long_17.ogg"),
                SoundList("bass_drum_short_01.ogg"),
                SoundList("bass_drum_short_02.ogg"),
                SoundList("bass_drum_short_03.ogg"),
                SoundList("bass_drum_short_04.ogg"),
                SoundList("bass_drum_short_05.ogg"),
                SoundList("bass_drum_short_06.ogg"),
                SoundList("bass_drum_short_07.ogg"),
                SoundList("bass_drum_short_08.ogg"),
                SoundList("bass_drum_short_09.ogg"),
                SoundList("bass_drum_short_10.ogg"),
                SoundList("bass_drum_short_11.ogg"),
                SoundList("bass_drum_short_12.ogg"),
            SoundList("tr_909_bass_drum_01.ogg"),
            SoundList("tr_909_bass_drum_02.ogg"),
            SoundList("tr_909_bass_drum_03.ogg"),
            SoundList("tr_8_bass_drum_01.ogg"),
            SoundList("tr_8_bass_drum_02.ogg"),
            SoundList("tr_8_bass_drum_03.ogg"),
            SoundList("tr_8_bass_drum_04.ogg")
                )

        bSoundList = arrayListOf(
                SoundList("clap_01.ogg"),
                SoundList("clap_02.ogg"),
                SoundList("clap_03.ogg"),
                SoundList("clap_04.ogg"),
                SoundList("clap_05.ogg"),
                SoundList("clap_06.ogg"),
                SoundList("clap_07.ogg"),
                SoundList("clap_08.ogg"),
                SoundList("clap_09.ogg"),
                SoundList("clap_10.ogg"),
                SoundList("clap_11.ogg"),
                SoundList("clap_12.ogg"),
                SoundList("clap_13.ogg"),
                SoundList("clap_14.ogg"),
                SoundList("clap_15.ogg"),
            SoundList("tr_909_clap.ogg"),
            SoundList("tr_8_clap_01.ogg"),
            SoundList("tr_8_clap_02.ogg"),
            SoundList("tr_8_clap_03.ogg")
        )
        cSoundList = arrayListOf(
                SoundList("claves_01.ogg"),
                SoundList("claves_02.ogg"),
                SoundList("claves_03.ogg"),
                SoundList("claves_04.ogg"),
                SoundList("claves_05.ogg"),
                SoundList("claves_06.ogg"),
                SoundList("claves_07.ogg"),
                SoundList("claves_08.ogg"),
                SoundList("claves_09.ogg"),
                SoundList("claves_10.ogg"),
                SoundList("claves_11.ogg")
        )
        dSoundList = arrayListOf(
                SoundList("clsd_hi_hat_01.ogg"),
                SoundList("clsd_hi_hat_02.ogg"),
                SoundList("clsd_hi_hat_03.ogg"),
                SoundList("clsd_hi_hat_04.ogg"),
                SoundList("clsd_hi_hat_05.ogg"),
                SoundList("clsd_hi_hat_06.ogg"),
                SoundList("clsd_hi_hat_07.ogg"),
                SoundList("clsd_hi_hat_08.ogg"),
                SoundList("clsd_hi_hat_09.ogg"),
                SoundList("clsd_hi_hat_10.ogg"),
                SoundList("clsd_hi_hat_11.ogg"),
                SoundList("clsd_hi_hat_12.ogg"),
            SoundList("tr_909_clsd_hi_hat_01.ogg"),
            SoundList("tr_909_clsd_hi_hat_02.ogg"),
            SoundList("tr_8_clsd_hi_hat_01.ogg"),
            SoundList("tr_8_clsd_hi_hat_02.ogg"),
            SoundList("tr_8_clsd_hi_hat_03.ogg")
        )
        eSoundList = arrayListOf(
                SoundList("high_conga_01.ogg"),
                SoundList("high_conga_02.ogg"),
                SoundList("high_conga_03.ogg"),
                SoundList("high_conga_04.ogg"),
                SoundList("high_conga_05.ogg"),
                SoundList("high_conga_06.ogg"),
                SoundList("high_conga_07.ogg"),
                SoundList("high_conga_08.ogg"),
                SoundList("high_conga_09.ogg"),
                SoundList("high_conga_10.ogg"),
                SoundList("mid_conga_01.ogg"),
                SoundList("mid_conga_02.ogg"),
                SoundList("mid_conga_03.ogg"),
                SoundList("mid_conga_04.ogg"),
                SoundList("mid_conga_05.ogg"),
                SoundList("mid_conga_06.ogg"),
                SoundList("mid_conga_07.ogg"),
                SoundList("mid_conga_08.ogg"),
                SoundList("mid_conga_09.ogg"),
                SoundList("mid_conga_10.ogg"),
                SoundList("low_conga_01.ogg"),
                SoundList("low_conga_02.ogg"),
                SoundList("low_conga_03.ogg"),
                SoundList("low_conga_04.ogg"),
                SoundList("low_conga_05.ogg"),
                SoundList("low_conga_06.ogg"),
                SoundList("low_conga_07.ogg"),
                SoundList("low_conga_08.ogg"),
                SoundList("low_conga_09.ogg"),
                SoundList("low_conga_10.ogg")
        )
        fSoundList = arrayListOf(
                SoundList("cowbell_01a.ogg"),
                SoundList("cowbell_01b.ogg"),
                SoundList("cowbell_02.ogg"),
                SoundList("cowbell_03.ogg"),
                SoundList("cowbell_04.ogg"),
                SoundList("cowbell_05.ogg"),
                SoundList("cowbell_06.ogg"),
                SoundList("cowbell_07.ogg"),
                SoundList("cowbell_08.ogg"),
                SoundList("cowbell_09.ogg"),
                SoundList("cowbell_10.ogg"),
                SoundList("cowbell_11.ogg"),
                SoundList("cowbell_12.ogg"),
                SoundList("cowbell_13.ogg"),
                SoundList("cowbell_14.ogg"),
                SoundList("cowbell_15.ogg"),
            SoundList("tr_8_cowbell.ogg")
        )
        gSoundList = arrayListOf(
                SoundList("cymbal_01.ogg"),
                SoundList("cymbal_02.ogg"),
                SoundList("cymbal_03.ogg"),
                SoundList("cymbal_04.ogg"),
                SoundList("cymbal_05.ogg"),
                SoundList("cymbal_06.ogg"),
                SoundList("cymbal_07.ogg"),
                SoundList("cymbal_08.ogg"),
                SoundList("cymbal_09.ogg"),
                SoundList("cymbal_10.ogg"),
                SoundList("cymbal_11.ogg"),
                SoundList("cymbal_12.ogg"),
                SoundList("cymbal_13.ogg"),
                SoundList("cymbal_14.ogg"),
                SoundList("cymbal_15.ogg"),
                SoundList("cymbal_16.ogg"),
            SoundList("tr_909_cymbal_01.ogg"),
            SoundList("tr_909_cymbal_02.ogg"),
            SoundList("tr_909_cymbal_03.ogg"),
            SoundList("tr_909_cymbal_04.ogg"),
            SoundList("tr_8_cymbal_01.ogg"),
            SoundList("tr_8_cymbal_02.ogg"),
            SoundList("tr_8_cymbal_03.ogg"),
            SoundList("tr_8_cymbal_04.ogg")
        )
        hSoundList = arrayListOf(
                SoundList("maracas_01.ogg"),
                SoundList("maracas_02.ogg"),
                SoundList("maracas_03.ogg"),
                SoundList("maracas_04.ogg"),
                SoundList("maracas_05.ogg"),
                SoundList("maracas_06.ogg"),
                SoundList("maracas_07.ogg"),
                SoundList("maracas_08.ogg"),
                SoundList("maracas_09.ogg"),
                SoundList("maracas_10.ogg"),
                SoundList("maracas_11.ogg")
        )
        iSoundList = arrayListOf(
                SoundList("open_hi_hat_01.ogg"),
                SoundList("open_hi_hat_02.ogg"),
                SoundList("open_hi_hat_03.ogg"),
                SoundList("open_hi_hat_04.ogg"),
                SoundList("open_hi_hat_05.ogg"),
                SoundList("open_hi_hat_06.ogg"),
                SoundList("open_hi_hat_07.ogg"),
                SoundList("open_hi_hat_08.ogg"),
                SoundList("open_hi_hat_09.ogg"),
                SoundList("open_hi_hat_10.ogg"),
                SoundList("open_hi_hat_11.ogg"),
                SoundList("open_hi_hat_12.ogg"),
                SoundList("open_hi_hat_13.ogg"),
            SoundList("tr_909_open_hi_hat_01.ogg"),
            SoundList("tr_909_open_hi_hat_02.ogg"),
            SoundList("tr_8_open_hi_hat_01.ogg"),
            SoundList("tr_8_open_hi_hat_02.ogg"),
            SoundList("tr_8_open_hi_hat_03.ogg")
        )
        jSoundList = arrayListOf(
                SoundList("rimshot_01.ogg"),
                SoundList("rimshot_02.ogg"),
                SoundList("rimshot_03.ogg"),
                SoundList("rimshot_04.ogg"),
                SoundList("rimshot_05.ogg"),
                SoundList("rimshot_06.ogg"),
                SoundList("rimshot_07.ogg"),
                SoundList("rimshot_08.ogg"),
                SoundList("rimshot_09.ogg"),
                SoundList("rimshot_10.ogg"),
                SoundList("rimshot_11.ogg"),
            SoundList("tr_909_rimshot.ogg"),
            SoundList("tr_8_rimshot_01.ogg"),
            SoundList("tr_8_rimshot_02.ogg"),
            SoundList("tr_8_rimshot_03.ogg")
        )
        kSoundList = arrayListOf(
                SoundList("snare_drum_01.ogg"),
                SoundList("snare_drum_02.ogg"),
                SoundList("snare_drum_03.ogg"),
                SoundList("snare_drum_04.ogg"),
                SoundList("snare_drum_05.ogg"),
                SoundList("snare_drum_06.ogg"),
                SoundList("snare_drum_07.ogg"),
                SoundList("snare_drum_08.ogg"),
                SoundList("snare_drum_09.ogg"),
                SoundList("snare_drum_10.ogg"),
                SoundList("snare_drum_11.ogg"),
                SoundList("snare_drum_12.ogg"),
                SoundList("snare_drum_13.ogg"),
                SoundList("snare_drum_14.ogg"),
                SoundList("snare_drum_15.ogg"),
                SoundList("snare_drum_16.ogg"),
                SoundList("snare_drum_17.ogg"),
                SoundList("snare_drum_18.ogg"),
                SoundList("snare_drum_19.ogg"),
                SoundList("snare_drum_20.ogg"),
            SoundList("tr_909_snare_drum_01.ogg"),
            SoundList("tr_909_snare_drum_02.ogg"),
            SoundList("tr_909_snare_drum_03.ogg"),
            SoundList("tr_8_snare_drum_01.ogg"),
            SoundList("tr_8_snare_drum_02.ogg"),
            SoundList("tr_8_snare_drum_03.ogg"),
            SoundList("tr_8_snare_drum_04.ogg")
        )
        lSoundList = arrayListOf(
                SoundList("low_tom_01.ogg"),
                SoundList("low_tom_02.ogg"),
                SoundList("low_tom_03.ogg"),
                SoundList("low_tom_04.ogg"),
                SoundList("low_tom_05.ogg"),
                SoundList("low_tom_06.ogg"),
                SoundList("low_tom_07.ogg"),
                SoundList("low_tom_08.ogg"),
                SoundList("low_tom_09.ogg"),
                SoundList("low_tom_10.ogg"),
                SoundList("low_tom_11.ogg"),
                SoundList("low_tom_12.ogg"),
                SoundList("mid_tom_01.ogg"),
                SoundList("mid_tom_02.ogg"),
                SoundList("mid_tom_03.ogg"),
                SoundList("mid_tom_04.ogg"),
                SoundList("mid_tom_05.ogg"),
                SoundList("mid_tom_06.ogg"),
                SoundList("mid_tom_07.ogg"),
                SoundList("mid_tom_08.ogg"),
                SoundList("mid_tom_09.ogg"),
                SoundList("mid_tom_10.ogg"),
                SoundList("mid_tom_11.ogg"),
                SoundList("mid_tom_12.ogg"),
                SoundList("high_tom_01.ogg"),
                SoundList("high_tom_02.ogg"),
                SoundList("high_tom_03.ogg"),
                SoundList("high_tom_04.ogg"),
                SoundList("high_tom_05.ogg"),
                SoundList("high_tom_06.ogg"),
                SoundList("high_tom_07.ogg"),
                SoundList("high_tom_08.ogg"),
                SoundList("high_tom_09.ogg"),
                SoundList("high_tom_10.ogg"),
                SoundList("high_tom_11.ogg"),
                SoundList("high_tom_12.ogg"),
            SoundList("tr_909_tom_01.ogg"),
            SoundList("tr_909_tom_02.ogg"),
            SoundList("tr_909_tom_03.ogg"),
            SoundList("tr_8_tom_01.ogg"),
            SoundList("tr_8_tom_02.ogg"),
            SoundList("tr_8_tom_03.ogg"),
            SoundList("tr_8_tom_04.ogg"),
            SoundList("tr_8_tom_05.ogg")
        )
        nSoundList = arrayListOf(
            SoundList("bass_drum_short_11.ogg"),
            SoundList("tr_909_clap.ogg"),
            SoundList("claves_04.ogg"),
            SoundList("clsd_hi_hat_01.ogg"),
            SoundList("high_conga_01.ogg"),
            SoundList("cowbell_01b.ogg"),
            SoundList("tr_8_cymbal_01.ogg"),
            SoundList("maracas_02.ogg"),
            SoundList("tr_909_open_hi_hat_01.ogg"),
            SoundList("rimshot_01.ogg"),
            SoundList("tr_8_snare_drum_01.ogg"),
            SoundList("tr_909_tom_01.ogg")
        )
        oSoundList = arrayListOf(

        )
        pSoundList = arrayListOf(

        )
        qSoundList = arrayListOf(

        )
        rSoundList = arrayListOf(

        )
        sSoundList = arrayListOf()
        tSoundList = arrayListOf()

        aCustomAdapter = CustomAdapter(this, aSoundList, this)
        bCustomAdapter = CustomAdapter(this, bSoundList, this)
        cCustomAdapter = CustomAdapter(this, cSoundList, this)
        dCustomAdapter = CustomAdapter(this, dSoundList, this)
        eCustomAdapter = CustomAdapter(this, eSoundList, this)
        fCustomAdapter = CustomAdapter(this, fSoundList, this)
        gCustomAdapter = CustomAdapter(this, gSoundList, this)
        hCustomAdapter = CustomAdapter(this, hSoundList, this)
        iCustomAdapter = CustomAdapter(this, iSoundList, this)
        jCustomAdapter = CustomAdapter(this, jSoundList, this)
        kCustomAdapter = CustomAdapter(this, kSoundList, this)
        lCustomAdapter = CustomAdapter(this, lSoundList, this)
        nCustomAdapter = CustomAdapter(this, nSoundList, this)
        oCustomAdapter = CustomAdapter(this, oSoundList, this)
        pCustomAdapter = CustomAdapter(this, pSoundList, this)
        qCustomAdapter = CustomAdapter(this, qSoundList, this)
        rCustomAdapter = CustomAdapter(this, rSoundList, this)
        sCustomAdapter = CustomAdapter(this, sSoundList, this)
        tCustomAdapter = CustomAdapter(this, tSoundList, this)

        soundListView.adapter = aCustomAdapter

        mp = MediaPlayer()

        supportActionBar?.title = actionTitle.replace("_", " ").uppercase()


            val audioUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            val cursor = contentResolver.query(audioUri, null, null, null, null)
            cursor!!.moveToFirst()
            val path: Array<String?> = arrayOfNulls(cursor.count)
            for (i in path.indices) {
                path[i] = cursor.getString(cursor.getColumnIndex("_data"))
                sSoundList.add(SoundList(path[i].toString()))
                cursor.moveToNext()
            }

            cursor.close()


        val meSpinner = findViewById<Spinner>(R.id.menu_spinner)

        val adapter3 = ArrayAdapter.createFromResource(this, R.array.spinnerItems, android.R.layout.simple_spinner_item)

        adapter3.setDropDownViewResource(R.layout.custom_spinner_dropdown)



        meSpinner.adapter = adapter3


        meSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long,
            ) {
                if (!meSpinner.isFocusable) {
                    meSpinner.isFocusable = true
                    return
                }

                when(position){
                    0 -> {
                        buttonB = 2
                        soundListView.adapter = aCustomAdapter
                        aCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    1 -> {
                        buttonB = 2
                        soundListView.adapter = bCustomAdapter
                        bCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    2 -> {
                        buttonB = 2
                        soundListView.adapter = cCustomAdapter
                        cCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    3 -> {
                        buttonB = 2
                        soundListView.adapter = dCustomAdapter
                        dCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    4 -> {
                        buttonB = 2
                        soundListView.adapter = eCustomAdapter
                        eCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    5 -> {
                        buttonB = 2
                        soundListView.adapter = fCustomAdapter
                        fCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    6 -> {
                        buttonB = 2
                        soundListView.adapter = gCustomAdapter
                        gCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    7 -> {
                        buttonB = 2
                        soundListView.adapter = hCustomAdapter
                        hCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    8 -> {
                        buttonB = 2
                        soundListView.adapter = iCustomAdapter
                        iCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    9 -> {
                        buttonB = 2
                        soundListView.adapter = jCustomAdapter
                        jCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    10 -> {
                        buttonB = 2
                        soundListView.adapter = kCustomAdapter
                        kCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    11 -> {
                        buttonB = 2
                        soundListView.adapter = lCustomAdapter
                        lCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    12 -> {
                        buttonB = 1
                        soundListView.adapter = sCustomAdapter
                        sCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    13 -> {
                        selectEX()
                        buttonB = 1
                        soundListView.adapter = tCustomAdapter
                        tCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        meSpinner.isFocusable = false


        val audioAttributes = AudioAttributes.Builder()

                .setUsage(AudioAttributes.USAGE_GAME)

                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

        soundPool = SoundPool.Builder()

                .setAudioAttributes(audioAttributes)

                .setMaxStreams(20)
                .build()

        try {
            sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound1 = soundPool.load(padText1, 1)
                binding.includeMainView.textView.text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound1 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView.textView.text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound2 = soundPool.load(assets.openFd("$padText2.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound2 = soundPool.load(padText2, 1)
                binding.includeMainView2.textView.text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound2 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView2.textView.text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound3 = soundPool.load(assets.openFd("$padText3.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound3 = soundPool.load(padText3, 1)
                binding.includeMainView3.textView.text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound3 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView3.textView.text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound4 = soundPool.load(padText4, 1)
                binding.includeMainView4.textView.text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound4 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView4.textView.text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound5 = soundPool.load(assets.openFd("$padText5.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound5 = soundPool.load(padText5, 1)
                binding.includeMainView5.textView.text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound5 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView5.textView.text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound6 = soundPool.load(assets.openFd("$padText6.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound6 = soundPool.load(padText6, 1)
                binding.includeMainView6.textView.text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound6 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView6.textView.text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound7 = soundPool.load(padText7, 1)
                binding.includeMainView7.textView.text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound7 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView7.textView.text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound8 = soundPool.load(assets.openFd("$padText8.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound8 = soundPool.load(padText8, 1)
                binding.includeMainView8.textView.text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound8 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView8.textView.text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound9 = soundPool.load(assets.openFd("$padText9.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound9 = soundPool.load(padText9, 1)
                binding.includeMainView9.textView.text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound9 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView9.textView.text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound10 = soundPool.load(padText10, 1)
                binding.includeMainView10.textView.text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound10 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView10.textView.text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound11 = soundPool.load(assets.openFd("$padText11.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound11 = soundPool.load(padText11, 1)
                binding.includeMainView11.textView.text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound11 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView11.textView.text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound12 = soundPool.load(assets.openFd("$padText12.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound12 = soundPool.load(padText12, 1)
                binding.includeMainView12.textView.text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound12 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView12.textView.text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound13 = soundPool.load(padText13, 1)
                binding.includeMainView13.textView.text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound13 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView13.textView.text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound14 = soundPool.load(assets.openFd("$padText14.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound14 = soundPool.load(padText14, 1)
                binding.includeMainView14.textView.text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound14 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView14.textView.text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound15 = soundPool.load(assets.openFd("$padText15.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound15 = soundPool.load(padText15, 1)
                binding.includeMainView15.textView.text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound15 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView15.textView.text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        sound17 = soundPool.load(assets.openFd("soundless.ogg"), 1)

        beat5Sequence()

        lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://$packageName/raw/bpm120_bass_drum"))

        lmp.stop()

        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence).setOnTouchListener { _, _ ->

            if (a1[sequencerSize] == 0) {
                a1[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a1[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence2).setOnTouchListener { _, _ ->

            if (a2[sequencerSize] == 0) {
                a2[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a2[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence3).setOnTouchListener { _, _ ->

            if (a3[sequencerSize] == 0) {
                a3[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a3[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence4).setOnTouchListener { _, _ ->

            if (a4[sequencerSize] == 0) {
                a4[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a4[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence5).setOnTouchListener { _, _ ->

            if (a5[sequencerSize] == 0) {
                a5[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a5[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence6).setOnTouchListener { _, _ ->

            if (a6[sequencerSize] == 0) {
                a6[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a6[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence7).setOnTouchListener { _, _ ->

            if (a7[sequencerSize] == 0) {
                a7[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a7[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence8).setOnTouchListener { _, _ ->

            if (a8[sequencerSize] == 0) {
                a8[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a8[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence9).setOnTouchListener { _, _ ->

            if (a9[sequencerSize] == 0) {
                a9[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a9[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence10).setOnTouchListener { _, _ ->

            if (a10[sequencerSize] == 0) {
                a10[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a10[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence11).setOnTouchListener { _, _ ->

            if (a11[sequencerSize] == 0) {
                a11[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a11[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence12).setOnTouchListener { _, _ ->

            if (a12[sequencerSize] == 0) {
                a12[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a12[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence13).setOnTouchListener { _, _ ->

            if (a13[sequencerSize] == 0) {
                a13[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a13[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence14).setOnTouchListener { _, _ ->

            if (a14[sequencerSize] == 0) {
                a14[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a14[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence15).setOnTouchListener { _, _ ->

            if (a15[sequencerSize] == 0) {
                a15[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a15[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence16).setOnTouchListener { _, _ ->

            if (a16[sequencerSize] == 0) {
                a16[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#d03933"))
            } else {
                a16[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }

        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence).setOnTouchListener { _, _ ->

            if (b1[sequencerSize] == 0) {
                b1[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b1[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence2).setOnTouchListener { _, _ ->

            if (b2[sequencerSize] == 0) {
                b2[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b2[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence3).setOnTouchListener { _, _ ->

            if (b3[sequencerSize] == 0) {
                b3[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b3[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence4).setOnTouchListener { _, _ ->

            if (b4[sequencerSize] == 0) {
                b4[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b4[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence5).setOnTouchListener { _, _ ->

            if (b5[sequencerSize] == 0) {
                b5[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b5[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence6).setOnTouchListener { _, _ ->

            if (b6[sequencerSize] == 0) {
                b6[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b6[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence7).setOnTouchListener { _, _ ->

            if (b7[sequencerSize] == 0) {
                b7[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b7[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence8).setOnTouchListener { _, _ ->

            if (b8[sequencerSize] == 0) {
                b8[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b8[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence9).setOnTouchListener { _, _ ->

            if (b9[sequencerSize] == 0) {
                b9[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b9[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence10).setOnTouchListener { _, _ ->

            if (b10[sequencerSize] == 0) {
                b10[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b10[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence11).setOnTouchListener { _, _ ->

            if (b11[sequencerSize] == 0) {
                b11[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b11[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence12).setOnTouchListener { _, _ ->

            if (b12[sequencerSize] == 0) {
                b12[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b12[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence13).setOnTouchListener { _, _ ->

            if (b13[sequencerSize] == 0) {
                b13[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b13[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence14).setOnTouchListener { _, _ ->

            if (b14[sequencerSize] == 0) {
                b14[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b14[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence15).setOnTouchListener { _, _ ->

            if (b15[sequencerSize] == 0) {
                b15[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b15[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence16).setOnTouchListener { _, _ ->

            if (b16[sequencerSize] == 0) {
                b16[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#e98e2f"))
            } else {
                b16[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list2).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }

        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence).setOnTouchListener { _, _ ->

            if (c1[sequencerSize] == 0) {
                c1[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c1[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence2).setOnTouchListener { _, _ ->

            if (c2[sequencerSize] == 0) {
                c2[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c2[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence3).setOnTouchListener { _, _ ->

            if (c3[sequencerSize] == 0) {
                c3[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c3[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence4).setOnTouchListener { _, _ ->

            if (c4[sequencerSize] == 0) {
                c4[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c4[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence5).setOnTouchListener { _, _ ->

            if (c5[sequencerSize] == 0) {
                c5[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c5[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence6).setOnTouchListener { _, _ ->

            if (c6[sequencerSize] == 0) {
                c6[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c6[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence7).setOnTouchListener { _, _ ->

            if (c7[sequencerSize] == 0) {
                c7[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c7[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence8).setOnTouchListener { _, _ ->

            if (c8[sequencerSize] == 0) {
                c8[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c8[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence9).setOnTouchListener { _, _ ->

            if (c9[sequencerSize] == 0) {
                c9[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c9[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence10).setOnTouchListener { _, _ ->

            if (c10[sequencerSize] == 0) {
                c10[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c10[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence11).setOnTouchListener { _, _ ->

            if (c11[sequencerSize] == 0) {
                c11[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c11[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence12).setOnTouchListener { _, _ ->

            if (c12[sequencerSize] == 0) {
                c12[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c12[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence13).setOnTouchListener { _, _ ->

            if (c13[sequencerSize] == 0) {
                c13[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c13[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence14).setOnTouchListener { _, _ ->

            if (c14[sequencerSize] == 0) {
                c14[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c14[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence15).setOnTouchListener { _, _ ->

            if (c15[sequencerSize] == 0) {
                c15[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c15[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence16).setOnTouchListener { _, _ ->

            if (c16[sequencerSize] == 0) {
                c16[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#dfd441"))
            } else {
                c16[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list3).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }

        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence).setOnTouchListener { _, _ ->

            if (d1[sequencerSize] == 0) {
                d1[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d1[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence2).setOnTouchListener { _, _ ->

            if (d2[sequencerSize] == 0) {
                d2[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d2[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence3).setOnTouchListener { _, _ ->

            if (d3[sequencerSize] == 0) {
                d3[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d3[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence4).setOnTouchListener { _, _ ->

            if (d4[sequencerSize] == 0) {
                d4[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d4[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence5).setOnTouchListener { _, _ ->

            if (d5[sequencerSize] == 0) {
                d5[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d5[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence6).setOnTouchListener { _, _ ->

            if (d6[sequencerSize] == 0) {
                d6[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d6[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence7).setOnTouchListener { _, _ ->

            if (d7[sequencerSize] == 0) {
                d7[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d7[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence8).setOnTouchListener { _, _ ->

            if (d8[sequencerSize] == 0) {
                d8[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d8[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence9).setOnTouchListener { _, _ ->

            if (d9[sequencerSize] == 0) {
                d9[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d9[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence10).setOnTouchListener { _, _ ->

            if (d10[sequencerSize] == 0) {
                d10[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d10[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence11).setOnTouchListener { _, _ ->

            if (d11[sequencerSize] == 0) {
                d11[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d11[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence12).setOnTouchListener { _, _ ->

            if (d12[sequencerSize] == 0) {
                d12[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d12[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence13).setOnTouchListener { _, _ ->

            if (d13[sequencerSize] == 0) {
                d13[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d13[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence14).setOnTouchListener { _, _ ->

            if (d14[sequencerSize] == 0) {
                d14[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d14[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence15).setOnTouchListener { _, _ ->

            if (d15[sequencerSize] == 0) {
                d15[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d15[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence16).setOnTouchListener { _, _ ->

            if (d16[sequencerSize] == 0) {
                d16[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#e9e8e7"))
            } else {
                d16[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list4).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }

        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence).setOnTouchListener { _, _ ->

            if (e1[sequencerSize] == 0) {
                e1[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e1[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence2).setOnTouchListener { _, _ ->

            if (e2[sequencerSize] == 0) {
                e2[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e2[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence2).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence3).setOnTouchListener { _, _ ->

            if (e3[sequencerSize] == 0) {
                e3[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e3[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence3).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence4).setOnTouchListener { _, _ ->

            if (e4[sequencerSize] == 0) {
                e4[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e4[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence4).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence5).setOnTouchListener { _, _ ->

            if (e5[sequencerSize] == 0) {
                e5[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e5[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence5).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence6).setOnTouchListener { _, _ ->

            if (e6[sequencerSize] == 0) {
                e6[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e6[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence6).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence7).setOnTouchListener { _, _ ->

            if (e7[sequencerSize] == 0) {
                e7[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e7[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence7).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence8).setOnTouchListener { _, _ ->

            if (e8[sequencerSize] == 0) {
                e8[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e8[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence8).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence9).setOnTouchListener { _, _ ->

            if (e9[sequencerSize] == 0) {
                e9[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e9[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence9).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence10).setOnTouchListener { _, _ ->

            if (e10[sequencerSize] == 0) {
                e10[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e10[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence10).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence11).setOnTouchListener { _, _ ->

            if (e11[sequencerSize] == 0) {
                e11[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e11[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence11).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence12).setOnTouchListener { _, _ ->

            if (e12[sequencerSize] == 0) {
                e12[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e12[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence12).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence13).setOnTouchListener { _, _ ->

            if (e13[sequencerSize] == 0) {
                e13[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e13[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence13).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence14).setOnTouchListener { _, _ ->

            if (e14[sequencerSize] == 0) {
                e14[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e14[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence14).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence15).setOnTouchListener { _, _ ->

            if (e15[sequencerSize] == 0) {
                e15[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e15[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence15).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }
        findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence16).setOnTouchListener { _, _ ->

            if (e16[sequencerSize] == 0) {
                e16[sequencerSize] = 1
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                e16[sequencerSize] = 0
                findViewById<View>(R.id.sequencer_list5).findViewById<ImageView>(R.id.sequence16).setBackgroundColor(Color.parseColor("#5A5A66"))
            }
            false
        }

        binding.includeMainView.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
            }
                false
        }

        binding.includeMainView2.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
                }
            }
                false
        }

        binding.includeMainView3.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
                }
            }
                false
        }

        binding.includeMainView4.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
                }
            }
                false
        }

        binding.includeMainView5.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
                }
            }
                false
        }

        binding.includeMainView6.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
                }
            }
                false
        }

        binding.includeMainView7.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
                }
            }
                false
        }

        binding.includeMainView8.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
                }
            }
                false
        }

        binding.includeMainView9.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
                }
            }
                false

        }

        binding.includeMainView10.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
                }
            }
                false
        }

        binding.includeMainView11.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
                }
            }
                false
        }

        binding.includeMainView12.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
                }
            }
                false
        }

        binding.includeMainView13.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
                }
            }
                false
        }

        binding.includeMainView14.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
                }
            }
                false
        }

        binding.includeMainView15.imageView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
            }
                false
        }


        binding.includeMainView.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 1
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView2.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 2
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView3.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 3
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView4.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 4
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView5.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 5
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView6.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 6
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView7.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 7
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView8.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 8
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView9.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 9
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView10.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 10
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView11.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 11
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView12.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 12
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView13.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 13
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView14.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 14
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView15.imageView.setOnClickListener {
            if (paste == 1) {
                buttonA = 15
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }

        binding.includeMainView.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
            }
            false
        }

        binding.includeMainView2.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
                }
            }
            false
        }

        binding.includeMainView3.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
                }
            }
            false
        }

        binding.includeMainView4.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
                }
            }
            false
        }

        binding.includeMainView5.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
                }
            }
            false
        }

        binding.includeMainView6.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
                }
            }
            false
        }

        binding.includeMainView7.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
                }
            }
            false
        }

        binding.includeMainView8.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
                }
            }
            false
        }

        binding.includeMainView9.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
                }
            }
            false

        }

        binding.includeMainView10.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
                }
            }
            false
        }

        binding.includeMainView11.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
                }
            }
            false
        }

        binding.includeMainView12.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
                }
            }
            false
        }

        binding.includeMainView13.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
                }
            }
            false
        }

        binding.includeMainView14.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
                }
            }
            false
        }

        binding.includeMainView15.backgroundView.setOnTouchListener { _, event ->
            when {
                gridView.isVisible -> {
                    gridView.visibility = View.INVISIBLE
                }
                gridView2.isVisible -> {
                    gridView2.visibility = View.INVISIBLE
                }
                soundListView.isVisible -> {
                    soundListView.visibility = View.INVISIBLE
                }
                event!!.actionMasked == MotionEvent.ACTION_POINTER_DOWN -> {
                    soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
                event.actionMasked == MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
            }
            false
        }


        binding.includeMainView.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 1
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView2.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 2
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView3.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 3
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView4.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 4
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView5.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 5
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView6.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 6
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView7.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 7
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView8.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 8
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView9.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 9
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView10.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 10
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView11.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 11
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView12.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 12
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView13.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 13
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView14.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 14
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        binding.includeMainView15.backgroundView.setOnClickListener {
            if (paste == 1) {
                buttonA = 15
                meSpinner.avoidDropdownFocus()
                meSpinner.performClick()
            }
        }
        
        findViewById<TextView>(R.id.textView18).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPoolVolume = 0.5f
                    soundPoolTempo = 1.0f
                    soundPoolVolume2 = 0.5f
                    soundPoolTempo2 = 1.0f
                    soundPoolVolume3 = 0.5f
                    soundPoolTempo3 = 1.0f
                    soundPoolVolume4 = 0.5f
                    soundPoolTempo4 = 1.0f
                    soundPoolVolume5 = 0.5f
                    soundPoolTempo5 = 1.0f
                    soundPoolVolume6 = 0.5f
                    soundPoolTempo6 = 1.0f
                    soundPoolVolume7 = 0.5f
                    soundPoolTempo7 = 1.0f
                    soundPoolVolume8 = 0.5f
                    soundPoolTempo8 = 1.0f
                    soundPoolVolume9 = 0.5f
                    soundPoolTempo9 = 1.0f
                    soundPoolVolume10 = 0.5f
                    soundPoolTempo10 = 1.0f
                    soundPoolVolume11 = 0.5f
                    soundPoolTempo11 = 1.0f
                    soundPoolVolume12 = 0.5f
                    soundPoolTempo12 = 1.0f
                    soundPoolVolume13 = 0.5f
                    soundPoolTempo13 = 1.0f
                    soundPoolVolume14 = 0.5f
                    soundPoolTempo14 = 1.0f
                    soundPoolVolume15 = 0.5f
                    soundPoolTempo15 = 1.0f
                    spvF = 5
                    sptF = 10
                    spvF2 = 5
                    sptF2 = 10
                    spvF3 = 5
                    sptF3 = 10
                    spvF4 = 5
                    sptF4 = 10
                    spvF5 = 5
                    sptF5 = 10
                    spvF6 = 5
                    sptF6 = 10
                    spvF7 = 5
                    sptF7 = 10
                    spvF8 = 5
                    sptF8 = 10
                    spvF9 = 5
                    sptF9 = 10
                    spvF10 = 5
                    sptF10 = 10
                    spvF11 = 5
                    sptF11 = 10
                    spvF12 = 5
                    sptF12 = 10
                    spvF13 = 5
                    sptF13 = 10
                    spvF14 = 5
                    sptF14 = 10
                    spvF15 = 5
                    sptF15 = 10
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                }
            }
            false
        }
        findViewById<TextView>(R.id.textView19).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPoolVolume = 1.0f
                    soundPoolVolume2 = 1.0f
                    soundPoolVolume3 = 1.0f
                    soundPoolVolume4 = 1.0f
                    soundPoolVolume5 = 1.0f
                    soundPoolVolume6 = 1.0f
                    soundPoolVolume7 = 1.0f
                    soundPoolVolume8 = 1.0f
                    soundPoolVolume9 = 1.0f
                    soundPoolVolume10 = 1.0f
                    soundPoolVolume11 = 1.0f
                    soundPoolVolume12 = 1.0f
                    soundPoolVolume13 = 1.0f
                    soundPoolVolume14 = 1.0f
                    soundPoolVolume15 = 1.0f
                    spvF = 10
                    spvF2 = 10
                    spvF3 = 10
                    spvF4 = 10
                    spvF5 = 10
                    spvF6 = 10
                    spvF7 = 10
                    spvF8 = 10
                    spvF9 = 10
                    spvF10 = 10
                    spvF11 = 10
                    spvF12 = 10
                    spvF13 = 10
                    spvF14 = 10
                    spvF15 = 10
                    findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.volume).text = soundPoolVolume.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.volume).text = soundPoolVolume4.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.volume).text = soundPoolVolume7.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.volume).text = soundPoolVolume10.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.volume).text = soundPoolVolume13.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                    findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
                }
            }
            false
        }
        findViewById<View>(R.id.include_view).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view2).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view3).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view4).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view5).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view6).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view7).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view8).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view9).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view10).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view11).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view12).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view13).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view14).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view15).findViewById<ImageButton>(R.id.pad).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
            }
            false
        }

        findViewById<View>(R.id.include_view).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF > 1) {
                        spvF -= 1
                        soundPoolVolume = spvF/10.0f
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.volume).text = soundPoolVolume.toString().replace("f", "")
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume.toString()
                                .replace("f", "") + "            " + soundPoolTempo.toString()
                                .replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                    }
                    soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
                }
            }
            false
        }
        findViewById<View>(R.id.include_view).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF < 10) {
                spvF += 1
                soundPoolVolume = spvF/10.0f
                findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.volume).text = soundPoolVolume.toString().replace("f", "")
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF > 2) {
                sptF -= 1
                soundPoolTempo = sptF/10.0f
                findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo == 0.2f) {
                soundPoolTempo = 0.125f
                findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo == 0.125f) {
                soundPoolTempo = 0.2f
                findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF < 80) {
                sptF += 1
                soundPoolTempo = sptF/10.0f
                findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound1, soundPoolVolume, soundPoolVolume, 1, 0, soundPoolTempo)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view2).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF2 > 1) {
                spvF2 -= 1
                soundPoolVolume2 = spvF2/10.0f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view2).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF2 < 10) {
                spvF2 += 1
                soundPoolVolume2 = spvF2/10.0f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view2).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF2 > 2) {
                sptF2 -= 1
                soundPoolTempo2 = sptF2/10.0f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo2 == 0.2f) {
                soundPoolTempo2 = 0.125f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view2).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo2 == 0.125f) {
                soundPoolTempo2 = 0.2f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF2 < 80) {
                sptF2 += 1
                soundPoolTempo2 = sptF2/10.0f
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound2, soundPoolVolume2, soundPoolVolume2, 1, 0, soundPoolTempo2)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view3).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF3 > 1) {
                spvF3 -= 1
                soundPoolVolume3 = spvF3/10.0f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view3).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF3 < 10) {
                spvF3 += 1
                soundPoolVolume3 = spvF3/10.0f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view3).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF3 > 2) {
                sptF3 -= 1
                soundPoolTempo3 = sptF3/10.0f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo3 == 0.2f) {
                soundPoolTempo3 = 0.125f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view3).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo3 == 0.125f) {
                soundPoolTempo3 = 0.2f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF3 < 80) {
                sptF3 += 1
                soundPoolTempo3 = sptF3/10.0f
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound3, soundPoolVolume3, soundPoolVolume3, 1, 0, soundPoolTempo3)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view4).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF4 > 1) {
                spvF4 -= 1
                soundPoolVolume4 = spvF4/10.0f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.volume).text = soundPoolVolume4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view4).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF4 < 10) {
                spvF4 += 1
                soundPoolVolume4 = spvF4/10.0f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.volume).text = soundPoolVolume4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view4).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF4 > 2) {
                sptF4 -= 1
                soundPoolTempo4 = sptF4/10.0f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo4 == 0.2f) {
                soundPoolTempo4 = 0.125f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view4).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo4 == 0.125f) {
                soundPoolTempo4 = 0.2f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF4 < 80) {
                sptF4 += 1
                soundPoolTempo4 = sptF4/10.0f
                findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound4, soundPoolVolume4, soundPoolVolume4, 1, 0, soundPoolTempo4)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view5).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF5 > 1) {
                spvF5 -= 1
                soundPoolVolume5 = spvF5/10.0f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view5).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF5 < 10) {
                spvF5 += 1
                soundPoolVolume5 = spvF5/10.0f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view5).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF5 > 2) {
                sptF5 -= 1
                soundPoolTempo5 = sptF5/10.0f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo5 == 0.2f) {
                soundPoolTempo5 = 0.125f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view5).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo5 == 0.125f) {
                soundPoolTempo5 = 0.2f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF5 < 80) {
                sptF5 += 1
                soundPoolTempo5 = sptF5/10.0f
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound5, soundPoolVolume5, soundPoolVolume5, 1, 0, soundPoolTempo5)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view6).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF6 > 1) {
                spvF6 -= 1
                soundPoolVolume6 = spvF6/10.0f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view6).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF6 < 10) {
                spvF6 += 1
                soundPoolVolume6 = spvF6/10.0f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view6).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF6 > 2) {
                sptF6 -= 1
                soundPoolTempo6 = sptF6/10.0f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo6 == 0.2f) {
                soundPoolTempo6 = 0.125f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view6).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo6 == 0.125f) {
                soundPoolTempo6 = 0.2f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF6 < 80) {
                sptF6 += 1
                soundPoolTempo6 = sptF6/10.0f
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound6, soundPoolVolume6, soundPoolVolume6, 1, 0, soundPoolTempo6)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view7).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF7 > 1) {
                spvF7 -= 1
                soundPoolVolume7 = spvF7/10.0f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.volume).text = soundPoolVolume7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view7).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF7 < 10) {
                spvF7 += 1
                soundPoolVolume7 = spvF7/10.0f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.volume).text = soundPoolVolume7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view7).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF7 > 2) {
                sptF7 -= 1
                soundPoolTempo7 = sptF7/10.0f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo7 == 0.2f) {
                soundPoolTempo7 = 0.125f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view7).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo7 == 0.125f) {
                soundPoolTempo7 = 0.2f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF7 < 80) {
                sptF7 += 1
                soundPoolTempo7 = sptF7/10.0f
                findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound7, soundPoolVolume7, soundPoolVolume7, 1, 0, soundPoolTempo7)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view8).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF8 > 1) {
                spvF8 -= 1
                soundPoolVolume8 = spvF8/10.0f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view8).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF8 < 10) {
                spvF8 += 1
                soundPoolVolume8 = spvF8/10.0f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view8).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF8 > 2) {
                sptF8 -= 1
                soundPoolTempo8 = sptF8/10.0f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo8 == 0.2f) {
                soundPoolTempo8 = 0.125f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view8).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo8 == 0.125f) {
                soundPoolTempo8 = 0.2f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF8 < 80) {
                sptF8 += 1
                soundPoolTempo8 = sptF8/10.0f
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound8, soundPoolVolume8, soundPoolVolume8, 1, 0, soundPoolTempo8)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view9).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF9 > 1) {
                spvF9 -= 1
                soundPoolVolume9 = spvF9/10.0f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view9).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF9 < 10) {
                spvF9 += 1
                soundPoolVolume9 = spvF9/10.0f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view9).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF9 > 2) {
                sptF9 -= 1
                soundPoolTempo9 = sptF9/10.0f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo9 == 0.2f) {
                soundPoolTempo9 = 0.125f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view9).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo9 == 0.125f) {
                soundPoolTempo9 = 0.2f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF9 < 80) {
                sptF9 += 1
                soundPoolTempo9 = sptF9/10.0f
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound9, soundPoolVolume9, soundPoolVolume9, 1, 0, soundPoolTempo9)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view10).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF10 > 1) {
                spvF10 -= 1
                soundPoolVolume10 = spvF10/10.0f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.volume).text = soundPoolVolume10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view10).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF10 < 10) {
                spvF10 += 1
                soundPoolVolume10 = spvF10/10.0f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.volume).text = soundPoolVolume10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view10).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF10 > 2) {
                sptF10 -= 1
                soundPoolTempo10 = sptF10/10.0f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo10 == 0.2f) {
                soundPoolTempo10 = 0.125f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view10).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo10 == 0.125f) {
                soundPoolTempo10 = 0.2f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF10 < 80) {
                sptF10 += 1
                soundPoolTempo10 = sptF10/10.0f
                findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound10, soundPoolVolume10, soundPoolVolume10, 1, 0, soundPoolTempo10)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view11).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF11 > 1) {
                spvF11 -= 1
                soundPoolVolume11 = spvF11/10.0f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view11).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF11 < 10) {
                spvF11 += 1
                soundPoolVolume11 = spvF11/10.0f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view11).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF11 > 2) {
                sptF11 -= 1
                soundPoolTempo11 = sptF11/10.0f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo11 == 0.2f) {
                soundPoolTempo11 = 0.125f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view11).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo11 == 0.125f) {
                soundPoolTempo11 = 0.2f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF11 < 80) {
                sptF11 += 1
                soundPoolTempo11 = sptF11/10.0f
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound11, soundPoolVolume11, soundPoolVolume11, 1, 0, soundPoolTempo11)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view12).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF12 > 1) {
                spvF12 -= 1
                soundPoolVolume12 = spvF12/10.0f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view12).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF12 < 10) {
                spvF12 += 1
                soundPoolVolume12 = spvF12/10.0f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view12).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF12 > 2) {
                sptF12 -= 1
                soundPoolTempo12 = sptF12/10.0f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo12 == 0.2f) {
                soundPoolTempo12 = 0.125f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view12).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo12 == 0.125f) {
                soundPoolTempo12 = 0.2f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF12 < 80) {
                sptF12 += 1
                soundPoolTempo12 = sptF12/10.0f
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound12, soundPoolVolume12, soundPoolVolume12, 1, 0, soundPoolTempo12)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view13).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF13 > 1) {
                spvF13 -= 1
                soundPoolVolume13 = spvF13/10.0f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.volume).text = soundPoolVolume13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view13).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF13 < 10) {
                spvF13 += 1
                soundPoolVolume13 = spvF13/10.0f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.volume).text = soundPoolVolume13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view13).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF13 > 2) {
                sptF13 -= 1
                soundPoolTempo13 = sptF13/10.0f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo13 == 0.2f) {
                soundPoolTempo13 = 0.125f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view13).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo13 == 0.125f) {
                soundPoolTempo13 = 0.2f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF13 < 80) {
                sptF13 += 1
                soundPoolTempo13 = sptF13/10.0f
                findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound13, soundPoolVolume13, soundPoolVolume13, 1, 0, soundPoolTempo13)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view14).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF14 > 1) {
                spvF14 -= 1
                soundPoolVolume14 = spvF14/10.0f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view14).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF14 < 10) {
                spvF14 += 1
                soundPoolVolume14 = spvF14/10.0f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view14).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF14 > 2) {
                sptF14 -= 1
                soundPoolTempo14 = sptF14/10.0f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo14 == 0.2f) {
                soundPoolTempo14 = 0.125f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view14).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo14 == 0.125f) {
                soundPoolTempo14 = 0.2f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF14 < 80) {
                sptF14 += 1
                soundPoolTempo14 = sptF14/10.0f
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound14, soundPoolVolume14, soundPoolVolume14, 1, 0, soundPoolTempo14)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view15).findViewById<ImageButton>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF15 > 1) {
                spvF15 -= 1
                soundPoolVolume15 = spvF15/10.0f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view15).findViewById<ImageButton>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (spvF15 < 10) {
                spvF15 += 1
                soundPoolVolume15 = spvF15/10.0f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view15).findViewById<ImageButton>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (sptF15 > 2) {
                sptF15 -= 1
                soundPoolTempo15 = sptF15/10.0f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo15 == 0.2f) {
                soundPoolTempo15 = 0.125f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
        }
            }
            false
        }
        findViewById<View>(R.id.include_view15).findViewById<ImageButton>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            if (soundPoolTempo15 == 0.125f) {
                soundPoolTempo15 = 0.2f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (sptF15 < 80) {
                sptF15 += 1
                soundPoolTempo15 = sptF15/10.0f
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
                }
            }
            false
        }

        findViewById<View>(R.id.tuning_sequencer).findViewById<ImageView>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF > 1) {
                        spvF -= 1
                        soundPoolVolume = spvF/10.0f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume.toString()
                                .replace("f", "") + "            " + soundPoolTempo.toString()
                                .replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer).findViewById<ImageView>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF < 10) {
                        spvF += 1
                        soundPoolVolume = spvF/10.0f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume.toString()
                                .replace("f", "") + "            " + soundPoolTempo.toString()
                                .replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer).findViewById<ImageView>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sptF > 2) {
                        sptF -= 1
                        soundPoolTempo = sptF/10.0f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                    } else if (soundPoolTempo == 0.2f) {
                        soundPoolTempo = 0.125f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer).findViewById<ImageView>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (soundPoolTempo == 0.125f) {
                        soundPoolTempo = 0.2f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                    } else if (sptF < 80) {
                        sptF += 1
                        soundPoolTempo = sptF/10.0f
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer).findViewById<TextView>(R.id.tempo).text = soundPoolTempo.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer2).findViewById<ImageView>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF4 > 1) {
                        spvF4 -= 1
                        soundPoolVolume4 = spvF4/10.0f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume4.toString()
                                .replace("f", "") + "            " + soundPoolTempo4.toString()
                                .replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume4.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer2).findViewById<ImageView>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF4 < 10) {
                        spvF4 += 1
                        soundPoolVolume4 = spvF4/10.0f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume4.toString()
                                .replace("f", "") + "            " + soundPoolTempo4.toString()
                                .replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume4.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer2).findViewById<ImageView>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sptF4 > 2) {
                        sptF4 -= 1
                        soundPoolTempo4 = sptF4/10.0f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                    } else if (soundPoolTempo4 == 0.2f) {
                        soundPoolTempo4 = 0.125f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer2).findViewById<ImageView>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (soundPoolTempo4 == 0.125f) {
                        soundPoolTempo4 = 0.2f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                    } else if (sptF4 < 80) {
                        sptF4 += 1
                        soundPoolTempo4 = sptF4/10.0f
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer2).findViewById<TextView>(R.id.tempo).text = soundPoolTempo4.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer3).findViewById<ImageView>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF7 > 1) {
                        spvF7 -= 1
                        soundPoolVolume7 = spvF7/10.0f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume7.toString()
                                .replace("f", "") + "            " + soundPoolTempo7.toString()
                                .replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume7.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer3).findViewById<ImageView>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF7 < 10) {
                        spvF7 += 1
                        soundPoolVolume7 = spvF7/10.0f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume7.toString()
                                .replace("f", "") + "            " + soundPoolTempo7.toString()
                                .replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume7.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer3).findViewById<ImageView>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sptF7 > 2) {
                        sptF7 -= 1
                        soundPoolTempo7 = sptF7/10.0f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                    } else if (soundPoolTempo7 == 0.2f) {
                        soundPoolTempo7 = 0.125f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer3).findViewById<ImageView>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (soundPoolTempo7 == 0.125f) {
                        soundPoolTempo7 = 0.2f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                    } else if (sptF7 < 80) {
                        sptF7 += 1
                        soundPoolTempo7 = sptF7/10.0f
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer3).findViewById<TextView>(R.id.tempo).text = soundPoolTempo7.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer4).findViewById<ImageView>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF10 > 1) {
                        spvF10 -= 1
                        soundPoolVolume10 = spvF10/10.0f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume10.toString()
                                .replace("f", "") + "            " + soundPoolTempo10.toString()
                                .replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume10.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer4).findViewById<ImageView>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF10 < 10) {
                        spvF10 += 1
                        soundPoolVolume10 = spvF10/10.0f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume10.toString()
                                .replace("f", "") + "            " + soundPoolTempo10.toString()
                                .replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume10.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer4).findViewById<ImageView>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sptF10 > 2) {
                        sptF10 -= 1
                        soundPoolTempo10 = sptF10/10.0f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                    } else if (soundPoolTempo10 == 0.2f) {
                        soundPoolTempo10 = 0.125f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer4).findViewById<ImageView>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (soundPoolTempo10 == 0.125f) {
                        soundPoolTempo10 = 0.2f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                    } else if (sptF10 < 80) {
                        sptF10 += 1
                        soundPoolTempo10 = sptF10/10.0f
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer4).findViewById<TextView>(R.id.tempo).text = soundPoolTempo10.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer5).findViewById<ImageView>(R.id.volume_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF13 > 1) {
                        spvF13 -= 1
                        soundPoolVolume13 = spvF13/10.0f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume13.toString()
                                .replace("f", "") + "            " + soundPoolTempo13.toString()
                                .replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume13.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer5).findViewById<ImageView>(R.id.volume_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (spvF13 < 10) {
                        spvF13 += 1
                        soundPoolVolume13 = spvF13/10.0f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text =
                            ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text =
                            soundPoolVolume13.toString()
                                .replace("f", "") + "            " + soundPoolTempo13.toString()
                                .replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.volume).text =
                            soundPoolVolume13.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer5).findViewById<ImageView>(R.id.tempo_minus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (sptF13 > 2) {
                        sptF13 -= 1
                        soundPoolTempo13 = sptF13/10.0f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                    } else if (soundPoolTempo13 == 0.2f) {
                        soundPoolTempo13 = 0.125f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                    }
                }
            }
            false
        }
        findViewById<View>(R.id.tuning_sequencer5).findViewById<ImageView>(R.id.tempo_plus).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (soundPoolTempo13 == 0.125f) {
                        soundPoolTempo13 = 0.2f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                    } else if (sptF13 < 80) {
                        sptF13 += 1
                        soundPoolTempo13 = sptF13/10.0f
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
                        findViewById<View>(R.id.tuning_sequencer5).findViewById<TextView>(R.id.tempo).text = soundPoolTempo13.toString().replace("f", "")
                    }
                }
            }
            false
        }
    }

    private fun x53() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view12).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view14).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view15).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view12).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view14).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view15).visibility = View.VISIBLE
        padCheck = 53
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x43() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view12).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view12).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 43
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x33() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view10).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view6).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view9).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 33
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x52() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view14).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view14).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 52
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x42() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view11).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 42
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x32() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view8).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view10).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 32
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x22() {
        findViewById<View>(R.id.include_main_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view7).visibility = View.GONE
        findViewById<View>(R.id.include_main_view8).visibility = View.GONE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view5).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view7).visibility = View.GONE
        findViewById<View>(R.id.include_view8).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view10).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 22
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x21() {
        findViewById<View>(R.id.include_main_view2).visibility = View.GONE
        findViewById<View>(R.id.include_main_view5).visibility = View.GONE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view7).visibility = View.GONE
        findViewById<View>(R.id.include_main_view8).visibility = View.GONE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view2).visibility = View.GONE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view5).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view7).visibility = View.GONE
        findViewById<View>(R.id.include_view8).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view10).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 21
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x51() {
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view2).visibility = View.GONE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view5).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view8).visibility = View.GONE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view13).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view2).visibility = View.GONE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view5).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view8).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 51
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x41(){
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view2).visibility = View.GONE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view5).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view8).visibility = View.GONE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view10).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view2).visibility = View.GONE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view5).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view8).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 41
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }
    private fun x31() {
        findViewById<View>(R.id.include_main_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_main_view2).visibility = View.GONE
        findViewById<View>(R.id.include_main_view3).visibility = View.GONE
        findViewById<View>(R.id.include_main_view5).visibility = View.GONE
        findViewById<View>(R.id.include_main_view6).visibility = View.GONE
        findViewById<View>(R.id.include_main_view8).visibility = View.GONE
        findViewById<View>(R.id.include_main_view9).visibility = View.GONE
        findViewById<View>(R.id.include_main_view10).visibility = View.GONE
        findViewById<View>(R.id.include_main_view11).visibility = View.GONE
        findViewById<View>(R.id.include_main_view12).visibility = View.GONE
        findViewById<View>(R.id.include_main_view13).visibility = View.GONE
        findViewById<View>(R.id.include_main_view14).visibility = View.GONE
        findViewById<View>(R.id.include_main_view15).visibility = View.GONE
        findViewById<View>(R.id.include_view7).visibility = View.VISIBLE
        findViewById<View>(R.id.include_view2).visibility = View.GONE
        findViewById<View>(R.id.include_view3).visibility = View.GONE
        findViewById<View>(R.id.include_view5).visibility = View.GONE
        findViewById<View>(R.id.include_view6).visibility = View.GONE
        findViewById<View>(R.id.include_view8).visibility = View.GONE
        findViewById<View>(R.id.include_view9).visibility = View.GONE
        findViewById<View>(R.id.include_view10).visibility = View.GONE
        findViewById<View>(R.id.include_view11).visibility = View.GONE
        findViewById<View>(R.id.include_view12).visibility = View.GONE
        findViewById<View>(R.id.include_view13).visibility = View.GONE
        findViewById<View>(R.id.include_view14).visibility = View.GONE
        findViewById<View>(R.id.include_view15).visibility = View.GONE
        padCheck = 31
        binding.gridView.visibility = View.INVISIBLE
        binding.notes.visibility = View.GONE
    }

    private fun stickyImmersiveMode() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Log.d("debug", "The system bars are visible")
            } else {
                Log.d("debug", "The system bars are NOT visible")
            }
        }
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("rewarded ads", adError.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d("rewarded ads", "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })

        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("rewarded ads", "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d("rewarded ads", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("rewarded ads", "Ad showed fullscreen content.")
                mRewardedAd = null
            }
        }
    }

    private fun showRewardAd() {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this) { rewardItem ->
                binding.adView.visibility = View.GONE
                binding.topSpace.visibility = View.GONE
                binding.bottomSpace.visibility = View.GONE
                binding.gridView.visibility = View.INVISIBLE
                adCheck = 1
                stickyImmersiveMode()
                var rewardAmount = rewardItem.amount
                var rewardType = rewardItem.type
                Log.d("TAG", rewardItem.toString())
                Log.d("TAG", "User earned the reward.")
            }
        } else {
            stickyImmersiveMode()
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
        }
    }

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density
            var adWidthPixels = adViewContainer.width.toFloat()
            if (adWidthPixels == 0.0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()


            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this@MainActivity, adWidth)
        }

    private fun initAdMob() {
        adViewContainer = findViewById(R.id.adView)

        MobileAds.initialize(this) {}
        admobmAdView = AdView(this)
        admobmAdView.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        admobmAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.topSpace.visibility = View.GONE
            }
            override fun onAdLoaded() {
                adViewContainer.addView(admobmAdView)
                binding.topSpace.visibility = View.GONE
            }
        }
    }

    private fun loadAdMob() {
        val request = AdRequest.Builder().build()
        admobmAdView.adSize = adSize
        admobmAdView.loadAd(request)
    }

    private fun effect(imageView: ImageView, mpDuration: Int) {
        val cx = imageView.width / 2
        val cy = imageView.height / 2

        val initialRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, initialRadius, 0f)

        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                imageView.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                imageView.visibility = View.VISIBLE
            }
        })

        anim.duration = mpDuration.toLong()
        anim.start()
    }

    override fun clicked(soundList: SoundList) {
        sound16 = if (buttonB == 1) {
            soundPool.load(soundList.name, 1)
        } else {
            soundPool.load(assets.openFd(soundList.name), 1)
        }
            soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f)
            }
    }

    private fun playMode() {
        val tuning = arrayOf(
            "Change Pad Sounds",
            "Random Pad Sounds",
            "Change Pad Colors",
            "Save Pad Settings",
            "Load Pad Settings",
            "Adjusting Sounds",
            "Lock Settings",
            "Hide banner Ads",
            "EXIT",
            "5x3","5x2","5x1",
            "4x3","4x2","4x1",
            "3x3","3x2","3x1",
            "2x2","2x1"
        )
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown, tuning)
        val gridView: GridView = findViewById(R.id.grid_view)
        gridView.adapter = adapter
        paste = 0
        binding.toolbarMain.setBackgroundColor(Color.parseColor("#5A5A66"))
        gridView.visibility = View.INVISIBLE
        gridView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun clicked2(soundList: SoundList) {
        try {
            when {
                buttonA == 1 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView.imageView,800)
                    sound1 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText1 = soundList.name
                    soundPoolVolume = 0.5f
                    soundPoolTempo = 1.0f
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 2 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView2.imageView,800)
                    sound2 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration2 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView2.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText2 = soundList.name
                    soundPoolVolume2 = 0.5f
                    soundPoolTempo2 = 1.0f
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 3 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView3.imageView,800)
                    sound3 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration3 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView3.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText3 = soundList.name
                    soundPoolVolume3 = 0.5f
                    soundPoolTempo3 = 1.0f
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 4 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView4.imageView,800)
                    sound4 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration4 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView4.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText4 = soundList.name
                    soundPoolVolume4 = 0.5f
                    soundPoolTempo4 = 1.0f
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 5 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView5.imageView,800)
                    sound5 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration5 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView5.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText5 = soundList.name
                    soundPoolVolume5 = 0.5f
                    soundPoolTempo5 = 1.0f
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 6 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView6.imageView,800)
                    sound6 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration6 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView6.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText6 = soundList.name
                    soundPoolVolume6 = 0.5f
                    soundPoolTempo6 = 1.0f
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 7 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView7.imageView,800)
                    sound7 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration7 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView7.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText7 = soundList.name
                    soundPoolVolume7 = 0.5f
                    soundPoolTempo7 = 1.0f
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 8 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView8.imageView,800)
                    sound8 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration8 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView8.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText8 = soundList.name
                    soundPoolVolume8 = 0.5f
                    soundPoolTempo8 = 1.0f
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 9 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView9.imageView,800)
                    sound9 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration9 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView9.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText9 = soundList.name
                    soundPoolVolume9 = 0.5f
                    soundPoolTempo9 = 1.0f
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 10 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView10.imageView,800)
                    sound10 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration10 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView10.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText10 = soundList.name
                    soundPoolVolume10 = 0.5f
                    soundPoolTempo10 = 1.0f
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 11 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView11.imageView,800)
                    sound11 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration11 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView11.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText11 = soundList.name
                    soundPoolVolume11 = 0.5f
                    soundPoolTempo11 = 1.0f
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 12 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView12.imageView,800)
                    sound12 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration12 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView12.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText12 = soundList.name
                    soundPoolVolume12 = 0.5f
                    soundPoolTempo12 = 1.0f
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 13 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView13.imageView,800)
                    sound13 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration13 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView13.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText13 = soundList.name
                    soundPoolVolume13 = 0.5f
                    soundPoolTempo13 = 1.0f
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 14 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView14.imageView,800)
                    sound14 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration14 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView14.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText14 = soundList.name
                    soundPoolVolume14 = 0.5f
                    soundPoolTempo14 = 1.0f
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 15 && buttonB == 1 -> {
                    playMode()
                    effect(binding.includeMainView15.imageView,800)
                    sound15 = soundPool.load(soundList.name, 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(this, Uri.parse(soundList.name))
                    getmpDuration.prepare()
                    mpDuration15 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView15.textView.text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText15 = soundList.name
                    soundPoolVolume15 = 0.5f
                    soundPoolTempo15 = 1.0f
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 16 && buttonB == 1 -> {
                    lmp.release()
                    lmp = LoopMediaPlayer(this@MainActivity, Uri.parse(soundList.name))
                    lmp.stop()
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    actionTitle = soundList.name
                    supportActionBar?.title = actionTitle.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                }
                buttonA == 1 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView.imageView,800)
                    sound1 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText1 = soundList.name.replace(".ogg","")
                    soundPoolVolume = 0.5f
                    soundPoolTempo = 1.0f
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 2 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView2.imageView,800)
                    sound2 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration2 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView2.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText2 = soundList.name.replace(".ogg","")
                    soundPoolVolume2 = 0.5f
                    soundPoolTempo2 = 1.0f
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 3 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView3.imageView,800)
                    sound3 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration3 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView3.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText3 = soundList.name.replace(".ogg","")
                    soundPoolVolume3 = 0.5f
                    soundPoolTempo3 = 1.0f
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 4 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView4.imageView,800)
                    sound4 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration4 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView4.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText4 = soundList.name.replace(".ogg","")
                    soundPoolVolume4 = 0.5f
                    soundPoolTempo4 = 1.0f
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 5 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView5.imageView,800)
                    sound5 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration5 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView5.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText5 = soundList.name.replace(".ogg","")
                    soundPoolVolume5 = 0.5f
                    soundPoolTempo5 = 1.0f
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 6 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView6.imageView,800)
                    sound6 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration6 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView6.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText6 = soundList.name.replace(".ogg","")
                    soundPoolVolume6 = 0.5f
                    soundPoolTempo6 = 1.0f
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 7 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView7.imageView,800)
                    sound7 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration7 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView7.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText7 = soundList.name.replace(".ogg","")
                    soundPoolVolume7 = 0.5f
                    soundPoolTempo7 = 1.0f
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 8 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView8.imageView,800)
                    sound8 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration8 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView8.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText8 = soundList.name.replace(".ogg","")
                    soundPoolVolume8 = 0.5f
                    soundPoolTempo8 = 1.0f
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 9 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView9.imageView,800)
                    sound9 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration9 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView9.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText9 = soundList.name.replace(".ogg","")
                    soundPoolVolume9 = 0.5f
                    soundPoolTempo9 = 1.0f
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 10 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView10.imageView,800)
                    sound10 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration10 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView10.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText10 = soundList.name.replace(".ogg","")
                    soundPoolVolume10 = 0.5f
                    soundPoolTempo10 = 1.0f
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 11 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView11.imageView,800)
                    sound11 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration11 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView11.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText11 = soundList.name.replace(".ogg","")
                    soundPoolVolume11 = 0.5f
                    soundPoolTempo11 = 1.0f
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 12 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView12.imageView,800)
                    sound12 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration12 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView12.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText12 = soundList.name.replace(".ogg","")
                    soundPoolVolume12 = 0.5f
                    soundPoolTempo12 = 1.0f
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 13 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView13.imageView,800)
                    sound13 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration13 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView13.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText13 = soundList.name.replace(".ogg","")
                    soundPoolVolume13 = 0.5f
                    soundPoolTempo13 = 1.0f
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 14 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView14.imageView,800)
                    sound14 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration14 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView14.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText14 = soundList.name.replace(".ogg","")
                    soundPoolVolume14 = 0.5f
                    soundPoolTempo14 = 1.0f
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 15 && buttonB == 2 -> {
                    playMode()
                    effect(binding.includeMainView15.imageView,800)
                    sound15 = soundPool.load(assets.openFd(soundList.name), 1)
                    getmpDuration = MediaPlayer()
                    getmpDuration.setDataSource(assets.openFd(soundList.name).fileDescriptor,
                        assets.openFd(soundList.name).startOffset,
                        assets.openFd(soundList.name).declaredLength)
                    getmpDuration.prepare()
                    mpDuration15 = getmpDuration.duration
                    getmpDuration.release()
                    soundPool.setOnLoadCompleteListener { soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                    binding.includeMainView15.textView.text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
                    padText15 = soundList.name.replace(".ogg","")
                    soundPoolVolume15 = 0.5f
                    soundPoolTempo15 = 1.0f
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                }
                buttonA == 16 -> {
                    lmp.release()
                    lmp = LoopMediaPlayer(this@MainActivity, Uri.parse("android.resource://" + packageName + "/raw/" + soundList.name.replace(".ogg", "")))
                    lmp.stop()
                    handler.removeCallbacks(runnable)
                    noteCount = 0
                    count = 5
                    bpm = 10
                    actionTitle = soundList.name.replace(".ogg","")
                    supportActionBar?.title = actionTitle.replace("_", " ").uppercase()
                    soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_LONG).show()
        }
        findViewById<ListView>(R.id.list_view).visibility = View.INVISIBLE
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                stickyImmersiveMode()
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult1,
                        Toast.LENGTH_LONG
                ).show()
            } else {
                stickyImmersiveMode()
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult2,
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                stickyImmersiveMode()
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult1,
                        Toast.LENGTH_LONG
                ).show()
            } else {
                stickyImmersiveMode()
                Toast.makeText(
                        this,
                        R.string.onRequestPermissionsResult2,
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun create () {
        mRealm.executeTransaction {
            val ss = mRealm.createObject<SaveSlot>(primaryKeyValue = "1")
            ss.pad = padText1
            ss.pad2 = padText2
            ss.pad3 = padText3
            ss.pad4 = padText4
            ss.pad5 = padText5
            ss.pad6 = padText6
            ss.pad7 = padText7
            ss.pad8 = padText8
            ss.pad9 = padText9
            ss.pad10 = padText10
            ss.pad11 = padText11
            ss.pad12 = padText12
            ss.pad13 = padText13
            ss.pad14 = padText14
            ss.pad15 = padText15
            ss.volume = soundPoolVolume
            ss.volume2 = soundPoolVolume2
            ss.volume3 = soundPoolVolume3
            ss.volume4 = soundPoolVolume4
            ss.volume5 = soundPoolVolume5
            ss.volume6 = soundPoolVolume6
            ss.volume7 = soundPoolVolume7
            ss.volume8 = soundPoolVolume8
            ss.volume9 = soundPoolVolume9
            ss.volume10 = soundPoolVolume10
            ss.volume11 = soundPoolVolume11
            ss.volume12 = soundPoolVolume12
            ss.volume13 = soundPoolVolume13
            ss.volume14 = soundPoolVolume14
            ss.volume15 = soundPoolVolume15
            ss.tempo = soundPoolTempo
            ss.tempo2 = soundPoolTempo2
            ss.tempo3 = soundPoolTempo3
            ss.tempo4 = soundPoolTempo4
            ss.tempo5 = soundPoolTempo5
            ss.tempo6 = soundPoolTempo6
            ss.tempo7 = soundPoolTempo7
            ss.tempo8 = soundPoolTempo8
            ss.tempo9 = soundPoolTempo9
            ss.tempo10 = soundPoolTempo10
            ss.tempo11 = soundPoolTempo11
            ss.tempo12 = soundPoolTempo12
            ss.tempo13 = soundPoolTempo13
            ss.tempo14 = soundPoolTempo14
            ss.tempo15 = soundPoolTempo15
            ss.vF = spvF
            ss.vF2 = spvF2
            ss.vF3 = spvF3
            ss.vF4 = spvF4
            ss.vF5 = spvF5
            ss.vF6 = spvF6
            ss.vF7 = spvF7
            ss.vF8 = spvF8
            ss.vF9 = spvF9
            ss.vF10 = spvF10
            ss.vF11 = spvF11
            ss.vF12 = spvF12
            ss.vF13 = spvF13
            ss.vF14 = spvF14
            ss.vF15 = spvF15
            ss.tF = sptF
            ss.tF2 = sptF2
            ss.tF3 = sptF3
            ss.tF4 = sptF4
            ss.tF5 = sptF5
            ss.tF6 = sptF6
            ss.tF7 = sptF7
            ss.tF8 = sptF8
            ss.tF9 = sptF9
            ss.tF10 = sptF10
            ss.tF11 = sptF11
            ss.tF12 = sptF12
            ss.tF13 = sptF13
            ss.tF14 = sptF14
            ss.tF15 = sptF15
            ss.check = padCheck
            ss.c_check = colorCheck
            mRealm.copyToRealm(ss)
        }

    }

    private fun update() {
        val data = mRealm.where(SaveSlot::class.java).equalTo("id","1").findFirst()
        mRealm.executeTransaction {
            data?.pad = padText1
            data?.pad2 = padText2
            data?.pad3 = padText3
            data?.pad4 = padText4
            data?.pad5 = padText5
            data?.pad6 = padText6
            data?.pad7 = padText7
            data?.pad8 = padText8
            data?.pad9 = padText9
            data?.pad10 = padText10
            data?.pad11 = padText11
            data?.pad12 = padText12
            data?.pad13 = padText13
            data?.pad14 = padText14
            data?.pad15 = padText15
            data?.volume = soundPoolVolume
            data?.volume2 = soundPoolVolume2
            data?.volume3 = soundPoolVolume3
            data?.volume4 = soundPoolVolume4
            data?.volume5 = soundPoolVolume5
            data?.volume6 = soundPoolVolume6
            data?.volume7 = soundPoolVolume7
            data?.volume8 = soundPoolVolume8
            data?.volume9 = soundPoolVolume9
            data?.volume10 = soundPoolVolume10
            data?.volume11 = soundPoolVolume11
            data?.volume12 = soundPoolVolume12
            data?.volume13 = soundPoolVolume13
            data?.volume14 = soundPoolVolume14
            data?.volume15 = soundPoolVolume15
            data?.tempo = soundPoolTempo
            data?.tempo2 = soundPoolTempo2
            data?.tempo3 = soundPoolTempo3
            data?.tempo4 = soundPoolTempo4
            data?.tempo5 = soundPoolTempo5
            data?.tempo6 = soundPoolTempo6
            data?.tempo7 = soundPoolTempo7
            data?.tempo8 = soundPoolTempo8
            data?.tempo9 = soundPoolTempo9
            data?.tempo10 = soundPoolTempo10
            data?.tempo11 = soundPoolTempo11
            data?.tempo12 = soundPoolTempo12
            data?.tempo13 = soundPoolTempo13
            data?.tempo14 = soundPoolTempo14
            data?.tempo15 = soundPoolTempo15
            data?.vF = spvF
            data?.vF2 = spvF2
            data?.vF3 = spvF3
            data?.vF4 = spvF4
            data?.vF5 = spvF5
            data?.vF6 = spvF6
            data?.vF7 = spvF7
            data?.vF8 = spvF8
            data?.vF9 = spvF9
            data?.vF10 = spvF10
            data?.vF11 = spvF11
            data?.vF12 = spvF12
            data?.vF13 = spvF13
            data?.vF14 = spvF14
            data?.vF15 = spvF15
            data?.tF = sptF
            data?.tF2 = sptF2
            data?.tF3 = sptF3
            data?.tF4 = sptF4
            data?.tF5 = sptF5
            data?.tF6 = sptF6
            data?.tF7 = sptF7
            data?.tF8 = sptF8
            data?.tF9 = sptF9
            data?.tF10 = sptF10
            data?.tF11 = sptF11
            data?.tF12 = sptF12
            data?.tF13 = sptF13
            data?.tF14 = sptF14
            data?.tF15 = sptF15
            data?.check = padCheck
            data?.c_check = colorCheck
        }

    }

    @SuppressLint("SetTextI18n")
    private fun read() {
        if (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad != null) {
            padText1 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad.toString())
            padText2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad2.toString())
            padText3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad3.toString())
            padText4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad4.toString())
            padText5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad5.toString())
            padText6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad6.toString())
            padText7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad7.toString())
            padText8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad8.toString())
            padText9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad9.toString())
            padText10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad10.toString())
            padText11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad11.toString())
            padText12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad12.toString())
            padText13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad13.toString())
            padText14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad14.toString())
            padText15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.pad15.toString())
            soundPoolVolume = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume!!)
            soundPoolVolume2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume2!!)
            soundPoolVolume3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume3!!)
            soundPoolVolume4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume4!!)
            soundPoolVolume5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume5!!)
            soundPoolVolume6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume6!!)
            soundPoolVolume7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume7!!)
            soundPoolVolume8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume8!!)
            soundPoolVolume9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume9!!)
            soundPoolVolume10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume10!!)
            soundPoolVolume11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume11!!)
            soundPoolVolume12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume12!!)
            soundPoolVolume13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume13!!)
            soundPoolVolume14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume14!!)
            soundPoolVolume15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.volume15!!)
            soundPoolTempo = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo!!)
            soundPoolTempo2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo2!!)
            soundPoolTempo3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo3!!)
            soundPoolTempo4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo4!!)
            soundPoolTempo5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo5!!)
            soundPoolTempo6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo6!!)
            soundPoolTempo7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo7!!)
            soundPoolTempo8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo8!!)
            soundPoolTempo9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo9!!)
            soundPoolTempo10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo10!!)
            soundPoolTempo11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo11!!)
            soundPoolTempo12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo12!!)
            soundPoolTempo13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo13!!)
            soundPoolTempo14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo14!!)
            soundPoolTempo15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tempo15!!)
            spvF = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF!!)
            spvF2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF2!!)
            spvF3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF3!!)
            spvF4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF4!!)
            spvF5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF5!!)
            spvF6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF6!!)
            spvF7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF7!!)
            spvF8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF8!!)
            spvF9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF9!!)
            spvF10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF10!!)
            spvF11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF11!!)
            spvF12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF12!!)
            spvF13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF13!!)
            spvF14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF14!!)
            spvF15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.vF15!!)
            sptF = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF!!)
            sptF2 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF2!!)
            sptF3 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF3!!)
            sptF4 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF4!!)
            sptF5 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF5!!)
            sptF6 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF6!!)
            sptF7 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF7!!)
            sptF8 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF8!!)
            sptF9 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF9!!)
            sptF10 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF10!!)
            sptF11 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF11!!)
            sptF12 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF12!!)
            sptF13 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF13!!)
            sptF14 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF14!!)
            sptF15 = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.tF15!!)
            padCheck = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.check!!)
            colorCheck = (mRealm.where(SaveSlot::class.java).equalTo("id", "1").findFirst()?.c_check!!)
            binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView2.textView.text = padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView3.textView.text = padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView5.textView.text = padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView6.textView.text = padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView8.textView.text = padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView9.textView.text = padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView11.textView.text = padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView12.textView.text = padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView14.textView.text = padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            binding.includeMainView15.textView.text = padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
            when (padCheck) {
                53 -> {
                    x53()
                }
                43 -> {
                    x43()
                }
                33 -> {
                    x33()
                }
                52 -> {
                    x52()
                }
                42 -> {
                    x42()
                }
                32 -> {
                    x32()
                }
                22 -> {
                    x22()
                }
                21 -> {
                    x21()
                }
                51 -> {
                    x51()
                }
                41 -> {
                    x41()
                }
                31 -> {
                    x31()
                }
            }
            if (colorCheck == 1) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                    findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                    findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                }
                else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                    findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                    findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                    findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                    findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                    findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                    findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                }
            } else {
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                }
                else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                    findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                }
            }
            try {
                sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound1 = soundPool.load(padText1, 1)
                    binding.includeMainView.textView.text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound1 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView.textView.text = ""
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound2 = soundPool.load(assets.openFd("$padText2.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound2 = soundPool.load(padText2, 1)
                    binding.includeMainView2.textView.text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound2 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView2.textView.text = ""
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound3 = soundPool.load(assets.openFd("$padText3.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound3 = soundPool.load(padText3, 1)
                    binding.includeMainView3.textView.text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound3 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView3.textView.text = ""
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound4 = soundPool.load(padText4, 1)
                    binding.includeMainView4.textView.text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound4 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView4.textView.text = ""
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound5 = soundPool.load(assets.openFd("$padText5.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound5 = soundPool.load(padText5, 1)
                    binding.includeMainView5.textView.text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound5 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView5.textView.text = ""
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound6 = soundPool.load(assets.openFd("$padText6.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound6 = soundPool.load(padText6, 1)
                    binding.includeMainView6.textView.text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound6 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView6.textView.text = ""
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound7 = soundPool.load(padText7, 1)
                    binding.includeMainView7.textView.text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound7 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView7.textView.text = ""
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound8 = soundPool.load(assets.openFd("$padText8.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound8 = soundPool.load(padText8, 1)
                    binding.includeMainView8.textView.text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound8 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView8.textView.text = ""
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound9 = soundPool.load(assets.openFd("$padText9.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound9 = soundPool.load(padText9, 1)
                    binding.includeMainView9.textView.text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound9 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView9.textView.text = ""
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound10 = soundPool.load(padText10, 1)
                    binding.includeMainView10.textView.text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound10 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView10.textView.text = ""
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound11 = soundPool.load(assets.openFd("$padText11.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound11 = soundPool.load(padText11, 1)
                    binding.includeMainView11.textView.text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound11 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView11.textView.text = ""
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound12 = soundPool.load(assets.openFd("$padText12.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound12 = soundPool.load(padText12, 1)
                    binding.includeMainView12.textView.text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound12 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView12.textView.text = ""
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound13 = soundPool.load(padText13, 1)
                    binding.includeMainView13.textView.text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound13 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView13.textView.text = ""
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound14 = soundPool.load(assets.openFd("$padText14.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound14 = soundPool.load(padText14, 1)
                    binding.includeMainView14.textView.text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound14 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView14.textView.text = ""
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
                }
            }
            try {
                sound15 = soundPool.load(assets.openFd("$padText15.ogg"), 1)
            } catch (e: Exception) {
                try {
                    sound15 = soundPool.load(padText15, 1)
                    binding.includeMainView15.textView.text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                } catch (e: Exception) {
                    sound15 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                    binding.includeMainView15.textView.text = ""
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                }
            }

        } else {
            Toast.makeText(applicationContext, R.string.empty, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val menuLamp = menu!!.findItem(R.id.menu1)
        val menuLamp2 = menu.findItem(R.id.menu10)
        val menuLampLock = menu.findItem(R.id.action_settings)
        if (menuSwitch) {
            menuLamp.setIcon(R.drawable.ic_baseline_play_arrow_24)
        } else {
            menuLamp.setIcon(R.drawable.ic_baseline_stop_24)
        }

        if (menuSwitchLock) {
            menuLampLock.setIcon(R.drawable.ic_baseline_tune_24)
            menuLamp.isVisible = true
            menuLamp2.isVisible = true
        } else {
            menuLampLock.setIcon(R.drawable.ic_baseline_lock_24)
            menuLamp.isVisible = false
            menuLamp2.isVisible = false
        }

        return true
    }

    private var menuSwitch = true
    private var menuSwitch2 = true
    private var menuSwitchLock = true
    private var switch1 = 0

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val soundListView = findViewById<ListView>(R.id.list_view)
        val actionGridView = findViewById<GridView>(R.id.grid_view)
        val chooseGridView = findViewById<GridView>(R.id.grid_view_choose)
        val tuningView = findViewById<View>(R.id.view)

        when (item.itemId) {

            R.id.menu1 -> {
                if (menuSwitchLock) {
                    when {
                        soundListView.isVisible -> {
                            soundListView.visibility = View.INVISIBLE
                        }
                        actionGridView.isVisible -> {
                            actionGridView.visibility = View.INVISIBLE
                        }
                        chooseGridView.isVisible -> {
                            chooseGridView.visibility = View.INVISIBLE
                        }
                    }
                    if (switch1 == 1) {
                        sequencerStop()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1 = 2
                    } else {
                        sequencerPlay()
                        changeSequence()
                        menuSwitch = false
                        invalidateOptionsMenu()
                        switch1 = 1
                    }
                }
                return true
            }

            R.id.menu10 -> {
                if (menuSwitchLock) {
                    when {
                        chooseGridView.isVisible -> {
                            actionGridView.visibility = View.INVISIBLE
                            chooseGridView.visibility = View.INVISIBLE
                            tuningView.visibility = View.INVISIBLE
                        }
                        soundListView.isVisible -> {
                            chooseGridView.visibility = View.VISIBLE
                            soundListView.visibility = View.INVISIBLE
                        }
                        actionGridView.isVisible -> {
                            chooseGridView.visibility = View.VISIBLE
                            actionGridView.visibility = View.INVISIBLE
                            tuningView.visibility = View.INVISIBLE
                        }
                        tuningView.isVisible -> {
                            chooseGridView.visibility = View.VISIBLE
                            actionGridView.visibility = View.INVISIBLE
                            tuningView.visibility = View.INVISIBLE
                        }
                        soundListView.isInvisible && actionGridView.isInvisible && tuningView.isInvisible -> {
                            chooseGridView.visibility = View.VISIBLE
                        }
                    }
                }
                return true
            }

            R.id.action_settings -> {
                if (menuSwitchLock) {
                    when {
                        chooseGridView.isVisible -> {
                            actionGridView.visibility = View.VISIBLE
                            chooseGridView.visibility = View.INVISIBLE
                            tuningView.visibility = View.INVISIBLE
                        }
                        soundListView.isVisible -> {
                            actionGridView.visibility = View.VISIBLE
                            soundListView.visibility = View.INVISIBLE
                        }
                        actionGridView.isInvisible && tuningView.isVisible -> {
                            tuningView.visibility = View.INVISIBLE
                        }
                        actionGridView.isInvisible -> {
                            actionGridView.visibility = View.VISIBLE
                        }
                        actionGridView.isVisible -> {
                            actionGridView.visibility = View.INVISIBLE
                            tuningView.visibility = View.INVISIBLE
                        }
                    }
                } else {
                    menuSwitchLock = true
                    invalidateOptionsMenu()
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        lmp.reset()
        lmp.release()
        mp.reset()
        mp.release()
        soundPool.autoPause()
        soundPool.release()
        sequencerCount = 0
        timer?.cancel()
        timer = null
        handler.removeCallbacks(runnable)
        noteCount = 0
        super.onDestroy()
        mRealm.close()
    }

    override fun onPause() {
        menuSwitch = true
        invalidateOptionsMenu()
        switch1 = 2
        handler.removeCallbacks(runnable)
        noteCount = 0
        sequencerStop()
        if (mp.isPlaying) {
            mp.stop()
            mp.prepare()
        }
        if (!menuSwitch2) {
            menuSwitch2 = true
            invalidateOptionsMenu()
        }

            lmp.stop()
            soundPool.autoPause()

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("DATA", adCheck)
        outState.putInt("padCheck", padCheck)
        outState.putInt("colorCheck", colorCheck)
        outState.putString("beatCheck", beatCheck)
        outState.putString("pad1", padText1.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad2", padText2.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad3", padText3.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad4", padText4.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad5", padText5.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad6", padText6.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad7", padText7.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad8", padText8.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad9", padText9.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad10", padText10.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad11", padText11.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad12", padText12.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad13", padText13.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad14", padText14.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("pad15", padText15.replace(" ", "_").replace("-", "_").lowercase())
        outState.putString("loop", actionTitle)
        outState.putFloat("spv1", soundPoolVolume)
        outState.putFloat("spv2", soundPoolVolume2)
        outState.putFloat("spv3", soundPoolVolume3)
        outState.putFloat("spv4", soundPoolVolume4)
        outState.putFloat("spv5", soundPoolVolume5)
        outState.putFloat("spv6", soundPoolVolume6)
        outState.putFloat("spv7", soundPoolVolume7)
        outState.putFloat("spv8", soundPoolVolume8)
        outState.putFloat("spv9", soundPoolVolume9)
        outState.putFloat("spv10", soundPoolVolume10)
        outState.putFloat("spv11", soundPoolVolume11)
        outState.putFloat("spv12", soundPoolVolume12)
        outState.putFloat("spv13", soundPoolVolume13)
        outState.putFloat("spv14", soundPoolVolume14)
        outState.putFloat("spv15", soundPoolVolume15)
        outState.putFloat("spt1", soundPoolTempo)
        outState.putFloat("spt2", soundPoolTempo2)
        outState.putFloat("spt3", soundPoolTempo3)
        outState.putFloat("spt4", soundPoolTempo4)
        outState.putFloat("spt5", soundPoolTempo5)
        outState.putFloat("spt6", soundPoolTempo6)
        outState.putFloat("spt7", soundPoolTempo7)
        outState.putFloat("spt8", soundPoolTempo8)
        outState.putFloat("spt9", soundPoolTempo9)
        outState.putFloat("spt10", soundPoolTempo10)
        outState.putFloat("spt11", soundPoolTempo11)
        outState.putFloat("spt12", soundPoolTempo12)
        outState.putFloat("spt13", soundPoolTempo13)
        outState.putFloat("spt14", soundPoolTempo14)
        outState.putFloat("spt15", soundPoolTempo15)
        outState.putInt("spvF1", spvF)
        outState.putInt("spvF2", spvF2)
        outState.putInt("spvF3", spvF3)
        outState.putInt("spvF4", spvF4)
        outState.putInt("spvF5", spvF5)
        outState.putInt("spvF6", spvF6)
        outState.putInt("spvF7", spvF7)
        outState.putInt("spvF8", spvF8)
        outState.putInt("spvF9", spvF9)
        outState.putInt("spvF10", spvF10)
        outState.putInt("spvF11", spvF11)
        outState.putInt("spvF12", spvF12)
        outState.putInt("spvF13", spvF13)
        outState.putInt("spvF14", spvF14)
        outState.putInt("spvF15", spvF15)
        outState.putInt("sptF1", sptF)
        outState.putInt("sptF2", sptF2)
        outState.putInt("sptF3", sptF3)
        outState.putInt("sptF4", sptF4)
        outState.putInt("sptF5", sptF5)
        outState.putInt("sptF6", sptF6)
        outState.putInt("sptF7", sptF7)
        outState.putInt("sptF8", sptF8)
        outState.putInt("sptF9", sptF9)
        outState.putInt("sptF10", sptF10)
        outState.putInt("sptF11", sptF11)
        outState.putInt("sptF12", sptF12)
        outState.putInt("sptF13", sptF13)
        outState.putInt("sptF14", sptF14)
        outState.putInt("sptF15", sptF15)
    }

    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adCheck = savedInstanceState.getInt("DATA")
        padCheck = savedInstanceState.getInt("padCheck")
        colorCheck = savedInstanceState.getInt("colorCheck")
        beatCheck = savedInstanceState.getString("beatCheck").toString()
        padText1 = savedInstanceState.getString("pad1").toString()
        padText2 = savedInstanceState.getString("pad2").toString()
        padText3 = savedInstanceState.getString("pad3").toString()
        padText4 = savedInstanceState.getString("pad4").toString()
        padText5 = savedInstanceState.getString("pad5").toString()
        padText6 = savedInstanceState.getString("pad6").toString()
        padText7 = savedInstanceState.getString("pad7").toString()
        padText8 = savedInstanceState.getString("pad8").toString()
        padText9 = savedInstanceState.getString("pad9").toString()
        padText10 = savedInstanceState.getString("pad10").toString()
        padText11 = savedInstanceState.getString("pad11").toString()
        padText12 = savedInstanceState.getString("pad12").toString()
        padText13 = savedInstanceState.getString("pad13").toString()
        padText14 = savedInstanceState.getString("pad14").toString()
        padText15 = savedInstanceState.getString("pad15").toString()
        actionTitle = savedInstanceState.getString("loop").toString()
        soundPoolVolume = savedInstanceState.getFloat("spv1")
        soundPoolVolume2 = savedInstanceState.getFloat("spv2")
        soundPoolVolume3 = savedInstanceState.getFloat("spv3")
        soundPoolVolume4 = savedInstanceState.getFloat("spv4")
        soundPoolVolume5 = savedInstanceState.getFloat("spv5")
        soundPoolVolume6 = savedInstanceState.getFloat("spv6")
        soundPoolVolume7 = savedInstanceState.getFloat("spv7")
        soundPoolVolume8 = savedInstanceState.getFloat("spv8")
        soundPoolVolume9 = savedInstanceState.getFloat("spv9")
        soundPoolVolume10 = savedInstanceState.getFloat("spv10")
        soundPoolVolume11 = savedInstanceState.getFloat("spv11")
        soundPoolVolume12 = savedInstanceState.getFloat("spv12")
        soundPoolVolume13 = savedInstanceState.getFloat("spv13")
        soundPoolVolume14 = savedInstanceState.getFloat("spv14")
        soundPoolVolume15 = savedInstanceState.getFloat("spv15")
        soundPoolTempo = savedInstanceState.getFloat("spt1")
        soundPoolTempo2 = savedInstanceState.getFloat("spt2")
        soundPoolTempo3 = savedInstanceState.getFloat("spt3")
        soundPoolTempo4 = savedInstanceState.getFloat("spt4")
        soundPoolTempo5 = savedInstanceState.getFloat("spt5")
        soundPoolTempo6 = savedInstanceState.getFloat("spt6")
        soundPoolTempo7 = savedInstanceState.getFloat("spt7")
        soundPoolTempo8 = savedInstanceState.getFloat("spt8")
        soundPoolTempo9 = savedInstanceState.getFloat("spt9")
        soundPoolTempo10 = savedInstanceState.getFloat("spt10")
        soundPoolTempo11 = savedInstanceState.getFloat("spt11")
        soundPoolTempo12 = savedInstanceState.getFloat("spt12")
        soundPoolTempo13 = savedInstanceState.getFloat("spt13")
        soundPoolTempo14 = savedInstanceState.getFloat("spt14")
        soundPoolTempo15 = savedInstanceState.getFloat("spt15")

        spvF = savedInstanceState.getInt("spvF1")
        spvF2 = savedInstanceState.getInt("spvF2")
        spvF3 = savedInstanceState.getInt("spvF3")
        spvF4 = savedInstanceState.getInt("spvF4")
        spvF5 = savedInstanceState.getInt("spvF5")
        spvF6 = savedInstanceState.getInt("spvF6")
        spvF7 = savedInstanceState.getInt("spvF7")
        spvF8 = savedInstanceState.getInt("spvF8")
        spvF9 = savedInstanceState.getInt("spvF9")
        spvF10 = savedInstanceState.getInt("spvF10")
        spvF11 = savedInstanceState.getInt("spvF11")
        spvF12 = savedInstanceState.getInt("spvF12")
        spvF13 = savedInstanceState.getInt("spvF13")
        spvF14 = savedInstanceState.getInt("spvF14")
        spvF15 = savedInstanceState.getInt("spvF15")
        sptF = savedInstanceState.getInt("sptF1")
        sptF2 = savedInstanceState.getInt("sptF2")
        sptF3 = savedInstanceState.getInt("sptF3")
        sptF4 = savedInstanceState.getInt("sptF4")
        sptF5 = savedInstanceState.getInt("sptF5")
        sptF6 = savedInstanceState.getInt("sptF6")
        sptF7 = savedInstanceState.getInt("sptF7")
        sptF8 = savedInstanceState.getInt("sptF8")
        sptF9 = savedInstanceState.getInt("sptF9")
        sptF10 = savedInstanceState.getInt("sptF10")
        sptF11 = savedInstanceState.getInt("sptF11")
        sptF12 = savedInstanceState.getInt("sptF12")
        sptF13 = savedInstanceState.getInt("sptF13")
        sptF14 = savedInstanceState.getInt("sptF14")
        sptF15 = savedInstanceState.getInt("sptF15")
        if (adCheck == 1) {
            binding.adView.visibility = View.GONE
            binding.topSpace.visibility = View.GONE
            binding.bottomSpace.visibility = View.GONE
        }
        binding.includeMainView.textView.text = padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView2.textView.text = padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView3.textView.text = padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView4.textView.text = padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView5.textView.text = padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView6.textView.text = padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView7.textView.text = padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView8.textView.text = padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView9.textView.text = padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView10.textView.text = padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView11.textView.text = padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView12.textView.text = padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView13.textView.text = padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView14.textView.text = padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        binding.includeMainView15.textView.text = padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = soundPoolVolume2.toString().replace("f", "") + "            " + soundPoolTempo2.toString().replace("f", "") + "\n" + padText2.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = soundPoolVolume3.toString().replace("f", "") + "            " + soundPoolTempo3.toString().replace("f", "") + "\n" + padText3.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = soundPoolVolume5.toString().replace("f", "") + "            " + soundPoolTempo5.toString().replace("f", "") + "\n" + padText5.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = soundPoolVolume6.toString().replace("f", "") + "            " + soundPoolTempo6.toString().replace("f", "") + "\n" + padText6.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = soundPoolVolume8.toString().replace("f", "") + "            " + soundPoolTempo8.toString().replace("f", "") + "\n" + padText8.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = soundPoolVolume9.toString().replace("f", "") + "            " + soundPoolTempo9.toString().replace("f", "") + "\n" + padText9.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = soundPoolVolume11.toString().replace("f", "") + "            " + soundPoolTempo11.toString().replace("f", "") + "\n" + padText11.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = soundPoolVolume12.toString().replace("f", "") + "            " + soundPoolTempo12.toString().replace("f", "") + "\n" + padText12.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = soundPoolVolume14.toString().replace("f", "") + "            " + soundPoolTempo14.toString().replace("f", "") + "\n" + padText14.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").uppercase()
        when (padCheck) {
            53 -> {
                x53()
            }
            43 -> {
                x43()
            }
            33 -> {
                x33()
            }
            52 -> {
                x52()
            }
            42 -> {
                x42()
            }
            32 -> {
                x32()
            }
            22 -> {
                x22()
            }
            21 -> {
                x21()
            }
            51 -> {
                x51()
            }
            41 -> {
                x41()
            }
            31 -> {
                x31()
            }
        }
        if (colorCheck == 1) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
            }
            else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple3)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple4)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple5)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple6)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple7)
            }
        } else {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
            }
            else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                findViewById<View>(R.id.include_main_view).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view2).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view3).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view4).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view5).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view6).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view7).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view8).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view9).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view10).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view11).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view12).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view13).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view14).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
                findViewById<View>(R.id.include_main_view15).findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.my_ripple)
            }
        }
        when(beatCheck) {
            "hiphop_1_bpm80" -> { hiphopSequence() }
            "reggaeton_1_bpm90" -> { reggaetonSequence() }
            "electronica_1_bpm90" -> { electronicaSequence() }
            "dubstep_1_bpm140" -> { dubstepSequence() }
            "house_1_bpm130" -> { houseSequence() }
            "disco_1_bpm110" -> { discoSequence() }
            "techno_1_bpm110" -> { technoSequence() }
            "eurobeat_1_bpm130" -> { eurobeatSequence() }
            "two_step_1_bpm100" -> { twostepSequence() }
            "drum_n_bass_1_bpm170" -> { drumnbassSequence() }
            "beat_1" -> { beat1Sequence() }
            "beat_2" -> { beat2Sequence() }
            "beat_3" -> { beat3Sequence() }
            "beat_5" -> { beat5Sequence() }
            "beat_6" -> { beat6Sequence() }
            "beat_7" -> { beat7Sequence() }
            "beat_8" -> { beat8Sequence() }
            "beat_9" -> { beat9Sequence() }

        }
        try {
            sound1 = soundPool.load(assets.openFd("$padText1.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound1 = soundPool.load(padText1, 1)
                binding.includeMainView.textView.text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = padText1.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound1 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView.textView.text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound2 = soundPool.load(assets.openFd("$padText2.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound2 = soundPool.load(padText2, 1)
                binding.includeMainView2.textView.text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = padText2.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound2 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView2.textView.text = ""
                findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound3 = soundPool.load(assets.openFd("$padText3.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound3 = soundPool.load(padText3, 1)
                binding.includeMainView3.textView.text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = padText3.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound3 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView3.textView.text = ""
                findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound4 = soundPool.load(assets.openFd("$padText4.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound4 = soundPool.load(padText4, 1)
                binding.includeMainView4.textView.text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = padText4.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound4 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView4.textView.text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound5 = soundPool.load(assets.openFd("$padText5.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound5 = soundPool.load(padText5, 1)
                binding.includeMainView5.textView.text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = padText5.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound5 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView5.textView.text = ""
                findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound6 = soundPool.load(assets.openFd("$padText6.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound6 = soundPool.load(padText6, 1)
                binding.includeMainView6.textView.text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = padText6.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound6 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView6.textView.text = ""
                findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound7 = soundPool.load(assets.openFd("$padText7.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound7 = soundPool.load(padText7, 1)
                binding.includeMainView7.textView.text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = padText7.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound7 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView7.textView.text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound8 = soundPool.load(assets.openFd("$padText8.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound8 = soundPool.load(padText8, 1)
                binding.includeMainView8.textView.text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = padText8.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound8 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView8.textView.text = ""
                findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound9 = soundPool.load(assets.openFd("$padText9.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound9 = soundPool.load(padText9, 1)
                binding.includeMainView9.textView.text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = padText9.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound9 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView9.textView.text = ""
                findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound10 = soundPool.load(assets.openFd("$padText10.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound10 = soundPool.load(padText10, 1)
                binding.includeMainView10.textView.text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = padText10.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound10 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView10.textView.text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound11 = soundPool.load(assets.openFd("$padText11.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound11 = soundPool.load(padText11, 1)
                binding.includeMainView11.textView.text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = padText11.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound11 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView11.textView.text = ""
                findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound12 = soundPool.load(assets.openFd("$padText12.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound12 = soundPool.load(padText12, 1)
                binding.includeMainView12.textView.text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = padText12.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound12 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView12.textView.text = ""
                findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound13 = soundPool.load(assets.openFd("$padText13.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound13 = soundPool.load(padText13, 1)
                binding.includeMainView13.textView.text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = padText13.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound13 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView13.textView.text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound14 = soundPool.load(assets.openFd("$padText14.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound14 = soundPool.load(padText14, 1)
                binding.includeMainView14.textView.text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = padText14.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound14 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView14.textView.text = ""
                findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            sound15 = soundPool.load(assets.openFd("$padText15.ogg"), 1)
        } catch (e: Exception) {
            try {
                sound15 = soundPool.load(padText15, 1)
                binding.includeMainView15.textView.text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = padText15.replaceBeforeLast("/", "").replace("/", "").replace(".ogg", "").uppercase()
            } catch (e: Exception) {
                sound15 = soundPool.load(assets.openFd("soundless.ogg"), 1)
                binding.includeMainView15.textView.text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
            }
        }
        try {
            lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://$packageName/raw/bpm120_bass_drum"))
            lmp.stop()
            supportActionBar?.title = actionTitle.replace("_", " ").uppercase()
        } catch (e: Exception) {
            try {
                lmp = LoopMediaPlayer(this@MainActivity, Uri.parse(actionTitle))
                lmp.stop()
                supportActionBar?.title = actionTitle.replaceBeforeLast("/", "").replace("/", "").replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase()
            } catch (e: Exception) {
                lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://$packageName/raw/soundless"))
                lmp.stop()
                supportActionBar?.title = ""
            }
        }
    }
}
