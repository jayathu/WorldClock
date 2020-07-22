package com.jayashree.wordclock

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private var userid: String = ""
    private val TAG = "TAG-CLOCK"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        if(auth.currentUser != null) {
            findNavController().navigate(RegisterFragmentDirections.registerToDashboard())
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
            val name = et_name.text.toString()


            if(TextUtils.isEmpty(email)){
                et_username.error = resources.getString(R.string.email_required_message)
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                et_password.error = resources.getString(R.string.pwd_required_message)
                return@setOnClickListener
            }
            if(password.length < 6){
                et_password.error = resources.getString(R.string.pwd_six_chars)
                return@setOnClickListener
            }

            iv_progress.visibility = View.VISIBLE

            //Register user into Firebase
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    task -> if(task.isSuccessful){
                Toast.makeText(context, "User Created.", Toast.LENGTH_SHORT).show()
                userid = auth.currentUser?.uid ?: ""
                //create or get the existing collection from the database
                val documentReference = fireStore.collection("users").document(userid)
                val userdata = hashMapOf("full name" to name, "email" to email, "role" to "user")
                documentReference.set(userdata).addOnSuccessListener {
                        ref -> Log.d(TAG, "User profile is created for $userid")
                }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error adding document", e)
                    }

                findNavController().navigate(RegisterFragmentDirections.registerToDashboard())

                iv_progress.visibility = View.INVISIBLE
            }else {
                Toast.makeText(context, "Error! " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            }
        }

        alreadyRegistered.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.reisterToLogin())
        }
    }

}