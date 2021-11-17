package com.example.flo_clone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

     // 클릭 인터페이스 정의
    interface  MyItemClickListener {
        fun onItemClick(album: Album)
    }

    // 리스터 객체를 전달받는 함수랑 리스트 객체를 저장할 변수

    private lateinit var  myItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener : MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰 홀더에 던져줌
    override fun onCreateViewHolder(ViewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(ViewGroup.context), ViewGroup, false)

        return ViewHolder(binding)
    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            myItemClickListener.onItemClick(albumList[position])
        }
    }

    // 데이터 세트 크기를 알려주는 함수 -> 리사이클러뷰에게 마지막이 언제인지를 알려줌
    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding : ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemTitleTv.text = album.title
            binding.itemSingerTv.text = album.singer
            binding.itemAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}