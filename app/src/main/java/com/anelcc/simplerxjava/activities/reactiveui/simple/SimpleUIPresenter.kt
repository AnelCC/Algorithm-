package com.anelcc.simplerxjava.activities.reactiveui.simple

import com.anelcc.simplerxjava.modellayer.entities.Friend
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SimpleUIPresenter {
    val friends = BehaviorRelay.createDefault(listOf<Friend>())
    val title = BehaviorSubject.createDefault("Default Title")

    init {
        title.onNext("Load Friends")
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            title.onNext("Friends Loaded")

            var newFriends = listOf(
                Friend("Debi", "Darlington"),
                Friend("Arlie", "Abalos"),
                Friend("Jessica", "Jetton"),
                Friend("Tonia", "Threlkeld"),
                Friend("Donte", "Derosa"),
                Friend("Nohemi", "Notter"),
                Friend("Rod", "Rye"),
                Friend("Simonne", "Sala"),
                Friend("Kathaleen", "Kyles"),
                Friend("Loan", "Lawrie"),
                Friend("Elden", "Ellen"),
                Friend("Felecia", "Fortin"),
                Friend("Fiona", "Fiorini"),
                Friend("Joette", "July"),
                Friend("Beverley", "Bob"),
                Friend("Artie", "Aquino"),
                Friend("Yan", "Ybarbo"),
                Friend("Armando", "Araiza"),
                Friend("Dolly", "Delapaz"),
                Friend("Juliane", "Jobin"))
            CoroutineScope(Dispatchers.IO).launch {
                friends.accept(newFriends)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(6000) //6 seconds
            title.onError(Exception("Faked Error"))
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(7000) //7 seconds
            println("🦄 pushing new value")
            title.onNext("new Value")
        }
    }
}