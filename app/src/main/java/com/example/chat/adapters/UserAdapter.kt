package com.example.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.LatestChatUserItemBinding
import com.example.chat.models.User

class UserAdapter(val allUsersList:List<User>):RecyclerView.Adapter<UserAdapter.myViewHolder>() {

     class myViewHolder(val binding:LatestChatUserItemBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(LatestChatUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder:myViewHolder, position: Int) {
        val currentUser = allUsersList[position]
        holder.binding.apply {
            nameTextView.text = currentUser.username
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(currentUser) }
        }

    }

    override fun getItemCount(): Int {
        return allUsersList.size
    }

    // Related to on item clicked listener
    private var onItemClickListener:((User) -> Unit)? = null
    fun setOnItemClickListener(listener:(User) -> Unit){
        onItemClickListener = listener
    }
}