/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////File Name: OldRecordArrayAdapter.java                                        ////////
 * ////////Class Name: OldRecordArrayAdapter                                  ////////
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
import android.widget.Filter;
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
import java.util.Collections;
import java.util.Currency;

public class OldRecordArrayAdapter extends ArrayAdapter<Object> {

    //  TAG for debug purpose
    private static final String TAG = OldRecordArrayAdapter.class.getSimpleName();

    //  State fields
    private ArrayList<Object> mData;
    private ArrayList<Object> mOrgData;
    private Context mContext;
    private final Currency mCurrency;

    //  View Holder
    private static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
    }

    public OldRecordArrayAdapter(@NonNull Context context, @NonNull ArrayList<Object> data) {
        super(context, R.layout.old_records_list_item, data);

        mContext = context;
        //  Get sorted array
        mData = data;
        mOrgData = mData;

        //  Get currency
        mCurrency = Currency.getInstance(context.
                getSharedPreferences(Constants.SETTING_PREF_FILE, Context.MODE_PRIVATE)
                .getString(CurrencyPickerFragment.PREF_KEY_CURRENCY, "USD"));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //  Get the current object
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
            convertView = inflater.inflate(R.layout.old_records_list_item, parent
                    , false);
            //  Initialize view holder fields
            viewHolder.text1 = convertView.findViewById(R.id.oldRecordText1);
            viewHolder.text2 = convertView.findViewById(R.id.oldRecordText2);
            viewHolder.text3 = convertView.findViewById(R.id.oldRecordText3);
            viewHolder.text4 = convertView.findViewById(R.id.oldRecordText4);

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
            //  Date
            viewHolder.text1.setText(DateFormat.format(Constants.DATE_PATTERN
                    , livingExpenses.getDate()));
            //  Category
            viewHolder.text2.setText(livingExpenses.getCategory().getName());
            viewHolder.text2.setTextColor(livingExpenses.getCategory().getColor());
            //  Value
            viewHolder.text3.setText(livingExpenses.getValue().concat(" " + mCurrency.getSymbol()));
            //  Description
            String des = livingExpenses.getDescription();
            if (des == null || des.isEmpty())
                viewHolder.text4.setText(mContext.getString(R.string.no_description));
            else
                viewHolder.text4.setText(livingExpenses.getDescription());
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
            viewHolder.text4.setVisibility(View.GONE);
        }

        //  Return the final result
        return result;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String filterString = constraint.toString().toLowerCase();

                final ArrayList<Object> nList = new ArrayList<>();
                for (Object o : mOrgData) {
                    if (o.toString().toLowerCase().contains(filterString))
                        nList.add(o);
                }
                results.values = nList;
                results.count = nList.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData = (ArrayList<Object>) results.values;
            }
        };
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        sortingDataByDate(mData);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Custom Methods
    ///////////////////////////////////////////////////////////////////////////

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

    private void sortingDataByDate(ArrayList<Object> data) {
        Collections.sort(data, (o1, o2) -> {
            String d1 = "";
            String d2 = "";
            if (o1 instanceof LivingExpenses) {
                d1 = (String) DateFormat.format(Constants.DATE_PATTERN, ((LivingExpenses) o1).getDate());
                d2 = (String) DateFormat.format(Constants.DATE_PATTERN, ((LivingExpenses) o2).getDate());
            } else if (o1 instanceof FixedExpenses) {
                d1 = (String) DateFormat.format(Constants.DATE_PATTERN, ((FixedExpenses) o1).getDate());
                d2 = (String) DateFormat.format(Constants.DATE_PATTERN, ((FixedExpenses) o2).getDate());
            } else if (o1 instanceof Incomes) {
                d1 = (String) DateFormat.format(Constants.DATE_PATTERN, ((Incomes) o1).getDate());
                d2 = (String) DateFormat.format(Constants.DATE_PATTERN, ((Incomes) o2).getDate());
            }
            return d1.compareTo(d2) * -1;
        });
    }

}
