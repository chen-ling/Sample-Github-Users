package ccl.exercise.githubusers

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinModules.initKoin(this)
    }
}