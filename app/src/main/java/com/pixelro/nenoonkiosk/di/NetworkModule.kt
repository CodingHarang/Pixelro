package com.pixelro.nenoonkiosk.di

import com.fasterxml.jackson.databind.ser.std.DateSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.harang.data.api.NenoonKioskApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class LocalDateSerializer : JsonSerializer<LocalDateTime?> {
    override fun serialize(
        localDate: LocalDateTime?,
        srcType: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(formatter.format(localDate))
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {

//        val gson: Gson = GsonBuilder()
//            .setLenient() // Gson은 JSON에 대해 엄격함. 해당 옵션으로 parser가 허용하는 항목에 대해 자유로움.
////            .registerTypeAdapter(LocalDate::class.java, DateSerializer()) // 날짜관련된
////            .registerTypeAdapter(LocalDateTime::class.java, DateSerializer())
////            .registerTypeAdapter(LocalDate::class.java, DateSerializer())
//            .registerTypeAdapter(LocalDateTime::class.java, LocalDateSerializer())
////            .registerTypeAdapter(OffsetDateTime::class.java, DateSerializer())
//            .setPrettyPrinting()
//            .create()



        return Retrofit.Builder()
            .baseUrl("https://6366a2a6-c93a-4568-b838-c9e8a117847d.mock.pstmn.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNenoonApi(retrofit: Retrofit): NenoonKioskApi {
        return retrofit.create(NenoonKioskApi::class.java)
    }
}