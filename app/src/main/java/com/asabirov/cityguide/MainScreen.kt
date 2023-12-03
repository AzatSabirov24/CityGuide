package com.asabirov.cityguide

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.asabirov.cityguide.navigation.NavigationItem
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val items = listOf(
        NavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
        ),
        NavigationItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        val navController = rememberNavController()
        ModalNavigationDrawer(
            gesturesEnabled = false,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title)
                            },
                            selected = index == selectedItemIndex,
                            onClick = {
                                navController.navigate((item.title))
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "City Guide")
                        },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    drawerState.open()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Menu,
//                                    contentDescription = "Menu"
//                                )
//                            }
//                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
//                    NavHost(navController = navController, startDestination = "Search") {
//                        composable("Search") {
//                            val searchEntry = remember {
//                                navController.getBackStackEntry("Search")
//                            }
//                            val searchViewModel = hiltViewModel<SearchViewModel>(searchEntry)
//                            SearchScreen(
//                                navigateToMap = {
//                                    navController.navigate("MapPlaces")
//                                },
//                                openPlaceDetails = {
//                                    navController.navigate("PlaceDetails")
//                                },
//                                viewModel = searchViewModel
//                            )
//                        }
//                        composable("Favorites") { FavoriteScreen() }
//                        composable("Settings") { SettingsScreen() }
//                        composable("MapPlaces") {
//                            val searchEntry = remember {
//                                navController.getBackStackEntry("Search")
//                            }
//                            val searchViewModel = hiltViewModel<SearchViewModel>(searchEntry)
//                            MapPlacesScreen(
//                                viewModel = searchViewModel,
//                                openPlaceDetails = {
//                                    navController.navigate("PlaceDetails")
//                                }
//                            )
//                        }
//                        composable("MapPlaceDetails") {
//                            val searchEntry = remember {
//                                navController.getBackStackEntry("Search")
//                            }
//                            val searchViewModel = hiltViewModel<SearchViewModel>(searchEntry)
//                            MapPlaceDetailsScreen(viewModel = searchViewModel)
//                        }
//                        composable("PlaceDetails") {
//                            val searchEntry = remember {
//                                navController.getBackStackEntry("Search")
//                            }
//                            val searchViewModel = hiltViewModel<SearchViewModel>(searchEntry)
//                            PlaceDetailsScreen(
//                                navigateToMap = {
//                                    navController.navigate("MapPlaceDetails")
//                                },
//                                viewModel = searchViewModel
//                            )
//                        }
//                    }

                }
            }
        }
    }
}
