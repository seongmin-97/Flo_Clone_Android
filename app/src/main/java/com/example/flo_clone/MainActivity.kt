package com.example.flo_clone

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var gson : Gson = Gson()
    var song = Song("라일락", "아이유(IU)",0,  217, false, "music_lilac", R.drawable.img_album_exp2, false)
    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDatabase : SongDatabase
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var player: Player
    private val handler = Handler(Looper.getMainLooper())

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inputDummySongs()
        initPlayList()
        initSong()


        binding.mainPlayerLayout.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainPreviousButtonIv.setOnClickListener {
            moveSong(-1)
        }

        binding.mainNextButtonIv.setOnClickListener {
            moveSong(1)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            setPlayerStatus(false)
            player.isPlaying = false
            song.isPlaying = false
            mediaPlayer?.pause()
        }

        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }


    // 플레이버튼이랑 퍼즈버튼 클릭 리스너
    fun setPlayerStatus(isPlaying : Boolean) {
        if (isPlaying) {
            binding.mainPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else {
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }
    }


    // 맨 처음 노래 불러오기
    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

    // id로 노래 찾기
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    // 타이머
    private fun startTimer() {
        player = Player(songs[nowPos].playTime, songs[nowPos].isPlaying)
        player.start()
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
                            binding.mainProgressbar.progress=second*1000/playTime
                        }
                    }
                }
            } catch (e : InterruptedException) {
                Log.d("interrupt", "스레드가 종료되었습니다.")
            }
        }
    }

    // 플레이리스트 초기화
    @InternalCoroutinesApi
    private fun initPlayList() {
        songDatabase = SongDatabase.getInstance(this)!!
        songs.addAll(songDatabase.SongDao().getSongs())
    }


    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressbar.progress = (song.second * 1000 / song.playTime)

        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    @InternalCoroutinesApi
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.SongDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.SongDao().insert(Song("Lilac", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))
        songDB.SongDao().insert(Song("Coin", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))
        songDB.SongDao().insert(Song("어푸 (Ah-puh)", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))
        songDB.SongDao().insert(Song("아이와 나의 바다", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))
        songDB.SongDao().insert(Song("flu", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))
        songDB.SongDao().insert(Song("좋은날", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false))

    }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressbar.progress = (song.second*1000/song.playTime)

        if (song.isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    // 플레이리스트에서 재생 노래 변경 (이동량을 변수로 받아서 그만큼 인덱스 이동)
    private fun moveSong(direct: Int){

        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        player.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        setPlayer(songs[nowPos])
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        if (songId == 0) {
            song = songDB.SongDao().getSong(1)
        } else {
            song = songDB.SongDao().getSong(songId)
        }

        setMiniPlayer(song)
    }
}

