package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.activities.LoginRegActivity
import com.example.chat.adapters.UserAdapter
import com.example.chat.databinding.FragmentAllUsersBinding
import com.example.chat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.fragment_all_users.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AllUsersFragment : Fragment() {
    var _binding:FragmentAllUsersBinding? = null
    private val binding get() = _binding
    lateinit var firestoreRef:FirebaseFirestore
    lateinit var myAdapter:UserAdapter
    private val args by navArgs<AllUsersFragmentArgs>()
    lateinit var usersRef:Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllUsersBinding.inflate(inflater, container, false)

        Toast.makeText(activity,args.currentUser.username,Toast.LENGTH_SHORT).show()

        // by default empty list of users
        val allUserList = mutableListOf<User>()
        myAdapter = UserAdapter(allUserList)
        binding!!.allUsersRv.layoutManager = LinearLayoutManager(activity)
        binding!!.allUsersRv.adapter = myAdapter

        firestoreRef = FirebaseFirestore.getInstance()
        usersRef  = firestoreRef.collection("users").whereNotEqualTo("uid",args.currentUser.uid)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                usersRef.get().addOnSuccessListener {
                    val tempList:List<User> = it.toObjects(User::class.java)
                    allUserList.clear()
                    allUserList.addAll(tempList)
                    myAdapter.notifyDataSetChanged()
                    Log.d("taggggg", tempList.toString())
                }
            }catch (e:Exception){
                Toast.makeText(activity,"error fetching users",Toast.LENGTH_SHORT).show()
            }
        }

        // Handle on user_item clicked
        myAdapter.setOnItemClickListener {
            val action = AllUsersFragmentDirections.actionAllUsersFragmentToChatUserFragment(it,args.currentUser)
            Toast.makeText(activity,it.username,Toast.LENGTH_SHORT).show()
            findNavController().navigate(action)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}