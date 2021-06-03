package com.example.electronic_journal.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

class SubjectsWithGroupsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final List<Subject> subjects;
    private final Map<String, List<SubjectInfo>> subjectsWithGroups;

    SubjectsWithGroupsAdapter(Context context, List<Subject> subjects, Map<String, List<SubjectInfo>> subjectWithGroups) {
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
        return subjectsWithGroups.get(String.valueOf(subjects.get(groupPosition).getId())).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return subjects.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subjectsWithGroups.get(String.valueOf(subjects.get(groupPosition).getId())).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_subject_with_group, null);
        }

        Subject subject = subjects.get(groupPosition);

        TextView subjectTitleView = view.findViewById(R.id.subjectTitle);
        subjectTitleView.setText(subject.getTitle());

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_group_in_subject, null);
        }

        SubjectInfo subjectInfo = subjectsWithGroups.get(String.valueOf(subjects.get(groupPosition).getId())).get(childPosition);

        TextView groupTitleView = view.findViewById(R.id.groupTitle);
        groupTitleView.setText(subjectInfo.getGroup().getTitle());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

