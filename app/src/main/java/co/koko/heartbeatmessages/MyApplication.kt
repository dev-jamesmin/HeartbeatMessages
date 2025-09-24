package co.koko.heartbeatmessages

import android.app.Application
import android.util.Log
import co.koko.heartbeatmessages.util.AdManagerCompose


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AdManagerCompose.initialize(this)
        Log.d("AdInit", "AdManagerCompose initialized")

    }
}