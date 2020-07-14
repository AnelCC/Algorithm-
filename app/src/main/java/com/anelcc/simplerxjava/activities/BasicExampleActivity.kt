package com.anelcc.simplerxjava.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.modellayer.entities.Posting
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class BasicExampleActivity : AppCompatActivity() {

    private var bag = CompositeDisposable()

    //region Simple Network Layer

    interface JsonPlaceHolderService {
        //So whatever value we pass in as id will fill in that field on the service.
        // And this is gonna return a call and it's gonna get a posting.
        @GET("posts/{id}")
        fun getPosts(@Path("id") id: String): Call<Posting>
    }

    //Create an instance of Retrofit.
    private var retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .build()

    private var service = retrofit.create(JsonPlaceHolderService::class.java)

    //endregion

    //region Life Cycle Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_example)
    }

    //endregion
}