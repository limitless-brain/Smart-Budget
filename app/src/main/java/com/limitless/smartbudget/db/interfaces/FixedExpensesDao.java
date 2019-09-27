/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: FixedExpensesDao.java                                        ////////
 * ////////Class Name: FixedExpensesDao                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 9/27/19 9:09 PM                                       ////////
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

import com.limitless.smartbudget.db.model.FixedExpenses;

import java.util.List;

@Dao
public interface FixedExpensesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FixedExpenses fixedExpenses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FixedExpenses fixedExpenses);

    @Delete
    void delete(FixedExpenses fixedExpenses);

    @Query("SELECT * FROM fixed_expenses WHERE date BETWEEN :d1 AND :d2")
    List<FixedExpenses> getRecordsBetween(Long d1, Long d2);

    @Query("SELECT * FROM fixed_expenses")
    List<FixedExpenses> getAllRecords();

    @Query("SELECT * FROM fixed_expenses WHERE description = :des")
    List<FixedExpenses> getRecordsByDescription(String des);

    @Query("SELECT SUM(value) FROM fixed_expenses WHERE date BETWEEN :d1 AND :d2")
    Double getTotalPaidBetween(Long d1, Long d2);

    @Query("SELECT * FROM fixed_expenses WHERE id = :id")
    FixedExpenses getRecordById(int id);
}
