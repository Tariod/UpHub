package com.tariod.uphub.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tariod.uphub.R
import com.tariod.uphub.data.api.ApiException.Companion.UN_AUTH
import com.tariod.uphub.databinding.ActivityLoginBinding
import com.tariod.uphub.ui.base.BaseActivity
import com.tariod.uphub.utilities.livedata.observe
import com.tariod.uphub.utilities.ui.hideKeyboard
import com.tariod.uphub.utilities.ui.str


class LoginActivity : BaseActivity<LoginViewModel>() {

    companion object {

        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(
            this,
            R.layout.activity_login
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.popup.animate()
            .alpha(1F)
            .translationY(0F)
            .setInterpolator(DecelerateInterpolator()).duration = 650L
        binding.executePendingBindings()

        observe(viewModel.signUp) { hideKeyboard() }
        observe(viewModel.navigateBack) { onBackPressed() }
        observe(viewModel.success) { viewModel.onSkipPressed() }
        observe(viewModel.ask2FACode) { show2FACode() }
        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.error) {
            when (it.code) {
                UN_AUTH -> {
                    binding.passLayout.error = str(it.alert)
                    binding.loginLayout.error = str(it.alert)
                }
                else -> Toast.makeText(this, it.alert, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun show2FACode() {
        var dismiss: () -> Unit = {}
        AlertDialog.Builder(this)
            .setView(LayoutInflater
                .from(this)
                .inflate(R.layout.view_2fa_code, findViewById(android.R.id.content), false)
                .also { view ->
                    view.findViewById<Button>(R.id.signInButton).setOnClickListener {
                        viewModel.onSignUp(view.findViewById<EditText>(R.id.code).text.toString())
                        dismiss()
                    }
                })
            .create().also { dismiss = { it.dismiss() } }
            .show()
    }

    override fun provideViewModel(): LoginViewModel =
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
}