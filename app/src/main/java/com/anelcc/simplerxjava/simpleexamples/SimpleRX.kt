package com.anelcc.simplerxjava.simpleexamples

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.disposables.CompositeDisposable


//What is a disposable?
// when we create a subscription, we're given something like a pointer to that subscription
// so that when the life of this class or something dies, we can clean up all
// the subscriptions contained in that composite disposable.
object SimpleRX {
    var bag = CompositeDisposable()

    fun simpleValues() {
        println("~~~~~~simpleValues~~~~~~")

        val someInfo = BehaviorRelay.createDefault("1")
        println("🙈 someInfo.value ${ someInfo.value }")

        val plainString = someInfo.value
        println("🙈 plainString: $plainString")

        someInfo.accept("2")
        println("🙈 someInfo.value ${ someInfo.value }")

    }
}