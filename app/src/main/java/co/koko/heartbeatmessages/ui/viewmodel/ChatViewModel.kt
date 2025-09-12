package co.koko.heartbeatmessages.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.koko.heartbeatmessages.data.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 채팅 로직을 처리할 ViewModel
class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // 초기 AI 메시지
        _messages.value = listOf(Message("어떤 상황인지 알려주시면, 제가 멋진 멘트를 추천해 드릴게요!", isFromUser = false))
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return

        viewModelScope.launch {
            _messages.value = _messages.value + Message(text, isFromUser = true)
            _isLoading.value = true

            // TODO: 실제 API 호출 로직
            delay(1500)
            val aiResponse = "AI 응답: \"${text}\" 상황에 대한 심쿵멘트입니다."

            _messages.value = _messages.value + Message(aiResponse, isFromUser = false)
            _isLoading.value = false
        }
    }
}