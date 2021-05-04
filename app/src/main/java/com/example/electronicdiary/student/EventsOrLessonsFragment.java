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

import java.util.List;

public class EventsOrLessonsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_lessons, container, false);

        int position = getArguments().getInt("position");
        List<Integer> modules = Repository.getInstance().getModules();

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);

        int moduleExpand = studentPerformanceViewModel.getModuleExpand().getValue();
        int openPage = studentPerformanceViewModel.getOpenPage().getValue() + 1;

        //TODO можно ли создать expandableList с двойной вложенностью?
        final ExpandableListView expandableListView = root.findViewById(R.id.events_or_lessons_list);
        if (position == 1) {
            ExpandableListView.OnChildClickListener onEventClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                Event event = studentPerformanceViewModel.getEvents().getValue().get(modules.get(groupPosition)).get(childPosition);
                bundle.putInt("eventMinPoints", event.getMinPoints());
                bundle.putString("eventDeadlineDate", ((event.getDeadlineDate().getDate()) < 10 ? "0" + (event.getDeadlineDate().getDate()) :
                        (event.getDeadlineDate().getDate())) + "." +
                        ((event.getDeadlineDate().getMonth() + 1) < 10 ? "0" + (event.getDeadlineDate().getMonth() + 1) :
                                (event.getDeadlineDate().getMonth() + 1)) + "." + event.getDeadlineDate().getYear());
                bundle.putString("eventTitle", event.getTitle());
                TextView attemptNumber = v.findViewById(R.id.attemptNumber);
                bundle.putInt("attemptNumber", Integer.parseInt(attemptNumber.getText().toString()));
                bundle.putLong("eventId", event.getId());
                bundle.putLong("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putLong("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putLong("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putLong("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putLong("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putLong("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_event, bundle);
                return true;
            };

            studentPerformanceViewModel.getStudentPerformanceInSubject().observe(getViewLifecycleOwner(), studentPerformanceInSubject -> {
                if (studentPerformanceInSubject == null) {
                    return;
                }

                TextView earnedPoints = root.findViewById(R.id.earnedPoints);
                TextView bonusPoints = root.findViewById(R.id.bonusPoints);
                TextView mark = root.findViewById(R.id.mark);
                TextView earnedExamPoints = root.findViewById(R.id.earnedExamPoints);

                earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                earnedPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
                bonusPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
                mark.setVisibility(studentPerformanceViewModel.getSubjectInfo().getValue().isExam() ||
                        studentPerformanceViewModel.getSubjectInfo().getValue().isDifferentiatedCredit() ? View.VISIBLE : View.INVISIBLE);
                mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                earnedExamPoints.setVisibility(studentPerformanceViewModel.getSubjectInfo().getValue().isExam() ? View.VISIBLE : View.INVISIBLE);
                earnedExamPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
            });

            studentPerformanceViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
                if (events == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modules,
                        studentPerformanceViewModel.getModuleInfo().getValue(),
                        studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                        events, studentPerformanceViewModel.getStudentEvents().getValue());
                expandableListView.setAdapter(eventsAdapter);
                expandableListView.setOnChildClickListener(onEventClickListener);
                if (moduleExpand != -1 && openPage == position)
                    expandableListView.expandGroup(moduleExpand);
            });

            studentPerformanceViewModel.getStudentEvents().observe(getViewLifecycleOwner(), studentEvents -> {
                if (studentEvents == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modules,
                        studentPerformanceViewModel.getModuleInfo().getValue(),
                        studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                        studentPerformanceViewModel.getEvents().getValue(), studentEvents);
                expandableListView.setAdapter(eventsAdapter);
                expandableListView.setOnChildClickListener(onEventClickListener);
                if (moduleExpand != -1 && openPage == position)
                    expandableListView.expandGroup(moduleExpand);
            });
        } else if (position == 2) {
            ExpandableListView.OnChildClickListener onLectureClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", true);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                Lesson lesson = studentPerformanceViewModel.getLecturesByModules().getValue().
                        get(modules.get(groupPosition)).get(childPosition);
                bundle.putString("lessonDate", ((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                        (lesson.getDateAndTime().getDate())) + "." +
                        ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                                (lesson.getDateAndTime().getMonth() + 1)) + "." + lesson.getDateAndTime().getYear() + " " +
                        ((lesson.getDateAndTime().getHours()) < 10 ? "0" + (lesson.getDateAndTime().getHours()) :
                                (lesson.getDateAndTime().getHours())) + ":" +
                        ((lesson.getDateAndTime().getMinutes()) < 10 ? "0" + (lesson.getDateAndTime().getMinutes()) :
                                (lesson.getDateAndTime().getMinutes())));
                bundle.putLong("lessonId", lesson.getId());
                bundle.putLong("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putLong("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putLong("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putLong("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putLong("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putLong("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            studentPerformanceViewModel.getLecturesByModules().observe(getViewLifecycleOwner(),
                    lecturesByModules -> {
                        if (lecturesByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                lecturesByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                        if (moduleExpand != -1 && openPage == position)
                            expandableListView.expandGroup(moduleExpand);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                studentPerformanceViewModel.getLecturesByModules().getValue(), studentLessonsByModules);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                        if (moduleExpand != -1 && openPage == position)
                            expandableListView.expandGroup(moduleExpand);
                    });
        } else if (position == 3) {
            ExpandableListView.OnChildClickListener onSeminarClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", false);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                Lesson lesson = studentPerformanceViewModel.getSeminarsByModules().getValue().
                        get(modules.get(groupPosition)).get(childPosition);
                bundle.putString("lessonDate", ((lesson.getDateAndTime().getDate()) < 10 ? "0" + (lesson.getDateAndTime().getDate()) :
                        (lesson.getDateAndTime().getDate())) + "." +
                        ((lesson.getDateAndTime().getMonth() + 1) < 10 ? "0" + (lesson.getDateAndTime().getMonth() + 1) :
                                (lesson.getDateAndTime().getMonth() + 1)) + "." + lesson.getDateAndTime().getYear() + " " +
                        ((lesson.getDateAndTime().getHours()) < 10 ? "0" + (lesson.getDateAndTime().getHours()) :
                                (lesson.getDateAndTime().getHours())) + ":" +
                        ((lesson.getDateAndTime().getMinutes()) < 10 ? "0" + (lesson.getDateAndTime().getMinutes()) :
                                (lesson.getDateAndTime().getMinutes())));
                bundle.putLong("lessonId", lesson.getId());
                bundle.putLong("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putLong("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putLong("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putLong("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putLong("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putLong("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            studentPerformanceViewModel.getSeminarsByModules().observe(getViewLifecycleOwner(),
                    seminarsByModules -> {
                        if (seminarsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                seminarsByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                        if (moduleExpand != -1 && openPage == position)
                            expandableListView.expandGroup(moduleExpand);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                studentPerformanceViewModel.getSeminarsByModules().getValue(), studentLessonsByModules);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                        if (moduleExpand != -1 && openPage == position)
                            expandableListView.expandGroup(moduleExpand);
                    });
        }

        return root;
    }
}