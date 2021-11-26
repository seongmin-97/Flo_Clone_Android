package com.example.flo_clone

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    var song = Song("라일락", "아이유(IU)", 0,217, false, "music_lilac", R.drawable.img_album_exp2, false)
    private var mediaPlayer : MediaPlayer? = null
    private var gson : Gson = Gson()

    private lateinit var player:Player
    private val handler = Handler(Looper.getMainLooper())

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDatabase : SongDatabase

    private var isLiked : Boolean = false

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        allClickListener()

        isLiked = isLikedSong(song.id)
    }

    override fun onPause() {
        super.onPause()

        mediaPlayer?.pause() // 미디어 플레이어 중지
        player.isPlaying = false // seekbar, timer 스레드 중지
        songs[nowPos].second = (binding.songProgressbar.progress * songs[nowPos].playTime)/1000
        songs[nowPos].isPlaying = false
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

    @InternalCoroutinesApi
    private fun likeSong(userId : Int, songId : Int) {
        val songDB = SongDatabase.getInstance(this)!!
        val like = Like(userId, songId)

        songDB.SongDao().likeSong((like))
    }


    @InternalCoroutinesApi
    private fun dislikeSong(userId : Int, songId : Int) {
        val songDB = SongDatabase.getInstance(this)!!
        songDB.SongDao().disLikeSong(userId, songId)
    }


    @InternalCoroutinesApi
    private fun isLikedSong(songId : Int) : Boolean {
        val songDB = SongDatabase.getInstance(this)!!
        val userId = getJwt()

        val likeId : Int? = songDB.SongDao().isLikeSong(userId, songId)

        return likeId != null
    }

    private fun getJwt() : Int {
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return  spf!!.getInt("jwt", 0)
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

    // 플레이리스트 초기화
    @InternalCoroutinesApi
    private fun initPlayList() {
        songDatabase = SongDatabase.getInstance(this)!!
        songs.addAll(songDatabase.SongDao().getSongs())
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

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songProgressTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressbar.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
        setHeartStatus(song.isLike)

        mediaPlayer = MediaPlayer.create(this, music)
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

    // 좋아요 버튼 누르면 데이터 베이스 갱신 보관함에 뜰 수 있게

    private fun setLike(isLike: Boolean) {
        val userId : Int = getJwt()

        if (isLike) {
            setHeartStatus(false)
            likeSong(userId, song.id)
        } else {
            setHeartStatus(true)
            dislikeSong(userId, song.id)
        }
    }

    // 모든 클릭 리스너
    private fun allClickListener() {
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
            setLike(false)
        }

        binding.songHeartButtonOnIv.setOnClickListener {
            setLike(true)
        }

        binding.songPreviousButtonIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextButtonIv.setOnClickListener {
            moveSong(1)
        }
    }
}