package ccl.exercise.githubusers

import android.app.Application
import androidx.annotation.VisibleForTesting
import ccl.exercise.githubusers.service.GithubApi
import ccl.exercise.githubusers.service.GithubService
import ccl.exercise.githubusers.service.GithubServiceImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object KoinModules {
    private const val TIMEOUT_SECONDS = 15L
    private const val HEADER_ACCEPT = "Accept"
    private const val HEADER_APPLICATION_JSON = "application/vnd.github.v3+json"

    fun initKoin(app: Application) {
        startKoin {
            androidContext(app)
            modules(appModules)
        }
    }

    @VisibleForTesting
    val appModules = module {
        single {
            OkHttpClient.Builder().apply {
                connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(
                            HEADER_ACCEPT,
                            HEADER_APPLICATION_JSON
                        ).build()
                    chain.proceed(request)
                }
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                }

            }.build()
        }

        single<Retrofit> {
            Retrofit.Builder()
                .client(get())
                .baseUrl(GithubApi.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        }

        single<GithubApi> { get<Retrofit>().create(GithubApi::class.java) }

        single<GithubService> { GithubServiceImpl(get()) }
    }
}
