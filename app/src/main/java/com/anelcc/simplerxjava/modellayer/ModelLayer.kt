package com.anelcc.simplerxjava.modellayer

import com.anelcc.simplerxjava.modellayer.entities.Message
import com.anelcc.simplerxjava.modellayer.entities.Person
import com.anelcc.simplerxjava.modellayer.networklayer.NetworkLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PersistenceLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single

//Now the model layer for us has our network piece and our persistent piece which really is our database portion of this.
//It doesn't know where, it doesn't care, it just wants whatever data comes back from either of those layers.
class ModelLayer {

    companion object {
        val shared = ModelLayer()
    }

    //As we get data, that'll change and we can react to that, because it's a BehaviorRelay.
    val photoDescriptions = BehaviorRelay.createDefault(listOf<PhotoDescription>())
    //And all this message is, is an entity that has id, body, title and userId.
    val messages = BehaviorRelay.createDefault(listOf<Message>())

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

    //We will just return whatever comes back from the network layer.getMessages
    // and then we're gonna pass it a couple of lamdas to work with.
    // So if it's successful, we'll handle whatever messages come back.
    fun getMessages() {
        return networkLayer.getMessages({ messages ->
            this.messages.accept(messages)
        }, { errorMessage ->
            notifyOfError(errorMessage)
        })
    }

    private fun notifyOfError(errorMessage: String) {
        //notify user somehow
        println("❗️ Error occurred: $errorMessage")
    }

    //New Way use a way more reactive process
    fun getMessagesRx(): Single<List<Message>> {
        return networkLayer.getMessagesRx()
    }

    fun loadInfoFor(people: List<Person>): Observable<List<String>> {
        return networkLayer.loadInfoFor(people)
    }
}