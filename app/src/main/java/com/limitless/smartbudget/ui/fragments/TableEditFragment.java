/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: TableEditFragment.java                                        ////////
 * ////////Class Name: TableEditFragment                                  ////////
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

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.assistant.AssistantDataInterface;
import com.limitless.smartbudget.assistant.RecordModel;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.db.model.FixedExpenses;
import com.limitless.smartbudget.db.model.Incomes;
import com.limitless.smartbudget.db.model.LivingExpenses;
import com.limitless.smartbudget.interfaces.OnTableManagementListener;
import com.limitless.smartbudget.ui.arrayadapters.LatestRecordArrayAdapter;
import com.limitless.smartbudget.ui.arrayadapters.OldRecordArrayAdapter;
import com.limitless.smartbudget.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
 * Date change listener : {@linkplain TableEditFragment#onDateSelected(int, int, int, int)}
 * Table tab changed listener : {@linkplain TableEditFragment#onTargetTableChanged(int)}
 * Initialize User Interface : {@linkplain TableEditFragment#initUi(View)}
 * Show/Hide UI elements : {@linkplain TableEditFragment#handleUIVisibility(int)} )}
 * Show all UI elements : {@linkplain TableEditFragment#setAllUIVisible()}
 * Reset UI data : {@linkplain TableEditFragment#resetUI(int)}
 * Setup date picker : {@linkplain TableEditFragment#setupDatePicker(int, int, int)}
 * Set UI listeners : {@linkplain TableEditFragment#setUiListeners()}
 * Enable/disable Confirm button : {@linkplain TableEditFragment#checkRecordAvailability()}
 * Change UI on Click update record : {@linkplain TableEditFragment#updateUiForUpdateRecord(int, Object)}
 * Update record list : {@linkplain TableEditFragment#updateRecordList(int)}
 * Call update table methods : {@linkplain TableEditFragment#updateTargetTable(int)}
 * Get tables data from database : {@linkplain TableEditFragment#getTables()}
 * Add/Update data from database : {@linkplain TableEditFragment#interactWithTable(int, boolean)}
 * Delete data from database : {@linkplain TableEditFragment#deleteRecord(int, Object)}
 * Get selected category : {@linkplain TableEditFragment#getCategoryObject(String)}
 * Fill cloud chip view : {@linkplain TableEditFragment#fillCategoryChips()}
 */
public class TableEditFragment extends Fragment implements OnTableManagementListener,
        DatePickerTimeline.OnDateSelectedListener, AssistantDataInterface, SearchView.OnQueryTextListener {

    //  State Variables
    private Context mContext;
    private int mTable;
    private int selectedItem;
    private boolean addNew = true;
    private String mDateString;
    private String selectedCategory;
    private LatestRecordArrayAdapter mLatestRecordArrayAdapter;
    private OldRecordArrayAdapter mOldRecordArrayAdapter;

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
    private TextView mManageOldRecords;
    private Button mOldRecordDone;
    private BottomSheetDialog mOldRecordsBottomSheet;

    //  Table lists
    private List mFixedExpensesList;
    private List mLivingExpensesList;
    private List mIncomesList;
    //  private List mSavingList;
    private List mCategoryList;

    //  Database
    private ControlPanel controlPanel;
    private ChipListener mChipListener;
    private AdapterView.OnItemLongClickListener onListItemLongClickListener;
    private int mRecordID;

    public TableEditFragment() {
        // Required empty public constructor
    }

    TableEditFragment(ListView listView, Context context) {
        mContext = context;
        mListView = listView;

        controlPanel = ControlPanel.getInstance(mContext);
        getTables();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLatestRecordArrayAdapter = new LatestRecordArrayAdapter(new ArrayList<>()
                , mContext);
        mOldRecordArrayAdapter = new OldRecordArrayAdapter(mContext
                , new ArrayList<>());
        mListView.setAdapter(mLatestRecordArrayAdapter);
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

        //  Old records
        assert getParentFragment() != null;
        mManageOldRecords = Objects.requireNonNull(getParentFragment()
                .getView())
                .findViewById(R.id.tableManagementOldRecordManageBtn);
        mOldRecordsBottomSheet = new BottomSheetDialog(mContext);
        mOldRecordsBottomSheet.setContentView(R.layout.old_records_list);
        SearchView searchView = mOldRecordsBottomSheet.findViewById(R.id.manageOldRecordSearch);
        if (searchView != null)
            searchView.setOnQueryTextListener(this);
        mOldRecordDone = mOldRecordsBottomSheet.findViewById(R.id.manageOldRecordDoneBtn);

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
        selectedItem = -1;
        selectedCategory = "";
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
            fillCategoryChips();
        }
        //  Handling UI listeners
        setUiListeners();

        //  Check if record filled
        checkRecordAvailability();
    }

    /**
     * Fill Cloud chip view with category
     */
    private void fillCategoryChips() {
        mCategoryChipCloud.removeAllViews();
        if (mCategoryList != null) {
            for (int i = 0; i < mCategoryList.size(); i++) {
                mCategoryChipCloud.addChip(((Category) mCategoryList.get(i)).getName());
                TextView child = ((TextView) mCategoryChipCloud.getChildAt(i));
                int color = ((Category) mCategoryList.get(i)).getColor();
                child.setTextColor(color);
                GradientDrawable drawable = (GradientDrawable) getResources()
                        .getDrawable(R.drawable.rounded_stroke_chip, null);
                drawable.setStroke(4, color);
                child.setBackground(drawable);
                child.setPadding(16, 0, 16, 0);
            }
        }
    }

    private void showOneCategoryChip(String category) {
        Category categoryObject = getCategoryObject(category);
        mCategoryChipCloud.removeAllViews();
        mCategoryChipCloud.addChip(selectedCategory);
        mCategoryChipCloud.setChipListener(null);
        mCategoryChipCloud.setSelectedChip(0);
        mCategoryChipCloud.setChipListener(mChipListener);
        TextView child = ((TextView) mCategoryChipCloud.getChildAt(0));
        int color = Objects.requireNonNull(categoryObject).getColor();
        child.setTextColor(color);
        GradientDrawable drawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.rounded_stroke_chip, null);
        drawable.setStroke(8, color);
        child.setBackground(drawable);

        child.setPadding(16, 0, 16, 0);
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
        mDatePickerTimeline.setDateLabelAdapter((calendar, index) -> DateFormat.format("MMM", calendar.getTime()));
        mDatePickerTimeline.setSelectedDate(year, month, day);
        mDatePickerTimeline.postDelayed(() -> mDatePickerTimeline.setVisibility(View.GONE), 50);
    }

    /**
     * Set ui interface listener
     */
    @SuppressWarnings("ClickableViewAccessibility")
    private void setUiListeners() {

        //  Categories chip list
        mChipListener = new ChipListener() {
            @Override
            public void chipSelected(int index) {
                selectedCategory = ((Category) mCategoryList.get(index)).getName();
                mValueEditText.requestFocus();
                showOneCategoryChip(selectedCategory);
                checkRecordAvailability();
            }

            @Override
            public void chipDeselected(int index) {
                selectedCategory = null;
                if (mCategoryChipCloud.getChildCount() < 2)
                    fillCategoryChips();
                checkRecordAvailability();
            }
        };
        mCategoryChipCloud.setChipListener(mChipListener);

        //  Calender for handling date
        Calendar calendar = Calendar.getInstance();
        //  Date edit text touch listener
        mDateEditText.setOnTouchListener((view, motionEvent) -> {
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
                mRecordID = -1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        //  Long press list item listener
        onListItemLongClickListener = (adapterView, view, i, l) -> {
            selectedItem = i;
            PopupMenu menu = new PopupMenu(mContext, view);
            menu.inflate(R.menu.list_item_menu);
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        addNew = true;
                        deleteRecord(mTable, mLatestRecordArrayAdapter.getItem(i));
                        break;
                    case R.id.update:
                        addNew = false;
                        mRecordID = mLatestRecordArrayAdapter.getRecordId(i);
                        updateUiForUpdateRecord(mTable, mLatestRecordArrayAdapter.getItem(i));
                        break;
                }
                return false;
            });
            return false;
        };
        mListView.setOnItemLongClickListener(onListItemLongClickListener);

        //  Old records
        mManageOldRecords.setOnClickListener(v -> {
            mOldRecordsBottomSheet.show();
            ListView listView = mOldRecordsBottomSheet.findViewById(R.id.manageOldRecordList);
            if (listView != null) {
                listView.setAdapter(mOldRecordArrayAdapter);
                listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
                    selectedItem = i;
                    PopupMenu menu = new PopupMenu(mContext, view);
                    menu.inflate(R.menu.list_item_menu);
                    menu.show();
                    menu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                addNew = true;
                                deleteRecord(mTable, mOldRecordArrayAdapter.getItem(i));
                                break;
                            case R.id.update:
                                addNew = false;
                                mRecordID = mOldRecordArrayAdapter.getRecordId(i);
                                updateUiForUpdateRecord(mTable, mOldRecordArrayAdapter.getItem(i));
                                mOldRecordsBottomSheet.dismiss();
                                break;
                        }
                        return false;
                    });
                    return false;
                });
            }
        });
        mOldRecordDone.setOnClickListener(v -> mOldRecordsBottomSheet.dismiss());
    }

    /**
     * Check if record legal and enable confirm button
     */
    private void checkRecordAvailability() {
        boolean isEnabled = false;
        switch (mTable) {
            case Constants.LIVING_EXPENSES:
                isEnabled = selectedCategory != null && !selectedCategory.isEmpty() &&
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
     * @param table target table
     */
    private void updateUiForUpdateRecord(int table, Object record) {
        if (mDatePickerTimeline.getVisibility() == View.VISIBLE)
            mDatePickerTimeline.setVisibility(View.GONE);
        if (mDateEditText.getVisibility() == View.GONE)
            mDateEditText.setVisibility(View.VISIBLE);
        switch (table) {
            case Constants.LIVING_EXPENSES:
                LivingExpenses livingExpenses = (LivingExpenses) record;
                String date = (String) DateFormat.format(Constants.DATE_PATTERN
                        , livingExpenses.getDate());
                mDateString = date;
                mDateEditText.setText(date);
                mValueEditText.setText(livingExpenses.getValue());
                selectedCategory = livingExpenses.getCategory().getName();
                showOneCategoryChip(selectedCategory);
                mDescriptionEditText.setText(livingExpenses.getDescription());
                break;
            case Constants.FIXED_EXPENSES:
                FixedExpenses fixedExpenses = (FixedExpenses) record;
                String date1 = (String) DateFormat.format(Constants.DATE_PATTERN
                        , fixedExpenses.getDate());
                mDateString = date1;
                mDateEditText.setText(date1);
                mValueEditText.setText(fixedExpenses.getValue());
                mDescriptionEditText.setText(fixedExpenses.getDescription());
                break;
            case Constants.INCOMES:
                Incomes incomes = (Incomes) record;
                String date2 = (String) DateFormat.format(Constants.DATE_PATTERN
                        , incomes.getDate());
                mDateString = date2;
                mDateEditText.setText(date2);
                mValueEditText.setText(incomes.getValue());
                mDescriptionEditText.setText(incomes.getDescription());
                break;
        }
    }

    /**
     * Get selected category object
     *
     * @return Category instance
     */
    @Nullable
    private Category getCategoryObject(String categoryName) {
        for (Object o : mCategoryList) {
            if (((Category) o).getName().equals(categoryName))
                return (Category) o;
        }
        return null;
    }

    /**
     * update listView inside TableManagement fragment to target table
     *
     * @param table target table
     */
    private void updateRecordList(int table) {
        mLatestRecordArrayAdapter.clear();
        mOldRecordArrayAdapter.clear();
        Observable.fromIterable(getLatestRecordList(table))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mLatestRecordArrayAdapter.add(o);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private ArrayList<Object> getLatestRecordList(int table) {
        ArrayList<Object> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String today = Constants.dateToString(calendar.getTime(), Constants.DATE_PATTERN, Locale.US);
        String month;
        String recordDate;
        switch (table) {
            case Constants.LIVING_EXPENSES:
                for (Object o : mLivingExpensesList) {
                    LivingExpenses livingExpenses = (LivingExpenses) o;
                    recordDate = Constants.dateToString(livingExpenses.getDate()
                            , Constants.DATE_PATTERN, Locale.US);
                    if (today.equals(recordDate))
                        data.add(livingExpenses);
                    else {
                        //  Add it to old record
                        mOldRecordArrayAdapter.add(livingExpenses);
                    }

                }
                break;
            case Constants.FIXED_EXPENSES:
                for (Object o : mFixedExpensesList) {
                    FixedExpenses fixedExpenses = (FixedExpenses) o;
                    recordDate = (String) DateFormat.format("MM", fixedExpenses.getDate());
                    month = (String) DateFormat.format("MM", calendar.getTime());
                    if (recordDate.equals(month)) {
                        data.add(fixedExpenses);
                    } else {
                        //  Add to old records
                        mOldRecordArrayAdapter.add(fixedExpenses);
                    }
                }
                break;
            case Constants.INCOMES:
                for (Object o : mIncomesList) {
                    Incomes incomes = (Incomes) o;
                    recordDate = (String) DateFormat.format("MM", incomes.getDate());
                    month = (String) DateFormat.format("MM", calendar.getTime());
                    if (recordDate.equals(month)) {
                        data.add(incomes);
                    } else {
                        //  Add to old records
                        mOldRecordArrayAdapter.add(incomes);
                    }
                }
                break;
        }
        mOldRecordArrayAdapter.notifyDataSetChanged();
        return data;
    }

    /**
     * Call table and ui updating methods
     *
     * @param table target table
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
        Observable<Object> databaseObservable = Observable
                .fromIterable(getAllRecords())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all records in the background process
     *
     * @return List contain tables
     */
    private List<Object> getAllRecords() {
        List<Object> list = new ArrayList<>();
        mLivingExpensesList = (List) ControlPanel.getInstance(mContext)
                .getAllRecords(Constants.LIVING_EXPENSES);
        mFixedExpensesList = (List) ControlPanel.getInstance(mContext)
                .getAllRecords(Constants.FIXED_EXPENSES);
        mIncomesList = (List) ControlPanel.getInstance(mContext)
                .getAllRecords(Constants.INCOMES);
        mCategoryList = (List) ControlPanel
                .getInstance(mContext).getAllRecords(Constants.CATEGORIES);
        list.add(mLivingExpensesList);
        list.add(mFixedExpensesList);
        list.add(mIncomesList);
        list.add(mCategoryList);
        return list;
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

                if (addNew || selectedItem == -1) {
                    controlPanel.addRecordToTable(Constants.LIVING_EXPENSES, livingExpensesRecord);
                } else {
                    livingExpensesRecord.setId(mRecordID);
                    controlPanel.updateRecordInTable(Constants.LIVING_EXPENSES, livingExpensesRecord);
                }
                break;
            case Constants.FIXED_EXPENSES:
                FixedExpenses fixedExpensesRecord = new FixedExpenses(
                        Constants.dateFromString(date, Constants.DATE_PATTERN, Locale.US),
                        value, description);
                if (addNew || selectedItem == -1) {
                    controlPanel.addRecordToTable(Constants.FIXED_EXPENSES, fixedExpensesRecord);
                } else {
                    fixedExpensesRecord.setId(mRecordID);
                    controlPanel.updateRecordInTable(Constants.FIXED_EXPENSES, fixedExpensesRecord);
                }
                break;
            case Constants.INCOMES:
                Incomes incomesRecord = new Incomes(
                        Constants.dateFromString(date, Constants.DATE_PATTERN, Locale.US),
                        value, description);
                if (addNew || selectedItem == -1) {
                    controlPanel.addRecordToTable(Constants.INCOMES, incomesRecord);
                } else {
                    incomesRecord.setId(mRecordID);
                    controlPanel.updateRecordInTable(Constants.INCOMES, incomesRecord);
                }
                break;
        }
        selectedItem = -1;
        getTables();
        updateRecordList(table);
        resetUI(mTable);
    }

    /**
     * Delete a record inside a table
     *
     * @param table target table
     */
    private void deleteRecord(int table, Object record) {
        switch (table) {
            case Constants.LIVING_EXPENSES:
                controlPanel.deleteRecordInTable(Constants.LIVING_EXPENSES,
                        record);
                break;
            case Constants.FIXED_EXPENSES:
                controlPanel.deleteRecordInTable(Constants.FIXED_EXPENSES,
                        record);
                break;
            case Constants.INCOMES:
                controlPanel.deleteRecordInTable(Constants.INCOMES,
                        record);
                break;
        }
        getTables();
        updateRecordList(table);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Handling assistant data
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onReceiveRecord(RecordModel record) {
        //  Handling the record
        if (!record.hasOperation()) {
            Toast.makeText(mContext, "No Operation detected, Try again",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String value = record.getValue();
        String description = record.getDescription();
        Date date = Calendar.getInstance().getTime();
        mDateString = Constants.dateToString(date, Constants.DATE_PATTERN, Locale.US);
        if (record.hasValue())
            mValueEditText.setText(value);
        if (record.hasDescription())
            mDescriptionEditText.setText(description);
        if (mTable == Constants.LIVING_EXPENSES) {
            if (!record.hasCategory()) {
                Toast.makeText(mContext, "No Category detected, Try again"
                        , Toast.LENGTH_LONG).show();
                return;
            }
            selectedCategory = record.getCategory().getName();
        }
        try {
            interactWithTable(mTable, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Search Listener Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onQueryTextSubmit(String query) {
        mOldRecordArrayAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mOldRecordArrayAdapter.getFilter().filter(newText);
        return true;
    }
}
