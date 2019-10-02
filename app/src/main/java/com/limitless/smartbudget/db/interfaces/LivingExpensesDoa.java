/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: LivingExpensesDoa.java                                        ////////
 * ////////Class Name: LivingExpensesDoa                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 10/2/19 4:31 PM                                       ////////
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

import com.limitless.smartbudget.db.model.LivingExpenses;

import java.util.List;

@Dao
public interface LivingExpensesDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LivingExpenses livingExpenses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LivingExpenses livingExpenses);

    @Delete
    void delete(LivingExpenses livingExpenses);

    @Query("SELECT * FROM living_expenses")
    List<LivingExpenses> getAllRecords();

    @Query("SELECT * FROM living_expenses WHERE c_id = :category")
    List<LivingExpenses> getRecordsForCategory(int category);

    @Query("SELECT * FROM living_expenses WHERE date BETWEEN :d1 AND :d2")
    List<LivingExpenses> getRecordsBetween(Long d1, Long d2);

    @Query("SELECT * FROM living_expenses WHERE date BETWEEN :d1 AND :d2 AND c_id = :category")
    List<LivingExpenses> getRecordsForCategoryBetween(Long d1, Long d2, int category);

    @Query("SELECT * FROM living_expenses WHERE description LIKE :description")
    List<LivingExpenses> getRecordsByDescription(String description);

    @Query("SELECT SUM(value) FROM living_expenses")
    Double getTotalExpenses();

    @Query("SELECT SUM(value) FROM living_expenses WHERE date BETWEEN :d1 AND :d2")
    Double getTotalExpensesBetween(Long d1, Long d2);

    @Query("SELECT SUM(value) FROM living_expenses WHERE c_name = :category")
    Double getTotalExpensesForCategory(String category);


    @Query("SELECT SUM(value) FROM living_expenses " +
            "WHERE c_name = :category AND date = :date")
    Double getTotalExpensesForCategory(String category, Long date);

    @Query("SELECT SUM(value) FROM living_expenses WHERE description LIKE :description ")
    Double getTotalExpensesForDescription(String description);

    @Query("SELECT * FROM living_expenses WHERE id = :id")
    LivingExpenses getRecordById(int id);
}
