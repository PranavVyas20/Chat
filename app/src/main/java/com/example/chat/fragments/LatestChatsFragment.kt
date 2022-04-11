package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chat.fragments.LatestChatsFragmentDirections
import com.example.chat.R
import com.example.chat.activities.LoginRegActivity
import com.example.chat.databinding.FragmentAllUsersBinding
import com.example.chat.databinding.FragmentLatestChatsBinding
import com.example.chat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LatestChatsFragment : Fragment() {
    var _binding: FragmentLatestChatsBinding? = null
   lateinit var currentLoggedinUser: User
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentLatestChatsBinding.inflate(inflater, container, false)

        val currentLoggedinUserId = FirebaseAuth.getInstance().currentUser?.uid

        // Get object of current logged in user
        CoroutineScope(Dispatchers.IO).launch {
            Firebase.firestore.collection("users").document(currentLoggedinUserId!!).get()
                .addOnSuccessListener {
                    currentLoggedinUser = it.toObject(User::class.java)!!
                    Toast.makeText(activity, currentLoggedinUser.username,Toast.LENGTH_SHORT).show()
                }
        }

        // handle button click to go to all users fragment
        binding!!.floatingActionButton.setOnClickListener {
            if(currentLoggedinUser!=null){
                val action = LatestChatsFragmentDirections.actionLatestChatsFragmentToAllUsersFragment(
                    currentLoggedinUser
                )
                findNavController().navigate(action)
            }
        }

        // Handle signout button
        binding!!.signoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginRegActivity::class.java)
            startActivity(intent)
        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}