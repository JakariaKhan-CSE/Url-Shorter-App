package com.example.glyde.data

import com.example.glyde.data.local.HistoryDao
import com.example.glyde.data.local.HistoryEntity
import com.example.glyde.data.remote.UrlShortenerApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UrlRepository(private val historyDao: HistoryDao) {

    private val api: UrlShortenerApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://is.gd/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UrlShortenerApi::class.java)
    }

    suspend fun shortenUrl(longUrl: String): Result<String> {
        return try {
            val response = api.shortenUrl(url = longUrl)
            if (response.shorturl != null) {
                historyDao.insertHistory(HistoryEntity(originalUrl = longUrl, shortUrl = response.shorturl))
                Result.success(response.shorturl)
            } else {
                Result.failure(Exception(response.errormessage ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getHistory(): Flow<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }
}
