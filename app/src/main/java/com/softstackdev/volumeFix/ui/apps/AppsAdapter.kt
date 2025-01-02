package com.softstackdev.volumeFix.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.softstackdev.volumeFix.databinding.AppItemBinding

class AppsAdapter(private val viewModel: AppsViewModel) :
    ListAdapter<UIAppData, AppsAdapter.AppsViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AppItemBinding.inflate(layoutInflater, parent, false)

        return AppsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class AppsViewHolder(private val binding: AppItemBinding) : ViewHolder(binding.root) {

        fun bind(viewModel: AppsViewModel, item: UIAppData) {
            binding.viewModel = viewModel
            binding.app = item
            binding.executePendingBindings()
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<UIAppData>() {
    override fun areItemsTheSame(oldItem: UIAppData, newItem: UIAppData): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: UIAppData, newItem: UIAppData): Boolean {
        return oldItem == newItem
    }
}