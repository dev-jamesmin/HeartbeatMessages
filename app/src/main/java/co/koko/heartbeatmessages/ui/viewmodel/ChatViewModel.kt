package co.koko.heartbeatmessages.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.koko.heartbeatmessages.data.Message
import co.koko.heartbeatmessages.data.QuestionRequest
import co.koko.heartbeatmessages.data.RetrofitClient
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
            // [수정] StateFlow의 값 변경을 올바르게 감지하도록 새 리스트를 생성하여 할당
            _messages.value = _messages.value + Message(text, isFromUser = true)
            _isLoading.value = true

            try {
                val response = RetrofitClient.api.postQuestion(QuestionRequest(question = text))
                _messages.value = _messages.value + Message(response.answer, isFromUser = false)
            } catch (e: Exception) {
                _messages.value = _messages.value + Message("죄송해요, 지금은 답변을 드릴 수 없어요.", isFromUser = false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}