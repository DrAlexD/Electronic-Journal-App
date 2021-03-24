package com.example.electronicdiary.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.Group;
import com.example.electronicdiary.R;
import com.example.electronicdiary.Subject;

import java.util.ArrayList;
import java.util.HashMap;

class SubjectsWithGroupsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<Subject> subjects;
    private final HashMap<Subject, ArrayList<Group>> subjectsWithGroups;

    SubjectsWithGroupsAdapter(Context context, ArrayList<Subject> subjects, HashMap<Subject, ArrayList<Group>> subjectWithGroups) {
        this.inflater = LayoutInflater.from(context);
        this.subjects = subjects;
        this.subjectsWithGroups = subjectWithGroups;
    }

    @Override
    public int getGroupCount() {
        return subjects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subjectsWithGroups.get(subjects.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return subjects.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subjectsWithGroups.get(subjects.get(groupPosition)).get(childPosition);
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
        Subject subject = subjects.get(groupPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_subject_with_group, null);
        }

        TextView subjectTitleView = view.findViewById(R.id.subjectTitle);
        subjectTitleView.setText(subject.getTitle());

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        Group group = subjectsWithGroups.get(subjects.get(groupPosition)).get(childPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.holder_group_in_subject, null);
        }

        TextView groupTitleView = view.findViewById(R.id.groupTitle);
        groupTitleView.setText(group.getTitle());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

