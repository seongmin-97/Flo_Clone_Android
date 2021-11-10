package com.example.flo_clone

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    var song = Song("라일락", "아이유(IU)", 0,217, false, "")
    private var mediaPlayer : MediaPlayer? = null
    private var gson : Gson = Gson()

    private lateinit var player:Player
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.second = intent.getIntExtra("second", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            song.music = intent.getStringExtra("music")!!

            // 리소스 이름으로 리소스 찾기 : resources.getIdentifier
            val music = resources.getIdentifier(song.music, "raw", this.packageName)

            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)

            mediaPlayer = MediaPlayer.create(this, music)
        }

        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.songBackButtonIv.setOnClickListener {
            finish()
        }

        binding.songPlayButtonIv.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()
        }

        binding.songPauseButtonIv.setOnClickListener {
            setPlayerStatus(false)
            player.isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause()
        }

        binding.songRepeatButtonIv.setOnClickListener {
            setRepeatStatus(0)
        }

        binding.songRepeatButtonOnIv.setOnClickListener {
            setRepeatStatus(1)
        }

        binding.songRepeatButtonOn1Iv.setOnClickListener {
            setRepeatStatus(2)
        }

        binding.songRandomButtonIv.setOnClickListener {
            setRandomStatus(true)
        }

        binding.songRandomOnButtonIv.setOnClickListener {
            setRandomStatus(false)
        }

        binding.songHeartButtonIv.setOnClickListener {
            setHeartStatus(true)
        }

        binding.songHeartButtonOnIv.setOnClickListener {
            setHeartStatus(false)
        }
    }

    // 플레이버튼이랑 퍼즈버튼 클릭 리스너
    fun setPlayerStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.songPauseButtonIv.visibility = View.VISIBLE
            binding.songPlayButtonIv.visibility = View.GONE
        } else {
            binding.songPauseButtonIv.visibility = View.GONE
            binding.songPlayButtonIv.visibility = View.VISIBLE
        }
    }

    // repeat 버튼 클릭 리스터
    fun setRepeatStatus(status : Int) {
        if (status==0) {
            binding.songRepeatButtonIv.visibility = View.GONE
            binding.songRepeatButtonOnIv.visibility = View.VISIBLE
            binding.songRepeatButtonOn1Iv.visibility = View.GONE
        } else if (status==1) {
            binding.songRepeatButtonIv.visibility = View.GONE
            binding.songRepeatButtonOnIv.visibility = View.GONE
            binding.songRepeatButtonOn1Iv.visibility = View.VISIBLE
        } else {
            binding.songRepeatButtonIv.visibility = View.VISIBLE
            binding.songRepeatButtonOnIv.visibility = View.GONE
            binding.songRepeatButtonOn1Iv.visibility = View.GONE
        }
    }

    // random 버튼 클릭 리스너너
    fun setRandomStatus(status : Boolean) {
        if (status) {
            binding.songRandomButtonIv.visibility = View.GONE
            binding.songRandomOnButtonIv.visibility = View.VISIBLE
        } else {
            binding.songRandomButtonIv.visibility = View.VISIBLE
            binding.songRandomOnButtonIv.visibility = View.GONE
        }
    }

    // 하트 표시 클릭 리스너
    fun setHeartStatus(status : Boolean) {
        if (status) {
            binding.songHeartButtonIv.visibility = View.GONE
            binding.songHeartButtonOnIv.visibility = View.VISIBLE
        } else {
            binding.songHeartButtonIv.visibility = View.VISIBLE
            binding.songHeartButtonOnIv.visibility = View.GONE
        }
    }

    // thread 사용 seekbar 작동, timer 작동
    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread() {
        private var second = 0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(1000)
                        second = second + 1
                        handler.post {
                            binding.songProgressbar.progress=second*1000/playTime
                            binding.songProgressTimeTv.text=String.format("%02d:%02d", second/60, second%60)
                        }
                    }
                }
            } catch (e : InterruptedException) {
                Log.d("interrupt", "스레드가 종료되었습니다.")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        mediaPlayer?.pause() // 미디어 플레이어 중지
        player.isPlaying = false // seekbar, timer 스레드 중지
        song.isPlaying = false
        song.second = (binding.songProgressbar.progress * song.playTime)/1000
        setPlayerStatus(false) // 정지상태로 이미지 전환

        // sharedPreference 이용
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // 데이터를 내부 저장소에 저장
        // 바로 조작 불가능 -> 에디터 생성
        val editor = sharedPreferences.edit() // sharedPreference 조작할 때 사용

        // Gson 자바 객체와 Json 객체의 중간 다리 역할
        val json = gson.toJson(song)
        editor.putString("song", json)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        player.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 가지고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }
}