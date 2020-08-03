package com.anelcc.simplerxjava.activities.reactiveui.complex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import com.anelcc.simplerxjava.common.isEven
import io.reactivex.disposables.CompositeDisposable
import com.anelcc.simplerxjava.databinding.ActivityComplexUiBinding
import com.anelcc.simplerxjava.databinding.ItemReactiveUiBinding
import com.anelcc.simplerxjava.databinding.ItemReactiveUiOtherBinding
import com.minimize.android.rxrecycleradapter.OnGetItemViewType
import com.minimize.android.rxrecycleradapter.RxDataSource
import com.minimize.android.rxrecycleradapter.RxDataSourceSectioned
import com.minimize.android.rxrecycleradapter.ViewHolderInfo

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
        //showSimpleBindingExample()
        showComplexBindingExample()
    }

    private fun commonInit() {
        boundActivity = DataBindingUtil.setContentView(this, R.layout.activity_complex_ui)
        boundActivity.reactiveUIRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    //region Stage 1
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
    //endregion

    //region Stage 2
    private fun showComplexBindingExample() {
        val viewHolderInfoList = listOf(
            ViewHolderInfo(R.layout.item_reactive_ui, CellType.ITEM2.ordinal),
            ViewHolderInfo(R.layout.item_reactive_ui_other, CellType.ITEM.ordinal)
        )

        val rxDataSourceSectioned = RxDataSourceSectioned(dataSet, viewHolderInfoList, object: OnGetItemViewType() {
            override fun getItemViewType(position: Int): Int {
                //determine cell type
                return if(position.isEven) CellType.ITEM.ordinal
                else CellType.ITEM2.ordinal
            }
        })

        rxDataSourceSectioned.asObservable().subscribe{
            val ui = it.viewDataBinding ?: return@subscribe
            val data = it.item

            when (ui) { //Set two types of views
                is ItemReactiveUiBinding -> ui.textViewItem.text = "Cell Type 1: $data"
                is ItemReactiveUiOtherBinding -> ui.textViewItem2.text = "Cell Type 2: $data"
            }
        }

        rxDataSourceSectioned.bindRecyclerView(boundActivity.reactiveUIRecyclerView) //bind the RecyclerView
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}