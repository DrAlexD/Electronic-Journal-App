package com.example.electronicdiary.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import java.util.List;
import java.util.Map;

class LessonsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;

    private final List<Integer> modules;
    private final Map<String, Module> moduleByModules;
    private final Map<String, StudentPerformanceInModule> studentPerformanceByModules;
    private final Map<String, List<Lesson>> lessonsByModules;
    private final Map<String, List<StudentLesson>> studentLessonsByModules;

    LessonsAdapter(Context context, List<Integer> modules, Map<String, Module> moduleByModules,
                   Map<String, StudentPerformanceInModule> studentPerformanceByModules,
                   Map<String, List<Lesson>> lessonsByModules,
                   Map<String, List<StudentLesson>> studentLessonsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modules = modules;
        this.moduleByModules = moduleByModules;
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
        return lessonsByModules.get(String.valueOf(modules.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return modules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lessonsByModules.get(String.valueOf(modules.get(groupPosition))).get(childPosition);
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
        Module module = moduleByModules.get(String.valueOf(modules.get(groupPosition)));
        StudentPerformanceInModule studentPerformanceInModule = studentPerformanceByModules.get(String.valueOf(modules.get(groupPosition)));

        if (view == null) {
            view = inflater.inflate(R.layout.holder_module_performance, null);
        }

        TextView moduleTitleWithPointsView = view.findViewById(R.id.moduleTitleWithPoints);
        moduleTitleWithPointsView.setText(moduleTitle + " (" + module.getMinPoints() +
                "-" + module.getMaxPoints() + ")");
        moduleTitleWithPointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > module.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        TextView earnedModulePointsView = view.findViewById(R.id.earnedModulePoints);
        earnedModulePointsView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
        earnedModulePointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > module.getMinPoints() ?
                inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        Lesson lesson = lessonsByModules.get(String.valueOf(modules.get(groupPosition))).get(childPosition);
        List<StudentLesson> studentLessons = studentLessonsByModules.get(String.valueOf(modules.get(groupPosition)));

        StudentLesson studentLesson = null;
        for (int i = 0; i < studentLessons.size(); i++) {
            if (lesson.getId() == studentLessons.get(i).getLesson().getId()) {
                studentLesson = studentLessons.get(i);
            }
        }

        if (view == null) {
            view = inflater.inflate(R.layout.holder_lesson_performance, null);
        }

        TextView lessonDateView = view.findViewById(R.id.lessonDate);
        TextView lessonTimeView = view.findViewById(R.id.lessonTime);
        TextView bonusPointsView = view.findViewById(R.id.bonusPoints);

        lessonDateView.setText(((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                (lesson.getDateAndTime().getDate())) + "." +
                ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                        (lesson.getDateAndTime().getMonth() + 1)) + "." + lesson.getDateAndTime().getYear());
        lessonTimeView.setText(((lesson.getDateAndTime().getHours()) < 10 ? "0" + (lesson.getDateAndTime().getHours()) :
                (lesson.getDateAndTime().getHours())) + ":" +
                ((lesson.getDateAndTime().getMinutes()) < 10 ? "0" + (lesson.getDateAndTime().getMinutes()) :
                        (lesson.getDateAndTime().getMinutes())));

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

