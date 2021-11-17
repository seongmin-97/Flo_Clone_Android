package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackButtonIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
        }

        // Home 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        // Home 에서 넘어온 데이터를 반영
        binding.songAlbumIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()

        // 뷰페이저 어댑터

        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        // 탭레이아웃 연결

        TabLayoutMediator(binding.albumTabLayout, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()

        // 앨범 누르면 토스트 메시지
//
//        binding.albumItem1Layout.setOnClickListener {
//            Toast.makeText(activity, album_list_title1_tv.text.toString(), Toast.LENGTH_SHORT).show()
//        }
//
//        binding.albumItem2Layout.setOnClickListener {
//            Toast.makeText(activity, album_list_title2_tv.text.toString(), Toast.LENGTH_SHORT).show()
//        }
//
//        binding.albumItem3Layout.setOnClickListener {
//            Toast.makeText(activity, album_list_title3_tv.text.toString(), Toast.LENGTH_SHORT).show()
//        }

        // 좋아요 버튼 클릭 리스너

        binding.albumHeartButtonIv.setOnClickListener {
            setHeartStatus(true)
        }

        binding.albumHeartButtonOnIv.setOnClickListener {
            setHeartStatus(false)
        }

        return binding.root
    }
    //하트 조절 버튼
    fun setHeartStatus(status : Boolean) {
        if (status) {
            binding.albumHeartButtonIv.visibility = View.GONE
            binding.albumHeartButtonOnIv.visibility = View.VISIBLE
        } else {
            binding.albumHeartButtonIv.visibility = View.VISIBLE
            binding.albumHeartButtonOnIv.visibility = View.GONE
        }
    }
}