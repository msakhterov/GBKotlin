package ru.msakhterov.notesapp.ui.base

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.data.error.NoAuthException
import kotlin.coroutines.CoroutineContext


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {

    companion object {
        private const val SIGN_IN_REQUEST = 1
    }

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
    }

    override fun onStart() {
        super.onStart()
        dataJob = launch {
            viewModel.getViewState().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }

    override fun onStop() {
        dataJob.cancel()
        errorJob.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

    protected fun renderError(error: Throwable?) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error?.let {
                Timber.e(it)
                it.message?.let { showError(it) }
            }
        }
    }

    private fun startLogin() {
        val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.android_robot)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN_REQUEST && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    protected fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    abstract fun renderData(data: T)

}