/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: ControlPanel.java                                        ////////
 * ////////Class Name: ControlPanel                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 11/20/19 1:05 PM                                       ////////
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

package com.limitless.smartbudget;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.limitless.smartbudget.db.DatabaseManagement;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.db.model.FixedExpenses;
import com.limitless.smartbudget.db.model.Incomes;
import com.limitless.smartbudget.db.model.LivingExpenses;
import com.limitless.smartbudget.interfaces.OnControlPanelListener;
import com.limitless.smartbudget.utils.Constants;

import java.util.List;

public class ControlPanel implements OnControlPanelListener {

    public static final String TAG = ControlPanel.class.getSimpleName();
    private static ControlPanel INSTANCE = null;
    private Context context;
    private DatabaseManagement databaseManagement;

    public ControlPanel(Context context) {

        this.context = context;
        databaseManagement = Room.databaseBuilder(context
                , DatabaseManagement.class, "app_db")
                .allowMainThreadQueries()
                .build();
        initDatabase();
    }

    public static ControlPanel getInstance(@NonNull Context context) {
        if (INSTANCE == null) INSTANCE = new ControlPanel(context);
        return INSTANCE;
    }

    private void initDatabase() {
        List<Category> categories = databaseManagement.categoryDoa().getAllCategories();
        if (categories.isEmpty()) {
            //  Create default category
            // TODO: 9/22/2019 Add them to resources
            databaseManagement.categoryDoa().insert(new Category("Food", ColorTemplate.MATERIAL_COLORS[1]));
            databaseManagement.categoryDoa().insert(new Category("Transportation", ColorTemplate.MATERIAL_COLORS[0]));
            databaseManagement.categoryDoa().insert(new Category("Petty Cash", ColorTemplate.MATERIAL_COLORS[2]));
        } else {
            //  Created no need to do anything
        }
    }

    @Override
    public Object getAllRecords(int table) {
        Object object = null;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                object = (databaseManagement.livingExpensesDoa().getAllRecords());
                break;
            case Constants.FIXED_EXPENSES:
                object = (databaseManagement.fixedExpensesDao().getAllRecords());
                break;
            case Constants.INCOMES:
                object = (databaseManagement.incomesDao().getAllRecords());
                break;
            case Constants.SAVING:
                // Not implemented yet
                break;
            case Constants.CATEGORIES:
                object = (databaseManagement.categoryDoa().getAllCategories());
                break;
        }
        return object;
    }

    @Override
    public Object getRecordsByDay(int table, long d1) {

        Object object;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                object = (databaseManagement.livingExpensesDoa().getRecordsBetween(d1, d1));
                break;
            case Constants.FIXED_EXPENSES:
                object = (databaseManagement.fixedExpensesDao().getRecordsBetween(d1, d1));
                break;
            case Constants.INCOMES:
                object = (databaseManagement.incomesDao().getRecordsBetween(d1, d1));
                break;
            default:
                return null;
        }
        return object;
    }

    @Override
    public void addRecordToTable(int table, Object record) {
        switch (table) {
            case Constants.LIVING_EXPENSES:
                databaseManagement.livingExpensesDoa().insert((LivingExpenses) record);
                break;
            case Constants.FIXED_EXPENSES:
                databaseManagement.fixedExpensesDao().insert((FixedExpenses) record);
                break;
            case Constants.INCOMES:
                databaseManagement.incomesDao().insert((Incomes) record);
                break;
            case Constants.SAVING:
                // Not implemented yet
                break;
            case Constants.CATEGORIES:
                databaseManagement.categoryDoa().insert((Category) record);
                break;
        }
    }

    @Override
    public void updateRecordInTable(int table, Object record) {
        switch (table) {
            case Constants.LIVING_EXPENSES:
                databaseManagement.livingExpensesDoa().update((LivingExpenses) record);
                break;
            case Constants.FIXED_EXPENSES:
                databaseManagement.fixedExpensesDao().update((FixedExpenses) record);
                break;
            case Constants.INCOMES:
                databaseManagement.incomesDao().update((Incomes) record);
                break;
            case Constants.SAVING:
                // Not implemented yet
                break;
            case Constants.CATEGORIES:
                databaseManagement.categoryDoa().update((Category) record);
                break;
        }
    }

    @Override
    public void deleteRecordInTable(int table, Object record) {
        switch (table) {
            case Constants.LIVING_EXPENSES:
                databaseManagement.livingExpensesDoa().delete((LivingExpenses) record);
                break;
            case Constants.FIXED_EXPENSES:
                databaseManagement.fixedExpensesDao().delete((FixedExpenses) record);
                break;
            case Constants.INCOMES:
                databaseManagement.incomesDao().delete((Incomes) record);
                break;
            case Constants.SAVING:
                // Not implemented yet
                break;
            case Constants.CATEGORIES:
                databaseManagement.categoryDoa().delete((Category) record);
                break;
        }
    }

    @Override
    public Object getRecordById(int table, int id) {
        Object record = null;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                record = databaseManagement.livingExpensesDoa().getRecordById(id);
                break;
            case Constants.FIXED_EXPENSES:
                record = databaseManagement.fixedExpensesDao().getRecordById(id);
                break;
            case Constants.INCOMES:
                record = databaseManagement.incomesDao().getRecordById(id);
                break;
            case Constants.SAVING:
                // Not implemented yet
                break;
            case Constants.CATEGORIES:
                record = databaseManagement.categoryDoa().getRecordById(id);
                break;
        }
        return record;
    }

    @Override
    public Double getTotalValue(int table) {
        Double Result = 0d;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                Result = databaseManagement.livingExpensesDoa().getTotalExpenses();
                break;
            case Constants.FIXED_EXPENSES:
                Result = databaseManagement.fixedExpensesDao().getTotalExpenses();
                break;
            case Constants.INCOMES:
                Result = databaseManagement.incomesDao().getTotalIncomes();
                break;
        }
        if (Result == null)
            return 0d;
        return Result;
    }

    @Override
    public Double getTotalValue(int table, long date, Category category) {
        Double result = 0d;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                result = databaseManagement.livingExpensesDoa()
                        .getTotalExpensesForCategory(category.getName(), date);
                break;
            case Constants.SAVING:
                break;
        }
        return result == null ? 0d : result;

    }
}
