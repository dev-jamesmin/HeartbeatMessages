// co/koko/heartbeatmessages/ui/components/BottomNavBar.kt
package co.koko.heartbeatmessages.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar() {
    // material-icons-extended에서 제공하는 아이콘으로 변경
    val chatIcon = Icons.Filled.Chat
    val directQuestionIcon = Icons.Filled.Help
    val settingsIcon = Icons.Filled.Settings

    NavigationBar {
        NavigationBarItem(
            selected = true, // 현재 선택된 탭에 따라 변경
            onClick = { /* TODO: 화면 이동 구현 */ },
            icon = { Icon(imageVector = chatIcon, contentDescription = "대화 스타터") },
            label = { Text("대화 스타터") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: 화면 이동 구현 */ },
            icon = { Icon(imageVector = directQuestionIcon, contentDescription = "직접 질문") },
            label = { Text("직접 질문") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO: 화면 이동 구현 */ },
            icon = { Icon(imageVector = settingsIcon, contentDescription = "설정") },
            label = { Text("설정") }
        )
    }
}