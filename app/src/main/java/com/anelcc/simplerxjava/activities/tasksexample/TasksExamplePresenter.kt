package com.anelcc.simplerxjava.activities.tasksexample

import com.anelcc.simplerxjava.modellayer.ModelLayer
import com.anelcc.simplerxjava.modellayer.entities.Person

class TasksExamplePresenter {

    private val modelLayer = ModelLayer.shared
    private val people = listOf(
        Person("Norris",  "Najar",     0),
                                Person("Dylan",   "Decarlo",   1),
                                Person("Sonny",   "Stecher",   2),
                                Person("Napoleon","Nicols",    3),
                                Person("Jinny",   "Jordahl",   4),
                                Person("Wendi",   "Woodhouse", 5))
}