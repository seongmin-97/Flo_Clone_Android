package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentLockerBinding


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private var SongDatas = ArrayList<Song>();


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)


        SongDatas.apply{
            add(Song("라일락", "아이유(IU)", 0,217, false, ""))
            add(Song("Flu", "아이유(IU)", 0,217, false, ""))
            add(Song("좋은날", "아이유(IU)", 0,217, false, ""))
            add(Song("어푸", "아이유(IU)", 0,217, false, ""))
            add(Song("스물셋", "아이유(IU)", 0,217, false, ""))
        }

        val songRVAdapter = SongRVAdapter(SongDatas)
        binding.lockerRecyclerView.adapter = songRVAdapter
        binding.lockerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

}