package org.techtown.asap_front

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.techtown.asap_front.data_object.related_user
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Types.NULL

class JoinActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface : RetrofitInteface
    private lateinit var edtID : EditText
    private lateinit var edtPW : EditText
    private lateinit var edtCheckPw : EditText
    private lateinit var edtName : EditText
    private lateinit var edtNum : EditText
    private lateinit var edtAge : EditText
    private lateinit var rGroupSex : RadioGroup
    private lateinit var rButton1 : RadioButton
    private lateinit var rButton2 : RadioButton
    private lateinit var signBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_activity)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        edtID = findViewById(R.id.edit_id)
        edtPW = findViewById(R.id.edit_pw)
        edtCheckPw = findViewById(R.id.edit_ckpw)
        edtName = findViewById(R.id.edit_name)
        edtNum = findViewById(R.id.edit_num)
        edtAge = findViewById(R.id.edit_age)
        rGroupSex = findViewById(R.id.rGroup)
        rButton1 = findViewById(R.id.female)
        rButton2 = findViewById(R.id.male)
        signBtn = findViewById(R.id.signupBtn)

        val sex_ : Int
        if(rButton1.isChecked){ sex_ = 0 }
        else { sex_ = 1 }

        // 회원가입 버튼 클릭 시
        signBtn.setOnClickListener {
            if(edtID.text.toString().isEmpty() or edtPW.text.toString().isEmpty()or edtCheckPw.text.toString().isEmpty()or edtName.text.toString().isEmpty()or edtNum.text.toString().isEmpty()){
                Toast.makeText(this@JoinActivity,
                    "입력하지않은 정보가 있습니다.\n모두 채워주세요.", Toast.LENGTH_LONG).show()
            }
            // 회원가입 성공 시
            else {
                val map = JoinSend(edtName.text.toString()," ", "null",related_user(edtNum.text.toString(),edtID.text.toString(),edtPW.text.toString(),edtAge.text.toString().toInt(), sex_, edtPW.text.toString()))
                println(map) // 보내는 데이터 확인

                val call = retrofitInterface.executeSignup(map)

                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.code() == 201) {
                            Toast.makeText(this@JoinActivity,
                                    "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@JoinActivity, "응답코드 : "+response.code().toString() + " , 회원가입 실패 "+response.errorBody()+response.message()+response.raw()+response.headers(),
                                    Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(this@JoinActivity, t.message,
                                Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }
}