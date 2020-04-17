package ccl.exercise.githubusers

import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        KoinModules.initKoin(this)
    }
}