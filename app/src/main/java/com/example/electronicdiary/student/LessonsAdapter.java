package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.ModuleInfo;
import com.example.electronicdiary.R;
import com.example.electronicdiary.StudentLesson;
import com.example.electronicdiary.StudentPerformanceInModule;

import java.util.ArrayList;
import java.util.HashMap;

class LessonsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;

    private final ArrayList<Integer> modules;
    private final HashMap<Integer, ModuleInfo> moduleInfoByModules;
    private final HashMap<Integer, StudentPerformanceInModule> studentPerformanceByModules;
    private final HashMap<Integer, ArrayList<Lesson>> lessonsByModules;
    private final HashMap<Integer, ArrayList<StudentLesson>> studentLessonsByModules;

    LessonsAdapter(Context context, ArrayList<Integer> modules, HashMap<Integer, ModuleInfo> moduleInfoByModules,
                   HashMap<Integer, StudentPerformanceInModule> studentPerformanceByModules,
                   HashMap<Integer, ArrayList<Lesson>> lessonsByModules,
                   HashMap<Integer, ArrayList<StudentLesson>> studentLessonsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modules = modules;
        this.moduleInfoByModules = moduleInfoByModules;
        this.studentPerformanceByModules = studentPerformanceByModules;
        this.lessonsByModules = lessonsByModules;
        this.studentLessonsByModules = studentLessonsByModules;
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
        ModuleInfo moduleInfo = moduleInfoByModules.get(modules.get(groupPosition));
        StudentPerformanceInModule studentPerformanceInModule = studentPerformanceByModules.get(modules.get(groupPosition));

        if (view == null) {
            view = inflater.inflate(R.layout.holder_module_performance, null);
        }

        TextView moduleTitleWithPointsView = view.findViewById(R.id.moduleTitleWithPoints);
        moduleTitleWithPointsView.setText(moduleTitle + " (" + moduleInfo.getMinPoints() +
                "-" + moduleInfo.getMaxPoints() + ")");
        moduleTitleWithPointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > moduleInfo.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        TextView earnedModulePointsView = view.findViewById(R.id.earnedModulePoints);
        earnedModulePointsView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
        earnedModulePointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > moduleInfo.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        Lesson lesson = lessonsByModules.get(modules.get(groupPosition)).get(childPosition);
        ArrayList<StudentLesson> studentLessons = studentLessonsByModules.get(modules.get(groupPosition));

        StudentLesson studentLesson = null;
        for (int i = 0; i < studentLessons.size(); i++) {
            if (lesson.getId() == studentLessons.get(i).getLessonId()) {
                studentLesson = studentLessons.get(i);
            }
        }

        if (view == null) {
            view = inflater.inflate(R.layout.holder_lesson_performance, null);
        }

        TextView lessonDateView = view.findViewById(R.id.lessonDate);
        TextView lessonTimeView = view.findViewById(R.id.lessonTime);
        TextView bonusPointsView = view.findViewById(R.id.bonusPoints);

        lessonDateView.setText(lesson.getDateAndTime().getDate());
        lessonTimeView.setText(lesson.getDateAndTime().getHours() + ":" + lesson.getDateAndTime().getMinutes());

        if (studentLesson == null) {
            bonusPointsView.setText("Нет данных");
        } else {
            lessonDateView.setTextColor(studentLesson.isAttended() ? inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
            lessonTimeView.setTextColor(studentLesson.isAttended() ? inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
            if (studentLesson.getBonusPoints() != 0) {
                bonusPointsView.setVisibility(View.VISIBLE);
                bonusPointsView.setText(String.valueOf(studentLesson.getBonusPoints()));
                bonusPointsView.setTextColor(studentLesson.isAttended() ? inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
            }
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

