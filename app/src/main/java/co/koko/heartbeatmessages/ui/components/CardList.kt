// co/koko/heartbeatmessages/ui/components/CardList.kt
package co.koko.heartbeatmessages.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.data.HeartbeatMessage
import co.koko.heartbeatmessages.data.RelationshipStatus

@Composable
fun CardList(selectedTab: RelationshipStatus) {
    val messages = getDummyDataForTab(selectedTab)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(messages) { message ->
            HeartbeatCard(message)
        }
    }
}

private fun getDummyDataForTab(tab: RelationshipStatus): List<HeartbeatMessage> {
    return when (tab) {
        RelationshipStatus.Some -> listOf(
            HeartbeatMessage(
                "첫 데이트 후 연락이 늦어질 때",
                "오늘 정말 즐거웠어요? 집에 잘 도착하셨나요?"
            ),
            HeartbeatMessage(
                "상대방이 바쁘다고 할 때",
                "바쁜 중에도 연락 줘서 고마워요"
            ),
            HeartbeatMessage(
                "좋아한다고 고백하고 싶을 때",
                "요즘 당신 생각이 자꾸 나요"
            ),
            HeartbeatMessage(
                "상대방이 우울해할 때",
                "힘든 일이 있으시면 언제든 말해주세요"
            ),
            HeartbeatMessage(
                "생일 축하 메시지를 보낼 때",
                "특별한 하루 되세요! 🥳"
            )
        )
        // TODO: 다른 탭의 데이터 추가
        else -> emptyList()
    }
}