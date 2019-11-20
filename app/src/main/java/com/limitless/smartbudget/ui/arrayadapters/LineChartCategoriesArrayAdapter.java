/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: LineChartCategoriesArrayAdapter.java                                        ////////
 * ////////Class Name: LineChartCategoriesArrayAdapter                                  ////////
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

package com.limitless.smartbudget.ui.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.Category;

import java.util.ArrayList;

public class LineChartCategoriesArrayAdapter extends ArrayAdapter<Category> {

    //  State fields
    private Context mContext;
    private ArrayList<Category> mData;
    private ArrayList<Boolean> mDataCheckedState;

    //  ViewHolder
    private static class ViewHolder {
        CheckedTextView mCheckedTextView;
    }

    public LineChartCategoriesArrayAdapter(@NonNull Context context, ArrayList<Category> data) {

        super(context, R.layout.bottomsheet_line_chart_list_item, data);

        mContext = context;
        mData = data;
        mDataCheckedState = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //  Final result
        final View result;

        //  Instance of ViewHolder
        ViewHolder viewHolder;

        //  Check if we first time create the view
        if (convertView == null) {

            //  Initialize viewHolder
            viewHolder = new ViewHolder();

            //  Inflating root view
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.bottomsheet_line_chart_list_item
                    , parent, false);

            //  Initialize viewHolder fields
            viewHolder.mCheckedTextView = convertView
                    .findViewById(R.id.bottomSheetCategoryCheckTextView);

            //  Set the final result
            result = convertView;

            //  Tag convert view
            convertView.setTag(viewHolder);
        } else {
            //  We have convert view
            //  get view holder
            viewHolder = (ViewHolder) convertView.getTag();

            // Set the final result
            result = convertView;
        }

        // TODO: 10/30/2019 animate list items here

        viewHolder.mCheckedTextView.setChecked(isItemChecked(position));
        viewHolder.mCheckedTextView.setText(mData.get(position).getName());
        viewHolder.mCheckedTextView.setTextColor(mData.get(position).getColor());
        viewHolder.mCheckedTextView.setOnClickListener(v -> {
            viewHolder.mCheckedTextView.setChecked(!viewHolder.mCheckedTextView.isChecked());
            setItemChecked(position, viewHolder.mCheckedTextView.isChecked());
        });

        return result;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return mData.get(position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Custom methods
    ///////////////////////////////////////////////////////////////////////////

    private void setItemChecked(int position, boolean state) {
        if (mDataCheckedState.size() > position)
            mDataCheckedState.set(position, state);
        else
            mDataCheckedState.add(state);
        notifyDataSetChanged();
    }

    private boolean isItemChecked(int position) {
        if (mDataCheckedState.size() > position)
            return mDataCheckedState.get(position);
        mDataCheckedState.add(true);
        return true;
    }

    public ArrayList<Category> getShowedCategories() {
        ArrayList<Category> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mDataCheckedState.get(i)) {
                list.add(mData.get(i));
            }
        }
        return list;
    }
}
