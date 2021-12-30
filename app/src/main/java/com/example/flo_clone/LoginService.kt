package com.example.flo_clone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/users/login")
    fun login(@Body user : User) : Call<AuthResponse>
}