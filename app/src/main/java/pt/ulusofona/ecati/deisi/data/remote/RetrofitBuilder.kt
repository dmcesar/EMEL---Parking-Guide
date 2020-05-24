package pt.ulusofona.ecati.deisi.data.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    companion object {

        fun getInstance(path: String): Retrofit {

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

            return Retrofit.Builder()
                .baseUrl(path)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}