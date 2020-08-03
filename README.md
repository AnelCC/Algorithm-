# Algorithm-
RxJava can be simply thought of as a library that gives us an easy to use observer pattern. This means that we have a process to be notified of when data changes, when a task completes, or if there are any errors that happen along the way. 

 
 Observables
 They're two closely related concepts, and you can think of observables as a list of values that you can iterate over. Or really, a list of single events with those values. They are the things that you are watching. This list of values or events changes over time, and you can react to that. You might also hear observers called subscribers, or consumers. And in this course, I'll just call them subscribers. For Rx Java, they are effectively the same thing. Though, in some other flavors of ReactiveX, there are some slight distinctions. 
 
 Subscribers are the things that are doing the watching. The watcher sounds pretty creepy, but all it means is the subscribers just care about when the observables data or state changes. 
 
 Observables can be many things. It could be a continually growing list of UI events, such as taps or button clicks. It could be a single variable that notifies the changes to it over time. It could be a task of complex code that notifies when it's finished. Or, in the simplest case, a bounded array of elements. 
 
 Some examples of these might be a new event from a user interaction, a change event for a title after the data has been updated, a complete event when the network task finishes, or a next element event when you're processing a list of user IDs. A quick glance at some code will be helpful for this. First, we're creating a simple observable for this list of integers. This will allow us to enumerate over the sequence of numbers. But, because this is an observable, nothing really happens until we actually subscribe.


Observable types
Observable types are like onions. They have layers. There are three types of observables to work with, relays, subjects, and observables.


Relay 

Will fit most of your needs
Get and set anytime.
Can subscribes when it changes.
Hot Observable.
Never error out or complete.

Subjects.

Can recieve onError or onComplete eventes
"Die" after onError or onComplete eventen
These can also be subscribers and observables.
Hot Observable.


Subjects have three types: behavior, publish, and replay.
A behavior subject receives the last event or the default value if there are no events. Behavior only gives its most current value and notifies of changes to that value. It has no default or otherwise. 
Publish subjects only get new values. 
Replay subjects have a buffer of events that they will share.

One thing to note, all subjects will receive onError and onCompleted events. This means that they can error out or die and are not ideal for stuff in the UI layer.

Traits
When use Trails? when one-off call like a network call or something that wraps a single result.
They are single (onNext, onError) , completable (onCompleted, onError), and maybe (onNext/onCompleted, onError). 
Singles will receive only one onNext or one onError event. They won't receive a complete or undisposed events. 
Completables will only receive one complete or error event. They won't receive onNext at all, or a disposable event. And maybes will receive only one onNext or one completed event, but not both, and possibly, an error event. 


What are Subjects?
A Subject extends an Observable and implements Observer at the same time.
It acts as an Observable to clients and registers to multiple events taking place in the app.
It acts as an Observer by broadcasting the event to multiple subscribers.
When this BehaviorSubject is terminated via onError(Throwable) or onComplete(), the last observed item (if any) is cleared and late Observers only receive the respective terminal event.
Calling onNext(Object), onError(Throwable) and onComplete() is required to be serialized (called from the same thread or called non-overlappingly from different threads through external means of serialization).

Subjects can act as both an Observer and an Observable.
Subjects are considered as HOT Observables.

Error handling: When the onError(Throwable) is called, the BehaviorSubject enters into a terminal state and emits the same
Throwable instance to the last set of Observers. During this emission, if one or more Observers dispose their respective Disposables,
the Throwable is delivered to the global error handler via RxJavaPlugins.onError(Throwable) (multiple times if multiple Observers cancel at once).
If there were no Observers subscribed to this BehaviorSubject when the onError() was called, the global error handler is not invoked.

Description copied from interface:
Observer Notifies the Observer that the Observable has finished sending push-based notifications.
The Observable will not call this method if it calls Observer.onError(java.lang.Throwable).

#### What is an Observable?
In RxJava, Observables are the source that emits data to the Observers. We can understand observables as suppliers — they process and supply data to other components. It does some work and emits some values.
The following are the different types of Observables in RxJava
- Observable
- Flowable
- Single
- Maybe
- Completable

#### Types of Observables:
- Flowable: Emit a stream of elements (endlessly, with backpressure) Backpressure is a means of handling the situation where data is generated faster than it can be processed
- Single: Single is an Observable which only emits one item or throws an error. Single emits only one value and applying some of the operator makes no sense.
- Maybe: Maybe is similar to Single but this time, it allows your observable the ability to not emit any item at all. The MaybeObserver is defined as follows.
- Completable: This observable is only concerned about two things, if some action is executed or an error is encountered.

#### RxJava and Architecture Guidelines
- Async APIs (methods) vs sync.
- Return Observables when is posible.
- Observing on the main thread in view layer.

The first thing is it's easier if you design all your APIs as Asynchronous methods instead of returning a value directly.
This makes a lot of sense when working with the model layer and below.
Generally network, database, and file I/O tasks tend to be longer running, and an Asynchronous API keeps the model layer from blocking the UI.
So even if your database method returns immediately, it would be easier if all consumers are using the same type of API, I.e. asynchronous,
and assume the callback will be used whenever the data is ready.
Now I'm saying callbacks, but the second guideline is to return observables whenever possible.

Return Observables when is posible. The sooner the upper layers can manage subscriptions as opposed to providing lambdas and delegates.
This simplifies your code, keeping your classes free from interfaces, delegates, and lambdas, while containing subscriptions.

Always make sure you're observing on the main thread.
This means that your subscription block will be triggered on the main thread and it's easy to forget,
but if you always observe on the main thread right before you subscribe you won't have any problems. I




- Kotlin
- RxJava
- Room
- Pattern designs
- Retrofit

