package com.example.android.politicalpreparedness.election

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(context: Context) : ViewModel() {

    private val repository = ElectionsRepository(getInstance(context))

    //TODO: Create live data val for upcoming elections
    //TODO: Create live data val for saved elections
    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    val electionsList = repository.electionsList
    val savedElectionsList = repository.savedElectionsList

    init {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private val _navigateToElectionDetail = MutableLiveData<Election>()
    val navigateToElectionDetail: LiveData<Election> = _navigateToElectionDetail


    fun onElectionItemClicked(election: Election) {
        _navigateToElectionDetail.value = election
    }

    fun navigationReset() {
        _navigateToElectionDetail.value = null
    }
}