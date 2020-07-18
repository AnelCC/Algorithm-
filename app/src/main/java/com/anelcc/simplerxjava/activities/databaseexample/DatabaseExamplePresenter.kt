package com.anelcc.simplerxjava.activities.databaseexample

import com.anelcc.simplerxjava.modellayer.ModelLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//We can consume BehaviorRelay here.
class DatabaseExamplePresenter {
    val modelLayer = ModelLayer.shared //normally injected

    // TWe don't have to write a lot of logic around creating new BehaviorRelays on each level.
    // We can just return the one on the lowest level. And as long as we are managing our threading correctly,
    // observing our main thread, we shouldn't have any problems.
    val photoDescriptions: BehaviorRelay<List<PhotoDescription>>
        get() = modelLayer.photoDescriptions //bubbling up for the lower layers

    //I'm gonna put an init block, kick this onto a new thread, and I'll just say delay for three seconds.
    init {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000) //3 seconds
            // And I'm gonna tell the model layer to load all the photo descriptions from the database at that point.
            // The model layer will use the persistence layer, get all those descriptions,
            // We'll get a list back, and then we will bind that to our photoDescriptions BehaviorRelay.
            //That'll just bubble up to the presenter and really to anyone who's observing on it.
            modelLayer.loadAllPhotoDescriptions()
        }
    }

}