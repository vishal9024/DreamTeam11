package os.com.networkCall

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import os.com.constant.AppRequestCodes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    companion object {
        /**
         * create singleton for accessing variables
         */
        var mApiClient: ApiClient? = null
        var retrofit: Retrofit? = null

        val client: ApiClient
            get() {

                if (mApiClient == null) {
                    mApiClient = ApiClient()
                }
                return mApiClient as ApiClient
            }
    }

    /**
     * this method will return instance ApiInterface
     */
    fun getRetrofitService(): ApiInterface {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.connectTimeout(10, TimeUnit.MINUTES)
        clientBuilder.readTimeout(10, TimeUnit.MINUTES)

        val gson =
            GsonBuilder()
                .setLenient()
                .create()
        return Retrofit.Builder()
            .baseUrl(ApiConstant.getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiInterface::class.java)
    }

    fun getRetrofitServiceCashFree(): ApiInterface {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.connectTimeout(10, TimeUnit.MINUTES)
        clientBuilder.readTimeout(10, TimeUnit.MINUTES)
        val gson =
            GsonBuilder()
                .setLenient()
                .create()
        return Retrofit.Builder()
            .baseUrl(AppRequestCodes.cashfreeBaseUrlTest)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(clientBuilder.build())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiInterface::class.java)
    }
}
