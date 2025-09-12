package co.koko.heartbeatmessages

import HeartbeatMessagesTheme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.data.RelationshipStatus
import co.koko.heartbeatmessages.ui.components.CardList
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import co.koko.heartbeatmessages.ui.components.AppVersionText
import co.koko.heartbeatmessages.ui.components.SettingDescriptionNavigationItem
import co.koko.heartbeatmessages.ui.components.SettingNavigationItem
import android.content.pm.PackageManager
import android.os.Build
import co.koko.heartbeatmessages.ui.components.HeartbeatNavigationBar
import co.koko.heartbeatmessages.ui.screens.ChatScreen
import co.koko.heartbeatmessages.ui.screens.MainScreen
import co.koko.heartbeatmessages.ui.screens.SettingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeartbeatMessagesTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f

                SideEffect {
                    systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var selectedBottomNavItem by remember { mutableStateOf(0) }

                    Scaffold(
                        topBar = { HeartbeatTopAppBar() },
                        bottomBar = {
                            HeartbeatNavigationBar(selectedBottomNavItem) { index ->
                                selectedBottomNavItem = index
                            }
                        },
                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                                when (selectedBottomNavItem) {
                                    0 -> MainScreen()
                                    1 -> ChatScreen()
                                    2 -> SettingScreen()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartbeatTopAppBar() {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFBFBFC)),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Heart Icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "심쿵멘트",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "설렘이 필요할 때, 심쿵멘트",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFBFBFC)
        )
    )
}

@Composable
fun HeartbeatTabs(
    selectedTab: RelationshipStatus,
    onTabSelected: (RelationshipStatus) -> Unit
) {
    val tabs = remember { RelationshipStatus.entries }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        TabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            containerColor = Color.Transparent,
            indicator = { },
            divider = { }
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == selectedTab
                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (tab) {
                                RelationshipStatus.Some -> "썸"
                                RelationshipStatus.Dating1Year -> "연애 1년 미만"
                                RelationshipStatus.Dating2YearPlus -> "연애 1년 이상"
                            },
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//fun HeartbeatNavigationBar(
//    selectedItem: Int,
//    onItemSelected: (Int) -> Unit
//) {
//    NavigationBar(
//        modifier = Modifier.border(
//            BorderStroke(1.dp, Color(0xFFFBE7F3)),
//            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
//        ),
//        containerColor = MaterialTheme.colorScheme.tertiary,
//        contentColor = MaterialTheme.colorScheme.onSurface,
//    ) {
//        val items = listOf("대화 스타터", "설정")
//        val icons = listOf(Icons.Filled.Favorite, Icons.Filled.Settings)
//
//        items.forEachIndexed { index, item ->
//            NavigationBarItem(
//                icon = { Icon(icons[index], contentDescription = item) },
//                label = { Text(item) },
//                selected = selectedItem == index,
//                onClick = { onItemSelected(index) },
//                colors = NavigationBarItemDefaults.colors(
//                    indicatorColor = Color.Transparent,
//                    selectedIconColor = MaterialTheme.colorScheme.primary,
//                    selectedTextColor = MaterialTheme.colorScheme.primary,
//                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
//                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
//                )
//            )
//        }
//    }
//}