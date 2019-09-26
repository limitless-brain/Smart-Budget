/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Constants.java                                        ////////
 * ////////Class Name: Constants                                  ////////
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

package com.limitless.smartbudget.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {

    //  tables index
    public static final int LIVING_EXPENSES = 0;
    public static final int FIXED_EXPENSES = 1;
    public static final int INCOMES = 2;
    public static final int SAVING = 3;
    public static final int CATEGORIES = 4;

    //  tables names
    public static final String LIVING_EXPENSE_TBN = "living_expenses";
    public static final String FIXED_EXPENSES_TBN = "fixed_expenses";
    public static final String INCOMES_TBN = "incomes";
    public static final String SAVING_TBN = "saving";
    public static final String CATEGORIES_TBN = "categories";

    //  Text pattern constants
    public static final String DATE_PATTERN = "yyyy/MM/dd";

    //  Extract date from text
    public static Date dateFromString(String date, String pattern, Locale locale) throws ParseException {
        DateFormat format = new SimpleDateFormat(pattern, locale);
        return format.parse(date);
    }

    //  Convert date to text
    public static String dateToString(Date date, String pattern, Locale locale) {
        DateFormat format = new SimpleDateFormat(pattern, locale);
        return format.format(date);
    }
}
