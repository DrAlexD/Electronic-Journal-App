package com.example.electronic_journal.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;

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

        final ExpandableListView expandableListView = root.findViewById(R.id.events_or_lessons_list);
        if (position == 1) {
            LinearLayout studentSubjectPerformanceLayout = root.findViewById(R.id.student_subject_performance);
            studentSubjectPerformanceLayout.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubject.getId());
                bundle.putBoolean("isFromEventsPerformance", false);
                Navigation.findNavController(view).navigate(R.id.action_student_performance_to_dialog_student_performance_in_subject, bundle);
            });
            studentSubjectPerformanceLayout.setVisibility(View.VISIBLE);

            //TextView studentPerformanceInSubjectText = root.findViewById(R.id.studentPerformanceInSubjectText);
            //studentPerformanceInSubjectText.setText("Успеваемость студента " + studentPerformanceInSubject.getStudent().getFullName() + " в предмете");
            TextView earnedPoints = root.findViewById(R.id.earnedPoints);
            TextView bonusPoints = root.findViewById(R.id.bonusPoints);
            TextView mark = root.findViewById(R.id.mark);
            TextView earnedExamPoints = root.findViewById(R.id.earnedExamPoints);

            if (studentPerformanceInSubject.getEarnedPoints() != null)
                earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
            else
                earnedPoints.setText("-");
            if (studentPerformanceInSubject.getBonusPoints() != null)
                bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
            else
                bonusPoints.setText("-");

            if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null) {
                earnedPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.green : R.color.red));
                bonusPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.green : R.color.red));
            }
            mark.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ||
                    studentPerformanceInSubject.getSubjectInfo().isDifferentiatedCredit() ? View.VISIBLE : View.INVISIBLE);
            if (studentPerformanceInSubject.getMark() != null) {
                mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                if (studentPerformanceInSubject.getMark() == 2) {
                    mark.setTextColor(getResources().getColor(R.color.red));
                } else if (studentPerformanceInSubject.getMark() == 3) {
                    mark.setTextColor(getResources().getColor(R.color.yellow));
                } else if (studentPerformanceInSubject.getMark() == 4) {
                    mark.setTextColor(getResources().getColor(R.color.light_green));
                } else if (studentPerformanceInSubject.getMark() == 5) {
                    mark.setTextColor(getResources().getColor(R.color.green));
                }
            } else
                mark.setText("-");

            earnedExamPoints.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ? View.VISIBLE : View.INVISIBLE);
            if (studentPerformanceInSubject.getEarnedExamPoints() != null) {
                earnedExamPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
                earnedExamPoints.setTextColor(studentPerformanceInSubject.getEarnedExamPoints() >= 18 ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            } else {
                earnedExamPoints.setText("-");
            }

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

                int positionInList = 1;
                long studentId = studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getStudent().getId();
                for (Student s : studentPerformanceViewModel.getStudentsInGroup().getValue()) {
                    if (s.getId() == studentId) {
                        break;
                    }
                    positionInList++;
                }

                int numberOfStudents = studentPerformanceViewModel.getStudentsInGroup().getValue().size();
                if (event.getNumberOfVariants() > numberOfStudents)
                    bundle.putInt("variantNumber", positionInList);
                else {
                    if (positionInList % event.getNumberOfVariants() != 0)
                        bundle.putInt("variantNumber", positionInList % event.getNumberOfVariants());
                    else
                        bundle.putInt("variantNumber", event.getNumberOfVariants());
                }
                bundle.putInt("eventMaxPoints", event.getMaxPoints());
                bundle.putInt("eventType", event.getTypeNumber());
                bundle.putBoolean("isHasData", finalLastAttempt != 0);
                bundle.putInt("attemptNumber", finalLastAttempt);
                bundle.putLong("eventId", event.getId());
                if (finalStudentEventId != null)
                    bundle.putLong("studentEventId", finalStudentEventId);
                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubject.getId());
                bundle.putLong("subjectInfoId", studentPerformanceInSubject.getSubjectInfo().getId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_event, bundle);
                return true;
            };

            EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modulesNumbers,
                    modules,
                    studentPerformanceInModules,
                    events, studentEvents);
            expandableListView.setAdapter(eventsAdapter);
            expandableListView.setOnChildClickListener(onEventClickListener);
            if (moduleExpand != -1 && openPage == position)
                expandableListView.expandGroup(moduleExpand);
        } else if (position == 2) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            long userId = sharedPreferences.getLong("userId", -1);
            String role = sharedPreferences.getString("userRole", "");
            boolean isProfessor = role.equals("ROLE_ADMIN") || role.equals("ROLE_PROFESSOR");

            ExpandableListView.OnChildClickListener onLectureClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Lesson lesson = lectures.
                        get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

                boolean isHasData = false;
                StudentLesson studentLessonChosen = null;
                if (studentLessons != null && studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition))) != null) {
                    for (StudentLesson studentLesson : studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition)))) {
                        if (studentLesson.getLesson().getId() == lesson.getId()) {
                            studentLessonChosen = studentLesson;
                            isHasData = true;
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

                if (isProfessor) {
                    int professorType = 3;
                    if (studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() != null &&
                            studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor() != null) {
                        if (userId != studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() ||
                                userId != studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                            if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId()) {
                                professorType = 2;
                            } else if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                                professorType = 1;
                            }
                        }
                    } else if (studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() != null) {
                        if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId()) {
                            professorType = 2;
                        }
                    } else {
                        if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                            professorType = 1;
                        }
                    }
                    bundle.putInt("professorType", professorType);
                } else {
                    bundle.putInt("professorType", 0);
                }

                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", true);
                bundle.putBoolean("isHasData", isHasData);
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            long userId = sharedPreferences.getLong("userId", -1);
            String role = sharedPreferences.getString("userRole", "");
            boolean isProfessor = role.equals("ROLE_ADMIN") || role.equals("ROLE_PROFESSOR");

            ExpandableListView.OnChildClickListener onSeminarClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Lesson lesson = seminars.
                        get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

                boolean isHasData = false;
                StudentLesson studentLessonChosen = null;
                if (studentLessons != null && studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition))) != null) {
                    for (StudentLesson studentLesson : studentLessons.get(String.valueOf(modulesNumbers.get(groupPosition)))) {
                        if (studentLesson.getLesson().getId() == lesson.getId()) {
                            studentLessonChosen = studentLesson;
                            isHasData = true;
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

                if (isProfessor) {
                    int professorType = 3;
                    if (studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() != null &&
                            studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor() != null) {
                        if (userId != studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() ||
                                userId != studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                            if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId()) {
                                professorType = 2;
                            } else if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                                professorType = 1;
                            }
                        }
                    } else if (studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId() != null) {
                        if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getLecturerId()) {
                            professorType = 2;
                        }
                    } else {
                        if (userId == studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getSeminarsProfessor().getId()) {
                            professorType = 1;
                        }
                    }

                    bundle.putInt("professorType", professorType);
                } else {
                    bundle.putInt("professorType", 0);
                }

                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", false);
                bundle.putBoolean("isHasData", isHasData);
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