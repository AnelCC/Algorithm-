package com.anelcc.simplerxjava.modellayer.networklayer

import com.github.kittinunf.result.Result
import com.anelcc.simplerxjava.common.NullBox
import com.anelcc.simplerxjava.common.StringLambda
import com.anelcc.simplerxjava.common.VoidLambda
import com.anelcc.simplerxjava.modellayer.entities.Message
import com.anelcc.simplerxjava.modellayer.entities.Person
import com.anelcc.simplerxjava.modellayer.networklayer.EndpointInterfaces.JsonPlaceHolder
import com.anelcc.simplerxjava.modellayer.networklayer.helpers.ServiceGenerator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


typealias MessageLambda = (Message?)->Unit
typealias MessagesLambda = (List<Message>?)->Unit

class NetworkLayer {
    companion object { val instance = NetworkLayer() }

    private val placeHolderApi: JsonPlaceHolder

    init {
        placeHolderApi = ServiceGenerator.createService(JsonPlaceHolder::class.java)
    }

    //region End Point - Fully Rx
    fun getMessageRx(articleId: String): Single<Message> {
        return placeHolderApi.getMessageRx(articleId) //
    }

    fun getMessagesRx(): Single<List<Message>> {
        return placeHolderApi.getMessagesRx()
    }

    fun postMessageRx(message: Message): Single<Message> {
        return placeHolderApi.postMessageRx(message)
    }
    //endregion

    //region End Point - SemiRx Way
    fun getMessages(success: MessagesLambda, failure: StringLambda) {
        // give as a callback that will return a list of Messages
        val call = placeHolderApi.getMessages()

        call.enqueue(object: Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>?, response: Response<List<Message>>?) {
                val article = parseRespone(response)
                //it parse correctly we will return articles
                success(article)
            }

            override fun onFailure(call: Call<List<Message>>?, t: Throwable?) {
                println("Failed to GET Message: ${ t?.message }")
                failure(t?.localizedMessage ?: "Unknown error occured")
            }
        })
    }

    fun getMessage(articleId: String, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.getMessage(articleId)

        call.enqueue(object: Callback<Message> {
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to GET Message: ${ t?.message }")
                failure()
            }
        })
    }

    fun postMessage(message: Message, success: MessageLambda, failure: VoidLambda) {
        val call = placeHolderApi.postMessage(message)

        call.enqueue(object: Callback<Message>{
            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                val article = parseRespone(response)
                success(article)
            }

            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                println("Failed to POST Message: ${ t?.message }")
                failure()
            }
        })
    }

    private fun <T> parseRespone(response: Response<T>?): T? {
        val article = response?.body() ?: null

        if (article == null) {
            parseResponeError(response)
        }

        return article
    }

    private fun <T> parseResponeError(response: Response<T>?) {
        if(response == null) return

        val responseBody = response.errorBody()

        if(responseBody != null) {
            try {
                val text = "responseBody = ${ responseBody.string() }"
                println("$text")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val text = "responseBody == null"
            println("$text")
        }
    }


    //endregion

    //region Task Example

    //Make one Observable for each person in an list
    fun loadInfoFor(people: List<Person>): Observable<List<String>> {
        //Foreach person make a network call
        val networkObservables = people.map(::buildGetInfoNetworkCallFor)

        //when all server results have returned zip observables into a single observable
        return networkObservables.zip { list ->
            list.filter { box -> box.value != null }
                .map { it.value!! }
        }
    }

    //Wrap task in Reactive Observable
    //This pattern is used often for units of work
    private fun buildGetInfoNetworkCallFor(person: Person): Observable<NullBox<String>> {
        return Observable.create{ observer ->
            //Execute Request - Do actual work here
            getInfoFor(person) { result ->
                result.fold({ info ->
                    observer.onNext(info)
                    observer.onComplete()
                }, { error ->
                    //do something with error, or just pass it on
                    observer.onError(error)
                })
            }
        }
    }

    //Create a Network Task
    //The important part is to show you how we can group tasks. 
    // So you could easily put a network call in here, or something else. 
    //Create a Network Task
    fun getInfoFor(person: Person, finished:(Result<NullBox<String>, Exception>) -> Unit) {
        //Execute on Background Thread
        //Do your task here
        CoroutineScope(Dispatchers.IO).launch {
            println("start network call: $person")
            val randomTime = person.age * 1000L// to milliseconds
            delay(randomTime)
            println("finished network call: $person")

            //just randomly make odd people null
            var result = Result.of(NullBox(person.toString()))
            finished(result)
        }
    }

    //endregion

}