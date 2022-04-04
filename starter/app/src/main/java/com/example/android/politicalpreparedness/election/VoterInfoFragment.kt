package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Add binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        val selectedElection = VoterInfoFragmentArgs.fromBundle(requireArguments()).selectedElection

        binding.election = selectedElection
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel
        viewModel =
            ViewModelProvider(
                this,
                VoterInfoViewModelFactory(requireActivity().application, selectedElection)
            ).get(
                VoterInfoViewModel::class.java
            )
        binding.viewModel = viewModel

        //TODO: Handle loading of URLs
        viewModel.url.observe(viewLifecycleOwner) { url ->
            url?.let {
                viewModel.setUrl(url)
                loadUrl(url)
            }
        }

        //Set follow button click listener
        binding.followElectionButton.setOnClickListener { viewModel.onFollowButtonClicked() }

        return binding.root
    }

    //TODO: Populate voter info -- hide views without provided data.
    /**
    Hint: You will need to ensure proper data is provided from previous fragment.
     */


    //TODO: Create method to load URL intents
    /**
     * https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
     *
     * ACTION_VIEW
     * Activity Action: Display the data to the user. This is the most common action performed on data --
     * it is the generic action you can use on a piece of data to get the most reasonable thing to occur.
     * For example, when used on a contacts entry it will view the entry;
     * when used on a mailto: URI it will bring up a compose window filled with the information supplied by the URI;
     * when used with a tel: URI it will invoke the dialer.
     */
    private fun loadUrl(url: String) {
        val implicitIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(implicitIntent)
    }
}


