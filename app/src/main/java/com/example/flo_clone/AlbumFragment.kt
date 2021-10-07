package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentAlbumBinding
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding

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

        binding.albumItem1Layout.setOnClickListener {
            Toast.makeText(activity, album_list_title1_tv.text.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.albumItem2Layout.setOnClickListener {
            Toast.makeText(activity, album_list_title2_tv.text.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.albumItem3Layout.setOnClickListener {
            Toast.makeText(activity, album_list_title3_tv.text.toString(), Toast.LENGTH_SHORT).show()
        }

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