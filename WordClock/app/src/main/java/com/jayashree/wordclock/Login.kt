package com.jayashree.wordclock

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        et_username.setOnClickListener {
            et_username.setText("")
        }

        et_password.setOnClickListener {
            et_password.setText("")
        }

        btn_register.setOnClickListener {
            val email = et_username.text.toString().trim()
            val password = et_password.text.toString().trim()

            if(TextUtils.isEmpty(email)){
                et_username.setError("Email is Required.")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                et_username.setError("Password is Required.")
                return@setOnClickListener
            }
            iv_progress.visibility = View.VISIBLE

            //Authenticate the user

            //Register user into Firebase
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    task -> if(task.isSuccessful){
                Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                iv_progress.visibility = View.INVISIBLE
            }else {
                Toast.makeText(this, "Error! " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            }
        }

        tv_register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}