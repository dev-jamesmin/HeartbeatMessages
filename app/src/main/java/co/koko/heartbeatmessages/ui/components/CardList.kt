// co/koko/heartbeatmessages/ui/components/CardList.kt
package co.koko.heartbeatmessages.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.data.HeartbeatMessage // 수정된 HeartbeatMessage 임포트
import co.koko.heartbeatmessages.data.HeartbeatMessageData
import co.koko.heartbeatmessages.data.RelationshipStatus
import co.koko.heartbeatmessages.data.QuestionData
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.InputStreamReader

@Composable
fun CardList(selectedTab: RelationshipStatus) {
    val context = LocalContext.current
    var messages by remember { mutableStateOf<List<HeartbeatMessageData>>(emptyList()) }

    // 앱 실행 시 한 번만 JSON 데이터를 로드
    LaunchedEffect(Unit) {
        messages = loadHeartbeatMessages(context)
    }

    val filteredQuestions = getQuestionsForTab(messages, selectedTab)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filteredQuestions) { questionData -> // 변수명 충돌 방지를 위해 questionData로 변경
            HeartbeatCard(
                HeartbeatMessage(
                    title = questionData.question,
                    answers = questionData.answers // 이제 모든 답변 리스트를 전달
                )
            )
        }
    }
}

// JSON 파일을 읽어 데이터 모델로 변환하는 함수
private fun loadHeartbeatMessages(context: Context): List<HeartbeatMessageData> {
    return try {
        val inputStream: InputStream = context.assets.open("heartbeat_messages.json")
        val reader = InputStreamReader(inputStream)
        val jsonString = reader.use { it.readText() }
        Json.decodeFromString<List<HeartbeatMessageData>>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

// 선택된 탭에 맞는 질문들을 필터링하는 함수
private fun getQuestionsForTab(
    data: List<HeartbeatMessageData>,
    tab: RelationshipStatus
): List<QuestionData> {
    val categoryName = when (tab) {
        RelationshipStatus.Some -> "썸"
        RelationshipStatus.Dating1Year -> "연애 1년 미만"
        RelationshipStatus.Dating2YearPlus -> "연애 2년 이상"
        // RelationshipStatus에 추가적인 탭이 있다면 여기에 매핑 추가
        // 예를 들어, RelationshipStatus.Friend -> "친구"
    }
    return data.find { it.category == categoryName }?.questions ?: emptyList()
}