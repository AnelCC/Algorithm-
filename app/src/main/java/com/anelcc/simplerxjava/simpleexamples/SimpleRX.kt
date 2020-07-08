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

        // the declarative nature of RxJava.
        // we can tell RxJava what we want, but we don't tell it how to get it.
        // So in this case, we want to subscribe to the event, but we're not gonna ask for it
        // specifically like we did on line 16. Okay, so I'll say someInfo.subscribe,
        // which just means I want to see the new event or the new value coming through.
        // And we'll just say newValue and we will literally print out that value.

        someInfo.subscribe { newValue ->
            println("🦄 value has changed: $newValue")
        }


        //NOTE: Relays will never receive onError, and onComplete events
        someInfo.accept("3")

    }
}