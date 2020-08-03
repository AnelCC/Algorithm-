package com.anelcc.simplerxjava.activities.networkexample.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.activities.databaseexample.recycler.ItemClickedlambda
import com.anelcc.simplerxjava.common.disposedBy
import com.anelcc.simplerxjava.modellayer.entities.Message
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class MessageViewAdapter(var onItemClicked: ItemClickedlambda): RecyclerView.Adapter<MessageViewHolder>() {

    internal val messages = BehaviorRelay.createDefault(listOf<Message>())
    private val bag = CompositeDisposable()

    init {
        messages.observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                notifyDataSetChanged()
            }.disposedBy(bag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        val viewHolder = MessageViewHolder(view)

        view.setOnClickListener { v -> onItemClicked(v, viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.value[position]
        holder.configureWith(message)
    }

    override fun getItemCount(): Int = messages.value.size

}