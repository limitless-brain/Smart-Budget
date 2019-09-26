/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: OnControlPanelListener.java                                        ////////
 * ////////Class Name: OnControlPanelListener                                  ////////
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

package com.limitless.smartbudget.interfaces;

public interface OnControlPanelListener {
    //  Get all records from table
    Object getAllRecords(int table);

    //  Get record by id
    Object getRecordById(int table, int id);

    //  Add new record to database
    void addRecordToTable(int table, Object record);

    //  Update a record to database
    void updateRecordInTable(int table, Object record);

    //  Delete a record to database
    void deleteRecordInTable(int table, Object record);
}
