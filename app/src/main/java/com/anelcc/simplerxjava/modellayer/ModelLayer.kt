package com.anelcc.simplerxjava.modellayer

import com.anelcc.simplerxjava.modellayer.networklayer.NetworkLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PersistenceLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.jakewharton.rxrelay2.BehaviorRelay

class ModelLayer {

    companion object {
        val shared = ModelLayer()
    }

    //As we get data, that'll change and we can react to that, because it's a BehaviorRelay.
    val photoDescriptions = BehaviorRelay.createDefault(listOf<PhotoDescription>())

    private val networkLayer = NetworkLayer.instance
    private val persistenceLayer = PersistenceLayer.shared

    //Loading data from the database often can be a longer process,
    // so it is something that you would normally want to put on a background thread anyway.
    fun loadAllPhotoDescriptions() { //result may be immediate, but use async callbacks
        persistenceLayer.loadAllPhotoDescriptions { photoDescriptions ->
            // Anyone who's listening to our photoDescriptions BehaviorRelay, they'll be notified that new values.
            this.photoDescriptions.accept(photoDescriptions)
        }
    }
}