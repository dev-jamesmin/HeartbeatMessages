// co/koko/heartbeatmessages/data/HeartbeatMessage.kt
package co.koko.heartbeatmessages.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable // 만약 이 HeartbeatMessage를 직접 JSON 디코딩에 사용한다면 Serializable 필요
data class HeartbeatMessage(
    val title: String, // 질문을 나타내는 필드
    val answers: List<String> // 해당 질문에 대한 모든 답변 리스트
)

// HeartbeatMessageData와 QuestionData는 그대로 유지
@Immutable
@Serializable
data class HeartbeatMessageData(
    val category: String,
    val questions: List<QuestionData>
)

@Immutable
@Serializable
data class QuestionData(
    val question: String,
    val answers: List<String>
)