package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.TAG
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElections
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import retrofit2.HttpException

class VoterInfoViewModel(
    private val dataSource: ElectionDatabase,
    val election: Election,
) :
    ViewModel() {

    //TODO: Add live data to hold voter info
    val voterInfo = MutableLiveData<VoterInfoResponse>()

    //TODO: Add var and methods to populate voter info
    private suspend fun getVoterInfo() {
        withContext(Dispatchers.IO) {
            try {
                val voterInfoResponse =
                    CivicsApi.retrofitService.getVoterInfo(
                        election.division.state,
                        election.id
                    )
                voterInfo.postValue(voterInfoResponse.body())
            } catch (e: IOException) {
                Log.e(TAG, "IOException, please check your internet connection")
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, please check your internet connection")
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

    var isElectionIdFollowed = dataSource.electionDao.isElectionFollowedLiveData(election.id)
    private fun isElectionIdFollowedCheck() = dataSource.electionDao.isElectionFollowed(election.id)

    //In Android, we cannot perform DB operations directly on MainThread.
    //    having data returned with get() backing method will always return a new instance of the corresponding data.
    //    In this case, we only want single instance of the LiveData which wil be observed for the values
    //get() = dataSource.electionDao.isElectionFollowed(election.id)

    //TODO: Handle save button UI state

    //TODO: cont'd Handle save button clicks
    fun onFollowButtonClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            if (isElectionIdFollowedCheck()) {
                dataSource.electionDao.unfollowElection(FollowedElections(election.id))
            } else {
                dataSource.electionDao.followElection(FollowedElections(election.id))
            }
        }
    }
}