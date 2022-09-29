package com.lzm.lightLive.frag

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.lzm.lib_base.BaseBindingFragment
import com.lzm.lightLive.R
import com.lzm.lightLive.databinding.FragmentSettingBinding
import com.lzm.lightLive.util.PermissionUtil
import com.lzm.lightLive.util.UiTools

class SettingFragment : BaseBindingFragment<FragmentSettingBinding?>() {

    companion object {
        private const val TAG = "SettingFragment"
    }

    override fun setLayout(): Int {
        return R.layout.fragment_setting
    }

    override fun initData() {
        initViewClick()
    }

    private fun initViewClick() {
        mBind!!.tvFloat.setOnClickListener {
            val isChecked = mBind!!.tvFloat.isChecked
            if (isChecked && !PermissionUtil.canDrawOverlays(context)) {
                MessageDialog.show("需要授予系统悬浮窗权限", "是否去设置授权?")
                    .setOkButton("确认") { _: MessageDialog?, _: View? ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                        intent.data = Uri.parse("package:" + context?.packageName)
                        registerActivity.launch(intent) //.startActivityForResult(intent, 0)
                        false
                    }.dialogLifecycleCallback = object : DialogLifecycleCallback<MessageDialog?>() {
                    override fun onDismiss(dialog: MessageDialog?) {
                        mBind!!.tvFloat.isChecked = false
                        super.onDismiss(dialog)
                    }
                }
            }
        }
    }

    private val registerActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                if (!Settings.canDrawOverlays(activity)) {
                    UiTools.snackShort(mBind!!.tvFloat, "授权失败")
                } else {
                    UiTools.snackShort(mBind!!.tvFloat, "授权成功")
                    Log.e(TAG, "onActivityResult: 授权成功")
                }
            }
    }

}