package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val dataSource: ElectionDatabase, val election: Election) :
    ViewModel() {

    //TODO: Add live data to hold voter info
    val voterInfo = MutableLiveData<VoterInfoResponse>()

    //TODO: Add var and methods to populate voter info
    private suspend fun getVoterInfo() {
        withContext(Dispatchers.IO) {
            try {
                val voterInfoResponse =
                    CivicsApi.retrofitService.getVoterInfo(
                        election.division.toString(),
                        election.id
                    )
                voterInfo.value = voterInfoResponse.body()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        viewModelScope.launch {
            getVoterInfo()
        }
    }

    //TODO: Add var and methods to support loading URLs
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    fun setUrl(url: String?) {
        _url.value = url
    }

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    private val _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean>
        get() = dataSource.electionDao.isElectionFollowed(election.id)

    //TODO: Handle save button UI state

    //TODO: cont'd Handle save button clicks
    fun onFollowButtonClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            if (_isElectionFollowed.value == true) {
                dataSource.electionDao.unfollowElection(election.id)
            } else {
                dataSource.electionDao.followElection(election.id)
            }
        }
    }
}