package com.anafthdev.musicompose2.data.datasource.local.db.playlist

import androidx.room.*
import com.anafthdev.musicompose2.data.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
	
	@Query("SELECT * FROM playlist_table")
	fun getAllPlaylist(): Flow<List<Playlist>>
	
	@Query("SELECT * FROM playlist_table WHERE id LIKE :mID")
	fun get(mID: Int): Playlist?
	
	@Update
	suspend fun update(vararg playlist: Playlist)
	
	@Delete
	suspend fun delete(vararg playlist: Playlist)
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(vararg playlist: Playlist)
	
}