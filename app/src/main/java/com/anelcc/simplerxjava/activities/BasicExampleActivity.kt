package com.anelcc.simplerxjava.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anelcc.simplerxjava.R
import com.anelcc.simplerxjava.modellayer.entities.Posting
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException


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

        realSingleExample()
    }

    //region Rx Code
    //This just says however those come in on whatever network thread,
    // I don't care, just make sure that the results come in on the main thread.
    // And we'll look more at what that means versus what this subscribeOn means.
    private fun realSingleExample() {
        //load data and set in TextView
        loadPostAsSingle().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ posting ->
                userIdTextView.text = posting.title
                bodyTextView.text = posting.body
            }, {error ->
                print("❗️ an error occured: ${error.localizedMessage}")
                userIdTextView.text = "❗"
                bodyTextView.text = "❗"
            })
    }

    private fun loadPostAsSingle(): Single<Posting> {
        return Single.create { observe ->
            //simulate network
            Thread.sleep(2000)
            val postingId = 5
            service.getPosts(postingId.toString()).enqueue(object : Callback<Posting>{
                override fun onFailure(call: Call<Posting>, t: Throwable) {
                    val e = t ?: IOException("An Unknown network error occurred")
                    observe.onError(e)
                }

                override fun onResponse(call: Call<Posting>, response: Response<Posting>) {
                    val posting = response?.body()
                    if (posting != null) {
                        observe.onSuccess(posting)
                    } else {
                        val e = IOException("An Unknown network error occurred")
                        observe.onError(e)
                    }
                }

            })
        }
    }

    //we will clear the bag out, which means all of our subscriptions are released and
    // we won't have any memory leaks or issues with things that don't actually exist anymore.
    //endregion
    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}