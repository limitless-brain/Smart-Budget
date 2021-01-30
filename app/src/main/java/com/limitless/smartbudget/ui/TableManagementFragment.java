/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: TableManagementFragment.java                                        ////////
 * ////////Class Name: TableManagementFragment                                  ////////
 * ////////Project Name: $file.projectName                           ////////
 * ////////Copyright update: 1/30/21 7:57 AM                                       ////////
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.google.android.material.tabs.TabLayout;
import com.limitless.smartbudget.MainActivity;
import com.limitless.smartbudget.R;
import com.limitless.smartbudget.interfaces.OnTableManagementListener;
import com.limitless.smartbudget.ui.table.TableEditFragment;
import com.limitless.smartbudget.utils.Constants;

public class TableManagementFragment extends ListFragment {

    private FragmentManager fragmentManager;
    private TabLayout mTabLayout;
    private TextView mListTitle;

    private OnTableManagementListener mOnTableManagementListener;

    public TableManagementFragment() {
        //  Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_management, container, false);
        mTabLayout = view.findViewById(R.id.TMTabLayout);
        mListTitle = view.findViewById(R.id.listTitle);
        fragmentManager = getChildFragmentManager();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 2:
                        //  Incomes
                        mOnTableManagementListener.onTargetTableChanged(Constants.INCOMES);
                        mListTitle.setText(Constants.getCurrentMonth() + "'s Records");
                        break;
                    case 1:
                        //  Fixed
                        mOnTableManagementListener.onTargetTableChanged(Constants.FIXED_EXPENSES);
                        mListTitle.setText(Constants.getCurrentMonth() + "'s Records");
                        break;
                    case 0:
                        //  Living
                        mOnTableManagementListener.onTargetTableChanged(Constants.LIVING_EXPENSES);
                        mListTitle.setText("Today's Records");
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TableEditFragment fragment = new TableEditFragment(getListView(), getContext());
        ((MainActivity) requireActivity()).setAssistantDataListener(fragment);
        fragmentManager.beginTransaction()
                .replace(R.id.TableManagementFragmentLayout
                        , fragment)
                .commit();
        mOnTableManagementListener = fragment;
    }
}
