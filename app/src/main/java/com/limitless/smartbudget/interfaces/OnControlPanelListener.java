/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: OnControlPanelListener.java                                        ////////
 * ////////Class Name: OnControlPanelListener                                  ////////
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

package com.limitless.smartbudget.interfaces;

import com.limitless.smartbudget.db.model.Category;

public interface OnControlPanelListener {
    //  Get all records from table
    Object getAllRecords(int table);

    //  Get record by id
    Object getRecordById(int table, int id);

    //  Get day records
    Object getRecordsByDay(int table, long d1);

    //  Get total paid
    Double getTotalValue(int table);

    //  Get Total Expenses by Category and date
    Double getTotalValue(int table, long date, Category category);

    //  Add new record to database
    void addRecordToTable(int table, Object record);

    //  Update a record to database
    void updateRecordInTable(int table, Object record);

    //  Delete a record to database
    void deleteRecordInTable(int table, Object record);
}
