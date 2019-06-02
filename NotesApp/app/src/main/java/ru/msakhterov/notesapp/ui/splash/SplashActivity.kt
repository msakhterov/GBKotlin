package ru.msakhterov.notesapp.ui.splash

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import ru.msakhterov.notesapp.ui.base.BaseActivity
import ru.msakhterov.notesapp.ui.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    companion object {
        private const val START_DELAY = 500L
    }

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity (){
        MainActivity.start(this)
        finish()
    }
}
