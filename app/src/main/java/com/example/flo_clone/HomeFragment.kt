package com.example.flo_clone

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var AlbumDatas = ArrayList<Album>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 앨범 클릭시 앨범 프래그먼트로 이동
/*
        binding.homeTodayMusicAlbum1Cardview.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment())
                    .commitAllowingStateLoss()
        }

        binding.homeTodayMusicAlbum2Cardview.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment())
                    .commitAllowingStateLoss()
        }

        binding.homeTodayMusicAlbum3Cardview.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment())
                    .commitAllowingStateLoss()
        }*/

        // 오늘 발매 음악 리사이클러뷰
        // 데이터 리스트 생성

        AlbumDatas.apply{
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("IDOL", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("어푸", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("작은 것들을 위한 시", "방탄소년단 (BTS)", R.drawable.img_album_exp))
        }

        val albumRVAdapter = AlbumRVAdapter(AlbumDatas)
        binding.homeTodayMusicAlbumRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
        })

        binding.homeTodayMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        // 배너 뷰페이저

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBanner1Vp.adapter = bannerAdapter
        binding.homeBanner1Vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //스크롤 방향 좌우로

        return binding.root
    }

    fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment().apply{
                    arguments = Bundle().apply{
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString("album", albumJson)
                    }
                })
                .commitAllowingStateLoss()
    }
}