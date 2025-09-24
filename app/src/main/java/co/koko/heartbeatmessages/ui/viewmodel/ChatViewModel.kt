import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.koko.heartbeatmessages.data.Message
import co.koko.heartbeatmessages.data.QuestionRequest
import co.koko.heartbeatmessages.data.RetrofitClient
import co.koko.heartbeatmessages.data.db.ChatDatabase
import co.koko.heartbeatmessages.data.db.MessageDao
import co.koko.heartbeatmessages.data.db.MessageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ui/viewmodel/ChatViewModel.kt

// [수정] ViewModel이 Application을 받을 수 있도록 생성자 변경
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    // [수정] MessageDao 인스턴스 추가
    private val messageDao: MessageDao

    // [수정] _messages StateFlow를 DB와 연결
    val messages: StateFlow<List<Message>>

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        val database = ChatDatabase.getDatabase(application)
        messageDao = database.messageDao()

        // DB의 모든 메시지를 Flow로 구독하고, MessageEntity를 Message로 변환
        messages = messageDao.getAllMessages()
            .map { entities ->
                // 초기 메시지가 DB에 없으면 추가
                if (entities.isEmpty()) {
                    val initialMessage = MessageEntity(
                        text = "어떤 상황인지 알려주시면, 제가 멋진 멘트를 추천해 드릴게요!",
                        isFromUser = false
                    )
                    messageDao.insertMessage(initialMessage)
                }
                entities.map { Message(it.text, it.isFromUser) }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || _isLoading.value) return

        viewModelScope.launch {
            // 1. 사용자 메시지를 DB에 저장
            messageDao.insertMessage(MessageEntity(text = text, isFromUser = true))
            _isLoading.value = true

            try {
                val response = RetrofitClient.api.postQuestion(QuestionRequest(question = text))
                // 2. AI 응답을 DB에 저장
                messageDao.insertMessage(MessageEntity(text = response.answer, isFromUser = false))
            } catch (e: Exception) {
                messageDao.insertMessage(MessageEntity(text = "죄송해요, 지금은 답변을 드릴 수 없어요.", isFromUser = false))
            } finally {
                _isLoading.value = false
            }
        }
    }
}