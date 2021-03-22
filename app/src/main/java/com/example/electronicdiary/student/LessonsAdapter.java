package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.StudentLesson;

import java.util.ArrayList;
import java.util.HashMap;

class LessonsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<Integer> modules;
    private final HashMap<Integer, ArrayList<StudentLesson>> lessonsByModules;

    LessonsAdapter(Context context, ArrayList<Integer> modules, HashMap<Integer, ArrayList<StudentLesson>> lessonsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modules = modules;
        this.lessonsByModules = lessonsByModules;
    }

    @Override
    public int getGroupCount() {
        return modules.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lessonsByModules.get(modules.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return modules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lessonsByModules.get(modules.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String moduleTitle = "Модуль " + modules.get(groupPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_module, null);
        }

        TextView moduleTitleView = view.findViewById(R.id.moduleTitle);
        moduleTitleView.setText(moduleTitle);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        StudentLesson studentLesson = lessonsByModules.get(modules.get(groupPosition)).get(childPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_lesson, null);
        }

        TextView lessonDateView = view.findViewById(R.id.lessonDate);
        TextView lessonPointsView = view.findViewById(R.id.lessonPoints);
        lessonDateView.setText(studentLesson.getDate());
        lessonPointsView.setText(String.valueOf(studentLesson.getPoints()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

