package com.example.flo_clone

data class Auth(val userIdx: Int, val jwt : String)
data class AuthResponse (val isSuccess : Boolean, val code : Int, val message : String, val result : Auth?)