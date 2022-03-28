package com.dekola.githublogin.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.test.espresso.IdlingResource
import com.dekola.githublogin.R
import com.dekola.githublogin.databinding.ActivityLoginBinding
import com.dekola.githublogin.ui.adapter.GithubResultAdapter
import com.dekola.githublogin.utils.SimpleIdlingResource
import com.dekola.githublogin.utils.observeContentIfNotHandled
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var mIdlingResource: SimpleIdlingResource? = null

    private val viewModel: GithubSearchViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private val githubResultAdapter: GithubResultAdapter by lazy { GithubResultAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        viewModel.mIdlingResource = mIdlingResource

        githubResultAdapter.addLoadStateListener { loadState -> viewModel.manageLoadStates(loadState) }

        setViews()
        setObservers()
    }

    private fun setViews() {
        binding.run {
            githubUserRv.adapter = githubResultAdapter
            githubUserRv.addItemDecoration(DividerItemDecoration(this@LoginActivity,
                LinearLayout.VERTICAL))
        }
    }

    private fun setObservers() {
        viewModel.run {
            toastLiveData.observeContentIfNotHandled(this@LoginActivity) { toastWrapper ->
                showToast(getString(toastWrapper.message), toastWrapper.length)
            }
            githubSearchLiveData.observeContentIfNotHandled(this@LoginActivity) { githubSearchResponse ->
                githubResultAdapter.submitData(lifecycle, githubSearchResponse)
            }
            loadLiveData.observeContentIfNotHandled(this@LoginActivity) { load ->
                binding.loginButton.load(load)
            }
            showError.observeContentIfNotHandled(this@LoginActivity) { errorMessage ->
                ErrorDialogFragment.newInstance(errorMessage).run {
                    isCancelable = false
                    show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this@LoginActivity, message, length).show()
    }

    fun getIdlingResource(): IdlingResource {
        if (mIdlingResource == null) {
            mIdlingResource = SimpleIdlingResource()
        }
        return mIdlingResource!!
    }
}