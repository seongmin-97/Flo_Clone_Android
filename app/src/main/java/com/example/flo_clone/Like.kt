package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Like(var userId : Int, var songId : Int) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
