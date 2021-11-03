package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var btn_register : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        btn_register = findViewById(R.id.btn_register);

        // 회원가입 버튼
        btn_register.setOnClickListener {
            var intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}