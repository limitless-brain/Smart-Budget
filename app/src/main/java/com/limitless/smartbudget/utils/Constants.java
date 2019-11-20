/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Constants.java                                        ////////
 * ////////Class Name: Constants                                  ////////
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

package com.limitless.smartbudget.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constants {

    // About Constants
    public static final String YAZAN_PIC_URL = "https://med" +
            "ia.licdn.com/dms/image/C5603AQE1AUzGnxVJ2g/profile-displayphoto-shrink_200" +
            "_200/0?e=1576108800&v=beta&t=6Zh2Y7WEJqku1EsGEVwrTOkeP3k2-qfL4991qJSbx_o";
    public static final String ANAS_PIC_URL = "https://media.licdn.com/dms/image/C4E03AQE9qG0dR6HDvA/" +
            "profile-displayphoto-shrink_800_800/0?e=1576108800&v=beta&t=nX5e" +
            "LPZvoAluzZ2WE5bULBBeoq1IigMCn7QslnbxRnA";


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

    //  Sign-in
    public static final int FIREBASE_REQUEST_CODE = 100;

    //  Preferences
    public static final String SIGN_IN_KEY = "sign_in_key";

    //  Text pattern constants
    public static final String DATE_PATTERN = "yyyy/MM/dd";
    public static final String SETTING_PREF_FILE = "app_setting";

    //  Extract date from text
    public static Date dateFromString(String date, String pattern, Locale locale) throws ParseException {
        DateFormat format = new SimpleDateFormat(pattern, locale);
        return format.parse(date);
    }

    //  Convert date to text
    @NotNull
    public static String dateToString(Date date, String pattern, Locale locale) {
        DateFormat format = new SimpleDateFormat(pattern, locale);
        return format.format(date);
    }

    //  Round picture inside a view
    public static void roundPicture(@NotNull ImageView v, @NotNull Context context) {
        Bitmap bitmap = ((BitmapDrawable) v.getDrawable()).getBitmap();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                context.getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2f);
        v.setImageDrawable(roundedBitmapDrawable);
    }

    //  Generate color list
    @NotNull
    public static int[] getColorList() {
        ArrayList<Integer> colorList = new ArrayList<>();
        addArray(colorList, ColorTemplate.COLORFUL_COLORS);
        addArray(colorList, ColorTemplate.JOYFUL_COLORS);
        addArray(colorList, ColorTemplate.LIBERTY_COLORS);
        addArray(colorList, ColorTemplate.MATERIAL_COLORS);
        addArray(colorList, ColorTemplate.PASTEL_COLORS);
        addArray(colorList, ColorTemplate.VORDIPLOM_COLORS);
        int[] colors = new int[colorList.size()];
        for (int i = 0; i < colors.length; i++)
            colors[i] = colorList.get(i);
        return colors;
    }
    private static void addArray(ArrayList<Integer> colorList, @NotNull int[] colors) {
        for (int color : colors)
            colorList.add(color);
    }

    //  Get Current Month
    public static String getCurrentMonth() {
        return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
    }
}
