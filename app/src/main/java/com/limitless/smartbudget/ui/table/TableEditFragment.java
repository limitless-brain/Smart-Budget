/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: TableEditFragment.java                                        ////////
 * ////////Class Name: TableEditFragment                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 9/27/19 9:09 PM                                       ////////
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

package com.limitless.smartbudget.ui.table;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.db.model.FixedExpenses;
import com.limitless.smartbudget.db.model.Incomes;
import com.limitless.smartbudget.db.model.LivingExpenses;
import com.limitless.smartbudget.interfaces.OnTableManagementListener;
import com.limitless.smartbudget.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TableEditFragment extends Fragment implements OnTableManagementListener,
        DatePickerTimeline.OnDateSelectedListener {

    //  State Variables
    private Context mContext;
    private int mTable;
    private int selectedItem;
    private boolean addNew = true;
    private String mDateString;
    private String selectedCategory;
    private ArrayAdapter mArrayAdapter;

    //  UI elements
    private TextView mDateTitleTextView;
    private EditText mDateEditText;
    private DatePickerTimeline mDatePickerTimeline;
    private TextView mCategoryTitleTextView;
    private ChipCloud mCategoryChipCloud;
    private TextView mTargetTitleTextView;
    private EditText mTargetEditText;
    private TextView mValueTitleTextView;
    private EditText mValueEditText;
    private TextView mDescriptionTitleTextView;
    private EditText mDescriptionEditText;
    private Button mConfirmButton;
    private ListView mListView;

    //  Table lists
    private List mFixedExpensesList;
    private List mLivingExpensesList;
    private List mIncomesList;
    //  private List mSavingList;
    private List mCategoryList;

    //  Database
    private ControlPanel controlPanel;

    public TableEditFragment() {
        // Required empty public constructor
    }

    public TableEditFragment(ListView listView, Context context) {
        mContext = context;
        mListView = listView;
        mArrayAdapter = (ArrayAdapter) listView.getAdapter();
        controlPanel = ControlPanel.getInstance(mContext);
        getTables();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_edit, container, false);

        initUi(view);
        updateTargetTable(Constants.LIVING_EXPENSES);
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Handling implemented interfaces methods override
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Handling date picker onDateSelect interface method
     *
     * @param year  picked year
     * @param month picked month
     * @param day   picked day
     * @param index picked item index in the timeline
     */
    @Override
    public void onDateSelected(int year, int month, int day, int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        String dateText = Constants.dateToString(date, Constants.DATE_PATTERN, Locale.US);
        mDateEditText.setText(dateText);
        if (mDatePickerTimeline.getVisibility() == View.VISIBLE &&
                !mDateString.equals(dateText)) {
            mDateString = dateText;
            mDatePickerTimeline.setVisibility(View.GONE);
            mDateEditText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Received from TableManagement fragment
     *
     * @param table selected table
     */
    @Override
    public void onTargetTableChanged(int table) {
        mTable = table;
        addNew = true;
        selectedItem = -1;
        selectedCategory = null;
        updateTargetTable(mTable);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Initialize ui and handle ui events depend on table selection
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Initialize ui from layout
     *
     * @param root reference to root view
     */
    private void initUi(@NotNull View root) {

        mDateTitleTextView = root.findViewById(R.id.dateTitleTextView);
        mDateEditText = root.findViewById(R.id.dateEditText);
        mDatePickerTimeline = root.findViewById(R.id.datePicker);
        mCategoryTitleTextView = root.findViewById(R.id.categoryTitleTextView);
        mCategoryChipCloud = root.findViewById(R.id.categoryChipCloud);
        mTargetTitleTextView = root.findViewById(R.id.targetTitle);
        mTargetEditText = root.findViewById(R.id.targetEditText);
        mValueTitleTextView = root.findViewById(R.id.valueTitleTextView);
        mValueEditText = root.findViewById(R.id.valueEditText);
        mDescriptionTitleTextView = root.findViewById(R.id.descriptionTitleTextView);
        mDescriptionEditText = root.findViewById(R.id.descriptionEditText);
        mConfirmButton = root.findViewById(R.id.buttonConfirm);

    }

    /**
     * Handling ui visibility
     *
     * @param table traget table
     */
    private void handleUIVisibility(int table) {
        setAllUIVisible();
        switch (table) {
            case Constants.LIVING_EXPENSES:
                mTargetTitleTextView.setVisibility(View.GONE);
                mTargetEditText.setVisibility(View.GONE);
                break;
            case Constants.FIXED_EXPENSES:
            case Constants.INCOMES:
                mTargetTitleTextView.setVisibility(View.GONE);
                mTargetEditText.setVisibility(View.GONE);
                mCategoryTitleTextView.setVisibility(View.GONE);
                mCategoryChipCloud.setVisibility(View.GONE);
                break;
            case Constants.SAVING:
                mDateTitleTextView.setVisibility(View.GONE);
                mDateEditText.setVisibility(View.GONE);
                mDatePickerTimeline.setVisibility(View.GONE);
                mCategoryTitleTextView.setVisibility(View.GONE);
                mCategoryChipCloud.setVisibility(View.GONE);
                mDescriptionTitleTextView.setVisibility(View.GONE);
                mDescriptionEditText.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Set all ui element to be visible
     */
    private void setAllUIVisible() {
        mDateTitleTextView.setVisibility(View.VISIBLE);
        mDateEditText.setVisibility(View.VISIBLE);
        mDatePickerTimeline.setVisibility(View.VISIBLE);
        mCategoryTitleTextView.setVisibility(View.VISIBLE);
        mCategoryChipCloud.setVisibility(View.VISIBLE);
        mTargetTitleTextView.setVisibility(View.VISIBLE);
        mTargetEditText.setVisibility(View.VISIBLE);
        mValueTitleTextView.setVisibility(View.VISIBLE);
        mValueEditText.setVisibility(View.VISIBLE);
        mDescriptionTitleTextView.setVisibility(View.VISIBLE);
        mDescriptionEditText.setVisibility(View.VISIBLE);
        mConfirmButton.setVisibility(View.VISIBLE);
    }

    /**
     * Reset ui for the target table
     *
     * @param table target table
     */
    private void resetUI(int table) {
        //  Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //  Date formatter
        Date date = calendar.getTime();
        mDateString = Constants.dateToString(date, Constants.DATE_PATTERN, Locale.US);
        //  Show date inside editText
        mDateEditText.setHint(mDateString);

        //  Date picker
        if (table != Constants.SAVING)
            setupDatePicker(year, month, day);

        //  Value
        mValueEditText.setHint(String.valueOf(0));
        mValueEditText.setText("");

        //  Description
        if (table != Constants.SAVING) {
            mDescriptionEditText.setHint("Description");
            mDescriptionEditText.setText("");
        }

        //  Category chips
        if (table == Constants.LIVING_EXPENSES) {
            mCategoryChipCloud.removeAllViews();
            for (int i = 0; i < mCategoryList.size(); i++) {
                mCategoryChipCloud.addChip(((Category) mCategoryList.get(i)).getName());
            }
        }
        //  Handling UI listeners
        setUiListeners();

        //  Check if record filled
        checkRecordAvailability();
    }

    /**
     * Set date picker range
     *
     * @param year  target year
     * @param month target month
     * @param day   target day
     */
    private void setupDatePicker(int year, int month, int day) {
        mDatePickerTimeline.setOnDateSelectedListener(this);
        mDatePickerTimeline.setFirstVisibleDate(year, month - 1, 1);
        mDatePickerTimeline.setLastVisibleDate(year, month + 1, 1);
        mDatePickerTimeline.setFollowScroll(true);
        mDatePickerTimeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                DateFormat format = new SimpleDateFormat("MMM", Locale.US);
                return format.format(calendar.getTime());
            }
        });
        mDatePickerTimeline.setSelectedDate(year, month, day);
        mDatePickerTimeline.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatePickerTimeline.setVisibility(View.GONE);
            }
        }, 50);
    }

    /**
     * Set ui interface listener
     */
    @SuppressWarnings("ClickableViewAccessibility")
    private void setUiListeners() {

        //  Categories chip list
        mCategoryChipCloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                if (mCategoryChipCloud.getChildCount() < 2)
                    return;
                mCategoryChipCloud.removeAllViews();
                selectedCategory = ((Category) mCategoryList.get(index)).getName();
                mCategoryChipCloud.addChip(selectedCategory);
                mCategoryChipCloud.setSelectedChip(0);
                checkRecordAvailability();
            }

            @Override
            public void chipDeselected(int index) {
                mCategoryChipCloud.removeAllViews();
                selectedCategory = null;
                for (Object category : mCategoryList)
                    mCategoryChipCloud.addChip(((Category) category).getName());
                checkRecordAvailability();
            }
        });

        //  Calender for handling date
        Calendar calendar = Calendar.getInstance();
        //  Date edit text touch listener
        mDateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    Date d = Constants.dateFromString(mDateString, Constants.DATE_PATTERN, Locale.US);
                    calendar.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDatePickerTimeline.setVisibility(View.VISIBLE);
                mDatePickerTimeline.setSelectedDate(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerTimeline.centerOnSelection();
                mDateEditText.setVisibility(View.GONE);
                return false;
            }
        });
        //  Value edit text watcher
        mValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRecordAvailability();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO: 9/25/2019 Format value
            }
        });

        //  Description edit text watcher
        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRecordAvailability();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //  Confirm button click listener
        mConfirmButton.setOnClickListener(view -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            try {
                interactWithTable(mTable, addNew);
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
            }
            resetUI(mTable);
            updateRecordList(mTable);
        });

        //  Long press list item listener
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = i;
                PopupMenu menu = new PopupMenu(mContext, view);
                menu.inflate(R.menu.list_item_menu);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                addNew = true;
                                deleteRecord(mTable);
                                break;
                            case R.id.update:
                                addNew = false;
                                updateUiForUpdateRecord(mTable);
                                break;
                        }
                        return false;
                    }
                });
                return false;
            }
        });
    }

    /**
     * Check if record legal and enable confirm button
     */
    private void checkRecordAvailability() {
        boolean isEnabled = false;
        switch (mTable) {
            case Constants.LIVING_EXPENSES:
                isEnabled = mCategoryChipCloud.getChildCount() == 1 &&
                        !mValueEditText.getText().toString().isEmpty() &&
                        Double.parseDouble(mValueEditText.getText().toString()) > 0;
                break;
            case Constants.FIXED_EXPENSES:
            case Constants.INCOMES:
                isEnabled = !mValueEditText.getText().toString().isEmpty() &&
                        Double.parseDouble(mValueEditText.getText().toString()) > 0 &&
                        !mDescriptionEditText.getText().toString().isEmpty();
                break;
            case Constants.SAVING:
                // TODO: 9/26/2019 add logic here
                break;
        }
        mConfirmButton.setEnabled(isEnabled);
    }

    /**
     * Update ui for target table
     *
     * @param table
     */
    private void updateUiForUpdateRecord(int table) {
        if (mDatePickerTimeline.getVisibility() == View.VISIBLE && table != Constants.SAVING)
            mDatePickerTimeline.setVisibility(View.GONE);
        if (mDateEditText.getVisibility() == View.GONE && table != Constants.SAVING)
            mDateEditText.setVisibility(View.VISIBLE);
        switch (table) {
            case Constants.LIVING_EXPENSES:
                if (mLivingExpensesList.size() < 1) return;
                LivingExpenses record = (LivingExpenses) mLivingExpensesList.get(selectedItem);
                String date = Constants.dateToString(record.getDate(), Constants.DATE_PATTERN, Locale.US);
                mDateString = date;
                mDateEditText.setText(date);
                mCategoryChipCloud.removeAllViews();
                selectedCategory = record.getCategory().getName();
                mCategoryChipCloud.addChip(selectedCategory);
                mCategoryChipCloud.setSelectedChip(0);
                mValueEditText.setText(record.getValue());
                mDescriptionEditText.setText(record.getDescription());
                break;
            case Constants.FIXED_EXPENSES:
                if (mFixedExpensesList.size() < 1) return;
                FixedExpenses record1 = (FixedExpenses) mFixedExpensesList.get(selectedItem);
                String date1 = Constants.dateToString(record1.getDate(), Constants.DATE_PATTERN, Locale.US);
                mDateString = date1;
                mDateEditText.setText(date1);
                mValueEditText.setText(record1.getValue());
                mDescriptionEditText.setText(record1.getDescription());
                break;
            case Constants.INCOMES:
                if (mIncomesList.size() < 1) return;
                Incomes record2 = (Incomes) mIncomesList.get(selectedItem);
                String date2 = Constants.dateToString(record2.getDate(), Constants.DATE_PATTERN, Locale.US);
                mDateString = date2;
                mDateEditText.setText(date2);
                mValueEditText.setText(record2.getValue());
                mDescriptionEditText.setText(record2.getDescription());
                break;
        }
    }

    /**
     * update listView inside TableManagement fragment to target table
     *
     * @param table
     */
    private void updateRecordList(int table) {
        ArrayList<String> data = new ArrayList<>();
        switch (table) {
            case Constants.LIVING_EXPENSES:
                for (Object o : mLivingExpensesList) {
                    LivingExpenses livingExpenses = (LivingExpenses) o;
                    data.add(Objects.requireNonNull(livingExpenses.toString()));
                }
                break;
            case Constants.FIXED_EXPENSES:
                for (Object o : mFixedExpensesList) {
                    data.add(o.toString());
                }
                break;
            case Constants.INCOMES:
                for (Object o : mIncomesList) {
                    data.add(o.toString());
                }
                break;
        }

        mArrayAdapter.clear();
        mArrayAdapter.addAll(data);
    }

    /**
     * Call table and ui updating methods
     *
     * @param table
     */
    private void updateTargetTable(int table) {
        updateRecordList(table);
        handleUIVisibility(table);
        resetUI(table);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Table management functions
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all tables data
     */
    private void getTables() {
        mLivingExpensesList = (List) ControlPanel.getInstance(mContext).getAllRecords(Constants.LIVING_EXPENSES);
        mFixedExpensesList = (List) ControlPanel.getInstance(mContext).getAllRecords(Constants.FIXED_EXPENSES);
        mIncomesList = (List) ControlPanel.getInstance(mContext).getAllRecords(Constants.INCOMES);
        mCategoryList = (List) ControlPanel.getInstance(mContext).getAllRecords(Constants.CATEGORIES);
    }

    /**
     * Add or Update a record inside a table
     *
     * @param table  target table
     * @param addNew add new record if true or update if false
     * @throws ParseException error when extracting date from text
     */
    private void interactWithTable(int table, boolean addNew) throws ParseException {

        //  Record possible values
        String date = mDateString;
        String value = mValueEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        int id;

        switch (table) {
            case Constants.LIVING_EXPENSES:
                Category category = null;
                for (Object o : mCategoryList) {
                    if (((Category) o).getName().equals(selectedCategory))
                        category = (Category) o;
                }
                LivingExpenses livingExpensesRecord = new LivingExpenses(
                        Constants.dateFromString(date, Constants.DATE_PATTERN, Locale.US),
                        category, value, description);

                if (addNew || selectedItem == -1)
                    controlPanel.addRecordToTable(Constants.LIVING_EXPENSES, livingExpensesRecord);
                else {
                    id = ((LivingExpenses) mLivingExpensesList.get(selectedItem)).getId();
                    livingExpensesRecord.setId(id);
                    controlPanel.updateRecordInTable(Constants.LIVING_EXPENSES, livingExpensesRecord);
                }
                break;
            case Constants.FIXED_EXPENSES:
                FixedExpenses fixedExpensesRecord = new FixedExpenses(
                        Constants.dateFromString(date, Constants.DATE_PATTERN, Locale.US),
                        value, description);
                if (addNew || selectedItem == -1)
                    controlPanel.addRecordToTable(Constants.FIXED_EXPENSES, fixedExpensesRecord);
                else {
                    id = ((FixedExpenses) mFixedExpensesList.get(selectedItem)).getId();
                    fixedExpensesRecord.setId(id);
                    controlPanel.updateRecordInTable(Constants.FIXED_EXPENSES, fixedExpensesRecord);
                }
                break;
            case Constants.INCOMES:
                Incomes incomesRecord = new Incomes(
                        Constants.dateFromString(date, Constants.DATE_PATTERN, Locale.US),
                        value, description);
                if (addNew || selectedItem == -1)
                    controlPanel.addRecordToTable(Constants.INCOMES, incomesRecord);
                else {
                    id = ((Incomes) mIncomesList.get(selectedItem)).getId();
                    incomesRecord.setId(id);
                    controlPanel.updateRecordInTable(Constants.INCOMES, incomesRecord);
                }
                break;
        }
        addNew = true;
        selectedItem = -1;
        getTables();
        updateRecordList(table);
    }

    /**
     * Delete a record inside a table
     *
     * @param table target table
     */
    private void deleteRecord(int table) {
        switch (table) {
            case Constants.LIVING_EXPENSES:
                controlPanel.deleteRecordInTable(Constants.LIVING_EXPENSES,
                        mLivingExpensesList.get(selectedItem));
                break;
            case Constants.FIXED_EXPENSES:
                controlPanel.deleteRecordInTable(Constants.FIXED_EXPENSES,
                        mFixedExpensesList.get(selectedItem));
                break;
            case Constants.INCOMES:
                controlPanel.deleteRecordInTable(Constants.INCOMES,
                        mIncomesList.get(selectedItem));
                break;
        }
        getTables();
        updateRecordList(table);
    }
}
