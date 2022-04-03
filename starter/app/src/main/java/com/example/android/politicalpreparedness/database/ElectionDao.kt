package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElections

@Dao
interface ElectionDao {
    //https://developer.android.com/training/data-storage/room/accessing-data
    //https://medium.com/@rachmawidya7/10-1-part-b-deleting-data-from-a-room-database-1516d57b4a8d
    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun getSingleElection(electionId: Int): Election

    //TODO: Add delete query
    @Delete
    fun deleteElection(election: Election)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun clear()

    //Follow and unfollow elections
    @Query("INSERT INTO followed_elections_table VALUES(:electionId)")
    fun followElection(electionId: Int)

    @Query("DELETE FROM followed_elections_table WHERE id = :electionId")
    fun unfollowElection(electionId: Int)

    //Get followed elections
    @Query("SELECT * FROM election_table WHERE id IN (SELECT id FROM followed_elections_table) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    //Check whether election is followed
    @Query("SELECT CASE id WHEN NULL THEN 0 ELSE 1 END FROM followed_elections_table WHERE id = :electionId")
    fun isElectionFollowed(electionId: Int): LiveData<Boolean>
}