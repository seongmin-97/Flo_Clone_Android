package com.example.flo_clone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("/users")
    fun signUp(@Body user: User) : Call<AuthResponse>
}