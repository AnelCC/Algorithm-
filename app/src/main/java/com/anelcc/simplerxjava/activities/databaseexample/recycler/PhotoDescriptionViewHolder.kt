package com.anelcc.simplerxjava.activities.databaseexample.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.squareup.picasso.Picasso


class PhotoDescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val context = itemView.context
    val cardView: CardView
    val photoDescriptionImageView: ImageView
    val titleTextView: TextView
    val photoDescriptionInfoTextView: TextView

    init {
        cardView = itemView.findViewById(R.id.photoDescriptionCardView)
        photoDescriptionImageView = itemView.findViewById(R.id.photoDescriptionImageView)
        titleTextView = itemView.findViewById(R.id.photoDescriptionTitleTextView)
        photoDescriptionInfoTextView = itemView.findViewById(R.id.photoDescriptionInfoTextView)
    }

    fun configureWith(photoDescription: PhotoDescription) {

        titleTextView.text = photoDescription.title
        photoDescriptionInfoTextView.text = photoDescription.url

        Picasso.get()
                .load(photoDescription.thumbnailUrl)
                .resize(100, 100)
                .centerCrop()
                .into(photoDescriptionImageView)

    }
}