package com.capstone.hidroqu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.hidroqu.ui.article.ArticleActivity
import com.capstone.hidroqu.ui.comunity.ComunityActivity
import com.capstone.hidroqu.ui.detailarticle.DetailArticleActivity
import com.capstone.hidroqu.ui.home.HomeActivity
import com.capstone.hidroqu.ui.myplant.MyPlantActivity
import com.capstone.hidroqu.ui.theme.HidroQuTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.capstone.hidroqu.ui.profile.ProfileActivity
import androidx.navigation.compose.currentBackStackEntryAsState
import com.capstone.hidroqu.ui.editprofile.EditProfileActivity
import com.capstone.hidroqu.ui.home.getArticleById
import com.capstone.hidroqu.ui.login.LoginActivity
import com.capstone.hidroqu.ui.register.RegisterActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            true
        }

        Handler(Looper.getMainLooper()).postDelayed({
            splashScreen.setKeepOnScreenCondition {
                false
            }
        }, 2000)
        enableEdgeToEdge()
        setContent {
            HidroQuTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination
    val systemUiController = rememberSystemUiController()


    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("Artikel") }

    val keyboardController = LocalSoftwareKeyboardController.current

    if (currentDestination?.route == "Artikel") {
        searchQuery = ""
        title = "Artikel"
    }

    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colorScheme.primaryContainer,
    )

    systemUiController.setNavigationBarColor(
        color = MaterialTheme.colorScheme.onPrimary,
    )

    Scaffold(
        topBar = {
            when (currentDestination?.route) {
                "Artikel" -> {
                    TopAppBar(
                        title = {
                            if (isSearchVisible) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    label = { Text("Search") },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            title = searchQuery
                                            keyboardController?.hide()
                                            isSearchVisible = false
                                        }
                                    ),
                                    singleLine = true
                                )
                            } else {
                                Text(title)
                            }
                        },
                        actions = {
                            if (!isSearchVisible) {
                                IconButton(onClick = {
                                    isSearchVisible = true
                                    keyboardController?.show()
                                }) {
                                    Icon(Icons.Filled.Search, contentDescription = "Search")
                                }
                            } else {
                                IconButton(onClick = { isSearchVisible = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Close Search")
                                }
                            }
                        },
                        navigationIcon = {
                            if (!isSearchVisible) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    )
                }

                "DetailArticle/{articleId}" -> {
                    val articleId = currentBackStackEntry.value?.arguments?.getString("articleId")
                        ?.toIntOrNull()
                    val article = articleId?.let { getArticleById(it) }
                    TopAppBar(
                        title = { Text(article?.title ?: "Detail Artikel") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                "Profil" -> {
                    TopAppBar(
                        title = { Text("Profil") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                "EditProfil" -> {
                    TopAppBar(
                        title = { Text("Edit Profil") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Filled.Check, contentDescription = "Accept")
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                else -> {
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Home") { HomeActivity(navController) }
            composable("Tanamanku") {
                LoginActivity(
                    email = "",
                    password = "",
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onLoginClicked = {},
                    onRegisterClicked = {
                        navController.navigate("Daftar") {
                            popUpTo("Masuk") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("Komunitas") { ComunityActivity() }
            composable("Profil") { ProfileActivity(navController) }
            composable("EditProfil") { EditProfileActivity() }
            composable("Daftar") {
                RegisterActivity(
                    name = "",
                    email = "",
                    password = "",
                    checkValid = mutableListOf(),
                    onNameChanged = {},
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onLoginClicked = {
                        navController.navigate("Masuk") {
                            popUpTo("Daftar") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRegisterClicked = {
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("Masuk") {
                LoginActivity(
                    email = "",
                    password = "",
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onLoginClicked = {},
                    onRegisterClicked = {
                        navController.navigate("Daftar") {
                            popUpTo("Masuk") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("Artikel") { ArticleActivity(navController, searchQuery) }
            composable("DetailArticle/{articleId}") { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull()
                if (articleId != null) {
                    DetailArticleActivity(articleId)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Tanamanku", "Komunitas", "Profil")
    val routes = listOf("Home", "Tanamanku", "Komunitas", "Profil")
    val selectedIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Favorite,
        Icons.Filled.Person,
        Icons.Filled.Person
    )
    val unselectedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.FavoriteBorder,
        Icons.Outlined.Person,
        Icons.Outlined.Person
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = {
                    Text(
                        item,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Normal,
                        )
                    )
                },
                selected = selectedItem == index,
                onClick =
                {
                    selectedItem = index
                    navController.navigate(routes[index]) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Preview
@Composable
private fun MainAppPreview() {
    HidroQuTheme {
        MainApp()
    }
}

