/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Category.java                                        ////////
 * ////////Class Name: Category                                  ////////
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

package com.limitless.smartbudget.db.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private int color;

    public Category(String name, int color) {
        this.name = name;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Category))
            return false;
        return this.name.equals(((Category) obj).name + ';');
    }
}
