package com.dekola.githublogin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dekola.githublogin.databinding.HolderGithubUsersBinding
import com.dekola.githublogin.model.GithubSearchItem

class GithubResultAdapter :
    PagingDataAdapter<GithubSearchItem, GithubResultViewHolder>(GithubResultListDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubResultViewHolder {
        val binding = HolderGithubUsersBinding.inflate(LayoutInflater.from(parent.context))
        return GithubResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubResultViewHolder, position: Int) {
        getItem(position)?.let { holder.bindData(it) }
    }

    object GithubResultListDiff : DiffUtil.ItemCallback<GithubSearchItem>() {
        override fun areItemsTheSame(oldItem: GithubSearchItem, newItem: GithubSearchItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: GithubSearchItem, newItem: GithubSearchItem): Boolean {
            return oldItem == newItem
        }
    }
}
