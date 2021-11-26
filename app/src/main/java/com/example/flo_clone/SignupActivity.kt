package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySignupBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SignupActivity : AppCompatActivity() {
    lateinit var  binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            signUp()
        }
    }

    // 유저가 입력한 정보 가져오기
    private fun getUser() : User {
        val email : String = binding.signupIdEt.text.toString() + "@" + binding.signupMailAddressEt.text.toString()
        val pwd : String = binding.signupPasswordEt.text.toString()

        return User(email, pwd)
    }

    // 회원 가입 진행행
    private fun signUp() {
        if(binding.signupIdEt.text.toString().isEmpty() || binding.signupMailAddressEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signupPasswordEt.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signupPasswordEt.text.toString() != binding.signupPasswordConfirmEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.UserDao().insert(getUser())

        val users = userDB.UserDao().getUsers()
        Log.d("Singup", users.toString())
        finish()
    }
}