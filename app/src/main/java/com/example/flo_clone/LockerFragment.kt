package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.SongRVAdapter
import com.example.flo_clone.databinding.FragmentLockerBinding
import kotlinx.coroutines.InternalCoroutinesApi



class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    lateinit var songDB : SongDatabase


    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val songRVAdapter = SongRVAdapter()
        binding.lockerRecyclerView.adapter = songRVAdapter
        binding.lockerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        songDB.SongDao().getLikedSongs(true) as ArrayList
        songRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList)

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.SongDao().updateIslikeById(false, songId)
            }
        })


        return binding.root
    }

    @InternalCoroutinesApi
    private fun initView() {
        val jwt = getJwt()

        if (jwt == "") {
            binding.lockerLogin.text = "로그인"
            binding.lockerLogin.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        } else {
            binding.lockerLogin.text = "로그아웃"
            binding.lockerLogin.setOnClickListener {
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun getJwt() : String {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")!!
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }

    @InternalCoroutinesApi
    override fun onStart() {
        super.onStart()

        initView()
    }
}