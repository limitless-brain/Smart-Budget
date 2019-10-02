/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: FixedExpenses.java                                        ////////
 * ////////Class Name: FixedExpenses                                  ////////
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
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.limitless.smartbudget.utils.Constants;

import java.util.Date;
import java.util.Locale;

@Entity(tableName = "fixed_expenses", indices = @Index(value = "id", unique = true))
public class FixedExpenses {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "value")
    private String value;
    @ColumnInfo(name = "description")
    private String description;

    public FixedExpenses(Date date, String value, String description) {
        this.date = date;
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return Constants.dateToString(getDate(), Constants.DATE_PATTERN, Locale.US)
                + ';' + getValue() + ';'
                + getDescription() + ';';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof FixedExpenses))
            return false;
        return this.id == ((FixedExpenses) obj).id;
    }
}
