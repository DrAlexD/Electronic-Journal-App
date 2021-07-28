package com.example.electronic_journal.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        if (lessonsByModules.get(String.valueOf(modules.get(groupPosition))) != null)
            return lessonsByModules.get(String.valueOf(modules.get(groupPosition))).size();
        else
            return 0;
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_module_performance, null);
        }
        String moduleTitle = "Модуль " + modules.get(groupPosition);
        Module module = moduleByModules.get(String.valueOf(modules.get(groupPosition)));
        StudentPerformanceInModule studentPerformanceInModule = studentPerformanceByModules.get(String.valueOf(modules.get(groupPosition)));

        TextView moduleTitleWithPointsView = view.findViewById(R.id.moduleTitleWithPoints);
        if (module.getMaxAvailablePoints() != 0) {
            moduleTitleWithPointsView.setText(moduleTitle + " (" + module.getMinPoints() +
                    "-" + module.getMaxAvailablePoints() + ")");
        } else {
            moduleTitleWithPointsView.setText(moduleTitle);
        }
        if (studentPerformanceInModule.getEarnedPoints() != null) {
            moduleTitleWithPointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > module.getMinPoints() ?
                    inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

            TextView earnedModulePointsView = view.findViewById(R.id.earnedModulePoints);
            earnedModulePointsView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
            earnedModulePointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() > module.getMinPoints() ?
                    inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
        }


        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_lesson_performance, null);
        }
        Lesson lesson = lessonsByModules.get(String.valueOf(modules.get(groupPosition))).get(childPosition);
        List<StudentLesson> studentLessons = null;
        if (studentLessonsByModules != null) {
            studentLessons = studentLessonsByModules.get(String.valueOf(modules.get(groupPosition)));
        }

        StudentLesson studentLesson = null;
        if (studentLessons != null) {
            for (int i = 0; i < studentLessons.size(); i++) {
                if (lesson.getId() == studentLessons.get(i).getLesson().getId()) {
                    studentLesson = studentLessons.get(i);
                }
            }
        }


        TextView lessonDateView = view.findViewById(R.id.lessonDate);
        TextView lessonTimeView = view.findViewById(R.id.lessonTime);
        TextView bonusPointsView = view.findViewById(R.id.bonusPoints);

        DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        lessonDateView.setText(dateFormat1.format(lesson.getDateAndTime()));
        lessonTimeView.setText(dateFormat2.format(lesson.getDateAndTime()));

        if (studentLesson == null) {
            bonusPointsView.setText("Не посещено");
        } else {
            lessonDateView.setTextColor(studentLesson.isAttended() ? inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
            lessonTimeView.setTextColor(studentLesson.isAttended() ? inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
            if (studentLesson.getBonusPoints() != null && studentLesson.getBonusPoints() != 0) {
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

