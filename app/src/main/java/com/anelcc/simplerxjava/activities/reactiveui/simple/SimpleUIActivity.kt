package com.anelcc.simplerxjava.activities.reactiveui.simple

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import com.anelcc.simplerxjava.modellayer.entities.Friend
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_simple_ui.*

class SimpleUIActivity : AppCompatActivity() {

    private val presenter = SimpleUIPresenter()
    private lateinit var adapter: ArrayAdapter<String>
    private var bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_ui)

        rxExamples()
    }

    private fun rxExamples() {
        //simpleRx()
        rxSimpleListBind()
        rxBindTitle()
    }

    private fun rxBindTitle() {
        presenter.title.observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { "Default Value" }
            .share()
            .subscribe(RxTextView.text(simpleUITitleTextView))
            .disposedBy(bag)
    }

    private fun simpleRx() {
        /*presenter.title.observeOn(AndroidSchedulers.mainThread())
                .subscribe { simpleUITitleTextView.text = it }
                .disposedBy(bag)*/

        presenter.title.observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { "Default Value" } // this error is recieve Faked Error but we ignore and setting Default Value
            .share()
            .subscribe { simpleUITitleTextView.text = it }
            .disposedBy(bag)
    }


    private fun rxSimpleListBind() {
        val listItems = presenter.friends.value.map { it.toString() }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        simpleUIListView.adapter = adapter

        presenter.friends.subscribeOn(AndroidSchedulers.mainThread()).subscribe(::updateList)
    }

    fun updateList(items: List<Friend>) {
        val itemsArray = items.map {
            it.description
        }.toTypedArray()

        adapter.clear()
        adapter.addAll(*itemsArray)
        adapter.notifyDataSetChanged()
    }
}
