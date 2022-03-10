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
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import jp.chikaharu11.instant_drumpad_tr808.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.hypot


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

    private var actionTitle = "rimshot_01"
    private var padText1 = "tr_8_cymbal_01"
    private var padText2 = "open_hi_hat_01"
    private var padText3 = "clsd_hi_hat_01"
    private var padText4 = "cowbell_01b"
    private var padText5 = "low_tom_01"
    private var padText6 = "snare_drum_01"
    private var padText7 = "tr_8_rimshot_03"
    private var padText8 = "mid_tom_01"
    private var padText9 = "bass_drum_short_01"
    private var padText10 = "claves_02"
    private var padText11 = "high_tom_01"
    private var padText12 = "high_conga_01"
    private var padText13 = "tr_909_cymbal_01"
    private var padText14 = "tr_909_cymbal_02"
    private var padText15 = "clap_01"

    private var count = 0.5f
    private var bpm = 1.0f

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

    private val locale: Locale = Locale.getDefault()

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

    private var paste = 0

    private var buttonA = 0
    private var buttonB = 0

    private var adCheck = 0

    private var padCheck = 53

    private var colorCheck = 0


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "Range", "CutPasteId", "ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .apply { setContentView(this.root) }

        setSupportActionBar(findViewById(R.id.toolbar_main))

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
            if (colorCheck == 1) {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
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
                    Configuration.ORIENTATION_LANDSCAPE -> {
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
                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }
                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
                }
            } else {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
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
                    Configuration.ORIENTATION_LANDSCAPE -> {
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
                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }
                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
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

        val orientation = resources.configuration.orientation
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                findViewById<TextView>(R.id.padText0).text = "${actionTitle.uppercase()} loop"
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                findViewById<TextView>(R.id.padText0).text = "loop"
            }
            Configuration.ORIENTATION_SQUARE -> {
                TODO()
            }
            Configuration.ORIENTATION_UNDEFINED -> {
                TODO()
            }
        }

        val tuning = if (locale == Locale.JAPAN) {
            arrayOf(
                "Change Pad Sounds",
                "Random Pad Sounds",
                "Change Pad Colors",
                "Save Pad Settings",
                "Load Pad Settings",
                "Adjusting Sounds",
                "Hide banner Ads",
                "EXIT",
                "5x3","5x2","5x1",
                "4x3","4x2","4x1",
                "3x3","3x2","3x1",
                "2x2","2x1"
            ) } else {
            arrayOf(
                "Change Pad Sounds",
                "Random Pad Sounds",
                "Change Pad Colors",
                "Save Pad Settings",
                "Load Pad Settings",
                "Adjusting Sounds",
                "Hide banner Ads",
                "EXIT",
                "5x3","5x2","5x1",
                "4x3","4x2","4x1",
                "3x3","3x2","3x1",
                "2x2","2x1"
            )
            }
        val tuning2 = if (locale == Locale.JAPAN) {
            arrayOf(
                "Change to Play Mode",
                "Random Pad Sounds",
                "Change Pad Colors",
                "Save Pad Settings",
                "Load Pad Settings",
                "Adjusting Sounds",
                "Hide banner Ads",
                "EXIT",
                "5x3","5x2","5x1",
                "4x3","4x2","4x1",
                "3x3","3x2","3x1",
                "2x2","2x1"
            ) } else {
            arrayOf(
                "Change to Play Mode",
                "Random Pad Sounds",
                "Change Pad Colors",
                "Save Pad Settings",
                "Load Pad Settings",
                "Adjusting Sounds",
                "Hide banner Ads",
                "EXIT",
                "5x3","5x2","5x1",
                "4x3","4x2","4x1",
                "3x3","3x2","3x1",
                "2x2","2x1"
            )
        }
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown, tuning)
        val adapterA = ArrayAdapter(this, R.layout.custom_spinner_dropdown, tuning2)
        val gridView: GridView = findViewById(R.id.grid_view)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { adapterView, _, position, _ ->
            when(adapterView.getItemAtPosition(position)) {
                "Change Pad Sounds" -> {
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
                        when (orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> {
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
                            Configuration.ORIENTATION_LANDSCAPE -> {
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
                            Configuration.ORIENTATION_SQUARE -> {
                                TODO()
                            }
                            Configuration.ORIENTATION_UNDEFINED -> {
                                TODO()
                            }
                        }
                    } else {
                        when (orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> {
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
                            Configuration.ORIENTATION_LANDSCAPE -> {
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
                            Configuration.ORIENTATION_SQUARE -> {
                                TODO()
                            }
                            Configuration.ORIENTATION_UNDEFINED -> {
                                TODO()
                            }
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
                        val snackTextView: TextView = snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
                        snackTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        snackBar.setDuration(2000).show()
                        Handler().postDelayed({
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }, 2000)
                    } else {
                        update()
                        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        val snackBar = Snackbar.make(findViewById(R.id.snack_space) , R.string.Saved, Snackbar.LENGTH_LONG)
                        val snackTextView: TextView = snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
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
                        val snackTextView2: TextView = snackBar2.view.findViewById(com.google.android.material.R.id.snackbar_text)
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

        val choose = if (locale == Locale.JAPAN) {
            arrayOf(
                "Metronome Loops",
                "Sample Loops",
                "External sound Loops"
            ) } else {
            arrayOf(
                "Metronome Loops",
                "Sample Loops",
                "External sound Loops"
            )
        }
        val adapter2 = ArrayAdapter(this, R.layout.custom_spinner_dropdown, choose)
        val gridView2: GridView = findViewById(R.id.grid_view_choose)
        val soundListView = findViewById<ListView>(R.id.list_view)
        gridView2.adapter = adapter2

        gridView2.setOnItemClickListener { adapterView, _, position, _ ->
            when (adapterView.getItemAtPosition(position)) {
                "Metronome Loops" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    buttonA = 16
                    buttonB = 2
                    soundListView.adapter = nCustomAdapter
                    nCustomAdapter.notifyDataSetChanged()
                    soundListView.visibility = View.VISIBLE
                    gridView2.visibility = View.INVISIBLE
                }
                "Sample Loops" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    buttonA = 16
                    buttonB = 2
                    soundListView.adapter = oCustomAdapter
                    oCustomAdapter.notifyDataSetChanged()
                    soundListView.visibility = View.VISIBLE
                    gridView2.visibility = View.INVISIBLE
                }

                "External sound Loops" -> {
                    lmp.stop()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                    buttonA = 16
                    buttonB = 1
                    selectEX()
                    soundListView.adapter = tCustomAdapter
                    tCustomAdapter.notifyDataSetChanged()
                    soundListView.visibility = View.VISIBLE
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
            SoundList("loop_bd_85_01.ogg"),
            SoundList("loop_bd_85_02.ogg"),
            SoundList("loop_bd_85_03.ogg"),
            SoundList("loop_bd_85_04.ogg"),
            SoundList("loop_bd_85_05.ogg"),
            SoundList("loop_bd_85_06.ogg"),
            SoundList("loop_bd_85_07.ogg"),
            SoundList("loop_bd_85_08.ogg"),
            SoundList("loop_hats_85_01.ogg"),
            SoundList("loop_hats_85_02.ogg"),
            SoundList("loop_hats_85_03.ogg"),
            SoundList("loop_hats_85_04.ogg"),
            SoundList("loop_hats_85_05.ogg"),
            SoundList("loop_hats_85_06.ogg"),
            SoundList("loop_hats_85_07.ogg"),
            SoundList("loop_hats_85_08.ogg"),
            SoundList("loop_perc_85_01.ogg"),
            SoundList("loop_perc_85_02.ogg"),
            SoundList("loop_perc_85_03.ogg"),
            SoundList("loop_perc_85_04.ogg"),
            SoundList("loop_sd_85_01.ogg"),
            SoundList("loop_sd_85_02.ogg"),
            SoundList("loop_sd_85_03.ogg"),
            SoundList("loop_sd_85_04.ogg"),
            SoundList("loop_sd_85_05.ogg"),
            SoundList("loop_sd_85_06.ogg"),
            SoundList("loop_sd_85_07.ogg"),
            SoundList("loop_sd_85_08.ogg"),
            SoundList("loop_toms_85_01.ogg"),
            SoundList("loop_toms_85_02.ogg"),
            SoundList("loop_toms_85_03.ogg"),
            SoundList("loop_toms_85_04.ogg"),
                SoundList("loop_bd_95_01.ogg"),
                SoundList("loop_bd_95_02.ogg"),
                SoundList("loop_bd_95_03.ogg"),
                SoundList("loop_bd_95_04.ogg"),
                SoundList("loop_bd_95_05.ogg"),
                SoundList("loop_bd_95_06.ogg"),
                SoundList("loop_bd_95_07.ogg"),
                SoundList("loop_bd_95_08.ogg"),
                SoundList("loop_hats_95_01.ogg"),
                SoundList("loop_hats_95_02.ogg"),
                SoundList("loop_hats_95_03.ogg"),
                SoundList("loop_hats_95_04.ogg"),
                SoundList("loop_hats_95_05.ogg"),
                SoundList("loop_hats_95_06.ogg"),
                SoundList("loop_hats_95_07.ogg"),
                SoundList("loop_hats_95_08.ogg"),
                SoundList("loop_perc_95_01.ogg"),
                SoundList("loop_perc_95_02.ogg"),
                SoundList("loop_perc_95_03.ogg"),
                SoundList("loop_perc_95_04.ogg"),
                SoundList("loop_sd_95_01.ogg"),
                SoundList("loop_sd_95_02.ogg"),
                SoundList("loop_sd_95_03.ogg"),
                SoundList("loop_sd_95_04.ogg"),
                SoundList("loop_sd_95_05.ogg"),
                SoundList("loop_sd_95_06.ogg"),
                SoundList("loop_sd_95_07.ogg"),
                SoundList("loop_sd_95_08.ogg"),
                SoundList("loop_toms_95_01.ogg"),
                SoundList("loop_toms_95_02.ogg"),
                SoundList("loop_toms_95_03.ogg"),
                SoundList("loop_toms_95_04.ogg"),
            SoundList("loop_bd_105_01.ogg"),
            SoundList("loop_bd_105_02.ogg"),
            SoundList("loop_bd_105_03.ogg"),
            SoundList("loop_bd_105_04.ogg"),
            SoundList("loop_bd_105_05.ogg"),
            SoundList("loop_bd_105_06.ogg"),
            SoundList("loop_bd_105_07.ogg"),
            SoundList("loop_bd_105_08.ogg"),
            SoundList("loop_hats_105_01.ogg"),
            SoundList("loop_hats_105_02.ogg"),
            SoundList("loop_hats_105_03.ogg"),
            SoundList("loop_hats_105_04.ogg"),
            SoundList("loop_hats_105_05.ogg"),
            SoundList("loop_hats_105_06.ogg"),
            SoundList("loop_hats_105_07.ogg"),
            SoundList("loop_hats_105_08.ogg"),
            SoundList("loop_perc_105_01.ogg"),
            SoundList("loop_perc_105_02.ogg"),
            SoundList("loop_perc_105_03.ogg"),
            SoundList("loop_perc_105_04.ogg"),
            SoundList("loop_sd_105_01.ogg"),
            SoundList("loop_sd_105_02.ogg"),
            SoundList("loop_sd_105_03.ogg"),
            SoundList("loop_sd_105_04.ogg"),
            SoundList("loop_sd_105_05.ogg"),
            SoundList("loop_sd_105_06.ogg"),
            SoundList("loop_sd_105_07.ogg"),
            SoundList("loop_sd_105_08.ogg"),
            SoundList("loop_toms_105_01.ogg"),
            SoundList("loop_toms_105_02.ogg"),
            SoundList("loop_toms_105_03.ogg"),
            SoundList("loop_toms_105_04.ogg"),
            SoundList("loop_bd_124_01.ogg"),
            SoundList("loop_bd_124_02.ogg"),
            SoundList("loop_bd_124_03.ogg"),
            SoundList("loop_bd_124_04.ogg"),
            SoundList("loop_bd_124_05.ogg"),
            SoundList("loop_bd_124_06.ogg"),
            SoundList("loop_bd_124_07.ogg"),
            SoundList("loop_bd_124_08.ogg"),
            SoundList("loop_hats_124_01.ogg"),
            SoundList("loop_hats_124_02.ogg"),
            SoundList("loop_hats_124_03.ogg"),
            SoundList("loop_hats_124_04.ogg"),
            SoundList("loop_hats_124_05.ogg"),
            SoundList("loop_hats_124_06.ogg"),
            SoundList("loop_hats_124_07.ogg"),
            SoundList("loop_hats_124_08.ogg"),
            SoundList("loop_perc_124_01.ogg"),
            SoundList("loop_perc_124_02.ogg"),
            SoundList("loop_perc_124_03.ogg"),
            SoundList("loop_perc_124_04.ogg"),
            SoundList("loop_sd_124_01.ogg"),
            SoundList("loop_sd_124_02.ogg"),
            SoundList("loop_sd_124_03.ogg"),
            SoundList("loop_sd_124_04.ogg"),
            SoundList("loop_sd_124_05.ogg"),
            SoundList("loop_sd_124_06.ogg"),
            SoundList("loop_sd_124_07.ogg"),
            SoundList("loop_toms_124_01.ogg"),
            SoundList("loop_toms_124_02.ogg"),
            SoundList("loop_toms_124_03.ogg"),
            SoundList("loop_toms_124_04.ogg"),
            SoundList("loop_bd_132_01.ogg"),
            SoundList("loop_bd_132_02.ogg"),
            SoundList("loop_bd_132_03.ogg"),
            SoundList("loop_bd_132_04.ogg"),
            SoundList("loop_bd_132_05.ogg"),
            SoundList("loop_bd_132_06.ogg"),
            SoundList("loop_bd_132_07.ogg"),
            SoundList("loop_bd_132_08.ogg"),
            SoundList("loop_hats_132_01.ogg"),
            SoundList("loop_hats_132_02.ogg"),
            SoundList("loop_hats_132_03.ogg"),
            SoundList("loop_hats_132_04.ogg"),
            SoundList("loop_hats_132_05.ogg"),
            SoundList("loop_hats_132_06.ogg"),
            SoundList("loop_hats_132_07.ogg"),
            SoundList("loop_hats_132_08.ogg"),
            SoundList("loop_perc_132_01.ogg"),
            SoundList("loop_perc_132_02.ogg"),
            SoundList("loop_perc_132_03.ogg"),
            SoundList("loop_perc_132_04.ogg"),
            SoundList("loop_sd_132_01.ogg"),
            SoundList("loop_sd_132_02.ogg"),
            SoundList("loop_sd_132_03.ogg"),
            SoundList("loop_sd_132_04.ogg"),
            SoundList("loop_sd_132_05.ogg"),
            SoundList("loop_sd_132_06.ogg"),
            SoundList("loop_sd_132_07.ogg"),
            SoundList("loop_sd_132_08.ogg"),
            SoundList("loop_toms_132_01.ogg"),
            SoundList("loop_toms_132_02.ogg"),
            SoundList("loop_toms_132_03.ogg"),
            SoundList("loop_toms_132_04.ogg")
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

        supportActionBar?.title = "${actionTitle.uppercase()} loop"


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
                        buttonB = 2
                        soundListView.adapter = oCustomAdapter
                        oCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    13 -> {
                        buttonB = 1
                        soundListView.adapter = sCustomAdapter
                        sCustomAdapter.notifyDataSetChanged()
                        soundListView.visibility = View.VISIBLE
                    }
                    14 -> {
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

        lmp = LoopMediaPlayer.create(this, Uri.parse("android.resource://" + packageName + "/raw/" + R.raw.rimshot_01))

        lmp.stop()


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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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
                event.action == MotionEvent.ACTION_DOWN -> {
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

        findViewById<ImageButton>(R.id.volume_minus0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            lmp.volumeMinus()
            if (count > 0.1f) {
                count -= 0.1f
                findViewById<Button>(R.id.loop).text = ""
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    count = "%.1f".format(count).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + " " + actionTitle + " " + bpm.toString().replace("f", "").uppercase()
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    count = "%.1f".format(count).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + "\n\n" + "loop" + "\n\n" + bpm.toString().replace("f", "").uppercase()
                }
            }
                }
            }
            false
        }
        findViewById<ImageButton>(R.id.volume_plus0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            lmp.volumePlus()
            if (count < 1.0f) {
                count += 0.1f
                findViewById<Button>(R.id.loop).text = ""
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    count = "%.1f".format(count).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + " " + actionTitle + " " + bpm.toString().replace("f", "").uppercase()
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    count = "%.1f".format(count).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + "\n\n" + "loop" + "\n\n" + bpm.toString().replace("f", "").uppercase()
                }
            }
                }
            }
            false
        }
        findViewById<ImageButton>(R.id.tempo_minus0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            lmp.speedDown()
            if (bpm > 0.1f) {
                bpm -= 0.1f
                findViewById<Button>(R.id.loop).text = ""
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    bpm = "%.1f".format(bpm).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + " " + actionTitle + " " + bpm.toString().replace("f", "").uppercase()
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    bpm = "%.1f".format(bpm).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + "\n\n" + "loop" + "\n\n" + bpm.toString().replace("f", "").uppercase()
                }
                menuSwitch = false
                invalidateOptionsMenu()
                switch1 = 1
            }
                }
            }
            false
        }
        findViewById<ImageButton>(R.id.tempo_plus0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
            lmp.speedUp()
            if (bpm < 6.0f) {
                bpm += 0.1f
                findViewById<Button>(R.id.loop).text = ""
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    bpm = "%.1f".format(bpm).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + " " + actionTitle + " " + bpm.toString().replace("f", "").uppercase()
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    bpm = "%.1f".format(bpm).toFloat()
                    findViewById<TextView>(R.id.padText0).text = count.toString().replace("f", "") + "\n\n" + "loop" + "\n\n" + bpm.toString().replace("f", "").uppercase()
                }
                menuSwitch = false
                invalidateOptionsMenu()
                switch1 = 1
            }
                }
            }
            false
        }

        findViewById<Button>(R.id.loop).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (switch1 == 1) {
                        lmp.stop()
                        soundPool.autoPause()
                        menuSwitch = true
                        invalidateOptionsMenu()
                        switch1 = 2
                    } else {
                        lmp.start()
                        menuSwitch = false
                        invalidateOptionsMenu()
                        switch1 = 1
                    }
                }
            }
            false
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
                    if (soundPoolVolume > 0.1f) {
                        soundPoolVolume -= 0.1f
                        soundPoolVolume = "%.1f".format(soundPoolVolume).toFloat()
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
            if (soundPoolVolume < 1.0f) {
                soundPoolVolume += 0.1f
                soundPoolVolume = "%.1f".format(soundPoolVolume).toFloat()
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
            if (soundPoolTempo > 0.2f) {
                soundPoolTempo -= 0.1f
                soundPoolTempo = "%.1f".format(soundPoolTempo).toFloat()
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo == 0.2f) {
                soundPoolTempo = 0.125f
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
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = soundPoolVolume.toString().replace("f", "") + "            " + soundPoolTempo.toString().replace("f", "") + "\n" + padText1.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo < 8.0f) {
                soundPoolTempo += 0.1f
                soundPoolTempo = "%.1f".format(soundPoolTempo).toFloat()
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
            if (soundPoolVolume2 > 0.1f) {
                soundPoolVolume2 -= 0.1f
                soundPoolVolume2 = "%.1f".format(soundPoolVolume2).toFloat()
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
            if (soundPoolVolume2 < 1.0f) {
                soundPoolVolume2 += 0.1f
                soundPoolVolume2 = "%.1f".format(soundPoolVolume2).toFloat()
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
            if (soundPoolTempo2 > 0.2f) {
                soundPoolTempo2 -= 0.1f
                soundPoolTempo2 = "%.1f".format(soundPoolTempo2).toFloat()
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
            } else if (soundPoolTempo2 < 8.0f) {
                soundPoolTempo2 += 0.1f
                soundPoolTempo2 = "%.1f".format(soundPoolTempo2).toFloat()
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
            if (soundPoolVolume3 > 0.1f) {
                soundPoolVolume3 -= 0.1f
                soundPoolVolume3 = "%.1f".format(soundPoolVolume3).toFloat()
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
            if (soundPoolVolume3 < 1.0f) {
                soundPoolVolume3 += 0.1f
                soundPoolVolume3 = "%.1f".format(soundPoolVolume3).toFloat()
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
            if (soundPoolTempo3 > 0.2f) {
                soundPoolTempo3 -= 0.1f
                soundPoolTempo3 = "%.1f".format(soundPoolTempo3).toFloat()
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
            } else if (soundPoolTempo3 < 8.0f) {
                soundPoolTempo3 += 0.1f
                soundPoolTempo3 = "%.1f".format(soundPoolTempo3).toFloat()
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
            if (soundPoolVolume4 > 0.1f) {
                soundPoolVolume4 -= 0.1f
                soundPoolVolume4 = "%.1f".format(soundPoolVolume4).toFloat()
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
            if (soundPoolVolume4 < 1.0f) {
                soundPoolVolume4 += 0.1f
                soundPoolVolume4 = "%.1f".format(soundPoolVolume4).toFloat()
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
            if (soundPoolTempo4 > 0.2f) {
                soundPoolTempo4 -= 0.1f
                soundPoolTempo4 = "%.1f".format(soundPoolTempo4).toFloat()
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo4 == 0.2f) {
                soundPoolTempo4 = 0.125f
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
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = soundPoolVolume4.toString().replace("f", "") + "            " + soundPoolTempo4.toString().replace("f", "") + "\n" + padText4.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo4 < 8.0f) {
                soundPoolTempo4 += 0.1f
                soundPoolTempo4 = "%.1f".format(soundPoolTempo4).toFloat()
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
            if (soundPoolVolume5 > 0.1f) {
                soundPoolVolume5 -= 0.1f
                soundPoolVolume5 = "%.1f".format(soundPoolVolume5).toFloat()
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
            if (soundPoolVolume5 < 1.0f) {
                soundPoolVolume5 += 0.1f
                soundPoolVolume5 = "%.1f".format(soundPoolVolume5).toFloat()
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
            if (soundPoolTempo5 > 0.2f) {
                soundPoolTempo5 -= 0.1f
                soundPoolTempo5 = "%.1f".format(soundPoolTempo5).toFloat()
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
            } else if (soundPoolTempo5 < 8.0f) {
                soundPoolTempo5 += 0.1f
                soundPoolTempo5 = "%.1f".format(soundPoolTempo5).toFloat()
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
            if (soundPoolVolume6 > 0.1f) {
                soundPoolVolume6 -= 0.1f
                soundPoolVolume6 = "%.1f".format(soundPoolVolume6).toFloat()
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
            if (soundPoolVolume6 < 1.0f) {
                soundPoolVolume6 += 0.1f
                soundPoolVolume6 = "%.1f".format(soundPoolVolume6).toFloat()
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
            if (soundPoolTempo6 > 0.2f) {
                soundPoolTempo6 -= 0.1f
                soundPoolTempo6 = "%.1f".format(soundPoolTempo6).toFloat()
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
            } else if (soundPoolTempo6 < 8.0f) {
                soundPoolTempo6 += 0.1f
                soundPoolTempo6 = "%.1f".format(soundPoolTempo6).toFloat()
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
            if (soundPoolVolume7 > 0.1f) {
                soundPoolVolume7 -= 0.1f
                soundPoolVolume7 = "%.1f".format(soundPoolVolume7).toFloat()
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
            if (soundPoolVolume7 < 1.0f) {
                soundPoolVolume7 += 0.1f
                soundPoolVolume7 = "%.1f".format(soundPoolVolume7).toFloat()
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
            if (soundPoolTempo7 > 0.2f) {
                soundPoolTempo7 -= 0.1f
                soundPoolTempo7 = "%.1f".format(soundPoolTempo7).toFloat()
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo7 == 0.2f) {
                soundPoolTempo7 = 0.125f
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
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = soundPoolVolume7.toString().replace("f", "") + "            " + soundPoolTempo7.toString().replace("f", "") + "\n" + padText7.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo7 < 8.0f) {
                soundPoolTempo7 += 0.1f
                soundPoolTempo7 = "%.1f".format(soundPoolTempo7).toFloat()
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
            if (soundPoolVolume8 > 0.1f) {
                soundPoolVolume8 -= 0.1f
                soundPoolVolume8 = "%.1f".format(soundPoolVolume8).toFloat()
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
            if (soundPoolVolume8 < 1.0f) {
                soundPoolVolume8 += 0.1f
                soundPoolVolume8 = "%.1f".format(soundPoolVolume8).toFloat()
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
            if (soundPoolTempo8 > 0.2f) {
                soundPoolTempo8 -= 0.1f
                soundPoolTempo8 = "%.1f".format(soundPoolTempo8).toFloat()
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
            } else if (soundPoolTempo8 < 8.0f) {
                soundPoolTempo8 += 0.1f
                soundPoolTempo8 = "%.1f".format(soundPoolTempo8).toFloat()
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
            if (soundPoolVolume9 > 0.1f) {
                soundPoolVolume9 -= 0.1f
                soundPoolVolume9 = "%.1f".format(soundPoolVolume9).toFloat()
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
            if (soundPoolVolume9 < 1.0f) {
                soundPoolVolume9 += 0.1f
                soundPoolVolume9 = "%.1f".format(soundPoolVolume9).toFloat()
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
            if (soundPoolTempo9 > 0.2f) {
                soundPoolTempo9 -= 0.1f
                soundPoolTempo9 = "%.1f".format(soundPoolTempo9).toFloat()
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
            } else if (soundPoolTempo9 < 8.0f) {
                soundPoolTempo9 += 0.1f
                soundPoolTempo9 = "%.1f".format(soundPoolTempo9).toFloat()
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
            if (soundPoolVolume10 > 0.1f) {
                soundPoolVolume10 -= 0.1f
                soundPoolVolume10 = "%.1f".format(soundPoolVolume10).toFloat()
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
            if (soundPoolVolume10 < 1.0f) {
                soundPoolVolume10 += 0.1f
                soundPoolVolume10 = "%.1f".format(soundPoolVolume10).toFloat()
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
            if (soundPoolTempo10 > 0.2f) {
                soundPoolTempo10 -= 0.1f
                soundPoolTempo10 = "%.1f".format(soundPoolTempo10).toFloat()
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo10 == 0.2f) {
                soundPoolTempo10 = 0.125f
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
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = soundPoolVolume10.toString().replace("f", "") + "            " + soundPoolTempo10.toString().replace("f", "") + "\n" + padText10.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo10 < 8.0f) {
                soundPoolTempo10 += 0.1f
                soundPoolTempo10 = "%.1f".format(soundPoolTempo10).toFloat()
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
            if (soundPoolVolume11 > 0.1f) {
                soundPoolVolume11 -= 0.1f
                soundPoolVolume11 = "%.1f".format(soundPoolVolume11).toFloat()
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
            if (soundPoolVolume11 < 1.0f) {
                soundPoolVolume11 += 0.1f
                soundPoolVolume11 = "%.1f".format(soundPoolVolume11).toFloat()
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
            if (soundPoolTempo11 > 0.2f) {
                soundPoolTempo11 -= 0.1f
                soundPoolTempo11 = "%.1f".format(soundPoolTempo11).toFloat()
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
            } else if (soundPoolTempo11 < 8.0f) {
                soundPoolTempo11 += 0.1f
                soundPoolTempo11 = "%.1f".format(soundPoolTempo11).toFloat()
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
            if (soundPoolVolume12 > 0.1f) {
                soundPoolVolume12 -= 0.1f
                soundPoolVolume12 = "%.1f".format(soundPoolVolume12).toFloat()
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
            if (soundPoolVolume12 < 1.0f) {
                soundPoolVolume12 += 0.1f
                soundPoolVolume12 = "%.1f".format(soundPoolVolume12).toFloat()
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
            if (soundPoolTempo12 > 0.2f) {
                soundPoolTempo12 -= 0.1f
                soundPoolTempo12 = "%.1f".format(soundPoolTempo12).toFloat()
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
            } else if (soundPoolTempo12 < 8.0f) {
                soundPoolTempo12 += 0.1f
                soundPoolTempo12 = "%.1f".format(soundPoolTempo12).toFloat()
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
            if (soundPoolVolume13 > 0.1f) {
                soundPoolVolume13 -= 0.1f
                soundPoolVolume13 = "%.1f".format(soundPoolVolume13).toFloat()
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
            if (soundPoolVolume13 < 1.0f) {
                soundPoolVolume13 += 0.1f
                soundPoolVolume13 = "%.1f".format(soundPoolVolume13).toFloat()
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
            if (soundPoolTempo13 > 0.2f) {
                soundPoolTempo13 -= 0.1f
                soundPoolTempo13 = "%.1f".format(soundPoolTempo13).toFloat()
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo13 == 0.2f) {
                soundPoolTempo13 = 0.125f
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
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = soundPoolVolume13.toString().replace("f", "") + "            " + soundPoolTempo13.toString().replace("f", "") + "\n" + padText13.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            } else if (soundPoolTempo13 < 8.0f) {
                soundPoolTempo13 += 0.1f
                soundPoolTempo13 = "%.1f".format(soundPoolTempo13).toFloat()
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
            if (soundPoolVolume14 > 0.1f) {
                soundPoolVolume14 -= 0.1f
                soundPoolVolume14 = "%.1f".format(soundPoolVolume14).toFloat()
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
            if (soundPoolVolume14 < 1.0f) {
                soundPoolVolume14 += 0.1f
                soundPoolVolume14 = "%.1f".format(soundPoolVolume14).toFloat()
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
            if (soundPoolTempo14 > 0.2f) {
                soundPoolTempo14 -= 0.1f
                soundPoolTempo14 = "%.1f".format(soundPoolTempo14).toFloat()
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
            } else if (soundPoolTempo14 < 8.0f) {
                soundPoolTempo14 += 0.1f
                soundPoolTempo14 = "%.1f".format(soundPoolTempo14).toFloat()
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
            if (soundPoolVolume15 > 0.1f) {
                soundPoolVolume15 -= 0.1f
                soundPoolVolume15 = "%.1f".format(soundPoolVolume15).toFloat()
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
            if (soundPoolVolume15 < 1.0f) {
                soundPoolVolume15 += 0.1f
                soundPoolVolume15 = "%.1f".format(soundPoolVolume15).toFloat()
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
            if (soundPoolTempo15 > 0.2f) {
                soundPoolTempo15 -= 0.1f
                soundPoolTempo15 = "%.1f".format(soundPoolTempo15).toFloat()
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
            } else if (soundPoolTempo15 < 8.0f) {
                soundPoolTempo15 += 0.1f
                soundPoolTempo15 = "%.1f".format(soundPoolTempo15).toFloat()
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = ""
                findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = soundPoolVolume15.toString().replace("f", "") + "            " + soundPoolTempo15.toString().replace("f", "") + "\n" + padText15.replaceBeforeLast("/", "").replace("/", "").replace("tr_8", "TR-8").replace("tr_909", "TR-909").replace("_"," ").replace(".ogg", "").uppercase()
            }
            soundPool.play(sound15, soundPoolVolume15, soundPoolVolume15, 1, 0, soundPoolTempo15)
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

    @SuppressLint("SetTextI18n")
    override fun clicked2(soundList: SoundList) {
        try {
            when {
                buttonA == 1 && buttonB == 1 -> {
                    effect(binding.includeMainView.imageView,800)
                    sound1 = soundPool.load(soundList.name, 1)
                    println(soundList.name)
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
                    count = 0.5f
                    bpm = 1.0f
                    actionTitle = soundList.name.replaceBeforeLast("/", "").replace("/", "")
                        .replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase() + " loop"
                    supportActionBar?.title = actionTitle
                    soundPool.setOnLoadCompleteListener{ soundPool, _, _ ->
                        soundPool.stop(soundPool.play(sound16, 1.0f, 1.0f, 0, 0, 1.0f))
                    }
                }
                buttonA == 1 && buttonB == 2 -> {
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
                    padText1 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume = 0.5f
                    soundPoolTempo = 1.0f
                    findViewById<View>(R.id.include_view).findViewById<TextView>(R.id.padText).text = padText1
                }
                buttonA == 2 && buttonB == 2 -> {
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
                    padText2 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume2 = 0.5f
                    soundPoolTempo2 = 1.0f
                    findViewById<View>(R.id.include_view2).findViewById<TextView>(R.id.padText).text = padText2
                }
                buttonA == 3 && buttonB == 2 -> {
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
                    padText3 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume3 = 0.5f
                    soundPoolTempo3 = 1.0f
                    findViewById<View>(R.id.include_view3).findViewById<TextView>(R.id.padText).text = padText3
                }
                buttonA == 4 && buttonB == 2 -> {
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
                    padText4 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume4 = 0.5f
                    soundPoolTempo4 = 1.0f
                    findViewById<View>(R.id.include_view4).findViewById<TextView>(R.id.padText).text = padText4
                }
                buttonA == 5 && buttonB == 2 -> {
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
                    padText5 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume5 = 0.5f
                    soundPoolTempo5 = 1.0f
                    findViewById<View>(R.id.include_view5).findViewById<TextView>(R.id.padText).text = padText5
                }
                buttonA == 6 && buttonB == 2 -> {
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
                    padText6 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume6 = 0.5f
                    soundPoolTempo6 = 1.0f
                    findViewById<View>(R.id.include_view6).findViewById<TextView>(R.id.padText).text = padText6
                }
                buttonA == 7 && buttonB == 2 -> {
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
                    padText7 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume7 = 0.5f
                    soundPoolTempo7 = 1.0f
                    findViewById<View>(R.id.include_view7).findViewById<TextView>(R.id.padText).text = padText7
                }
                buttonA == 8 && buttonB == 2 -> {
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
                    padText8 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume8 = 0.5f
                    soundPoolTempo8 = 1.0f
                    findViewById<View>(R.id.include_view8).findViewById<TextView>(R.id.padText).text = padText8
                }
                buttonA == 9 && buttonB == 2 -> {
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
                    padText9 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume9 = 0.5f
                    soundPoolTempo9 = 1.0f
                    findViewById<View>(R.id.include_view9).findViewById<TextView>(R.id.padText).text = padText9
                }
                buttonA == 10 && buttonB == 2 -> {
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
                    padText10 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume10 = 0.5f
                    soundPoolTempo10 = 1.0f
                    findViewById<View>(R.id.include_view10).findViewById<TextView>(R.id.padText).text = padText10
                }
                buttonA == 11 && buttonB == 2 -> {
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
                    padText11 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume11 = 0.5f
                    soundPoolTempo11 = 1.0f
                    findViewById<View>(R.id.include_view11).findViewById<TextView>(R.id.padText).text = padText11
                }
                buttonA == 12 && buttonB == 2 -> {
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
                    padText12 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume12 = 0.5f
                    soundPoolTempo12 = 1.0f
                    findViewById<View>(R.id.include_view12).findViewById<TextView>(R.id.padText).text = padText12
                }
                buttonA == 13 && buttonB == 2 -> {
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
                    padText13 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume13 = 0.5f
                    soundPoolTempo13 = 1.0f
                    findViewById<View>(R.id.include_view13).findViewById<TextView>(R.id.padText).text = padText13
                }
                buttonA == 14 && buttonB == 2 -> {
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
                    padText14 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume14 = 0.5f
                    soundPoolTempo14 = 1.0f
                    findViewById<View>(R.id.include_view14).findViewById<TextView>(R.id.padText).text = padText14
                }
                buttonA == 15 && buttonB == 2 -> {
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
                    padText15 = soundList.name.replace("tr_8", "TR-8").replace("tr_909", "TR-909")
                        .replaceAfterLast(".", "").replace("_", " ").replace(".","").uppercase()
                    soundPoolVolume15 = 0.5f
                    soundPoolTempo15 = 1.0f
                    findViewById<View>(R.id.include_view15).findViewById<TextView>(R.id.padText).text = padText15
                }
                buttonA == 16 -> {
                    lmp.release()
                    lmp = LoopMediaPlayer(this@MainActivity, Uri.parse("android.resource://" + packageName + "/raw/" + soundList.name.replace(".ogg", "")))
                    lmp.stop()
                    count = 0.5f
                    bpm = 1.0f
                    actionTitle = soundList.name.replaceAfterLast(".", "").replace("_", " ").replace("."," ").uppercase() + " loop"
                    supportActionBar?.title = actionTitle
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
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    findViewById<TextView>(R.id.padText0).text = "${actionTitle.uppercase()} loop"
                }
                Configuration.ORIENTATION_LANDSCAPE -> {
                    findViewById<TextView>(R.id.padText0).text = "loop"
                }
                Configuration.ORIENTATION_SQUARE -> {
                    TODO()
                }
                Configuration.ORIENTATION_UNDEFINED -> {
                    TODO()
                }
            }
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
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
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
                    Configuration.ORIENTATION_LANDSCAPE -> {
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
                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }
                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
                }
            } else {
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
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
                    Configuration.ORIENTATION_LANDSCAPE -> {
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
                    Configuration.ORIENTATION_SQUARE -> {
                        TODO()
                    }
                    Configuration.ORIENTATION_UNDEFINED -> {
                        TODO()
                    }
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
        if (menuSwitch) {
            menuLamp.setIcon(R.drawable.ic_baseline_play_arrow_24)
        } else {
            menuLamp.setIcon(R.drawable.ic_baseline_stop_24)
        }

        return true
    }

    private var menuSwitch = true
    private var menuSwitch2 = true
    private var switch1 = 0


    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val soundListView = findViewById<ListView>(R.id.list_view)
        val actionGridView = findViewById<GridView>(R.id.grid_view)
        val chooseGridView = findViewById<GridView>(R.id.grid_view_choose)
        val tuningView = findViewById<View>(R.id.view)

        when (item.itemId) {

            R.id.menu1 -> {
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
                    lmp.stop()
                    soundPool.autoPause()
                    menuSwitch = true
                    invalidateOptionsMenu()
                    switch1 = 2
                } else {
                    lmp.start()
                    menuSwitch = false
                    invalidateOptionsMenu()
                    switch1 = 1
                }
                return true
            }

            R.id.menu10 -> {
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
                return true
            }

            R.id.action_settings -> {
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

        super.onDestroy()
        mRealm.close()
    }

    override fun onPause() {
        menuSwitch = true
        invalidateOptionsMenu()
        switch1 = 2
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
    }

    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adCheck = savedInstanceState.getInt("DATA")
        padCheck = savedInstanceState.getInt("padCheck")
        colorCheck = savedInstanceState.getInt("colorCheck")
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
        println(padText1)
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
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                findViewById<TextView>(R.id.padText0).text = "${actionTitle.uppercase()} loop"
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                findViewById<TextView>(R.id.padText0).text = "loop"
            }
            Configuration.ORIENTATION_SQUARE -> {
                TODO()
            }
            Configuration.ORIENTATION_UNDEFINED -> {
                TODO()
            }
        }
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
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
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
                Configuration.ORIENTATION_LANDSCAPE -> {
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
                Configuration.ORIENTATION_SQUARE -> {
                    TODO()
                }
                Configuration.ORIENTATION_UNDEFINED -> {
                    TODO()
                }
            }
        } else {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
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
                Configuration.ORIENTATION_LANDSCAPE -> {
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
                Configuration.ORIENTATION_SQUARE -> {
                    TODO()
                }
                Configuration.ORIENTATION_UNDEFINED -> {
                    TODO()
                }
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
    }
}
