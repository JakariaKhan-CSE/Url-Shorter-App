package com.example.glyde.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface UrlShortenerApi {
    @GET("create.php")
    suspend fun shortenUrl(
        @Query("format") format: String = "json",
        @Query("url") url: String
    ): ShortenResponse
}

data class ShortenResponse(
    val shorturl: String?,
    val errorcode: Int?,
    val errormessage: String?
)
