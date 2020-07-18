package com.anelcc.simplerxjava.activities.databaseexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.activities.databaseexample.recycler.PhotoDescriptionViewAdapter
import com.anelcc.simplerxjava.common.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_database_example.*

// Consume presenter this in the activity or really to anyone who's observing on it.
class DatabaseExampleActivity : AppCompatActivity() {

    private val presenter = DatabaseExamplePresenter()
    private var bag = CompositeDisposable()

    lateinit var adapter: PhotoDescriptionViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_example)

        // We'll consume to the onCreate method.
        presenter.photoDescriptions
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { descriptions ->
                //just print the descriptions
                descriptions.forEach { println("🕺: $it") }
                //When we get new values from the database we should set the adapter  and accepted this values toMutableList
                adapter.photoDescriptions.accept(descriptions.toMutableList())
            }.disposedBy(bag)

        attachUI()    }

    private fun attachUI() {
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        photoDescriptionRecyclerView.setHasFixedSize(true)
        photoDescriptionRecyclerView.setHasFixedSize(true)
        photoDescriptionRecyclerView.layoutManager = linearLayoutManager
        photoDescriptionRecyclerView.addItemDecoration(dividerItemDecoration)

        initializeListView()
    }

    private fun initializeListView() {
        adapter = PhotoDescriptionViewAdapter { view, position -> rowTapped(position) }
        photoDescriptionRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int) {
        println("🍄")
        //We've also made changes to that photoDescriptions and made it a behavior relay.
        println(adapter.photoDescriptions.value[position])
    }

    // Inside on the onDestroy let's clear out that bag, we'll say bag.clear.
    // That'll remove all subscriptions when the onDestroy event comes through.
    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}