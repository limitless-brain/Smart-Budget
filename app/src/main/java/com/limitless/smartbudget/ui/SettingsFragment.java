/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: SettingsFragment.java                                        ////////
 * ////////Class Name: SettingsFragment                                  ////////
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

package com.limitless.smartbudget.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private static final int SAVING = 1;
    private static final int LIVING = 0;
    private static final int OPERATION_ADD = 2;
    private static final int OPERATION_UPDATE = 3;
    private static final int OPERATION_REMOVE = 4;
    private static final String[] OPERATIONS = new String[]{
            "Add", "Update", "Remove"
    };

    //  State Variables
    private Context mContext;
    private ArrayAdapter<String> mCategories;
    private List mLivingCategories;
    private List mSavingCategories;
    private int mSelectedTable = LIVING;
    private String mCategoryName;

    //  UI elements
    private Button mManageButton;
    private LinearLayout mManageLayout;
    private TabLayout mManageTabLayout;
    private ListView mListView;
    private Button mAddButton;
    private Button mConfirmButton;


    public SettingsFragment() {
        //  Required empty constructor
    }

    public SettingsFragment(Context context) {
        mContext = context;
        mLivingCategories = new ArrayList();
        //  get tables data
        getTablesData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //  Inflating layout from xml
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initUi(view);
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // UI initialize
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Initialize ui element
     *
     * @param root reference to the root container
     */
    private void initUi(View root) {
        mManageButton = root.findViewById(R.id.settingManageCategoriesManage);
        mManageLayout = root.findViewById(R.id.settingManageCategoriesLayout);
        mManageTabLayout = root.findViewById(R.id.settingManageCategoriesTabLayout);
        mConfirmButton = root.findViewById(R.id.settingManageCategoriesConfirm);
        mListView = root.findViewById(R.id.settingManageCategoriesList);

        //  Handling list view here
        ArrayList<String> data = new ArrayList<>();
        mCategories = new ArrayAdapter<String>(
                mContext, R.layout.fragment_setting_category_list_item, data) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    convertView = inflater.inflate(R.layout.fragment_setting_category_list_item,
                            mListView, false);
                }
                TextView textView = convertView.findViewById(R.id.categoryName);
                ImageButton imageButton = convertView.findViewById(R.id.settingManageCategoriesRemove);
                textView.setText(getItem(position));
                imageButton.setTag(position);
                imageButton.setOnClickListener(view12 -> promptOperation((int) view12.getTag(), OPERATION_REMOVE));

                return convertView;
            }
        };
        View view1 = getLayoutInflater().inflate(
                R.layout.fragment_setting_category_list_footer, mListView, false);
        mAddButton = view1.findViewById(R.id.settingManageCategoriesAdd);
        mAddButton.setOnClickListener(view2 -> promptOperation(mCategories.getCount() > -1
                ? mCategories.getCount() : 0, OPERATION_ADD));
        mListView.addFooterView(view1);
        mListView.setAdapter(mCategories);
        mListView.setOnItemClickListener((adapterView, view22, i, l) -> promptOperation(i, OPERATION_UPDATE));
        setUiListeners();
        toggleManageUi(false);
    }

    /**
     * Set listener for user interact
     */
    private void setUiListeners() {
        //  Set tabLayout listener
        mManageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedTable = tab.getPosition();
                getTablesData();
                fillListWithData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //  Set Manage Button listener
        mManageButton.setOnClickListener(view -> {
            toggleManageUi(true);
            getTablesData();
            fillListWithData();
        });
        //  Set Confirm Button
        mConfirmButton.setOnClickListener(view -> toggleManageUi(false));
    }

    /**
     * Fill list with target table data
     */
    private void fillListWithData() {
        mCategories.clear();
        switch (mSelectedTable) {
            case LIVING:
                for (Object o : mLivingCategories) {
                    mCategories.add(((Category) o).getName());
                }
                break;
            case SAVING:
                break;
        }
        mCategories.notifyDataSetChanged();
    }

    /**
     * Show/Hide management ui
     *
     * @param visibility show/hide
     */
    private void toggleManageUi(boolean visibility) {
        mManageTabLayout.setVisibility(!visibility ? View.GONE : View.VISIBLE);
        mManageLayout.setVisibility(!visibility ? View.GONE : View.VISIBLE);
        mManageButton.setVisibility(!visibility ? View.VISIBLE : View.GONE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Categories management
    ///////////////////////////////////////////////////////////////////////////

    private void getTablesData() {
        mLivingCategories.clear();
        mLivingCategories = (List) ControlPanel
                .getInstance(mContext).getAllRecords(Constants.CATEGORIES);
        // TODO: 9/27/2019 get saving categories
    }

    private void preformOperation(int pos, int operation) {
        Category category;
        switch (operation) {
            case OPERATION_ADD:
                if (mSelectedTable == LIVING) {
                    mCategories.insert(mCategoryName, mCategories.getCount());
                    category = new Category(mCategoryName, 0);
                    ControlPanel.getInstance(mContext)
                            .addRecordToTable(Constants.CATEGORIES, category);
                }
                break;
            case OPERATION_UPDATE:
                if (mSelectedTable == LIVING) {
                    if (mCategoryName.isEmpty())
                        return;
                    mCategories.remove(mCategories.getItem(pos));
                    mCategories.insert(mCategoryName, pos);
                    category = ((Category) mLivingCategories.get(pos));
                    category.setName(mCategoryName);
                    ControlPanel.getInstance(mContext).
                            updateRecordInTable(Constants.CATEGORIES, category);
                }
                break;
            case OPERATION_REMOVE:
                if (mSelectedTable == SAVING) {
                    if (mCategories.getCount() < 2) {
                        Toast.makeText(mContext, "At least you must have one category!"
                                , Toast.LENGTH_LONG).show();
                        return;
                    }
                    mCategories.remove(mCategories.getItem(pos));
                    ControlPanel.getInstance(mContext).
                            deleteRecordInTable(Constants.CATEGORIES
                                    , mLivingCategories.get(pos));
                    mLivingCategories.remove(pos);
                }
                break;
        }
        Toast.makeText(mContext, "Operation "
                + OPERATIONS[operation - 2] + " done", Toast.LENGTH_LONG).show();
        getTablesData();
        fillListWithData();
        mListView.setSelection(mCategories.getCount());
    }

    private void promptOperation(int pos, int operation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText cate = new EditText(mContext);
        builder.setTitle(operation == OPERATION_ADD ?
                "Add Category" : operation == OPERATION_UPDATE ?
                "Modify Category" : "Delete Category");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (operation != OPERATION_REMOVE)
                mCategoryName = cate.getText().toString();
            preformOperation(pos, operation);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            //  Noting to do
            Toast.makeText(mContext, "Operation canceled!", Toast.LENGTH_LONG).show();
        });
        if (operation != OPERATION_REMOVE)
            builder.setView(cate);
        if (operation == OPERATION_UPDATE)
            cate.setText(mCategories.getItem(pos));
        if (operation == OPERATION_REMOVE)
            builder.setMessage("Are you sure you want to delete this category");
        builder.show();
    }
}
