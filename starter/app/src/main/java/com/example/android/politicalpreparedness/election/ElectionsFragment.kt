package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Add ViewModel values and create ViewModel
        viewModel =
            ViewModelProvider(this, ElectionsViewModelFactory(requireActivity().application)).get(
                ElectionsViewModel::class.java
            )

        //TODO: Add binding values
        binding = FragmentElectionBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setRecyclerViewAdapter()
        navigateToDetail()

        return binding.root
    }

    private fun setRecyclerViewAdapter() {
        //TODO: Initiate recycler adapters
        val adapterUpcomingElections =
            ElectionListAdapter(ElectionListAdapter.ElectionClickListener { election ->
                viewModel.onElectionItemClicked(election)
            })
        binding.upcomingElectionInfoRecycler.adapter = adapterUpcomingElections

        val adapterSavedElections =
            ElectionListAdapter(ElectionListAdapter.ElectionClickListener { election ->
                viewModel.onElectionItemClicked(election)
            })
        binding.savedElectionsInfoRecycler.adapter = adapterSavedElections

        //TODO: Populate recycler adapters
        //TODO: Refresh adapters when fragment loads
        viewModel.electionsList.observe(viewLifecycleOwner) {
            if (it != null) {
                (binding.upcomingElectionInfoRecycler.adapter as ElectionListAdapter).submitList(it)
            }
        }

        viewModel.savedElectionsList.observe(viewLifecycleOwner) {
            if (it != null) {
                (binding.savedElectionsInfoRecycler.adapter as ElectionListAdapter).submitList(it)
            }
        }
    }

    private fun navigateToDetail() {
        //TODO: Link elections to voter info
        viewModel.navigateToElectionDetail.observe(viewLifecycleOwner) { election ->
            if (election != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
                )
                viewModel.navigationReset()
            }
        }
    }
}