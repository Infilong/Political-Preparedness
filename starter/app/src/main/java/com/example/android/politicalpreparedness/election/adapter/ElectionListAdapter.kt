package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.UpcomingElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionClickListener) :
    androidx.recyclerview.widget.ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {
    class ElectionViewHolder (val binding: UpcomingElectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Election, clickListener: ElectionClickListener) {
            binding.election = item
            binding.clickListener = clickListener
        }

        //TODO: Add companion object to inflate ViewHolder (from)
        //TODO: Create ElectionViewHolder
        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UpcomingElectionListItemBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionItem = getItem(position)
        holder.bind(electionItem, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Create ElectionDiffCallback
    class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }
    }

    //TODO: Create ElectionListener
    class ElectionClickListener(val listener: (election: Election) -> Unit) {
        fun onClick(election: Election) = listener(election)
    }
}

