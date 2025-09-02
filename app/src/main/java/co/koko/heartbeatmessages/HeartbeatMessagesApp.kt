// co/koko/heartbeatmessages/HeartbeatMessagesApp.kt
package co.koko.heartbeatmessages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding // 이 import를 추가하세요.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.data.RelationshipStatus
import co.koko.heartbeatmessages.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartbeatMessagesApp() {
    var selectedTab: RelationshipStatus by remember { mutableStateOf(RelationshipStatus.Some) }

    Scaffold(
        // 이 부분을 수정합니다: Modifier.systemBarsPadding() 추가
        modifier = Modifier.systemBarsPadding(),
        topBar = { HeartbeatTopBar() },
        bottomBar = { BottomNavBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: 플로팅 버튼 액션 */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            RelationshipTabs(selectedTab) { newTab ->
                selectedTab = newTab
            }
            CardList(selectedTab)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    HeartbeatMessagesApp()
}