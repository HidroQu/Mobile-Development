package com.capstone.hidroqu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.capstone.hidroqu.ui.theme.HidroQuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HidroQuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainJetpack()
                }
            }
        }
    }
}
//
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.outlined.FavoriteBorder
//import androidx.compose.material.icons.outlined.Home
//import androidx.compose.material.icons.outlined.Person
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.SearchBarDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.capstone.hidroqu.ui.screen.article.ArticleActivity
//import com.capstone.hidroqu.ui.screen.detailarticle.DetailArticleActivity
//import com.capstone.hidroqu.ui.screen.home.HomeActivity
//import com.capstone.hidroqu.ui.screen.myplant.MyPlantActivity
//import com.capstone.hidroqu.ui.theme.HidroQuTheme
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
//import androidx.navigation.NavType
//import com.capstone.hidroqu.ui.screen.profile.ProfileActivity
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.navArgument
//import com.capstone.hidroqu.ui.screen.addplant.AddPlantActivity
//import com.capstone.hidroqu.ui.screen.camera.CameraPermissionScreen
//import com.capstone.hidroqu.utils.ListMyAddPlant
//import com.capstone.hidroqu.ui.screen.chooseplant.ChoosePlantActivity
//import com.capstone.hidroqu.ui.screen.community.CommunityActivity
//import com.capstone.hidroqu.ui.screen.detailcommunity.DetailPostCommunityActivity
//import com.capstone.hidroqu.ui.screen.detailmyplant.DetailMyPlantActivity
//import com.capstone.hidroqu.utils.getPlantById
//import com.capstone.hidroqu.ui.screen.editprofile.EditProfileActivity
//import com.capstone.hidroqu.ui.screen.formaddplant.FormAddPlantActivity
//import com.capstone.hidroqu.ui.screen.formcommunity.FormAddCommunityActivity
//import com.capstone.hidroqu.ui.screen.historymyplant.HistoryMyPlantActivity
//import com.capstone.hidroqu.ui.screen.home.getArticleById
//import com.capstone.hidroqu.utils.dummyListUserData
//import com.capstone.hidroqu.utils.getHealthHistoryByPlantAndHealthId
//import com.capstone.hidroqu.ui.screen.login.LoginActivity
//import com.capstone.hidroqu.ui.screen.register.RegisterActivity
//import com.capstone.hidroqu.ui.screen.resultpototanam.ResultPotoTanamActivity
//import com.capstone.hidroqu.ui.screen.resultscantanam.ResultScanTanamActivity
//
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val splashScreen = installSplashScreen()
//
//        splashScreen.setKeepOnScreenCondition {
//            true
//        }
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            splashScreen.setKeepOnScreenCondition {
//                false
//            }
//        }, 2000)
//        enableEdgeToEdge()
//        setContent {
//            HidroQuTheme {
//                MainApp()
//            }
//        }
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainApp() {
//    val navController = rememberNavController()
//    val currentBackStackEntry = navController.currentBackStackEntryAsState()
//    val currentDestination = currentBackStackEntry.value?.destination
//    val systemUiController = rememberSystemUiController()
//
//    var isSearchVisible by remember { mutableStateOf(false) }
//    var searchQuery by remember { mutableStateOf("") }
//    var title by remember { mutableStateOf("Artikel") }
//    var selectedPlant by remember { mutableStateOf<ListMyAddPlant?>(null) }
//    var items = remember {
//        mutableStateListOf(
//            "history"
//        )
//    }
//    if (currentDestination?.route == "Artikel") {
//        searchQuery = ""
//        title = "Artikel"
//    }
//    systemUiController.setSystemBarsColor(
//        color = MaterialTheme.colorScheme.primaryContainer,
//    )
//
//    systemUiController.setNavigationBarColor(
//        color = MaterialTheme.colorScheme.onPrimary,
//    )
//    Scaffold(
//        topBar = {
//            when (currentDestination?.route) {
//                "Artikel" -> {
//                    if (!isSearchVisible){
//                        TopAppBar(
//                            title = { Text(title) },
//                            actions = {
//                                IconButton(onClick = {
//                                    isSearchVisible = true
//                                }) {
//                                    Icon(Icons.Filled.Search, contentDescription = "Search")
//                                }
//                            },
//                            navigationIcon = {
//                                IconButton(onClick = { navController.popBackStack() }) {
//                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                                }
//                            },
//                            colors = TopAppBarDefaults.smallTopAppBarColors(
//                                containerColor = MaterialTheme.colorScheme.onPrimary
//                            ),
//                        )
//                    }
//
//                }
//
//                "DetailArticle/{articleId}" -> {
//                    val articleId = currentBackStackEntry.value?.arguments?.getString("articleId")
//                        ?.toIntOrNull()
//                    val article = articleId?.let { getArticleById(it) }
//                    TopAppBar(
//                        title = { Text(article?.title ?: "Detail Artikel") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//
//                "Profil" -> {
//                    TopAppBar(
//                        title = { Text("Profil") },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//
//                "Tanamanku" -> {
//                    TopAppBar(
//                        title = {
//                            Text(
//                                text = "Tanamanku",
//                                style = MaterialTheme.typography.bodyLarge,
//                                fontWeight = FontWeight.Bold,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        },
//                        colors = TopAppBarDefaults.mediumTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//
//                "EditProfil" -> {
//                    TopAppBar(
//                        title = { Text("Edit Profil") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        actions = {
//                            IconButton(onClick = {}) {
//                                Icon(Icons.Filled.Check, contentDescription = "Accept")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//                "DetailTanamanku/{plantId}" -> {
//                    val plantId = currentBackStackEntry.value?.arguments?.getString("plantId")
//                        ?.toIntOrNull()
//                    val plant = plantId?.let { getPlantById(it) }
//                    TopAppBar(
//                        title = { Text(plant?.name ?: "Tanamanku") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//                "PilihJenisTanaman" -> {
//                    TopAppBar(
//                        title = { Text("Pilih tanaman anda") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//
//                "FormTanaman/{plantId}" -> {
//                    TopAppBar(
//                        title = { Text("Pilih tanaman anda") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//
//                "HistoryTanamanku/{plantId}/{healthId}" -> {
//                    // Ambil plantId dan healthId dari argumen dengan kunci yang benar
//                    val plantId = currentBackStackEntry.value?.arguments?.getString("plantId")?.toIntOrNull()
//                    val healthId = currentBackStackEntry.value?.arguments?.getString("healthId")?.toIntOrNull()
//
//                    // Dapatkan riwayat kesehatan berdasarkan plantId dan healthId
//                    if (plantId != null && healthId != null) {
//                        val history = getHealthHistoryByPlantAndHealthId(plantId, healthId)
//
//                        // Menampilkan TopAppBar dengan judul yang sesuai
//                        TopAppBar(
//                            title = { Text(history?.dateHistory ?: "History tanaman") },
//                            navigationIcon = {
//                                IconButton(onClick = { navController.popBackStack() }) {
//                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                                }
//                            },
//                            colors = TopAppBarDefaults.smallTopAppBarColors(
//                                containerColor = MaterialTheme.colorScheme.onPrimary
//                            )
//                        )
//                    }
//                }
//
//                "PilihTanaman" -> {
//                    TopAppBar(
//                        title = { Text("Pilih tanamanmu") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//                "ResultPotoTanam/{photoUri}" -> {
//                    TopAppBar(
//                        title = { Text("Penyakit terdeteksi") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//                "ResultScanTanam/{photoUri}"-> {
//                    TopAppBar(
//                        title = { Text("Detail tanaman") },
//                        navigationIcon = {
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                            }
//                        },
//                        colors = TopAppBarDefaults.smallTopAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.onPrimary
//                        )
//                    )
//                }
//                else -> {
//                }
//            }
//        },
//        bottomBar = {
//            BottomNavigationBar(navController)
//            when (currentDestination?.route){
//                "DetailTanamanku/{plantId}" -> {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Button(
//                            onClick = {  },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
//                        ) {
//                            Text(text = "Edit", style = MaterialTheme.typography.labelLarge)
//                        }
//                    }
//                }
//                "PilihJenisTanaman" -> {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        NavigationBar(
//                            containerColor = MaterialTheme.colorScheme.onPrimary,
//                        ) {
//                            Button(
//                                onClick = {
//                                    selectedPlant?.let { plant ->
//                                        navController.navigate("FormTanaman/{plantId}"){
//                                            // Menjaga status halaman sebelumnya di stack navigasi
//                                            popUpTo("PilihJenisTanaman")
//                                            launchSingleTop = true
//                                        }
//                                    }
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(20.dp),
//                                enabled = selectedPlant != null,
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = if (selectedPlant != null)
//                                        MaterialTheme.colorScheme.primary
//                                    else
//                                        MaterialTheme.colorScheme.surfaceVariant
//                                )
//                            ) {
//                                Text(text = "Lanjut", style = MaterialTheme.typography.labelLarge)
//                            }
//                        }
//                    }
//                }
//                "FormTanaman/{plantId}" -> {
//                    LaunchedEffect(selectedPlant) {
//                        selectedPlant?.let { plant ->
//                            navController.navigate("FormTanaman/${plant.plantId}")
//                        }
//                    }
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Button(
//                            onClick = {
//                                selectedPlant?.let { plant ->
////                                    // Logika untuk menambahkan input user ke data "Tanamanku"
////                                    addPlantToUserCollection(plant, plantingDate, note)
////
////                                    // Tampilkan Toast jika berhasil
////                                    Toast.makeText(
////                                        LocalContext.current,
////                                        "Tanaman berhasil ditambahkan!",
////                                        Toast.LENGTH_SHORT
////                                    ).show()
//
//                                    // Arahkan ke halaman "Tanamanku"
//                                    navController.navigate("Tanamanku")
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            enabled = selectedPlant != null,
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = if (selectedPlant != null)
//                                    MaterialTheme.colorScheme.primary
//                                else
//                                    MaterialTheme.colorScheme.surfaceVariant
//                            )
//                        ) {
//                            Text(text = "Simpan", style = MaterialTheme.typography.labelLarge)
//                        }
//
//                    }
//                }
//                "ResultScanTanam/{photoUri}" -> {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Button(
//                            onClick = {
//
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
//                        ) {
//                            Text(text = "Simpan ke koleksi tanaman anda", style = MaterialTheme.typography.labelLarge)
//                        }
//                    }
//                }
//                "ResultPotoTanam/{photoUri}" -> {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Button(
//                            onClick = {
//                                navController.navigate("PilihTanaman")
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
//                        ) {
//                            Text(text = "Simpan dan Pilih Tanaman", style = MaterialTheme.typography.labelLarge)
//                        }
//                    }
//                }
//                "PilihTanaman"-> {
//                    NavigationBar(
//                        containerColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Button(
//                            onClick = { },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(20.dp),
//                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
//                        ) {
//                            Text(text = "Simpan riwayat penyakit", style = MaterialTheme.typography.labelLarge)
//                        }
//                    }
//                }
//                else -> {
//                }
//            }
//        }
//    ) { paddingValues ->
//        if (isSearchVisible) {
//            SearchBar(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                query = searchQuery,
//                onQueryChange = {
//                    searchQuery = it
//                },
//                onSearch = {
//                    items.add(searchQuery)
//                    isSearchVisible = false
//                    searchQuery = ""
//                },
//                active = isSearchVisible,
//                onActiveChange = {
//                    isSearchVisible = it
//                },
//                placeholder = {
//                    Text(
//                        text = "Cari artikel atau informasi...",
//                        style = MaterialTheme.typography.bodyMedium.copy(
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search Icon",
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                },
//                trailingIcon = {
//                    Icon(
//                        modifier = Modifier.clickable {
//                            searchQuery = ""
//                            isSearchVisible = false
//                        },
//                        imageVector = Icons.Default.Close,
//                        contentDescription = "Close Icon",
//                        tint = MaterialTheme.colorScheme.error
//                    )
//                },
//                colors = SearchBarDefaults.colors(
//                    containerColor = MaterialTheme.colorScheme.background
//                )
//            ) {
//                items.forEach { item ->
//                    Row(modifier = Modifier.padding(14.dp)) {
//                        Icon(
//                            modifier = Modifier.padding(end = 10.dp),
//                            imageVector = Icons.Default.Notifications,
//                            contentDescription = "Icon History",
//                            tint = MaterialTheme.colorScheme.secondary
//                        )
//                        Text(
//                            text = item,
//                            style = MaterialTheme.typography.bodyMedium.copy(
//                                color = MaterialTheme.colorScheme.onBackground
//                            )
//                        )
//                    }
//                }
//            }
//        }
//
//        NavHost(
//            navController = navController,
//            startDestination = "Home",
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable("Home") { HomeActivity(navController) }
//            composable(
//                route = "DetailTanamanku/{plantId}",
//                arguments = listOf(
//                    navArgument("plantId") {
//                        type = NavType.IntType
//                    }
//                )
//            ) { backStackEntry ->
//                val plantId = backStackEntry.arguments?.getInt("plantId")
//                if (plantId != null) {
//                    DetailMyPlantActivity(
//                        detailId = plantId,
//                        navController = navController
//                    )
//                }
//            }
//            composable("Tanamanku") {
//                MyPlantActivity(
//                    onAddClicked = {
//                        navController.navigate("PilihJenisTanaman") {
//                            popUpTo("Tanamanku"){
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                        }
//                    },
//                    onDetailClicked = { plantId ->
//                        navController.navigate("DetailTanamanku/$plantId")
//                    }
//                )
//            }
//            composable("PilihJenisTanaman") {
//                AddPlantActivity(
//                    selectedPlantAdd = selectedPlant,
//                    onPlantSelected = { plant ->
//                        selectedPlant = plant
//                    }
//                )
//            }
//
//            composable("FormTanaman/{plantId}") { backStackEntry ->
//                val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull()
//
//                if (plantId != null) {
//                    FormAddPlantActivity(plantId = plantId)
//                } else {
//                    Text("ID Tanaman tidak valid")
//                }
//            }
//
//            composable("HistoryTanamanku/{plantId}/{healthId}") { backStackEntry ->
//                val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull()
//                val healthId = backStackEntry.arguments?.getString("healthId")?.toIntOrNull()
//
//                if (plantId != null && healthId != null) {
//                    // Menampilkan aktivitas atau layar dengan data yang sesuai
//                    HistoryMyPlantActivity(plantId, healthId)
//                }
//            }
//
//            composable("Komunitas") { CommunityActivity(
//                    onAddClicked = {
//                        navController.navigate("TambahPostKomunitas") {
//                            popUpTo("Komunitas")
//                            launchSingleTop = true
//                        }
//                    },
//                    onDetailClicked = { postId ->
//                        navController.navigate("DetailKomunitas/$postId")
//                    }
//                )
//            }
//            composable(
//                route = "DetailKomunitas/{postId}",
//                arguments = listOf(
//                    navArgument("postId") {
//                        type = NavType.IntType
//                    }
//                )
//            ) { backStackEntry ->
//                val postId = backStackEntry.arguments?.getInt("postId")
//                if (postId != null) {
//                    DetailPostCommunityActivity(
//                        communityId = postId
//                    )
//                }
//            }
//            composable("TambahPostKomunitas") { FormAddCommunityActivity() }
//
//            composable("Profil") {
//                ProfileActivity(
//                    navController,
//                    dummyListUserData.first()
//                )
//            }
//            composable("EditProfil") {
//                EditProfileActivity(
//                    userData = dummyListUserData.first(),
//                    onNameChanged = {},
//                    onBioChanged = {}
//                )
//            }
//            composable("Daftar") {
//                RegisterActivity(
//                    name = "",
//                    email = "",
//                    password = "",
//                    checkValid = mutableListOf(),
//                    onNameChanged = {},
//                    onEmailChanged = {},
//                    onPasswordChanged = {},
//                    onLoginClicked = {
//                        navController.navigate("Masuk") {
//                            popUpTo("Daftar") { inclusive = true }
//                            launchSingleTop = true
//                        }
//                    },
//                    onRegisterClicked = {
//                    },
//                    onBackClick = {
//                        navController.popBackStack()
//                    }
//                )
//            }
//            composable("Masuk") {
//                LoginActivity(
//                    email = "",
//                    password = "",
//                    onEmailChanged = {},
//                    onPasswordChanged = {},
//                    onLoginClicked = {},
//                    onRegisterClicked = {
//                        navController.navigate("Daftar") {
//                            popUpTo("Masuk") { inclusive = true }
//                            launchSingleTop = true
//                        }
//                    }
//                )
//            }
//
//            composable("Artikel") { ArticleActivity(navController, searchQuery) }
//            composable("DetailArticle/{articleId}") { backStackEntry ->
//                val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull()
//                if (articleId != null) {
//                    DetailArticleActivity(articleId)
//                }
//            }
//            composable("CameraPotoTanam") { CameraPermissionScreen("Poto Tanam", navController) }
//            composable("CameraScanTanam") { CameraPermissionScreen("Scan Tanam", navController) }
//            composable(
//                "ResultPotoTanam/{photoUri}",
//                arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val photoUri = backStackEntry.arguments?.getString("photoUri")
//                ResultPotoTanamActivity(photoUri = photoUri, navController)
//            }
//            composable(
//                "ResultScanTanam/{photoUri}",
//                arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val photoUri = backStackEntry.arguments?.getString("photoUri")
//                ResultScanTanamActivity(photoUri = photoUri, navController)
//            }
//            composable("PilihTanaman") { ChoosePlantActivity(navController) }
//        }
//    }
//}
//
//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    var selectedItem by remember { mutableIntStateOf(0) }
//    val items = listOf("Home", "Tanamanku", "Komunitas", "Profil")
//    val routes = listOf("Home", "Tanamanku", "Komunitas", "Profil")
//    val selectedIcons = listOf(
//        Icons.Filled.Home,
//        Icons.Filled.Favorite,
//        Icons.Filled.Person,
//        Icons.Filled.Person
//    )
//    val unselectedIcons = listOf(
//        Icons.Outlined.Home,
//        Icons.Outlined.FavoriteBorder,
//        Icons.Outlined.Person,
//        Icons.Outlined.Person
//    )
//    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.onPrimary
//    ) {
//        items.forEachIndexed { index, item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
//                        contentDescription = item
//                    )
//                },
//                label = {
//                    Text(
//                        item,
//                        style = MaterialTheme.typography.labelLarge.copy(
//                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
//                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
//                            fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Normal,
//                        )
//                    )
//                },
//                selected = selectedItem == index,
//                onClick =
//                {
//                    selectedItem = index
//                    navController.navigate(routes[index]) {
//                        popUpTo(navController.graph.startDestinationId)
//                        launchSingleTop = true
//                    }
//                },
//                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
//                )
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun MainAppPreview() {
//    HidroQuTheme {
//        MainApp()
//    }
//}
//
