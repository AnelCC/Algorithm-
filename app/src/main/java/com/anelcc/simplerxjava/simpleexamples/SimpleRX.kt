package com.anelcc.simplerxjava.simpleexamples

import com.anelcc.simplerxjava.common.disposedBy
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


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

    /*
    * The next thing for us to look at, is a behavior subject.
    * Subject that emits the most recent item it has observed
    * and all subsequent observed items to each subscribed Observer.
    * Subjects can act as both an Observer and an Observable.
    * Subjects are considered as HOT Observables.
    * */
    fun subjects() {
        /*
        * Create a subject and emit a single integer from the subject.
        * (Subject is acting as an Observable in this case)
        * */
        val behaviorSubject = BehaviorSubject.createDefault(24)

        /*
        *Subscribe to the Subject - emissions from the subject will be printed
        * */
        val disposable = behaviorSubject.subscribe({ newValue -> //onNext
            println("🕺 behaviorSubject subscription: $newValue")
        }, { error -> //onError
            println("🕺 error: ${ error.localizedMessage }")
        }, { //onCompleted
            println("🕺 completed")
        }, { disposable -> //onSubscribed
            println("🕺 subscribed")
        })

        /*
        * Again emit values from subject.
        * */
        //we pushed 34, 48, 48, and you can see that in the logs below,
        // that those values come through to that subscription block as well.
        behaviorSubject.onNext(34)
        behaviorSubject.onNext(48)
        behaviorSubject.onNext(48) //duplicates show as new events by default

        /*
        * Error handling:
        * When the onError(Throwable) is called, the BehaviorSubject enters into a terminal state
        * and emits the same Throwable instance to the last set of Observers. During this emission,
        * if one or more Observers dispose their respective Disposables,
        * the Throwable is delivered to the global error handler via RxJavaPlugins.onError(Throwable)
        * (multiple times if multiple Observers cancel at once).
        * If there were no Observers subscribed to this BehaviorSubject when the onError() was called,
        * the global error handler is not invoked.
        * */

        // observer will receive only onError
        /*val someException = IllegalArgumentException("some fake error")
        behaviorSubject.onError(someException)
        behaviorSubject.onNext(109) //will never show*/

        /*
        * Description copied from interface: Observer
        * Notifies the Observer that the Observable has finished sending push-based notifications.
        * The Observable will not call this method if it calls Observer.onError(java.lang.Throwable).*/

        // observer will receive only onComplete
        behaviorSubject.onComplete()
        behaviorSubject.onNext(10983) //will never show
    }

    fun basicObservable() {
        //The observable
        val observable = Observable.create<String> { observer ->
            //This lambda is called for every subscriber - by default
            println("🍄 ~~ Observable logic being triggered ~~")

            //Do work on a background thread
            CoroutineScope(Dispatchers.IO).launch  {
                delay(1000) //artificial delay 1 second

                observer.onNext("some value 23")
                observer.onComplete()
            }
        }

        //understand that this observable will be disposed with everything else in that bag.
        observable.subscribe { someString ->
            println("🍄 new value: $someString")
        }.disposedBy(bag)

        val observer = observable.subscribe { someString ->
            println("🍄 Another subscriber: $someString")
        }

        observer.disposedBy(bag)
    }

    //Easy methods to create simple values
    // There's lots of different ways to create these simple one-off values, and when would you use this?
    // Well, I think having a list of user IDs or something that you want to push into a stream over time,
    // that's a great time when you would use this. Otherwise, you would create an observable the way that we did in this basic observable.
    //e.g where we're creating this whole lambda, and it actually does some work underneath.
    fun creatingObservables () {
        //It can determine the value by what you pass in. It can determine the value by what you pass in.
        // All of these are just generic containers.
        //val observable = Observable.just("ANEL Elizabeth")

        //You could also use a timer
        //val observable = Observable.interval(300, TimeUnit.MICROSECONDS).timeInterval(AndroidSchedulers.mainThread())

        //Also, arrays of values are pretty normal for you to work with.
        val userIds = arrayOf(1,2,3,4,5,6)
        //val observable = Observable.fromArray(*userIds)
        val observable = userIds.toObservable()
    }
}