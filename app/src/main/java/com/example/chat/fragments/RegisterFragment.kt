package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chat.activities.ChatActivity
import com.example.chat.databinding.FragmentRegisterBinding
import com.example.chat.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    lateinit var auth: FirebaseAuth
    private lateinit var userCollectionRef: CollectionReference
    private val binding get() = _binding!! // Use this !!!!!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        userCollectionRef = Firebase.firestore.collection("users")

        val firebaseUser = auth.currentUser

        if(firebaseUser!=null){
            goToHomeActivity()
        }

        // Navigate to login fragment
        binding.goToLoginTv.setOnClickListener {
            if(findNavController().previousBackStackEntry != null){
                findNavController().popBackStack()
            }else{
                findNavController().navigate(RegisterFragmentDirections
                    .actionRegisterFragmentToLoginFragment())
            }
        }

        // Register user
        binding.btnRegister.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val username = binding.inputUsername.text.toString()
            registerUser(email,password, username)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function to register user
    private fun registerUser(email:String,password:String,username:String) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener {
                    if (it.isSuccessful) {
                        // if registered succesfully then save the user in the db as well
                            Toast.makeText(activity,auth.currentUser!!.uid,Toast.LENGTH_SHORT).show()
                        saveUserToDb(email, password, auth.currentUser!!.uid)
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // Function to save user to firestore
    private fun saveUserToDb(email: String,username:String,uid:String){
        val newUser = User(email,username,uid)
        auth.currentUser?.let {
            userCollectionRef.document(it.uid).set(newUser).addOnCompleteListener(OnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(activity,"User saved in db", Toast.LENGTH_SHORT).show()
                    // when user saved in the db then navigate to Home Activity
                    goToHomeActivity()
                } else{
                    Toast.makeText(activity,"Error saving in db", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Function to go to home activity with intent
    private fun goToHomeActivity(){
        val intent = Intent(activity, ChatActivity::class.java)
        // Clear back stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}