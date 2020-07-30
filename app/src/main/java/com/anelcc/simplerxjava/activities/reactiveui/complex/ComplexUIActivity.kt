package com.anelcc.simplerxjava.activities.reactiveui.complex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import io.reactivex.disposables.CompositeDisposable
import com.anelcc.simplerxjava.databinding.ActivityComplexUiBinding
import com.anelcc.simplerxjava.databinding.ItemReactiveUiBinding
import com.minimize.android.rxrecycleradapter.RxDataSource

private enum class CellType {
    ITEM,
    ITEM2
}

class ReactiveUIActivity : AppCompatActivity() {
    private val dataSet = (0..100).toList().map { it.toString() }
    private var bag = CompositeDisposable()
    lateinit var boundActivity: ActivityComplexUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex_ui)

        commonInit()
        showSimpleBindingExample()
    }

    private fun commonInit() {
        boundActivity = DataBindingUtil.setContentView(this, R.layout.activity_complex_ui)
        boundActivity.reactiveUIRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showSimpleBindingExample() {
        val rxDataSource = RxDataSource<ItemReactiveUiBinding, String>(R.layout.item_reactive_ui, dataSet)
        rxDataSource.bindRecyclerView(boundActivity.reactiveUIRecyclerView)

        rxDataSource.map { it.toUpperCase() }
            .asObservable()
            .subscribe {
                val ui = it.viewDataBinding ?: return@subscribe
                val data = it.item

                ui.textViewItem.text = data
            }.disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}