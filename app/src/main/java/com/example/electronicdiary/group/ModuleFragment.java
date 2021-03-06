package com.example.electronicdiary.group;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Student;

import java.util.ArrayList;
import java.util.Date;

public class ModuleFragment extends Fragment {
    private int position;

    private String subject;
    private String group;
    private ArrayList<Student> students;
    private ArrayList<Date> lessons;
    private ArrayList<StudentInModule> studentsInModule;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        position = getArguments().getInt("position");

        GroupPerformanceViewModel groupPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(GroupPerformanceViewModel.class);
        studentsInModule = groupPerformanceViewModel.getStudentsInModule().getValue();
        students = groupPerformanceViewModel.getStudents().getValue();
        lessons = groupPerformanceViewModel.getLessons().getValue();
        group = groupPerformanceViewModel.getGroup().getValue();
        subject = groupPerformanceViewModel.getSubject().getValue();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        Button addLessonButton = root.findViewById(R.id.addLessonButton);
        addLessonButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addLessonButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("subject", subject);
            bundle.putString("group", group);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_adding, bundle);
        });

        ModuleViewModel moduleViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        moduleViewModel.setPosition(position);

        generateLessonsTable(root);

        return root;
    }

    private void generateLessonsTable(View root) {
        TableLayout studentsInModuleLessonsTable = root.findViewById(R.id.studentsInModuleLessonsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow lessonsRow = generateLessonsRow(padding2inDp, padding5inDp);
        studentsInModuleLessonsTable.addView(lessonsRow);

        for (int i = 0; i < studentsInModule.size(); i++) {
            TableRow pointsRow = generatePointsRow(i, padding2inDp, padding5inDp);
            studentsInModuleLessonsTable.addView(pointsRow);
        }
    }

    private TableRow generateLessonsRow(int padding2inDp, int padding5inDp) {
        TableRow lessonsRow = new TableRow(getContext());
        lessonsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        lessonsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView infoView = new TextView(getContext());
        infoView.setTextSize(20);
        infoView.setText("Студент\\Занятие");
        infoView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        infoView.setGravity(Gravity.CENTER);
        lessonsRow.addView(infoView);

        for (Date lesson : lessons) {
            TextView lessonView = new TextView(getContext());
            lessonView.setTextSize(20);
            lessonView.setText(lesson.getDate() + "." + (lesson.getMonth() + 1));
            lessonView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            lessonView.setGravity(Gravity.CENTER);
            lessonView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("subject", subject);
                bundle.putString("group", group);
                bundle.putString("lessonDate", lesson.getDate() + "." + (lesson.getMonth() + 1));
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_info, bundle);
            });
            //lessonView.setRotation(90);
            lessonsRow.addView(lessonView);
        }

        return lessonsRow;
    }

    private TableRow generatePointsRow(int i, int padding2inDp, int padding5inDp) {
        StudentInModule studentInModule = studentsInModule.get(i);

        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView studentView = new TextView(getContext());
        studentView.setText(students.get(i).getFullName());
        studentView.setTextSize(20);
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("student", ((TextView) view).getText().toString());
            bundle.putString("group", group);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        for (Date lesson : lessons) {
            TextView pointsView = new TextView(getContext());
            pointsView.setTextSize(20);
            pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            if (studentInModule.getLessonsWithPoints().containsKey(lesson))
                pointsView.setText(studentInModule.getLessonsWithPoints().get(lesson).toString());
            else
                pointsView.setText("");
            pointsView.setGravity(Gravity.CENTER);
            pointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("subject", subject);
                bundle.putString("group", group);
                bundle.putString("lessonDate", lesson.getDate() + "." + (lesson.getMonth() + 1));
                bundle.putString("isAttended", "true");
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_performance, bundle);
            });
            pointsRow.addView(pointsView);
        }

        return pointsRow;
    }
}