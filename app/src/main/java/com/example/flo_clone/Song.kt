package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
        var title : String,
        var singer : String,
        var second : Int,
        var playTime : Int,
        var isPlaying : Boolean,
        var music : String,
        var coverImg : Int,
        var isLike : Boolean
) {
        @PrimaryKey(autoGenerate = true) var id : Int = 0
}
