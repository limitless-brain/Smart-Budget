/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: DatabaseManagement.java                                        ////////
 * ////////Class Name: DatabaseManagement                                  ////////
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

package com.limitless.smartbudget.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.limitless.smartbudget.db.interfaces.CategoryDoa;
import com.limitless.smartbudget.db.interfaces.FixedExpensesDao;
import com.limitless.smartbudget.db.interfaces.IncomesDao;
import com.limitless.smartbudget.db.interfaces.LivingExpensesDoa;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.db.model.FixedExpenses;
import com.limitless.smartbudget.db.model.Incomes;
import com.limitless.smartbudget.db.model.LivingExpenses;

@Database(entities = {FixedExpenses.class, Incomes.class, LivingExpenses.class, Category.class}
        , version = 1, exportSchema = false)
@TypeConverters(com.limitless.smartbudget.TypeConverters.class)
public abstract class DatabaseManagement extends RoomDatabase {
    public abstract FixedExpensesDao fixedExpensesDao();

    public abstract IncomesDao incomesDao();

    public abstract LivingExpensesDoa livingExpensesDoa();

    public abstract CategoryDoa categoryDoa();
}
