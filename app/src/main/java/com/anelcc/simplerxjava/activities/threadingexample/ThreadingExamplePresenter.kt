package com.anelcc.simplerxjava.activities.threadingexample

import com.anelcc.simplerxjava.modellayer.entities.Friend
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ThreadingExamplePresenter {

    val friends = BehaviorRelay.createDefault(listOf<Friend>())
    val title   = BehaviorSubject.createDefault("Default Title")
    val friendsLoaded = BehaviorRelay.createDefault(false)

    init {
        loadFriends()
    }

    fun loadFriends() {
        title.onNext("Loading Friends")

//        thread(start = true){
//            Thread.sleep(3000)

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)

            title.onNext("Friends Loaded")
            friendsLoaded.accept(true)

            var newFriends = listOf(Friend("Debi", "Darlington"),
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

//            launch(UI) {
                friends.accept(newFriends)
//            }
        }
    }
}