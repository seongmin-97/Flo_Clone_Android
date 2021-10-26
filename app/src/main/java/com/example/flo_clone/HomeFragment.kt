package com.example.flo_clone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 앨범 클릭시 앨범 프래그먼트로 이동

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
        }

        // 배너 뷰페이저

        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBanner1Vp.adapter = bannerAdapter
        binding.homeBanner1Vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //스크롤 방향 좌우로

        return binding.root
    }
}