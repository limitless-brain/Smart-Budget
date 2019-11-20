/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: LatestRecordArrayAdapter.java                                        ////////
 * ////////Class Name: LatestRecordArrayAdapter                                  ////////
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limitless.smartbudget.R;
import com.limitless.smartbudget.db.model.FixedExpenses;
import com.limitless.smartbudget.db.model.Incomes;
import com.limitless.smartbudget.db.model.LivingExpenses;
import com.limitless.smartbudget.ui.fragments.Showcase.CurrencyPickerFragment;
import com.limitless.smartbudget.utils.Constants;

import java.util.ArrayList;
import java.util.Currency;

public class LatestRecordArrayAdapter extends ArrayAdapter<Object> {

    //  Tag for debug purpose
    private static final String TAG = LatestRecordArrayAdapter.class.getSimpleName();
    //  State fields
    private ArrayList<Object> mData;
    private final Currency mCurrency;
    private Context mContext;

    //  View Holder
    private static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
    }

    public LatestRecordArrayAdapter(@NonNull ArrayList<Object> data, @NonNull Context context) {
        super(context, R.layout.latest_record_list_item, data);
        mData = data;
        mContext = context;
        String code = context.getSharedPreferences(Constants.SETTING_PREF_FILE, Context.MODE_PRIVATE)
                .getString(CurrencyPickerFragment.PREF_KEY_CURRENCY, "USD");
        mCurrency = Currency.getInstance(code);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //  Get the current record object
        Object o = mData.get(position);
        LivingExpenses livingExpenses = null;
        FixedExpenses fixedExpenses = null;
        Incomes incomes = null;

        //  determine the type of object
        if (o instanceof LivingExpenses)
            livingExpenses = (LivingExpenses) o;
        else if (o instanceof FixedExpenses)
            fixedExpenses = (FixedExpenses) o;
        else if (o instanceof Incomes)
            incomes = (Incomes) o;
        else
            Log.d(TAG, "getView: Unknown object type");

        //  final view to return
        final View result;
        //  View holder to hold list item views
        ViewHolder viewHolder;
        //  check if convert view is null
        if (convertView == null) {
            //  Initialize view holder
            viewHolder = new ViewHolder();
            //  Instantiate layout inflater then inflating the layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.latest_record_list_item, parent
                    , false);
            //  Initialize view holder fields
            viewHolder.text1 = convertView.findViewById(R.id.tableDataItem1);
            viewHolder.text2 = convertView.findViewById(R.id.tableDataItem2);
            viewHolder.text3 = convertView.findViewById(R.id.tableDataItem3);

            //  Set the final result
            result = convertView;
            //  Tag convertView with viewHolder
            convertView.setTag(viewHolder);
        } else {
            //  We have convertView so we can get our viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
            //  Set the final result
            result = convertView;
        }

        // TODO: 10/29/2019 Animate our list here

        //  Set views data
        if (livingExpenses != null) {
            //  Living expenses
            //  Category
            viewHolder.text1.setText(livingExpenses.getCategory().getName());
            viewHolder.text1.setTextColor(livingExpenses.getCategory().getColor());
            //  Value
            viewHolder.text2.setText(livingExpenses.getValue().concat(" " + mCurrency.getSymbol()));
            //  Description
            String des = livingExpenses.getDescription();
            if (des == null || des.isEmpty())
                viewHolder.text3.setText(mContext.getString(R.string.no_description));
            else
                viewHolder.text3.setText(livingExpenses.getDescription());
        } else if ((fixedExpenses != null) || (incomes != null)) {
            //  FixedExpenses or Incomes
            //  Date
            String date = (String) DateFormat.format(Constants.DATE_PATTERN,
                    fixedExpenses != null ? fixedExpenses.getDate() : incomes.getDate());
            viewHolder.text1.setText(date);
            //  Value
            String value = fixedExpenses != null ? fixedExpenses.getValue() : incomes.getValue()
                    + " " + mCurrency.getSymbol();
            viewHolder.text2.setText(value);
            //  Description
            String des = fixedExpenses != null ? fixedExpenses.getDescription() : incomes.getDescription();
            viewHolder.text3.setText(des);
        }

        //  Return the final result
        return result;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Custom methods
    ///////////////////////////////////////////////////////////////////////////

    public void remove(int position) {
        remove(getItem(position));
    }

    public int getRecordId(int position) {
        Object o = mData.get(position);
        if (o instanceof LivingExpenses)
            return ((LivingExpenses) o).getId();
        else if (o instanceof FixedExpenses)
            return ((FixedExpenses) o).getId();
        else if (o instanceof Incomes)
            return ((Incomes) o).getId();
        else return -1;
    }
}
