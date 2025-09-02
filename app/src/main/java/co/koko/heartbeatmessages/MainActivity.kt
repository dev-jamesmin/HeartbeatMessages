// co/koko/heartbeatmessages/MainActivity.kt
package co.koko.heartbeatmessages

import HeartbeatMessagesTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat // 이 import를 추가하세요.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 엣지 투 엣지를 활성화합니다.
        // 이 코드는 시스템 바 영역까지 화면 콘텐츠를 확장하도록 합니다.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HeartbeatMessagesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HeartbeatMessagesApp()
                }
            }
        }
    }
}