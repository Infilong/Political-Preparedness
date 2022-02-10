package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    //https://developer.android.com/training/data-storage/room/accessing-data
    //https://medium.com/@rachmawidya7/10-1-part-b-deleting-data-from-a-room-database-1516d57b4a8d
    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getElectionDays(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table LIMIT 1")
    fun getSingleElection(): LiveData<Election>

    //TODO: Add delete query
    @Delete
    fun deleteElection(election: Election)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun deleteAllElections()
}