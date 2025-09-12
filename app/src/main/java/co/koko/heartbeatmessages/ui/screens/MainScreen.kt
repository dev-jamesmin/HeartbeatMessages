package co.koko.heartbeatmessages.ui.screens

import androidx.compose.runtime.*
import co.koko.heartbeatmessages.HeartbeatTabs
import co.koko.heartbeatmessages.data.RelationshipStatus
import co.koko.heartbeatmessages.ui.components.CardList

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(RelationshipStatus.Some) }

    HeartbeatTabs(selectedTab = selectedTab) { newTab ->
        selectedTab = newTab
    }
    CardList(selectedTab = selectedTab)
}