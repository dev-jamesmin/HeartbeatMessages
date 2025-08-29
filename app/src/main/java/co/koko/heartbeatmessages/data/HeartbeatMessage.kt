// co/koko/heartbeatmessages/data/HeartbeatMessage.kt
package co.koko.heartbeatmessages.data

data class HeartbeatMessage(
    val title: String,
    val message: String
)

sealed class RelationshipStatus(val status: String) {
    object Some : RelationshipStatus("썸")
    object LessThanOneYear : RelationshipStatus("연애 1년 미만")
    object MoreThanOneYear : RelationshipStatus("연애 1년 이상")
}