package com.jayashree.wordclock

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                et_username.error = resources.getString(R.string.email_required_message)
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                et_username.error = resources.getString(R.string.password)
                return@setOnClickListener
            }
            iv_progress.visibility = View.VISIBLE

            //Authenticate the user
            //Register user into Firebase
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    task -> if(task.isSuccessful){
                Toast.makeText(context, "Login Successful.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(LoginFragmentDirections.loginToDashboard())
                iv_progress.visibility = View.INVISIBLE
            }else {
                Toast.makeText(context, "Error! " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            }
        }

        tv_register.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.loginToRegister())
        }
    }

}