package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityLoginBinding
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InternalCoroutinesApi
class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            login()
        }
    }

//    private fun login() {
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginMailAddressEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.loginPasswordEt.toString().isEmpty()) {
//            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginMailAddressEt.text.toString()
//        val pwd : String = binding.loginPasswordEt.text.toString()
//
//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.UserDao().getUser(email, pwd)
//
//        user?.let{
//            Log.d("getuser", "userid: ${user.id},${user}")
//            // 발급받은 jwt를 저장해주는 함수
//            saveJwt(user.id)
//        }
//
//        if (user == null) {
//            Toast.makeText(this, "유저 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun login() {
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginMailAddressEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.loginPasswordEt.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginMailAddressEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()
        val loginService = retrofit.create(LoginService::class.java)

        loginService.login(getUser()).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resp = response.body()!!

                when(resp.code){
                    1000 -> {
                        resp.result?.let { saveJwt(it.jwt) }
                        startMainActivity()
                    }
                    2015, 2019, 3014 -> {
                        binding.loginEmailErrorTv.visibility = View.VISIBLE
                        binding.loginEmailErrorTv.text = resp.message.toString()
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUser() : User {
        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginMailAddressEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

        return User(email, pwd, null)
    }

    private fun saveJwt(jwt : String) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}