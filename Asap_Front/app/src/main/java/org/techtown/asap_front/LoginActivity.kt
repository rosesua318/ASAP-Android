package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBtn : Button
    private lateinit var signupBtn : Button
    private lateinit var edtID : EditText
    private lateinit var edtPW : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

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
            edtID.text.toString()
            edtPW.text.toString()

            // 로그인 확인 다이얼로그 출력
            val builder1 = AlertDialog.Builder(this@LoginActivity)
            builder1.setTitle("Login Success")
            builder1.setMessage(edtID.text.toString() + "님 환영합니다.")
            builder1.show()

            // 로그인 성공 시 메인화면으로 이동
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }
}