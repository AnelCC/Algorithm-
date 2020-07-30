package com.anelcc.simplerxjava.activities.reactiveui.complex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import io.reactivex.disposables.CompositeDisposable
import com.anelcc.simplerxjava.databinding.ActivityComplexUiBinding

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

    private fun showSimpleBindingExample() {

    }

    private fun commonInit() {

    }
}