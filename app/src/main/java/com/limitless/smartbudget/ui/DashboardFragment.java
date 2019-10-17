/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: DashboardFragment.java                                        ////////
 * ////////Class Name: DashboardFragment                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 10/17/19 2:53 PM                                       ////////
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

package com.limitless.smartbudget.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private Context mContext;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public DashboardFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        float incomes = (float) ControlPanel.getInstance(mContext)
                .getTotalValue(Constants.INCOMES).doubleValue();
        float expenses = (float) (ControlPanel.getInstance(mContext)
                .getTotalValue(Constants.FIXED_EXPENSES) +
                ControlPanel.getInstance(mContext)
                        .getTotalValue(Constants.LIVING_EXPENSES));
        float saving = incomes - expenses;

        PieEntry pieIncomesEntry = new PieEntry(incomes);
        pieIncomesEntry.setLabel("Incomes");
        PieEntry pieExpensesEntry = new PieEntry(expenses);
        pieExpensesEntry.setLabel("Expenses");
        ArrayList<PieEntry> entries = new ArrayList<>();
        PieEntry pieSavingEntry = new PieEntry(Math.abs(saving),
                saving > 0 ? "Savings" : "Consuming Savings");
        entries.add(pieExpensesEntry);
        entries.add(pieIncomesEntry);
        entries.add(pieSavingEntry);

        PieDataSet pieDataSet = new PieDataSet(entries, "Total for this month");
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3.0f);
        pieDataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.JOYFUL_COLORS[0]);
        colors.add(ColorTemplate.JOYFUL_COLORS[1]);
        colors.add(saving > 0 ? Color.GREEN : Color.RED);

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);

        PieChart pieChart = root.findViewById(R.id.incomeExpensesChart);

        pieChart.setData(pieData);
        pieChart.animateXY(1400, 1400);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(false);
        pieChart.setNoDataText("No Data");

        //  Line Chart
        int dayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        List categories = (List) ControlPanel.getInstance(mContext)
                .getAllRecords(Constants.CATEGORIES);
        ArrayList<LineDataSet> lines = new ArrayList<>();
        ArrayList<Integer> circleColors = new ArrayList<>();
        LineChart lineChart = root.findViewById(R.id.categoryLineChart);
        for (Object object : categories) {
            //  For each category
            ArrayList<Entry> entryArrayList = new ArrayList<>();
            for (int i = 1; i <= dayOfMonth; i++) {
                //  plot a line
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, i);
                String s = Constants.dateToString(calendar.getTime(), Constants.DATE_PATTERN
                        , Locale.US);
                Date date;
                date = getDate(s);

                float yValue = (float) (ControlPanel.getInstance(mContext)
                        .getTotalValue(Constants.LIVING_EXPENSES
                                , Objects.requireNonNull(date).getTime()
                                , (Category) object).doubleValue());
                if (yValue == 0) {
                    int color;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        color = getResources().getColor(android.R.color.transparent, null);
                    else
                        color = getResources().getColor(android.R.color.transparent);
                    circleColors.add(color);
                } else
                    circleColors.add(ColorTemplate.getHoloBlue());
                Entry entry;
                entry = new Entry(i, yValue);
                entryArrayList.add(entry);
            }
            LineDataSet categoryLine = new LineDataSet(entryArrayList, ((Category) object).getName());
            int categoryColor = ((Category) object).getColor();
            categoryLine.setColor(categoryColor);
            categoryLine.setLineWidth(3);
            categoryLine.setDrawFilled(true);
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.BOTTOM_TOP
                    , new int[]{Color.WHITE, categoryColor});
            gradientDrawable.setAlpha(128);
            categoryLine.setFillDrawable(gradientDrawable);
            categoryLine.setCircleColors(circleColors);
            categoryLine.setDrawCircleHole(false);
            categoryLine.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    String s;
                    if (value == 0)
                        s = "";
                    else
                        s = super.getFormattedValue(value);
                    return s;
                }
            });
            lines.add(categoryLine);
        }

        LineData lineData = new LineData();
        for (LineDataSet dataSet : lines)
            lineData.addDataSet(dataSet);
        lineData.setHighlightEnabled(true);
        lineChart.setData(lineData);
        lineChart.getAxisRight().setEnabled(false);
        //lineChart.getAxisLeft().setEnabled(false);
        lineChart.getXAxis().setAxisMinimum(1);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1f);
        //lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDescription(null);
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("No Data");
        lineChart.animateXY(1400, 2000, Easing.Linear);
        return root;
    }

    private Date getDate(String s) {
        try {
            return Constants.dateFromString(s, Constants.DATE_PATTERN, Locale.US);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
