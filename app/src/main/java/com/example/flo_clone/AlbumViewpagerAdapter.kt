package com.example.flo_clone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SongFragment() // 수록곡 프래그먼트
            1 -> DetailFragment() // 상세정보 프래그먼트
            else -> VideoFragment() // 영상정보 프래그먼트 영상정보는 없는 경우도 있으므로 else
        }
    }
}