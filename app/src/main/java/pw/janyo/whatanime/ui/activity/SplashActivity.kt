package pw.janyo.whatanime.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import pw.janyo.whatanime.base.WABaseActivity
import pw.janyo.whatanime.config.Configure
import pw.janyo.whatanime.config.connectServer
import pw.janyo.whatanime.config.inBlackList
import pw.janyo.whatanime.viewModel.TestViewModel
import vip.mystery0.logs.Logs
import vip.mystery0.rx.PackageDataObserver
import vip.mystery0.tools.ResourceException

class SplashActivity : WABaseActivity<ViewDataBinding>(null) {
	private val testViewModel: TestViewModel by viewModel()

	override fun initView() {
		super.initView()
		when (Configure.nightMode) {
			0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
			1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			3 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
		}
	}

	override fun initData() {
		super.initData()
		testViewModel.connectServer.observe(this, object : PackageDataObserver<Pair<Boolean, Boolean>> {
			override fun content(data: Pair<Boolean, Boolean>?) {
				super.content(data)
				connectServer = data?.first ?: false
				inBlackList = data?.second ?: false
				doNext()
			}

			override fun error(data: Pair<Boolean, Boolean>?, e: Throwable?) {
				super.error(data, e)
				if (e is ResourceException) {
					//网络异常，打印toast
					e.toastLong()
				} else {
					Logs.wtf("error: ", e)
				}
				doNext()
			}
		})
	}

	override fun requestData() {
		super.requestData()
		testViewModel.doTest()
	}

	private fun doNext() {
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}
