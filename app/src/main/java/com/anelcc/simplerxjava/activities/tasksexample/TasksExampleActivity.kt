package com.anelcc.simplerxjava.activities.tasksexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.common.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class TasksExampleActivity : AppCompatActivity() {

    private val presenter = TasksExamplePresenter()
    private var bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_example)

        presenter.loadPeopleInfo()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({infoList ->
                println("🦄 all processes completed successfully")
                println("network results: \n\t $infoList")
                infoList.forEach { println("🐖: ${it}")}
            }, { error ->
                println("❗️ not all processes completec successfully")
                println(error.localizedMessage)
            }).disposedBy(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}

