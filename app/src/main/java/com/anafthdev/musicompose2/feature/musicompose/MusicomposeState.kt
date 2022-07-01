package com.anafthdev.musicompose2.feature.musicompose

import androidx.compose.runtime.compositionLocalOf
import com.anafthdev.musicompose2.data.model.Song

data class MusicomposeState(
	val songs: List<Song> =  emptyList()
)

val LocalMusicomposeState = compositionLocalOf { MusicomposeState() }