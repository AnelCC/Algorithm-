package com.anelcc.simplerxjava.activities.networkexample

import com.anelcc.simplerxjava.modellayer.ModelLayer
import com.anelcc.simplerxjava.modellayer.entities.Message
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable

class NetworkExamplePresenter {
    private val modelLayer = ModelLayer.shared //normally injected
    private var bag = CompositeDisposable()

    val messages: BehaviorRelay<List<Message>>
        get() = modelLayer.messages

    init {
        modelLayer.getMessages()
    }
}