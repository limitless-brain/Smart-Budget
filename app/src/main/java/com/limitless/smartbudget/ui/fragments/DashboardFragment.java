/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: DashboardFragment.java                                        ////////
 * ////////Class Name: DashboardFragment                                  ////////
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

package com.limitless.smartbudget.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.ui.arrayadapters.LineChartCategoriesArrayAdapter;
import com.limitless.smartbudget.ui.fragments.Showcase.CurrencyPickerFragment;
import com.limitless.smartbudget.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = DashboardFragment.class.getSimpleName();
    //  State field
    private Activity mActivity;
    private Context mContext;
    private Currency mCurrency;
    private ArrayList<Category> mCategoryArrayList = new ArrayList<>();
    private ArrayList<Category> mShowedCategories = new ArrayList<>();

    //  Ui elements
    private PieChart pieChart;
    private LineChart mLineChart;
    private ImageButton mLineChartCustomizeBtn;
    private BottomSheetDialog mLineChartCategoriesBS;
    private LineChartCategoriesArrayAdapter mLineChartBSCListAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  Get currency symbol
        String code = mActivity.getSharedPreferences(Constants.SETTING_PREF_FILE, Context.MODE_PRIVATE)
                .getString(CurrencyPickerFragment.PREF_KEY_CURRENCY, "USD");
        mCurrency = Currency.getInstance(code);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //  Initialize categories list
        initCategoriesList();

        //  Initialize UI
        initUi(root);

        //  Set UI Listeners
        setUIListeners();

        // Pie chart
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


        pieChart.setData(pieData);
        pieChart.animateXY(1400, 1400);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(false);
        pieChart.setNoDataText("No Data");

        //  Line Chart
        initLineChart();
        return root;
    }

    private void setUIListeners() {
        mLineChartCustomizeBtn.setOnClickListener(v -> {
            mLineChartCategoriesBS.show();
        });
    }

    private void initLineChart() {
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisLeft().setAxisMinimum(0);
        mLineChart.getXAxis().setAxisMinimum(1);
        //  Set data to be integer not float
        mLineChart.getXAxis().setGranularityEnabled(true);
        mLineChart.getXAxis().setGranularity(1f);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setDrawBorders(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setDescription(null);
        mLineChart.setNoDataText("No Data");
        mLineChart.animateXY(1400,
                1400, Easing.EaseInBounce);
    }

    private void drawLineChart() {
        mLineChart.clear();
        int dayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<LineDataSet> lines = new ArrayList<>();
        ArrayList<Integer> circleColors = new ArrayList<>();
        if (mShowedCategories.size() > 0) {
            for (Category category : mShowedCategories) {
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
                                    , category).doubleValue());
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
                LineDataSet categoryLine = new LineDataSet(entryArrayList, category.getName());
                int categoryColor = category.getColor();
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
                            s = super.getFormattedValue(value) + "\t" + mCurrency.getSymbol();
                        return s;
                    }
                });
                lines.add(categoryLine);
            }

            LineData lineData = new LineData();
            for (LineDataSet dataSet : lines)
                lineData.addDataSet(dataSet);
            lineData.setHighlightEnabled(true);
            mLineChart.setData(lineData);
        }
        else {
            mLineChart.setData(null);
        }
        mLineChart.invalidate();
    }

    private void initCategoriesList() {
        Observable<Category> observable = Observable.fromIterable(getCategoryList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<Category>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Category o) {
                mCategoryArrayList.add(o);
                mShowedCategories.add(o);
                Log.d(TAG, "onNext: " + o.getName());
                drawLineChart();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void initUi(View root) {
        pieChart = root.findViewById(R.id.incomeExpensesChart);
        mLineChart = root.findViewById(R.id.categoryLineChart);
        mLineChartCustomizeBtn = root.findViewById(R.id.dashboardLineChartCustomizeBtn);
        mLineChartCategoriesBS = new BottomSheetDialog(mContext);
        mLineChartCategoriesBS.setContentView(R.layout.bottomsheet_line_chart);
        ListView listView = mLineChartCategoriesBS.findViewById(R.id.linechartCategoriesList);
        if (listView != null) {
            mLineChartBSCListAdapter = new LineChartCategoriesArrayAdapter(mContext
                    , mCategoryArrayList);
            mLineChartBSCListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    mShowedCategories = mLineChartBSCListAdapter.getShowedCategories();
                    drawLineChart();
                }

                @Override
                public void onInvalidated() {
                    super.onInvalidated();
                }
            });
            listView.setAdapter(mLineChartBSCListAdapter);
        }
    }

    private List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>();
        List categoriesObjects = (List) ControlPanel.getInstance(mContext)
                .getAllRecords(Constants.CATEGORIES);
        for (Object o : categoriesObjects)
            categories.add((Category) o);
        return categories;
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
