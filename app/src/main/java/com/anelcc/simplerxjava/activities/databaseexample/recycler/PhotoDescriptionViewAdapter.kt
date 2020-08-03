package com.anelcc.simplerxjava.activities.databaseexample.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

typealias ItemClickedlambda = (v: View, position: Int) -> Unit

//Consume in the ViewAdapter for the list in the database.
class PhotoDescriptionViewAdapter(var onItemClicked: ItemClickedlambda): RecyclerView.Adapter<PhotoDescriptionViewHolder>() {

    //Added BehaviorRelay, so that we can start reacting to those new events.
    internal var photoDescriptions = BehaviorRelay.createDefault(mutableListOf<PhotoDescription>())
    private val bag = CompositeDisposable()

    //sWe're gonna add an observer on that PhotoDescriptions.
    // It's basically, if anyone makes any changes to this,
    // we'll notify this adapter to update its dataset, which will update the list view.
    init {
        photoDescriptions.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                notifyDataSetChanged()
            }.disposedBy(bag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_description, parent, false)
        val viewHolder = PhotoDescriptionViewHolder(view)

        view.setOnClickListener { v -> onItemClicked(v, viewHolder.adapterPosition) }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PhotoDescriptionViewHolder, position: Int) {
        //because we've changed photoDescriptions via BehaviorRelay, we need to get the value out of it.
        val photoDescription = photoDescriptions.value[position]
        holder.configureWith(photoDescription)
    }

    //Use value instead of just using it as a variable directly
    override fun getItemCount(): Int = photoDescriptions.value.size

}