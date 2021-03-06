package pw.janyo.whatanime.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import pw.janyo.whatanime.R
import pw.janyo.whatanime.config.*
import vip.mystery0.tools.base.BasePreferenceFragment
import vip.mystery0.tools.utils.fastClick

class AboutFragment : BasePreferenceFragment(R.xml.pref_about) {
	private val clipboardManager: ClipboardManager by inject()
	private val languageArray by lazy { resources.getStringArray(R.array.language) }
	private val nightModeArray by lazy { resources.getStringArray(R.array.night_mode) }
	private val previewConfigArray by lazy { resources.getStringArray(R.array.preview_config_summary) }

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		val deviceIdPreference: Preference = findPreferenceById(R.string.key_device_id)
		val versionPreference: Preference = findPreferenceById(R.string.key_app_version)
		val hideSexPreference: CheckBoxPreference = findPreferenceById(R.string.key_hide_sex)
		val languagePreference: Preference = findPreferenceById(R.string.key_language)
		val nightModePreference: Preference = findPreferenceById(R.string.key_night_mode)
		val previewConfigPreference: Preference = findPreferenceById(R.string.key_preview_config)
		val cloudCompressPreference: CheckBoxPreference = findPreferenceById(R.string.key_cloud_compress)

		deviceIdPreference.summary = publicDeviceId
		languagePreference.summary = languageArray[Configure.language]
		nightModePreference.summary = nightModeArray[Configure.nightMode]
		previewConfigPreference.summary = previewConfigArray[Configure.previewConfig]
		cloudCompressPreference.isChecked = Configure.enableCloudCompress
		hideSexPreference.isChecked = Configure.hideSex

		deviceIdPreference.setOnPreferenceClickListener {
			val clipData = ClipData.newPlainText(getString(R.string.app_name), deviceIdPreference.summary)
			clipboardManager.setPrimaryClip(clipData)
			toast(getString(R.string.hint_copy_device_id))
			true
		}
		hideSexPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
			Configure.hideSex = !hideSexPreference.isChecked
			true
		}
		languagePreference.setOnPreferenceClickListener {
			var select = Configure.language
			val activity = requireActivity()
			MaterialAlertDialogBuilder(activity)
					.setTitle(R.string.title_change_language)
					.setSingleChoiceItems(languageArray, select) { _, which ->
						select = which
					}
					.setPositiveButton(android.R.string.ok) { _, _ ->
						val needRestart = select != Configure.language
						Configure.language = select
						languagePreference.summary = languageArray[Configure.language]
						if (needRestart) {
							val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
							intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
							activity.startActivity(intent)
							activity.finish()
						}
					}
					.setNegativeButton(android.R.string.cancel, null)
					.show()
			true
		}
		nightModePreference.setOnPreferenceClickListener {
			var select = Configure.nightMode
			val activity = requireActivity()
			MaterialAlertDialogBuilder(activity)
					.setTitle(" ")
					.setSingleChoiceItems(nightModeArray, select) { _, which ->
						select = which
					}
					.setPositiveButton(android.R.string.ok) { _, _ ->
						val needRestart = select != Configure.nightMode
						Configure.nightMode = select
						nightModePreference.summary = nightModeArray[Configure.nightMode]
						if (needRestart) {
							val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
							intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
							activity.startActivity(intent)
							activity.finish()
						}
					}
					.setNegativeButton(android.R.string.cancel, null)
					.show()
			true
		}
		previewConfigPreference.setOnPreferenceClickListener {
			var select = Configure.previewConfig
			val activity = requireActivity()
			MaterialAlertDialogBuilder(activity)
					.setTitle(R.string.title_change_preview_config)
					.setSingleChoiceItems(R.array.preview_config, select) { _, which ->
						select = which
					}
					.setPositiveButton(android.R.string.ok) { _, _ ->
						Configure.previewConfig = select
						previewConfigPreference.summary = previewConfigArray[Configure.previewConfig]
					}
					.setNegativeButton(android.R.string.cancel, null)
					.show()
			true
		}
		cloudCompressPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
			Configure.enableCloudCompress = !cloudCompressPreference.isChecked
			true
		}

		versionPreference.setOnPreferenceClickListener {
			fastClick(5) {
				MaterialAlertDialogBuilder(requireActivity())
						.setTitle("debug")
						.setMessage("connectServer: $connectServer\ninBlackList: $inBlackList\ndeviceId: $publicDeviceId")
						.setPositiveButton(android.R.string.ok, null)
						.show()
			}
			true
		}
		findPreferenceById<Preference>(R.string.key_about_github).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_about_github))
			true
		}
		findPreferenceById<Preference>(R.string.key_about_license).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_about_license))
			true
		}
		findPreferenceById<Preference>(R.string.key_about_open_on_play).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_about_open_on_play))
			true
		}
		findPreferenceById<Preference>(R.string.key_janyo_license).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_janyo_license))
			true
		}
		findPreferenceById<Preference>(R.string.key_developer_whatanime).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_developer_whatanime))
			true
		}
		findPreferenceById<Preference>(R.string.key_whatanime).setOnPreferenceClickListener {
			requireActivity().toCustomTabs(getString(R.string.link_whatanime))
			true
		}
	}
}