package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySignupBinding
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val name : String = binding.signupNicknameEt.text.toString()

        return User(email, pwd, name)
    }

    // 회원 가입 진행행
/*    private fun signUp() {
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
    }*/

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

        if(binding.signupNicknameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 45분부터
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()

        val signUpService = retrofit.create(SignupService::class.java)

        signUpService.signUp(getUser()).enqueue(object : Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resp = response.body()!!

                Log.d("signup", resp.toString())

                when(resp.code) {
                    1000 -> finish()
                    2016, 2017 -> {
                        binding.signupEmailErrorTv.visibility = View.VISIBLE
                        binding.signupEmailErrorTv.text = resp.message
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("signup", "fail")
            }
        })


    }
}