package com.jonbott.learningrxjava.SimpleExamples

import com.anelcc.simplerxjava.common.disposedBy
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

object TraitsRx {
    var bag = CompositeDisposable()


    //Single is really similar to an observable.
    // In fact, if you look at how it's created on, It's gonna be the same type of thing.
    // The only distinction between these two is "single.onSuccess("nice work!")"
    // where we have an on success event as opposed to in our observable we have,.
    fun traits_single() {
        val single = Single.create<String> { single ->
            //do some logic here
            val success = true


            //That's because observers are meant to have one or more values, but with the implication that there might be more than one value.
            // A single will only have one value.
            // So, instead of this on next and this on complete we're going to have this on success.
            if (success) { //return a value
                single.onSuccess("nice work!")
            } else {
                val someException = IllegalArgumentException("some fake error")
                single.onError(someException)
            }
        }

        single.subscribe({ result ->
                //do something with result
            println("👻 single: ${ result }")
        }, { error ->
            //do something for error
        }).disposedBy(bag)
    }

    //You need to determine if this was successful or not.
    // And completables don't have values. They either complete or they error.
    // You would use a completable, say,
    // when you're doing a post and there's no results coming back other than a success code,
    // a 200, or maybe some sort of failure code.
    fun traits_completable() {

        val completable = Completable.create { completable ->
            //do logic here
            val success = true

            if (success) {
                completable.onComplete()
            } else {
                val someException = IllegalArgumentException("some fake error")
                completable.onError(someException)
            }
        }

        completable.subscribe({
            //handle on complete
            println("👻 Completable completed")
        }, { error ->
            //do something for error
        }).disposedBy(bag)

    }

    //Maybe can have three different states.
    // It can have a result that we're gonna return back, or it can just complete, or it can error out
    fun traits_maybe() {
        val maybe = Maybe.create<String> { maybe ->
            //do something
            val success = true
            val hasResult = true


            if (success) {
                if (hasResult) {
                    maybe.onSuccess("some result")
                } else {
                    maybe.onComplete()
                }
            } else {
                val someException = IllegalArgumentException("some fake error")
                maybe.onError(someException)
            }
        }

        maybe.subscribe({ result ->
            //do something with result
            println("👻 Maybe - result: ${ result }")
        }, { error ->
            //do something with the error
        }, {
            //do something about completing
            println("👻 Maybe - completed")
        }).disposedBy(bag)
    }
}