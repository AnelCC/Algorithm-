package com.anelcc.simplerxjava

import android.app.Application
import androidx.room.Room
import com.anelcc.simplerxjava.common.fromJson
import com.anelcc.simplerxjava.modellayer.persistencelayer.LocalDatabase
import com.anelcc.simplerxjava.modellayer.persistencelayer.PersistenceLayer
import com.anelcc.simplerxjava.modellayer.persistencelayer.PhotoDescription
import com.anelcc.simplerxjava.simpleexamples.SimpleRX
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LearningRxJavaApplication: Application() {

    companion object {
        lateinit var database: LocalDatabase
    }

    override fun onCreate() {
        super.onCreate()

        println("Simple App being used.")

        setupDatabase()


        SimpleRX.simpleValues()

    }

    //region Database Setup Methods

    fun setupDatabase(){
        database = Room.databaseBuilder(this, LocalDatabase::class.java, "LearningRxJavaLocalDatabase").build()

        CoroutineScope(Dispatchers.IO).launch {
            val photoDescriptions = loadJson()
            PersistenceLayer.shared.prepareDb(photoDescriptions)
        }
    }

    fun loadJson(): List<PhotoDescription> {
        val json = loadDescriptionsFromFile()
        val photoDescriptions = Gson().fromJson<List<PhotoDescription>>(json)
        return photoDescriptions
    }

    private fun loadDescriptionsFromFile(): String {
        //ignoring IOExceptions
        val inputStream = assets.open("PhotoDescription.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        return json
    }

    //endregion
}

