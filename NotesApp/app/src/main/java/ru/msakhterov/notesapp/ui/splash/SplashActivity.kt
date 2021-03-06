package ru.msakhterov.notesapp.ui.splash

import android.os.Handler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import ru.msakhterov.notesapp.ui.base.BaseActivity
import ru.msakhterov.notesapp.ui.main.MainActivity

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class SplashActivity : BaseActivity<Boolean>() {

    companion object {
        private const val START_DELAY = 500L
    }

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean) {
        data?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity (){
        MainActivity.start(this)
        finish()
    }
}
