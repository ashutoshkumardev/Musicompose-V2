package com.anafthdev.musicompose2.runtime.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.musicompose2.data.MusicomposeDestination
import com.anafthdev.musicompose2.data.SortType
import com.anafthdev.musicompose2.feature.language.LanguageScreen
import com.anafthdev.musicompose2.feature.main.MainScreen
import com.anafthdev.musicompose2.feature.musicompose.LocalMusicomposeState
import com.anafthdev.musicompose2.feature.search.SearchScreen
import com.anafthdev.musicompose2.feature.setting.SettingScreen
import com.anafthdev.musicompose2.feature.sort_sheet.SortSheetScreen
import com.anafthdev.musicompose2.feature.theme.ThemeScreen
import com.anafthdev.musicompose2.foundation.common.BottomSheetLayoutConfig
import com.anafthdev.musicompose2.foundation.common.LocalSongController
import com.anafthdev.musicompose2.foundation.uicomponent.BottomMusicPlayer
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MusicomposeNavHost(
	modifier: Modifier = Modifier
) {
	
	val lifeCycleOwner = LocalLifecycleOwner.current
	val songController = LocalSongController.current
	val musicomposeState = LocalMusicomposeState.current
	
	val scope = rememberCoroutineScope()
	val bottomSheetNavigator = rememberBottomSheetNavigator()
	val navController = rememberNavController(bottomSheetNavigator)
	
	var bottomSheetLayoutConfig by remember { mutableStateOf(BottomSheetLayoutConfig()) }
	
	DisposableEffect(lifeCycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			when (event) {
				Lifecycle.Event.ON_CREATE -> {
					scope.launch {
						delay(600)
						songController?.showBottomMusicPlayer()
					}
				}
				else -> {}
			}
		}
		
		lifeCycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifeCycleOwner.lifecycle.removeObserver(observer)
		}
	}
	
	ModalBottomSheetLayout(
		sheetBackgroundColor = bottomSheetLayoutConfig.sheetBackgroundColor,
		bottomSheetNavigator = bottomSheetNavigator,
		sheetShape = MaterialTheme.shapes.large,
		modifier = modifier
	) {
		Box {
			NavHost(
				navController = navController,
				startDestination = MusicomposeDestination.Main.route,
				modifier = Modifier
					.fillMaxSize()
			) {
				composable(MusicomposeDestination.Main.route) {
					MainScreen(navController = navController)
				}
				
				composable(MusicomposeDestination.Search.route) {
					SearchScreen(navController = navController)
				}
				
				composable(MusicomposeDestination.Setting.route) {
					SettingScreen(navController = navController)
				}
				
				composable(MusicomposeDestination.Language.route) {
					LanguageScreen(navController = navController)
				}
				
				composable(MusicomposeDestination.Theme.route) {
					ThemeScreen(navController = navController)
				}
				
				bottomSheet(
					route = MusicomposeDestination.BottomSheet.Sort.route,
					arguments = listOf(
						navArgument(
							name = "type"
						) {
							type = NavType.IntType
						}
					)
				) { entry ->
					val sortType = SortType.values()[entry.arguments?.getInt("type") ?: 0]
					
					bottomSheetLayoutConfig = bottomSheetLayoutConfig.copy(
						sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
					)
					
					SortSheetScreen(
						sortType = sortType,
						navController = navController
					)
				}
			}
			
			AnimatedVisibility(
				visible = musicomposeState.isBottomMusicPlayerShowed,
				enter = slideInVertically(
					initialOffsetY = { it }
				),
				exit = slideOutVertically(
					targetOffsetY = { it }
				),
				modifier = Modifier
					.navigationBarsPadding()
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
			) {
				BottomMusicPlayer(
					isPlaying = musicomposeState.isPlaying,
					currentSong = musicomposeState.currentSongPlayed,
					onClick = {
					
					},
					onFavoriteClicked = { isFavorite ->
						songController?.setFavorite(isFavorite)
					},
					onPlayPauseClicked = { isPlaying ->
						if (isPlaying) songController?.resume()
						else songController?.pause()
					}
				)
			}
		}
	}
}
