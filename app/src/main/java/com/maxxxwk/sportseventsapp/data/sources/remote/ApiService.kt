package com.maxxxwk.sportseventsapp.data.sources.remote

import com.maxxxwk.sportseventsapp.data.models.SportResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/demo")
    suspend fun getSportsEvents(): List<SportResponse>
}

