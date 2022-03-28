package com.dekola.githublogin.ui.adapter

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.recyclerview.widget.RecyclerView
import com.dekola.githublogin.databinding.HolderGithubUsersBinding
import com.dekola.githublogin.model.GithubSearchItem
import com.squareup.picasso.Picasso

class GithubResultViewHolder(private val binding: HolderGithubUsersBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: GithubSearchItem) {
        binding.run {
            loginTv.text = SpannableStringBuilder().append("Login: ")
                .scale(1.1F) { bold { append("${item.login}") } }

            typeTv.text = SpannableStringBuilder().append("Type: ")
                .scale(1.1F) { bold { append("${item.type}") } }

            avatarUrlTv.text = SpannableStringBuilder().append("Avatar URL: ")
                .scale(1.1F) { bold { append("${item.avatarUrl}") } }

            Picasso.get().load(item.avatarUrl).into(avatarImg)
        }
    }
}