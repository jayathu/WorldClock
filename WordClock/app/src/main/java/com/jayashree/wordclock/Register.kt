package com.jayashree.wordclock

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*


class Register : AppCompatActivity() {

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
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        if(auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        et_name.setOnClickListener {
            et_name.setText("")
        }

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
                et_username.setError("Email is required.")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                et_password.setError("Password is required!")
                return@setOnClickListener
            }
            if(password.length < 6){
                et_password.setError("Password must be atleast 6 characters long")
                return@setOnClickListener
            }

            iv_progress.visibility = View.VISIBLE

            //Register user into Firebase
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                task -> if(task.isSuccessful){
                Toast.makeText(this, "User Created.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                iv_progress.visibility = View.INVISIBLE
            }else {
                Toast.makeText(this, "Error! " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            }
        }

        alreadyRegistered.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}