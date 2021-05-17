package com.example.electronicdiary.group.actions.student_lesson;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StudentLessonDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private StudentLessonViewModel studentLessonViewModel;
    private View root;
    private LiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModulesLiveData;
    private boolean isHasData;
    private boolean isFromGroupPerformance;
    private long subjectInfoId;
    private boolean finalIsLecture;
    private boolean isProfessor;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_lesson, null);

        isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");
        boolean isLecture = true;
        if (!isFromGroupPerformance)
            isLecture = getArguments().getBoolean("isLecture");
        isHasData = getArguments().getBoolean("isHasData");
        long lessonId = getArguments().getLong("lessonId");
        String lessonDate = getArguments().getString("lessonDate");
        long studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");
        subjectInfoId = getArguments().getLong("subjectInfoId");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isProfessor = sharedPreferences.getBoolean("isUserProfessor", true);
        studentLessonViewModel = new ViewModelProvider(this).get(StudentLessonViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentLessonIsAttended);
        EditText bonusPoints = root.findViewById(R.id.studentLessonBonusPoints);
        isAttended.setEnabled(isProfessor);
        bonusPoints.setEnabled(isProfessor);

        if (isHasData) {
            studentLessonViewModel.downloadStudentLessonById(getArguments().getLong("studentLessonId"));
            if (isProfessor) {
                studentLessonViewModel.downloadLessonById(lessonId);
                studentLessonViewModel.downloadStudentPerformanceInModules(studentPerformanceInSubjectId);

                LiveData<StudentLesson> studentLessonLiveData = studentLessonViewModel.getStudentLesson();
                LiveData<Lesson> lessonLiveData = Transformations.switchMap(studentLessonLiveData, studentLesson -> {
                    isAttended.setChecked(studentLesson.isAttended());
                    if (studentLesson.getBonusPoints() != null)
                        bonusPoints.setText(String.valueOf(studentLesson.getBonusPoints()));

                    return studentLessonViewModel.getLesson();
                });

                studentPerformanceInModulesLiveData =
                        Transformations.switchMap(lessonLiveData, g -> studentLessonViewModel.getStudentPerformanceInModules());
            } else {
                studentLessonViewModel.getStudentLesson().observe(this, studentLesson -> {
                    if (studentLesson != null) {
                        isAttended.setChecked(studentLesson.isAttended());
                        if (studentLesson.getBonusPoints() != null)
                            bonusPoints.setText(String.valueOf(studentLesson.getBonusPoints()));
                    }
                });
            }
        } else {
            bonusPoints.setVisibility(View.GONE);

            if (isProfessor) {
                studentLessonViewModel.downloadLessonById(lessonId);
                studentLessonViewModel.downloadStudentPerformanceInModules(studentPerformanceInSubjectId);

                LiveData<Lesson> lessonLiveData = studentLessonViewModel.getLesson();
                studentPerformanceInModulesLiveData =
                        Transformations.switchMap(lessonLiveData, g -> studentLessonViewModel.getStudentPerformanceInModules());
            }
        }

        finalIsLecture = isLecture;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(root)
                .setTitle("Посещение " + lessonDate);
        if (isProfessor)
            builder.setPositiveButton("Подтвердить", null);
        else
            builder.setPositiveButton("Подтвердить", (dialog, id) -> dismiss());

        if (isProfessor && isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> dismiss())
                    .setNeutralButton("Удалить", null);
        }

        dialog = builder.create();
        dialog.setOnShowListener(dialog -> {
            if (isHasData)
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        CheckBox isAttended = root.findViewById(R.id.studentLessonIsAttended);
        EditText bonusPoints = root.findViewById(R.id.studentLessonBonusPoints);

        if (isProfessor) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                LiveData<Boolean> answerLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                        studentPerformanceInModule -> {
                            Lesson lesson = studentLessonViewModel.getLesson().getValue();
                            if (isHasData)
                                studentLessonViewModel.editStudentLesson(getArguments().getLong("studentLessonId"), studentLessonViewModel.getStudentLesson().getValue().getStudentPerformanceInModule(),
                                        studentLessonViewModel.getStudentLesson().getValue().getLesson(), isAttended.isChecked(),
                                        bonusPoints.getText().toString().isEmpty() ? null : Integer.parseInt(bonusPoints.getText().toString()));
                            else
                                studentLessonViewModel.addStudentLesson(studentPerformanceInModule.get(String.valueOf(lesson.getModule().getModuleNumber())),
                                        lesson, isAttended.isChecked());

                            return studentLessonViewModel.getAnswer();
                        });

                answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        int moduleNumber = studentLessonViewModel.getLesson().getValue().getModule().getModuleNumber();
                        if (isFromGroupPerformance) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", moduleNumber - 1);
                            bundle.putLong("subjectInfoId", subjectInfoId);

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", finalIsLecture ? 1 : 2);
                            bundle.putInt("moduleExpand", moduleNumber - 1);
                            bundle.putLong("studentPerformanceInSubjectId", studentLessonViewModel.getStudentPerformanceInModules()
                                    .getValue().get(String.valueOf(moduleNumber))
                                    .getStudentPerformanceInSubject().getId());

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_student_performance, bundle);
                        }
                    }
                });
            });
        }

        if (isProfessor && isHasData) {
            dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(view -> {
                LiveData<Boolean> answerLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                        studentPerformanceInModule -> {
                            studentLessonViewModel.deleteStudentLesson(getArguments().getLong("studentLessonId"));
                            return studentLessonViewModel.getAnswer();
                        });

                answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        int moduleNumber = studentLessonViewModel.getLesson().getValue().getModule().getModuleNumber();

                        if (isFromGroupPerformance) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", moduleNumber - 1);
                            bundle.putLong("subjectInfoId", subjectInfoId);

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", finalIsLecture ? 1 : 2);
                            bundle.putInt("moduleExpand", moduleNumber - 1);
                            bundle.putLong("studentPerformanceInSubjectId", studentLessonViewModel.getStudentLesson().getValue()
                                    .getStudentPerformanceInModule().getStudentPerformanceInSubject().getId());


                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_student_performance, bundle);
                        }
                    }
                });
            });
        }
    }
}
