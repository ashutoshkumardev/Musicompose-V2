package com.anafthdev.musicompose2.feature.playlist.environment

import com.anafthdev.musicompose2.data.model.Playlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IPlaylistEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getPlaylist(): Flow<Playlist>
	
	suspend fun setPlaylist(playlistID: Int)
	
}