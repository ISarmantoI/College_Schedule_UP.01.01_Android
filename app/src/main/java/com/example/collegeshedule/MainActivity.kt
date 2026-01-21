package com.example.collegeshedule
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.collegeshedule.utils.ScheduleScreen
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.collegeshedule.data.api.ScheduleApi
import com.example.collegeshedule.data.preferences.FavoritesManager
import com.example.collegeshedule.data.repository.ScheduleRepository
import com.example.collegeshedule.ui.favorites.FavoritesScreen
import com.example.collegeshedule.utils.ScheduleScreen
import com.example.collegeshedule.ui.theme.CollegeSheduleTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeSheduleTheme() {
                CollegeScheduleApp()
            }
        }
    }
}
@PreviewScreenSizes
@Composable
fun CollegeScheduleApp() {
    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.HOME) }
    var selectedGroupForSchedule by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val favoritesManager = remember { FavoritesManager(context) }
    var favorites by remember { mutableStateOf(favoritesManager.getFavorites()) }
    
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5144") // localhost для Android Emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    55
    val api = remember { retrofit.create(ScheduleApi::class.java) }
    val repository = remember { ScheduleRepository(api) }
    NavigationSuiteScaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { 
                        currentDestination = it
                        if (it == AppDestinations.FAVORITES) {
                            favorites = favoritesManager.getFavorites()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> {
                    ScheduleScreen(preselectedGroup = selectedGroupForSchedule)
                    selectedGroupForSchedule = null
                }
                AppDestinations.FAVORITES -> FavoritesScreen(
                    favorites = favorites,
                    onRemoveFavorite = { groupName ->
                        favoritesManager.removeFavorite(groupName)
                        favorites = favoritesManager.getFavorites()
                    },
                    onViewSchedule = { groupName ->
                        selectedGroupForSchedule = groupName
                        currentDestination = AppDestinations.HOME
                    }
                )
                AppDestinations.PROFILE ->
                    Text("Профиль студента", modifier =
                        Modifier.padding(innerPadding))
            }
        }
    }
}
enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}