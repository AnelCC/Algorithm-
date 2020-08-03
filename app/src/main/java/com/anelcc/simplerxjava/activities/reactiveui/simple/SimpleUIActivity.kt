package com.anelcc.simplerxjava.activities.reactiveui.simple

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import kotlinx.android.synthetic.main.activity_simple_ui.*

class SimpleUIActivity : AppCompatActivity() {

    private val presenter = SimpleUIPresenter()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_ui)

        rxExamples()
    }

    private fun rxExamples() {
        rxSimpleListBind()
    }


    private fun rxSimpleListBind() {
        val listItems = presenter.friends.map { it.toString() }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        simpleUIListView.adapter = adapter
    }
}
