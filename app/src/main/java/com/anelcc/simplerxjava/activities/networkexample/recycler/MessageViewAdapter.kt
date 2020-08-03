package com.anelcc.simplerxjava.activities.networkexample.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.activities.databaseexample.recycler.ItemClickedlambda
import com.anelcc.simplerxjava.modellayer.entities.Message


class MessageViewAdapter(var onItemClicked: ItemClickedlambda): RecyclerView.Adapter<MessageViewHolder>() {
    internal var messages = listOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        val viewHolder = MessageViewHolder(view)

        view.setOnClickListener { v -> onItemClicked(v, viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.configureWith(message)
    }

    override fun getItemCount(): Int = messages.size

}