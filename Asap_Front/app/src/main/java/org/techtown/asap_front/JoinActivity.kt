package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Types.NULL

class JoinActivity : AppCompatActivity() {
    private lateinit var edtID : EditText
    private lateinit var edtPW : EditText
    private lateinit var edtCheckPw : EditText
    private lateinit var edtName : EditText
    private lateinit var edtNum : EditText
    private lateinit var signBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_activity)

        edtID = findViewById(R.id.edit_id)
        edtPW = findViewById(R.id.edit_pw)
        edtCheckPw = findViewById(R.id.edit_ckpw)
        edtName = findViewById(R.id.edit_name)
        edtNum = findViewById(R.id.edit_num)
        signBtn = findViewById(R.id.signupBtn)

        // 회원가입 버튼 클릭 시
        signBtn.setOnClickListener {
            if(edtID.text.toString().isEmpty() or edtPW.text.toString().isEmpty()or edtCheckPw.text.toString().isEmpty()or edtName.text.toString().isEmpty()or edtNum.text.toString().isEmpty()){
                Toast.makeText(this@JoinActivity,
                    "입력하지않은 정보가 있습니다. 모두 채워주세요.", Toast.LENGTH_LONG).show()
            }
            // 회원가입 성공 시
            else {
                Toast.makeText(this@JoinActivity,
                    "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}