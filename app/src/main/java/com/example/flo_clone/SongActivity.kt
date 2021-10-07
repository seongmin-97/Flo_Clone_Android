package com.example.flo_clone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }

        binding.songBackButtonIv.setOnClickListener {
            finish()
        }

        binding.songPlayButtonIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPauseButtonIv.setOnClickListener {
            setPlayerStatus(true)
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
            binding.songPauseButtonIv.visibility = View.GONE
            binding.songPlayButtonIv.visibility = View.VISIBLE
        } else {
            binding.songPauseButtonIv.visibility = View.VISIBLE
            binding.songPlayButtonIv.visibility = View.GONE
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
}