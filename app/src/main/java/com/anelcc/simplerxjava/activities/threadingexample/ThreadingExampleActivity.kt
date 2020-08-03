package com.anelcc.simplerxjava.activities.threadingexample

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import com.anelcc.simplerxjava.modellayer.entities.Friend
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_threading_example.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ThreadingExampleActivity : AppCompatActivity() {

    private val presenter = ThreadingExamplePresenter()
    private var bag = CompositeDisposable()
    private lateinit var adapter: ArrayAdapter<String>
    private var mainThreadId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threading_example)

        mainThreadId = Thread.currentThread().id

        setupUI()
        threadingExamples()
    }

    private fun threadingExamples() {
        //threading()
        threading2()
    }


    private fun threading() {
        presenter.friends
                .observeOn(AndroidSchedulers.mainThread()) //for rxJava said run in the main threat
                .subscribe { items ->
                    println("🚦 current thread: ${Thread.currentThread().id}")
                    println("⁉️🌲 is on UI thread: ${Thread.currentThread().id == mainThreadId}")

//                    GlobalScope.launch(Dispatchers.Main) {
                        updateList(items)
//                   }
                }.disposedBy(bag)
    }

    //discuss ObserveOn vs SubscribeOn
    private fun threading2() {
        val single = getResult()


        //you would think that the observeOn block determines what the observable runs on and
        // the subscribeOn block determines what the subscription runs on.
        // But it's the opposite.
        // Just remember that if you want your subscription block on the main thread use "observeOn".
        // If you want your "observable" part to run on a specific thread use the subscribeOn method
        // and always try to "observeOn" the main thread for your UI elements.
        single
            .observeOn(AndroidSchedulers.mainThread())
            //.observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.computation())
            .subscribe{ result ->
                println("🚦 current thread: ${Thread.currentThread().id}")
                println("⁉️🐢 is on UI thread: ${Thread.currentThread().id == mainThreadId}")
                print("result: ${result}")
            }.disposedBy(bag)
    }

    fun getResult(): Observable<String> {
        return Observable.create { observer ->
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)

                println("🚦 current thread: ${Thread.currentThread().id}")
                println("⁉️🐖 is on UI thread: ${Thread.currentThread().id == mainThreadId}")

                observer.onNext("some result")
                observer.onComplete()
            }
        }
    }

    //region Helper Methods

    private fun setupUI() {
        val listItems = presenter.friends.value.map { it.toString() }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        threadingListView.adapter = adapter

    }

    private fun updateList(items: List<Friend>){
            val itemsArray = items.map { it.description }.toTypedArray()

            adapter.clear()
            adapter.addAll(*itemsArray)
            adapter.notifyDataSetChanged()
    }


    //endregion

}

