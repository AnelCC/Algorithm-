package com.anelcc.simplerxjava.activities.networkexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.activities.networkexample.recycler.MessageViewAdapter
import com.anelcc.simplerxjava.activities.networkexample.NetworkExamplePresenter
import com.anelcc.simplerxjava.common.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_network_example.*

class NetworkExampleActivity : AppCompatActivity() {

    private val presenter = NetworkExamplePresenter()
    lateinit var adapter: MessageViewAdapter
    private val bag = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_example)

        //Notify info from the presenter
        presenter.messages
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ messages ->
                adapter.messages.accept(messages) //messages come through
            }.disposedBy(bag)


        attachUI()
    }

    private fun attachUI() {
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        networkRecyclerView.layoutManager = linearLayoutManager
        networkRecyclerView.setHasFixedSize(true)
        networkRecyclerView.addItemDecoration(dividerItemDecoration)

        initializeListView()
    }

    private fun initializeListView() {
        adapter = MessageViewAdapter { view, position -> rowTapped(position) }
        networkRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int) {
        println("🍄")
        println(adapter.messages.value[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}

