package com.example.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ItemMessageBinding
import com.example.chat.models.Message
import com.example.chat.models.User

class MessageAdapter(val msgsList:List<Message>): RecyclerView.Adapter<MessageAdapter.myViewHolder>() {

    class myViewHolder(val binding:ItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.myViewHolder {
        return myViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentMsg = msgsList[position]
        holder.binding.apply {
            messageBox.text = currentMsg.msgText
        }
    }

    override fun getItemCount(): Int {
        return msgsList.size
    }

    // Related to on item clicked listener
    private var onItemClickListener:((User) -> Unit)? = null
    fun setOnItemClickListener(listener:(User) -> Unit){
        onItemClickListener = listener
    }




}