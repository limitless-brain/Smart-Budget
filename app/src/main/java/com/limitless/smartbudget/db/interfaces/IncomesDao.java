/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: IncomesDao.java                                        ////////
 * ////////Class Name: IncomesDao                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 9/26/19 12:52 PM                                       ////////
 * ////////Author: yazan                                                   ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////                                                                                    ////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.limitless.smartbudget.db.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.limitless.smartbudget.db.model.Incomes;

import java.util.List;

@Dao
public interface IncomesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Incomes incomes);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Incomes incomes);

    @Delete
    void delete(Incomes incomes);


    @Query("SELECT * FROM incomes WHERE date BETWEEN :d1 AND :d2")
    List<Incomes> getRecordsBetween(Long d1, Long d2);

    @Query("SELECT * FROM incomes")
    List<Incomes> getAllRecords();

    @Query("SELECT * FROM incomes WHERE description = :des")
    List<Incomes> getRecordsByDescription(String des);

    @Query("SELECT SUM(value) FROM incomes WHERE date BETWEEN :d1 AND :d2")
    Double getTotalIncomesBetween(Long d1, Long d2);

    @Query("SELECT * FROM incomes WHERE id = :id")
    Incomes getRecordById(int id);
}
