package com.example.mycostcalendar.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Cost cost);

    @Delete
    void delete(Cost cost);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Cost cost);

    @Query("DELETE from cost_table")
    void deleteAll();

    @Query("SELECT * from cost_table")
    LiveData<List<Cost>> getAllCosts();

    // TODO: check if there is a special query to get distinct years from the timestamp
    @Query("SELECT * from cost_table")
    List<Cost> getAllCostsList();

    @Query("SELECT * from cost_table LIMIT 1")
    Cost[] getAnyCost();

    @Query("SELECT * from cost_table WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Cost>> getCostsByDate(long startDate, long endDate);
}
