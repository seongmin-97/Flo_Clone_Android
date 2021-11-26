package com.example.flo_clone

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIslikeById(isLike : Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Insert
    fun likeSong(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND songId = :songId")
    fun isLikeSong(userId : Int, songId : Int) : Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND songId = :songId")
    fun disLikeSong(userId : Int, songId : Int) : Int?

    @Query("SELECT ST.* FROM LikeTable as LT LEFT JOIN SongTable as ST ON LT.songId = ST.id WHERE LT.userId = :userId")
    fun getLikeSongs(userId : Int) : List<Song>
}