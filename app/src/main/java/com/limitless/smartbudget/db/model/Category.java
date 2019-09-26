/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Category.java                                        ////////
 * ////////Class Name: Category                                  ////////
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

package com.limitless.smartbudget.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Random;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "c_id")
    private int id;

    @ColumnInfo(name = "c_name")
    private String name;

    @ColumnInfo(name = "c_icon")
    private int iconRes;

    public Category(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
        id = new Random(Calendar.getInstance().getTimeInMillis()).nextInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
