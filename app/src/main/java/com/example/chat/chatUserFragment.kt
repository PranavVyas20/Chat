package com.example.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.adapters.MessageAdapter
import com.example.chat.databinding.FragmentChatUserBinding
import com.example.chat.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class chatUserFragment : Fragment() {
    private var _binding: FragmentChatUserBinding? = null
    private val binding get() = _binding
    private lateinit var roomsRef: CollectionReference
    private val args by navArgs<chatUserFragmentArgs>()
//    private lateinit var messagesQuery: Query
    private lateinit var senderRoomRef:CollectionReference
    private lateinit var receiverRoomRef:CollectionReference
    lateinit var msgsList: MutableList<Message>
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatUserBinding.inflate(inflater, container, false)

        senderRoomRef = Firebase.firestore.collection("senderRoom")
        receiverRoomRef = Firebase.firestore.collection("receiverRoom")

        Log.d("taggggggggg",args.currentUser.username + args.chatUser.username)


        val currentLoggedInUser = FirebaseAuth.getInstance().currentUser?.uid

        // empty message list initialised
        msgsList = mutableListOf()

        // set username of the user with whom we are chatting
        binding!!.chatUsername.text = args.chatUser.username

        // set up the adapter
        messageAdapter = MessageAdapter(msgsList)
        binding!!.chatUserRv.adapter = messageAdapter
        binding!!.chatUserRv.layoutManager = LinearLayoutManager(activity)

        Toast.makeText(activity, args.currentUser.uid, Toast.LENGTH_SHORT).show()

//        roomsRef = FirebaseFirestore.getInstance().collection("")
//        messagesQuery = Firebase.firestore.collection("rooms")
//            .document(args.currentUser.uid+args.chatUser.uid)
//

        // Handle send button click listener
        binding!!.sendMsgBtn.setOnClickListener {
            val msg: String = binding!!.msgInputEtv.text.toString()
            sendMessage(msg, args.chatUser.uid, args.currentUser.uid)
            binding!!.msgInputEtv.text.clear()
        }

//        Firebase.firestore.collection(args.currentUser.uid + args.chatUser.uid).orderBy("timeCreated",Query.Direction.ASCENDING).addSnapshotListener { querySnapshot, firestoreException ->
//            firestoreException?.let {
//                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
//                return@addSnapshotListener
//            }
//            val allMsgs = querySnapshot!!.toObjects(Message::class.java)
//            msgsList.clear()
//            msgsList.addAll(allMsgs)
//            messageAdapter.notifyDataSetChanged()
//        }

        Firebase.firestore.collection("rooms")
            .document(args.chatUser.uid+args.currentUser.uid)
            .collection("messages")
            .orderBy("timeCreated",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val allMsgs = value!!.toObjects(Message::class.java)
            msgsList.clear()
            msgsList.addAll(allMsgs)
            messageAdapter.notifyDataSetChanged()
        }


        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun sendMessage(msg: String, sendTo: String, sentFrom: String) {
        val time = System.currentTimeMillis().toString()
        val newMessage = Message(sendTo, sentFrom, msg, time)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Firebase.firestore.collection("rooms")
                    .document(args.currentUser.uid + args.chatUser.uid)
                    .collection("messages")
                    .add(newMessage)

                Firebase.firestore.collection("rooms")
                    .document(args.chatUser.uid + args.currentUser.uid)
                    .collection("messages")
                    .add(newMessage)


//                Firebase.firestore.collection(args.currentUser.uid + args.chatUser.uid)
//                    .add(newMessage)
//
//                Firebase.firestore.collection(args.chatUser.uid + args.currentUser.uid)
//                    .add(newMessage)

//                messagesRef.add(newMessage).addOnSuccessListener {
//                    Toast.makeText(activity, "msg sent succesfully", Toast.LENGTH_SHORT).show()
//                }
            } catch (e: Exception) {
                Toast.makeText(activity, "msg not sent succesfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

