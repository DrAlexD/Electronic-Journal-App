package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class EventsOrLessonsFragment extends Fragment {
    private StudentPerformanceViewModel studentPerformanceViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_lessons, container, false);

        int position = getArguments().getInt("position");

        studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);

        if ((position == 1 && studentPerformanceViewModel.getEvents().getValue() != null) ||
                (position == 2 && studentPerformanceViewModel.getLectures().getValue() != null) ||
                (position == 3 && studentPerformanceViewModel.getSeminars().getValue() != null)) {
            generateEventOrLessons(root, position);
        }

        return root;
    }

    private void generateEventOrLessons(View root, int position) {
        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();

        int moduleExpand = studentPerformanceViewModel.getModuleExpand().getValue();
        int openPage = studentPerformanceViewModel.getOpenPage().getValue() + 1;
        StudentPerformanceInSubject studentPerformanceInSubject = studentPerformanceViewModel.getStudentPerformanceInSubject().getValue();
        Map<String, List<Event>> events = studentPerformanceViewModel.getEvents().getValue();
        Map<String, List<StudentEvent>> studentEvents = studentPerformanceViewModel.getStudentEvents().getValue();
        Map<String, Module> modules = studentPerformanceViewModel.getModules().getValue();
        Map<String, StudentPerformanceInModule> studentPerformanceInModules = studentPerformanceViewModel.getStudentPerformanceInModules().getValue();
        Map<String, List<StudentLesson>> studentLessons = studentPerformanceViewModel.getStudentLessons().getValue();
        Map<String, List<Lesson>> lectures = studentPerformanceViewModel.getLectures().getValue();
        Map<String, List<Lesson>> seminars = studentPerformanceViewModel.getSeminars().getValue();

        //TODO можно ли создать expandableList с двойной вложенностью?
        final ExpandableListView expandableListView = root.findViewById(R.id.events_or_lessons_list);
        if (position == 1) {
            ExpandableListView.OnChildClickListener onEventClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                Event event = events.get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

                StudentEvent studentEventChosen = null;
                int lastAttempt = 0;
                if (studentEvents != null && studentEvents.get(String.valueOf(modulesNumbers.get(groupPosition))) != null) {
                    for (StudentEvent studentEvent : studentEvents.get(String.valueOf(modulesNumbers.get(groupPosition)))) {
                        if (studentEvent.getEvent().getId() == event.getId() && studentEvent.getAttemptNumber() > lastAttempt) {
                            studentEventChosen = studentEvent;
                            lastAttempt = studentEvent.getAttemptNumber();
                        }
                    }
                }

                int finalLastAttempt = lastAttempt;
                Long finalStudentEventId;
                if (studentEventChosen != null)
                    finalStudentEventId = studentEventChosen.getId();
                else
                    finalStudentEventId = null;

                bundle.putString("eventTitle", event.getTitle());
                bundle.putInt("attemptNumber", finalLastAttempt);
                bundle.putLong("eventId", event.getId());
                if (finalStudentEventId != null)
                    bundle.putLong("studentEventId", finalStudentEventId);
                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubject.getId());
                bundle.putLong("subjectInfoId", studentPerformanceInSubject.getSubjectInfo().getId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_event, bundle);
                return true;
            };

            TextView earnedPoints = root.findViewById(R.id.earnedPoints);
            TextView bonusPoints = root.findViewById(R.id.bonusPoints);
            TextView mark = root.findViewById(R.id.mark);
            TextView earnedExamPoints = root.findViewById(R.id.earnedExamPoints);

            earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
            bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
            if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null) {
                earnedPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
                bonusPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
            }
            mark.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ||
                    studentPerformanceInSubject.getSubjectInfo().isDifferentiatedCredit() ? View.VISIBLE : View.INVISIBLE);
            mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
            earnedExamPoints.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ? View.VISIBLE : View.INVISIBLE);
            earnedExamPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));

            EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modulesNumbers,
                    modules,
                    studentPerformanceInModules,
                    events, studentEvents);
            expandableListView.setAdapter(eventsAdapter);
            expandableListView.setOnChildClickListener(onEventClickListener);
            if (moduleExpand != -1 && openPage == position)
                expandableListView.expandGroup(moduleExpand);
        } else if (position == 2) {
            ExpandableListView.OnChildClickListener onLectureClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Lesson lesson = lectures.
                        get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

                StudentLesson studentLessonChosen = null;
                if (studentLessons != null && studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition))) != null) {
                    for (StudentLesson studentLesson : studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition)))) {
                        if (studentLesson.getLesson().getId() == lesson.getId()) {
                            studentLessonChosen = studentLesson;
                            break;
                        }
                    }
                }

                Long finalStudentLessonId;
                if (studentLessonChosen != null)
                    finalStudentLessonId = studentLessonChosen.getId();
                else
                    finalStudentLessonId = null;

                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", true);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                bundle.putString("lessonDate", dateFormat.format(lesson.getDateAndTime()));
                bundle.putLong("lessonId", lesson.getId());
                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubject.getId());
                if (finalStudentLessonId != null)
                    bundle.putLong("studentLessonId", finalStudentLessonId);
                bundle.putLong("subjectInfoId", studentPerformanceInSubject.getSubjectInfo().getId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modulesNumbers,
                    modules,
                    studentPerformanceInModules,
                    lectures, studentLessons);
            expandableListView.setAdapter(lessonsAdapter);
            expandableListView.setOnChildClickListener(onLectureClickListener);
            if (moduleExpand != -1 && openPage == position)
                expandableListView.expandGroup(moduleExpand);
        } else if (position == 3) {
            ExpandableListView.OnChildClickListener onSeminarClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Lesson lesson = seminars.
                        get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

                StudentLesson studentLessonChosen = null;
                if (studentLessons != null && studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition))) != null) {
                    for (StudentLesson studentLesson : studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition)))) {
                        if (studentLesson.getLesson().getId() == lesson.getId()) {
                            studentLessonChosen = studentLesson;
                            break;
                        }
                    }
                }

                Long finalStudentLessonId;
                if (studentLessonChosen != null)
                    finalStudentLessonId = studentLessonChosen.getId();
                else
                    finalStudentLessonId = null;

                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", false);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                bundle.putString("lessonDate", dateFormat.format(lesson.getDateAndTime()));
                bundle.putLong("lessonId", lesson.getId());
                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubject.getId());
                if (finalStudentLessonId != null)
                    bundle.putLong("studentLessonId", finalStudentLessonId);
                bundle.putLong("subjectInfoId", studentPerformanceInSubject.getSubjectInfo().getId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modulesNumbers,
                    modules,
                    studentPerformanceInModules,
                    seminars, studentLessons);
            expandableListView.setAdapter(lessonsAdapter);
            expandableListView.setOnChildClickListener(onSeminarClickListener);
            if (moduleExpand != -1 && openPage == position)
                expandableListView.expandGroup(moduleExpand);
        }

    }
}