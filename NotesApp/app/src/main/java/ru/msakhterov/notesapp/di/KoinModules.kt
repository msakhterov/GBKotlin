package ru.msakhterov.notesapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.provider.FireStoreProvider
import ru.msakhterov.notesapp.data.provider.RemoteDataProvider
import ru.msakhterov.notesapp.ui.main.MainViewModel
import ru.msakhterov.notesapp.ui.note.NoteViewModel
import ru.msakhterov.notesapp.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}