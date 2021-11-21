package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface
    private lateinit var loginBtn : Button
    private lateinit var signupBtn : Button
    private lateinit var edtID : EditText
    private lateinit var edtPW : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        signupBtn = findViewById(R.id.btn_register)
        loginBtn = findViewById(R.id.btn_login)
        edtID = findViewById(R.id.edit_id)
        edtPW = findViewById(R.id.edit_pw)

        // 회원가입 버튼
        signupBtn.setOnClickListener {
            var intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        loginBtn.setOnClickListener {

            val map = HashMap<String, String>()
            map.put("login_ID", edtID.text.toString())
            map.put("login_PW", edtPW.text.toString())

            val call = retrofitInterface.executeLogin(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (response.code() == 200) {
                        // 로그인 확인 다이얼로그 출력
                        val builder1 = AlertDialog.Builder(this@LoginActivity)
                        builder1.setTitle("Login Success")
                        builder1.setMessage(edtID.text.toString() + "님 환영합니다.")
                        builder1.show()

                        // 로그인 성공 시 메인화면으로 이동
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("ID", edtID.text.toString()) // 메인화면으로 아이디 전송
                        startActivityForResult(intent, 0)
                    }
                    else {
                        Toast.makeText(this@LoginActivity, "응답코드 : " + response.code().toString() +", 아이디와 비밀번호를 다시 확인하세요.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message,
                        Toast.LENGTH_LONG).show()
                }
            })

        }
    }
}