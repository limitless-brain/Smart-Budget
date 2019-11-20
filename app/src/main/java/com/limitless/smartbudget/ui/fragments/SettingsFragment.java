/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: SettingsFragment.java                                        ////////
 * ////////Class Name: SettingsFragment                                  ////////
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.limitless.smartbudget.ControlPanel;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;
import com.limitless.smartbudget.firebase.FirebaseManager;
import com.limitless.smartbudget.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;

import static android.app.Activity.RESULT_OK;

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
    private int mSelectedColor = Color.BLACK;
    private String mCategoryName;

    //  UI elements
    private Button mManageButton;
    private LinearLayout mManageLayout;
    private TabLayout mManageTabLayout;
    private ListView mListView;
    private Button mConfirmButton;
    //  User ui
    private Button mSignAccount;
    private Button mDeleteAccount;
    private ImageView mProfileImageView;
    private TextView mProfileTextView;


    public SettingsFragment() {
        //  Required empty constructor
    }

    ///////////////////////////////////////////////////////////////////////////
    // Fragment Overridden methods
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Initialize state fields
        mContext = getContext();
        mLivingCategories = new ArrayList();
        //  Get tables data
        getTablesData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //  Inflating layout from xml
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FirebaseManager.getInstance().addAuthStateListener(firebaseAuth -> {
            updateAccountUi();
        });
        initUi(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.FIREBASE_REQUEST_CODE) {
            //  it's sign in request
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //  User sign-in
            } else {
                if (response == null) {
                    //  User press back button
                    Snackbar.make(Objects.requireNonNull(mSignAccount), "Sign-in canceled!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        }
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
        //  Table management
        mManageButton = root.findViewById(R.id.settingManageCategoriesManage);
        mManageLayout = root.findViewById(R.id.settingManageCategoriesLayout);
        mManageTabLayout = root.findViewById(R.id.settingManageCategoriesTabLayout);
        mConfirmButton = root.findViewById(R.id.settingManageCategoriesConfirm);
        mListView = root.findViewById(R.id.settingManageCategoriesList);

        //  Handling list view here
        // TODO: 9/28/2019 Organize this block
        {
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
                    textView.setTextColor(((Category) mLivingCategories.get(position)).getColor());
                    imageButton.setTag(position);
                    imageButton.setOnClickListener(o -> promptOperation((int) o.getTag(), OPERATION_REMOVE));

                    return convertView;
                }
            };
        }

        View view1 = getLayoutInflater().inflate(
                R.layout.fragment_setting_category_list_footer, mListView, false);
        Button mAddButton = view1.findViewById(R.id.settingManageCategoriesAdd);
        mAddButton.setOnClickListener(view2 -> promptOperation(mCategories.getCount() > -1
                ? mCategories.getCount() : 0, OPERATION_ADD));
        mListView.addFooterView(view1);
        mListView.setAdapter(mCategories);
        mListView.setOnItemClickListener((adapterView, view22, i, l) -> promptOperation(i, OPERATION_UPDATE));

        //  Account setting
        mSignAccount = root.findViewById(R.id.settingAccountSign);
        mDeleteAccount = root.findViewById(R.id.settingAccountDelete);
        mProfileImageView = root.findViewById(R.id.settingAccountProfilePic);
        mProfileTextView = root.findViewById(R.id.settingAccountProfileName);

        setUiListeners();
        toggleManageUi(false);
        updateAccountUi();
    }

    /**
     * Set listener for user interact
     */
    private void setUiListeners() {

        //  Table Setting
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

        //  Account Settings
        mSignAccount.setOnClickListener(view -> {
            if (FirebaseManager.getInstance().isSignedIn() &&
                    FirebaseManager.getInstance().getCurrentUser() != null) {
                //  Sign user out
                FirebaseManager.getInstance().signOut();
            } else {
                //  We sign user in
                startActivityForResult(FirebaseManager.getInstance().getSignInIntent(), Constants.FIREBASE_REQUEST_CODE);
            }
        });

        mDeleteAccount.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete Account")
                    .setMessage("Deleting account will delete all records stored online.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            FirebaseManager.getInstance().getCurrentUser().delete().addOnCompleteListener(task ->
                            {
                                Toast.makeText(mContext, "Account deleted.", Toast.LENGTH_LONG).show();
                            });
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
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
        mListView.setStackFromBottom(true);
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
                    /*if (mCategoryName.isEmpty() || !mCategoryName.
                            matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$"))
                        return;*/
                    mCategories.insert(mCategoryName, mCategories.getCount());
                    category = new Category(mCategoryName, mSelectedColor);
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
                    category.setColor(mSelectedColor);
                    ControlPanel.getInstance(mContext).
                            updateRecordInTable(Constants.CATEGORIES, category);
                }
                break;
            case OPERATION_REMOVE:
                if (mSelectedTable == LIVING) {
                    if (mCategories.getCount() < 2) {
                        Toast.makeText(mContext, "At least you must have one category!"
                                , Toast.LENGTH_LONG).show();
                        return;
                    }
                    ControlPanel.getInstance(mContext).
                            deleteRecordInTable(Constants.CATEGORIES
                                    , mLivingCategories.get(pos));
                    mLivingCategories.remove(pos);
                    mCategories.remove(mCategories.getItem(pos));
                }
                break;
        }
        Toast.makeText(mContext, "Operation "
                + OPERATIONS[operation - 2] + " done", Toast.LENGTH_LONG).show();
        getTablesData();
        fillListWithData();
    }

    private void promptOperation(int pos, int operation) {
        Log.e(getClass().getSimpleName(), String.valueOf(pos));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText cate = new EditText(mContext);
        builder.setTitle(operation == OPERATION_ADD ?
                "Add Category" : operation == OPERATION_UPDATE ?
                "Modify Category" : "Delete Category");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (operation != OPERATION_REMOVE)
                mCategoryName = cate.getText().toString();
            promptColorSelection(pos, operation);
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

    /**
     * Dialog to get color from the user
     */
    private void promptColorSelection(int pos, int operation) {

        if (operation == OPERATION_REMOVE) {
            preformOperation(pos, operation);
            return;
        }

        int[] colorList = Constants.getColorList();
        ColorPicker colorPicker = new ColorPicker(Objects.requireNonNull(getActivity()));
        colorPicker.setColors(colorList);
        colorPicker.setColumns(5);
        colorPicker.setRoundColorButton(true);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                mSelectedColor = color;
                preformOperation(pos, operation);
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "Color Cancel, Default color has been chosen!"
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Firebase user update ui
    ///////////////////////////////////////////////////////////////////////////
    private void updateAccountUi() {
        if (FirebaseManager.getInstance().getCurrentUser() != null &&
                FirebaseManager.getInstance().isSignedIn()) {
            mSignAccount.setText("Sign Out");
            mDeleteAccount.setVisibility(View.VISIBLE);
            mProfileImageView.setVisibility(View.VISIBLE);
            Uri profileUri = FirebaseManager.getInstance().getCurrentUser().getPhotoUrl();
            if (profileUri.toString().contains("facebook")) {
                String base = profileUri.toString();
                String highRes = "?type=large&redirect=true&width=600&height=600";
                if (base.endsWith("e")) {
                    profileUri = Uri.parse(base + highRes);
                }
            }
            Picasso.get()
                    .load(profileUri)
                    .into(mProfileImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (isVisible()) {
                                Constants.roundPicture(mProfileImageView, mContext);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            mProfileImageView.setImageResource(R.mipmap.ic_launcher_foreground);
                        }
                    });
            mProfileTextView.setVisibility(View.VISIBLE);
            mProfileTextView.setText(FirebaseManager.getInstance().getCurrentUser().getDisplayName());
        } else {
            mSignAccount.setText("Sign In");
            mDeleteAccount.setVisibility(View.GONE);
            mProfileImageView.setVisibility(View.GONE);
            mProfileTextView.setVisibility(View.GONE);
        }
    }
}
