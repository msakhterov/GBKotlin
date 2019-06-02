package ru.msakhterov.notesapp.ui.base

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.github.ajalt.timberkt.Timber
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.data.error.NoAuthException


abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val SIGN_IN_REQUEST = 1
    }

    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
        viewModel.getViewState().observe(this, object : Observer<S> {
            override fun onChanged(t: S?) {
                if (t == null) return
                if (t.error != null) {
                    renderError(t.error)
                    return
                }
                renderData(t.data)
            }
        })
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