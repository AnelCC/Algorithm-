package com.anelcc.simplerxjava.activities.networkexample

import com.anelcc.simplerxjava.modellayer.ModelLayer
import com.anelcc.simplerxjava.modellayer.entities.Message
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable

class NetworkExamplePresenter {
    private val modelLayer = ModelLayer.shared //normally injected

    val message: BehaviorRelay<List<Message>>
    get() = modelLayer.messages

    private var bag = CompositeDisposable()

    init {
        modelLayer.getMessages()
    }
}