package co.koko.heartbeatmessages.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ApiService {
    @POST("heartbeatmessages/question")
    suspend fun postQuestion(
        @Body request: QuestionRequest
    ): AnswerResponse
}

@Serializable
data class QuestionRequest(
    val question: String
)

@Serializable
data class AnswerResponse(
    @SerialName("answer") // JSON의 'answer' 키와 매핑
    val answer: String
)

object RetrofitClient {
    private const val BASE_URL = "https://api.pumda.co/"

    private val json = Json { ignoreUnknownKeys = true }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}