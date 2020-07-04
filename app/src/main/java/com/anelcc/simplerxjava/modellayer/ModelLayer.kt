package com.anelcc.simplerxjava.modellayer

import com.anelcc.simplerxjava.modellayer.networklayer.NetworkLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PersistenceLayer

class ModelLayer {

    companion object {
        val shared = ModelLayer()
    }

    private val networkLayer = NetworkLayer.instance
    private val persistenceLayer = PersistenceLayer.shared
}