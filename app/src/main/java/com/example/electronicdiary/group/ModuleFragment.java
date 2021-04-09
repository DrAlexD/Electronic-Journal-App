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
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class ModuleFragment extends Fragment {
    private int moduleNumber;

    private GroupPerformanceViewModel groupPerformanceViewModel;
    private SubjectInfo subjectInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        moduleNumber = getArguments().getInt("position");

        groupPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(GroupPerformanceViewModel.class);
        subjectInfo = groupPerformanceViewModel.getSubjectInfo().getValue();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        Button addLessonButton = root.findViewById(R.id.addLessonButton);
        addLessonButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addLessonButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("moduleNumber", moduleNumber);
            bundle.putInt("groupId", subjectInfo.getGroup().getId());
            bundle.putInt("subjectId", subjectInfo.getSubjectId());
            bundle.putInt("lecturerId", subjectInfo.getLecturerId());
            bundle.putInt("seminarianId", subjectInfo.getSeminarianId());
            bundle.putInt("semesterId", subjectInfo.getSemesterId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_adding, bundle);
        });

        groupPerformanceViewModel.getStudentsLessons().observe(getViewLifecycleOwner(), studentsLessons -> {
            if (studentsLessons == null) {
                return;
            }

            generateLessonsTable(root, studentsLessons);
        });

        return root;
    }

    private void generateLessonsTable(View root, HashMap<Integer, ArrayList<ArrayList<StudentLesson>>> studentsLessons) {
        TableLayout studentsInModuleLessonsTable = root.findViewById(R.id.studentsInModuleLessonsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        ArrayList<Integer> modules = Repository.getInstance().getModules();
        ArrayList<Lesson> lessons = groupPerformanceViewModel.getLessons().getValue().get(modules.get(moduleNumber - 1));
        TableRow lessonsRow = generateLessonsRow(padding2inDp, padding5inDp, lessons);
        studentsInModuleLessonsTable.addView(lessonsRow);

        ArrayList<Student> students = groupPerformanceViewModel.getStudentsInGroup().getValue();
        for (int i = 0; i < students.size(); i++) {
            TableRow pointsRow = generatePointsRow(padding2inDp, padding5inDp, students.get(i), lessons,
                    studentsLessons.get(modules.get(moduleNumber - 1)).get(i),
                    groupPerformanceViewModel.getStudentsPerformancesInModules().getValue().get(moduleNumber).get(i));
            studentsInModuleLessonsTable.addView(pointsRow);
        }
    }

    private TableRow generateLessonsRow(int padding2inDp, int padding5inDp, ArrayList<Lesson> lessons) {
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

        for (Lesson lesson : lessons) {
            TextView lessonView = new TextView(getContext());
            lessonView.setTextSize(20);
            lessonView.setText(((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                    (lesson.getDateAndTime().getDate())) + "." +
                    ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                            (lesson.getDateAndTime().getMonth() + 1)));
            lessonView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            lessonView.setGravity(Gravity.CENTER);
            lessonView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("lessonId", lesson.getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_editing, bundle);
            });
            //lessonView.setRotation(90);
            lessonsRow.addView(lessonView);
        }

        TextView moduleView = new TextView(getContext());
        moduleView.setTextSize(20);
        moduleView.setText("Модуль " + moduleNumber);
        moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        moduleView.setGravity(Gravity.CENTER);
        moduleView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("moduleNumber", moduleNumber);
            bundle.putInt("groupId", subjectInfo.getGroup().getId());
            bundle.putInt("subjectId", subjectInfo.getSubjectId());
            bundle.putInt("lecturerId", subjectInfo.getLecturerId());
            bundle.putInt("seminarianId", subjectInfo.getSeminarianId());
            bundle.putInt("semesterId", subjectInfo.getSemesterId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_module_info_editing, bundle);
        });
        lessonsRow.addView(moduleView);

        return lessonsRow;
    }

    private TableRow generatePointsRow(int padding2inDp, int padding5inDp, Student student, ArrayList<Lesson> lessons,
                                       ArrayList<StudentLesson> studentLessons, StudentPerformanceInModule studentPerformanceInModule) {
        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView studentView = new TextView(getContext());
        studentView.setText(student.getFullName());
        studentView.setTextSize(20);
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("studentId", student.getId());
            bundle.putInt("semesterId", subjectInfo.getSemesterId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        for (Lesson lesson : lessons) {
            TextView pointsView = new TextView(getContext());
            pointsView.setTextSize(20);
            pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            boolean isHasData = false;
            for (StudentLesson studentLesson : studentLessons) {
                if (studentLesson.getStudentId() == student.getId() && studentLesson.getLessonId() == lesson.getId()) {
                    if (!studentLesson.isAttended()) {
                        pointsView.setText("Н");
                    } else {
                        if (studentLesson.getBonusPoints() == -1)
                            pointsView.setText(String.valueOf(lesson.getPointsPerVisit()));
                        else
                            pointsView.setText(String.valueOf(lesson.getPointsPerVisit() + studentLesson.getBonusPoints()));
                    }
                    isHasData = true;
                    break;
                }
            }
            if (!isHasData)
                pointsView.setText("");
            pointsView.setGravity(Gravity.CENTER);

            boolean finalIsHasData = isHasData;
            pointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", true);
                bundle.putBoolean("isHasData", finalIsHasData);
                bundle.putString("lessonDate", ((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                        (lesson.getDateAndTime().getDate())) + "." +
                        ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                                (lesson.getDateAndTime().getMonth() + 1)) + "." + lesson.getDateAndTime().getYear() + " " +
                        ((lesson.getDateAndTime().getHours()) < 10 ? "0" + (lesson.getDateAndTime().getHours()) :
                                (lesson.getDateAndTime().getHours())) + ":" +
                        ((lesson.getDateAndTime().getMinutes()) < 10 ? "0" + (lesson.getDateAndTime().getMinutes()) :
                                (lesson.getDateAndTime().getMinutes())));
                bundle.putInt("lessonId", lesson.getId());
                bundle.putInt("studentId", student.getId());
                bundle.putInt("moduleNumber", moduleNumber);
                bundle.putInt("groupId", subjectInfo.getGroup().getId());
                bundle.putInt("subjectId", subjectInfo.getSubjectId());
                bundle.putInt("lecturerId", subjectInfo.getLecturerId());
                bundle.putInt("seminarianId", subjectInfo.getSeminarianId());
                bundle.putInt("semesterId", subjectInfo.getSemesterId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_lesson, bundle);
            });
            pointsRow.addView(pointsView);
        }

        TextView moduleView = new TextView(getContext());
        moduleView.setTextSize(20);
        if (studentPerformanceInModule.getEarnedPoints() == -1) {
            moduleView.setText("-");
        } else {
            moduleView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
            moduleView.setTextColor(studentPerformanceInModule.isHaveCredit() ?
                    getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        }
        moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        moduleView.setGravity(Gravity.CENTER);
        pointsRow.addView(moduleView);

        return pointsRow;
    }
}