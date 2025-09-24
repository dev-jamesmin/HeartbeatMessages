package co.koko.heartbeatmessages.ui.screens

import ChatViewModel
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.koko.heartbeatmessages.data.Message
import co.koko.heartbeatmessages.ui.components.rememberKeyboardHeight
import co.koko.heartbeatmessages.ui.components.rememberKeyboardState
import co.koko.heartbeatmessages.ui.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(context.applicationContext as Application)
    )

    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // 키보드 상태 감지
    val isKeyboardVisible by rememberKeyboardState()
    val keyboardHeight by rememberKeyboardHeight()
    val density = LocalDensity.current

    // 키보드 높이를 dp로 변환
    val keyboardHeightDp = with(density) { keyboardHeight.toDp() }

    // 입력창 고정 높이
    val inputBarHeight = 80.dp

    // 입력창의 실제 위치 계산
    val inputBarOffset = if (isKeyboardVisible) 250.dp else 0.dp
    val listBottomPadding = inputBarOffset + inputBarHeight

    // 키보드 상태 변화 감지 및 스크롤 처리
    LaunchedEffect(isKeyboardVisible, messages.size) {
        if (messages.isNotEmpty()) {
            delay(80) // 키보드 애니메이션 대기
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // 채팅 목록 - 애니메이션 없이 즉시 패딩 적용
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = listBottomPadding), // 즉시 적용
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(messages) { index, message ->
                if (index == 0 && !message.isFromUser) {
                    InitialMessageBubble(message = message)
                } else {
                    MessageBubble(message = message)
                }
            }
        }

        Log.d("inputBarOffset:", inputBarOffset.toString())
//            .offset(y = -inputBarOffset) // 애니메이션이 적용된 offset
        // 입력창만 부드러운 애니메이션 적용
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(inputBarHeight)
                .align(Alignment.BottomCenter)
                .offset(y = -inputBarOffset) // 계산된 오프셋 사용
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("어떤 상황인지 알려주세요...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 3,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText.trim())
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank() && !isLoading,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("전송", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}



// 첫 번째 메시지를 위한 버튼 없는 버블
@Composable
private fun InitialMessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 일반 메시지 버블
@Composable
private fun MessageBubble(message: Message) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 0.dp,
                bottomEnd = if (message.isFromUser) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(16.dp),
                    color = if (message.isFromUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!message.isFromUser) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.End)
                    ) {
                        TextButton(onClick = { clipboardManager.setText(AnnotatedString(message.text)) }) {
                            Icon(Icons.Default.ContentCopy, "복사", Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("복사")
                        }
                        TextButton(onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, message.text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        }) {
                            Icon(Icons.Default.Share, "공유", Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("공유")
                        }
                    }
                }
            }
        }
    }
}
