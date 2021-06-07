package com.example.electronic_journal.group;

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

import com.example.electronic_journal.R;
import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ModuleFragment extends Fragment {
    private int moduleNumber;

    private GroupPerformanceLessonsViewModel groupPerformanceLessonsViewModel;
    private long userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_module, container, false);

        moduleNumber = getArguments().getInt("position");

        groupPerformanceLessonsViewModel = new ViewModelProvider(getParentFragment()).get(GroupPerformanceLessonsViewModel.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);
        userId = sharedPreferences.getLong("userId", -1);

        Button addLessonButton = root.findViewById(R.id.addLessonButton);
        addLessonButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addLessonButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();

            int professorType = 3;
            if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null &&
                    groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor() != null) {
                if (userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() ||
                        userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                    if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                        professorType = 2;
                    } else if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                        professorType = 1;
                    }
                }
            } else if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null) {
                if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                    professorType = 2;
                }
            } else {
                if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                    professorType = 1;
                }
            }
            bundle.putInt("professorType", professorType);

            bundle.putLong("moduleId", groupPerformanceLessonsViewModel.getModules().getValue().get(String.valueOf(moduleNumber)).getId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_adding, bundle);
        });

        if (groupPerformanceLessonsViewModel.getLessons().getValue() != null &&
                groupPerformanceLessonsViewModel.getLessons().getValue().get(String.valueOf(moduleNumber)) != null) {
            generateLessonsTable(root);
        }

        return root;
    }

    private void generateLessonsTable(View root) {
        TableLayout studentsInModuleLessonsTable = root.findViewById(R.id.studentsInModuleLessonsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow lessonsRow = generateLessonsRow(padding2inDp, padding5inDp);
        studentsInModuleLessonsTable.addView(lessonsRow);

        List<Student> students = groupPerformanceLessonsViewModel.getStudentsInGroup().getValue();
        for (Student student : students) {
            TableRow pointsRow = generatePointsRow(padding2inDp, padding5inDp, student);
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
        //infoView.setTextColor(getResources().getColor(R.color.black));
        infoView.setText("");
        infoView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        infoView.setGravity(Gravity.CENTER);
        lessonsRow.addView(infoView);

        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
        List<Lesson> lessons = groupPerformanceLessonsViewModel.getLessons().getValue().get(String.valueOf(modulesNumbers.get(moduleNumber - 1)));

        for (Lesson lesson : lessons) {
            TextView lessonView = new TextView(getContext());
            lessonView.setTextSize(20);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM");
            lessonView.setText(dateFormat.format(lesson.getDateAndTime()));
            if (lesson.isLecture())
                lessonView.setBackgroundColor(getResources().getColor(R.color.very_light_red));
            else
                lessonView.setBackgroundColor(getResources().getColor(R.color.very_light_blue));
            //lessonView.setTextColor(getResources().getColor(R.color.black));

            lessonView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            lessonView.setGravity(Gravity.CENTER);
            lessonView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();

                int professorType = 3;
                if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null &&
                        groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor() != null) {
                    if (userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() ||
                            userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                        if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                            professorType = 2;
                        } else if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                            professorType = 1;
                        }
                    }
                } else if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null) {
                    if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                        professorType = 2;
                    }
                } else {
                    if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                        professorType = 1;
                    }
                }
                bundle.putInt("professorType", professorType);

                bundle.putLong("groupId", groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getGroup().getId());

                boolean isHasStudentLesson = false;
                for (Student student : groupPerformanceLessonsViewModel.getStudentsInGroup().getValue()) {
                    List<StudentLesson> studentLessons = null;
                    if (groupPerformanceLessonsViewModel.getStudentsLessons().getValue() != null &&
                            groupPerformanceLessonsViewModel.getStudentsLessons().getValue()
                                    .get(String.valueOf(modulesNumbers.get(moduleNumber - 1))) != null) {
                        studentLessons = groupPerformanceLessonsViewModel.getStudentsLessons().getValue()
                                .get(String.valueOf(modulesNumbers.get(moduleNumber - 1))).get(String.valueOf(student.getId()));
                    }

                    if (studentLessons != null) {
                        for (StudentLesson studentLesson : studentLessons) {
                            if (studentLesson.getLesson().getId() == lesson.getId()) {
                                isHasStudentLesson = true;
                                break;
                            }
                        }
                    }

                    if (isHasStudentLesson)
                        break;
                }

                bundle.putBoolean("isHasStudentLesson", isHasStudentLesson);
                bundle.putLong("lessonId", lesson.getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_lesson_editing, bundle);
            });
            //lessonView.setRotation(90);
            lessonsRow.addView(lessonView);
        }

        Map<String, Module> modules = groupPerformanceLessonsViewModel.getModules().getValue();

        TextView moduleView = new TextView(getContext());
        moduleView.setTextSize(20);
        //moduleView.setTextColor(getResources().getColor(R.color.black));
        moduleView.setText("Модуль " + moduleNumber);
        moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        moduleView.setGravity(Gravity.CENTER);
        moduleView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("moduleId", modules.get(String.valueOf(moduleNumber)).getId());
            bundle.putInt("moduleNumber", moduleNumber);
            bundle.putBoolean("isFromEventsPerformance", false);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_lessons_to_dialog_module_info_editing, bundle);
        });
        lessonsRow.addView(moduleView);

        return lessonsRow;
    }

    private TableRow generatePointsRow(int padding2inDp, int padding5inDp, Student student) {
        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();

        List<Lesson> lessons = groupPerformanceLessonsViewModel.getLessons().getValue()
                .get(String.valueOf(modulesNumbers.get(moduleNumber - 1)));

        List<StudentLesson> studentLessons = null;
        if (groupPerformanceLessonsViewModel.getStudentsLessons().getValue() != null &&
                groupPerformanceLessonsViewModel.getStudentsLessons().getValue()
                        .get(String.valueOf(modulesNumbers.get(moduleNumber - 1))) != null) {
            studentLessons = groupPerformanceLessonsViewModel.getStudentsLessons().getValue()
                    .get(String.valueOf(modulesNumbers.get(moduleNumber - 1))).get(String.valueOf(student.getId()));
        }

        StudentPerformanceInModule studentPerformanceInModule = null;
        for (StudentPerformanceInModule st : groupPerformanceLessonsViewModel.getStudentsPerformancesInModules()
                .getValue().get(String.valueOf(moduleNumber))) {
            if (st.getStudentPerformanceInSubject().getStudent().getId() == student.getId()) {
                studentPerformanceInModule = st;
            }
        }

        StudentPerformanceInSubject studentPerformanceInSubject = studentPerformanceInModule.getStudentPerformanceInSubject();

        TextView studentView = new TextView(getContext());
        studentView.setText(student.getFirstName().charAt(0) + ". " + student.getSecondName());
        studentView.setTextSize(20);
        //studentView.setTextColor(getResources().getColor(R.color.black));
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("studentId", student.getId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_lessons_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        for (Lesson lesson : lessons) {
            TextView pointsView = new TextView(getContext());
            pointsView.setTextSize(20);
            //pointsView.setTextColor(getResources().getColor(R.color.black));
            pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            boolean isHasData = false;
            Long studentLessonId = null;
            if (studentLessons != null) {
                for (StudentLesson studentLesson : studentLessons) {
                    if (studentLesson.getLesson().getId() == lesson.getId()) {
                        if (!studentLesson.isAttended()) {
                            pointsView.setText("Н");
                        } else {
                            if (studentLesson.getBonusPoints() == null)
                                pointsView.setText(String.valueOf(lesson.getPointsPerVisit()));
                            else
                                pointsView.setText(String.valueOf(lesson.getPointsPerVisit() + studentLesson.getBonusPoints()));
                        }
                        isHasData = true;
                        studentLessonId = studentLesson.getId();
                        break;
                    }
                }
            }
            if (!isHasData)
                pointsView.setText("");
            pointsView.setGravity(Gravity.CENTER);

            boolean finalIsHasData = isHasData;
            Long finalStudentLessonId = studentLessonId;
            Long finalStudentPerformanceInSubjectId = studentPerformanceInSubject.getId();
            pointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();

                int professorType = 3;
                if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null &&
                        groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor() != null) {
                    if (userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() ||
                            userId != groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                        if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                            professorType = 2;
                        } else if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                            professorType = 1;
                        }
                    }
                } else if (groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId() != null) {
                    if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getLecturerId()) {
                        professorType = 2;
                    }
                } else {
                    if (userId == groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getSeminarsProfessor().getId()) {
                        professorType = 1;
                    }
                }
                bundle.putInt("professorType", professorType);

                bundle.putBoolean("isFromGroupPerformance", true);
                bundle.putBoolean("isHasData", finalIsHasData);
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                bundle.putString("lessonDate", dateFormat.format(lesson.getDateAndTime()));
                bundle.putBoolean("isLecture", lesson.isLecture());
                bundle.putLong("lessonId", lesson.getId());
                bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubjectId);
                if (finalStudentLessonId != null)
                    bundle.putLong("studentLessonId", finalStudentLessonId);
                bundle.putLong("subjectInfoId", groupPerformanceLessonsViewModel.getSubjectInfo().getValue().getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_lesson, bundle);
            });
            pointsRow.addView(pointsView);
        }

        TextView moduleView = new TextView(getContext());
        moduleView.setTextSize(20);
        //moduleView.setTextColor(getResources().getColor(R.color.black));
        if (studentPerformanceInModule.getEarnedPoints() == null) {
            moduleView.setText("-");
        } else {
            moduleView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
            if (studentPerformanceInModule.isHaveCredit() != null) {
                moduleView.setTextColor(studentPerformanceInModule.isHaveCredit() ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            }
        }
        moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        moduleView.setGravity(Gravity.CENTER);
        pointsRow.addView(moduleView);

        return pointsRow;
    }
}