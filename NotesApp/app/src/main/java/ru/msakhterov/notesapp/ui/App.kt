package ru.msakhterov.notesapp.ui

import android.app.Application
import com.github.ajalt.timberkt.Timber
import org.koin.android.ext.android.startKoin
import ru.msakhterov.notesapp.di.appModule
import ru.msakhterov.notesapp.di.mainModule
import ru.msakhterov.notesapp.di.noteModule
import ru.msakhterov.notesapp.di.splashModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber.log.Timber.DebugTree())
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}