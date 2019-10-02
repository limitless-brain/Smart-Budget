/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: Constants.java                                        ////////
 * ////////Class Name: Constants                                  ////////
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
import java.util.Date;
import java.util.Locale;

public class Constants {

    // About Constants
    public static final String YAZAN_PIC_URL = "https://scontent.fbey4-1.fna.fbcdn.net/v/t1.0-9/" +
            "21149948_1447958455284982_7969909462353557852_n.jpg?_" +
            "nc_cat=105&_nc_oc=AQlzPl2ohmp3giVxfNwY5vX9fbpgyzH7zNBHt59qag8r" +
            "_lVjh5YyNs01p2xioRHs-ek&_nc_ht=scontent.fbey4-1.fna&oh=56c5bdd9e215" +
            "f5722aa876a07ee7043b&oe=5E36F9B7";


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
}
