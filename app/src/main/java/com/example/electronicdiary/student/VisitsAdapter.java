package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.R;

import java.util.ArrayList;
import java.util.HashMap;

class VisitsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<String> modules;
    private final HashMap<String, ArrayList<Visit>> visitsByModules;

    VisitsAdapter(Context context, ArrayList<String> modules, HashMap<String, ArrayList<Visit>> visitsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modules = modules;
        this.visitsByModules = visitsByModules;
    }

    @Override
    public int getGroupCount() {
        return modules.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return visitsByModules.get(modules.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return modules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return visitsByModules.get(modules.get(groupPosition)).get(childPosition);
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
        String moduleTitle = modules.get(groupPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_module, null);
        }

        TextView moduleTitleView = view.findViewById(R.id.moduleTitle);
        moduleTitleView.setText(moduleTitle);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        Visit visit = visitsByModules.get(modules.get(groupPosition)).get(childPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_visit, null);
        }

        TextView visitDateView = view.findViewById(R.id.visitDate);
        TextView visitPointsView = view.findViewById(R.id.visitPoints);
        visitDateView.setText(visit.getDate());
        visitPointsView.setText(String.valueOf(visit.getPoints()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

