package com.anelcc.simplerxjava.activities.reactiveui.simple

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
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
        simpleRx()
        rxSimpleListBind()
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
    }
}
