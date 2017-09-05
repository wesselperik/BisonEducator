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
import com.wesselperik.educator.model.Goal;

import java.util.List;

/**
 * Created by Wessel on 24-5-2017.
 */

public class GoalListAdapter extends ArrayAdapter {

    private Context context;

    public GoalListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Goal> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false);
        }

        Goal goal = (Goal) getItem(position);
        TextView name = null;
        TextView progress = null;
        TextView achieved = null;
        if (goal != null) {
            name = (TextView) convertView.findViewById(R.id.goal_name);
            progress = (TextView) convertView.findViewById(R.id.goal_progress);
            achieved = (TextView) convertView.findViewById(R.id.goal_achieved);

            name.setText(goal.getName());
            progress.setText(goal.getProgress() + "%");
            achieved.setText(goal.getAchieved());
        }

        return convertView;
    }
}
