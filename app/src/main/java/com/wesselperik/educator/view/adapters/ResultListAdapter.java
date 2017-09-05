package com.wesselperik.educator.view.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wesselperik.educator.R;
import com.wesselperik.educator.model.Result;

import java.util.List;

/**
 * Created by Wessel on 24-5-2017.
 */

public class ResultListAdapter extends ArrayAdapter {

    private Context context;

    public ResultListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Result> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false);
        }

        Result result = (Result) getItem(position);
        TextView name = null;
        TextView grade = null;
        //TextView attempt = null;
        if (result != null) {
            name = (TextView) convertView.findViewById(R.id.result_name);
            grade = (TextView) convertView.findViewById(R.id.result_grade);
            //attempt = (TextView) convertView.findViewById(R.id.result_attempt);

            name.setText(result.getName());

            if (result.getGrade() == 0 && result.getAttempt() == 0) {
                grade.setText("-");
                grade.setBackgroundResource(R.drawable.circle_grey);
                //attempt.setText("");
            } else {
                grade.setText(Integer.toString(result.getGrade()));
                if (result.getGrade() > 5) {
                    grade.setBackgroundResource(R.drawable.circle_green);
                } else {
                    grade.setBackgroundResource(R.drawable.circle_red);
                }
                //attempt.setText(Integer.toString(result.getAttempt()));
            }
        }

        return convertView;
    }
}
