/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: TableManagementFragment.java                                        ////////
 * ////////Class Name: TableManagementFragment                                  ////////
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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.interfaces.OnTableManagementListener;
import com.limitless.smartbudget.ui.table.TableEditFragment;
import com.limitless.smartbudget.utils.Constants;

import java.util.ArrayList;

public class TableManagementFragment extends ListFragment {

    private TableManagementViewModel mViewModel;
    private FragmentManager fragmentManager;
    private TabLayout mTabLayout;

    private OnTableManagementListener mOnTableManagementListener;

    private ArrayAdapter<String> mRecentDataAdapter;

    public TableManagementFragment() {
        //  Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TableManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_management, container, false);
        mTabLayout = view.findViewById(R.id.TMTabLayout);
        fragmentManager = getChildFragmentManager();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 3:/*
                        //  Saving
                        mOnTableManagementListener.onTargetTableChanged(Constants.SAVING);*/
                        break;
                    case 2:
                        //  Incomes
                        mOnTableManagementListener.onTargetTableChanged(Constants.INCOMES);
                        break;
                    case 1:
                        //  Fixed
                        mOnTableManagementListener.onTargetTableChanged(Constants.FIXED_EXPENSES);
                        break;
                    case 0:
                        //  Living
                        mOnTableManagementListener.onTargetTableChanged(Constants.LIVING_EXPENSES);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //  List view
        mRecentDataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>());
        mRecentDataAdapter.setNotifyOnChange(true);
        setListAdapter(mRecentDataAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TableEditFragment fragment = new TableEditFragment(getListView(), getContext());
        fragmentManager.beginTransaction()
                .replace(R.id.TableManagementFragmentLayout
                        , fragment)
                .commit();
        mOnTableManagementListener = fragment;
    }
}
