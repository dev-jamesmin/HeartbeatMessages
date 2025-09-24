package co.koko.heartbeatmessages.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.data.RelationshipStatus
import co.koko.heartbeatmessages.ui.components.CardList
import co.koko.heartbeatmessages.util.AdManagerCompose


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(RelationshipStatus.Some) }

    Column(modifier = modifier.fillMaxSize()) {
        HeartbeatTabs(selectedTab = selectedTab) { newTab ->
            selectedTab = newTab
        }

        // CardList가 남은 공간을 모두 차지하도록 weight 적용
        Box(modifier = Modifier.weight(1f)) {
            CardList(selectedTab = selectedTab)
        }

        // 하단 배너 광고
        AdManagerCompose.BannerAdView(modifier = Modifier.fillMaxWidth())
    }
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