package co.koko.heartbeatmessages.data

enum class RelationshipStatus {
    Some,
    Dating1Year,
    Dating2YearPlus;
    // 다른 탭이 있다면 추가

    val status: String
        get() = when (this) {
            Some -> "썸"
            Dating1Year -> "연애 1년 미만"
            Dating2YearPlus -> "연애 2년 이상"
        }
}