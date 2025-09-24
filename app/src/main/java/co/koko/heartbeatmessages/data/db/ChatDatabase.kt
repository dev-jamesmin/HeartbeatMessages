package co.koko.heartbeatmessages.data.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// 1. Entity: 데이터베이스 테이블에 저장될 데이터 객체
@Entity(tableName = "chat_messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    @ColumnInfo(name = "is_from_user") val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// 2. DAO (Data Access Object): 데이터베이스에 접근하여 CRUD 작업을 수행하는 인터페이스
@Dao
interface MessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearAllMessages()
}

// 3. Database: 데이터베이스의 전체적인 구조와 설정을 정의하는 클래스
@Database(entities = [MessageEntity::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getDatabase(context: Context): ChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}