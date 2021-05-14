package com.example.electronicdiary.group.actions.student_lesson;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class StudentLessonDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_lesson, null);

        boolean isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");
        boolean isLecture = true;
        if (!isFromGroupPerformance)
            isLecture = getArguments().getBoolean("isLecture");
        boolean isHasData = getArguments().getBoolean("isHasData");
        long lessonId = getArguments().getLong("lessonId");
        String lessonDate = getArguments().getString("lessonDate");
        long studentLessonId = getArguments().getLong("studentLessonId");
        long studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");
        long subjectInfoId = getArguments().getLong("subjectInfoId");

        StudentLessonViewModel studentLessonViewModel = new ViewModelProvider(this).get(StudentLessonViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentLessonIsAttended);
        EditText bonusPoints = root.findViewById(R.id.studentLessonBonusPoints);

        if (isHasData) {
            studentLessonViewModel.downloadStudentLessonById(studentLessonId);
            studentLessonViewModel.getStudentLesson().observe(this, studentLesson -> {
                if (studentLesson != null) {
                    isAttended.setChecked(studentLesson.isAttended());
                    if (studentLesson.getBonusPoints() != -1)
                        bonusPoints.setText(String.valueOf(studentLesson.getBonusPoints()));
                }
            });
        } else {
            bonusPoints.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        boolean finalIsLecture = isLecture;
        builder.setView(root)
                .setTitle("Посещение " + lessonDate)
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    studentLessonViewModel.downloadLessonById(lessonId);
                    studentLessonViewModel.downloadStudentPerformanceInModules(studentPerformanceInSubjectId);

                    LiveData<Lesson> lessonLiveData = studentLessonViewModel.getLesson();
                    LiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModulesLiveData =
                            Transformations.switchMap(lessonLiveData, g -> studentLessonViewModel.getStudentPerformanceInModules());

                    studentPerformanceInModulesLiveData.observe(getViewLifecycleOwner(), studentPerformanceInModule -> {
                        if (studentPerformanceInModule != null) {
                            Lesson lesson = studentLessonViewModel.getLesson().getValue();
                            if (isHasData)
                                studentLessonViewModel.editStudentLesson(studentLessonId, studentLessonViewModel.getStudentLesson().getValue().getStudentPerformanceInModule(),
                                        studentLessonViewModel.getStudentLesson().getValue().getLesson(), isAttended.isChecked(),
                                        Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()));
                            else
                                studentLessonViewModel.addStudentLesson(studentPerformanceInModule.get(String.valueOf(lesson.getModule().getModuleNumber())),
                                        lesson, isAttended.isChecked());

                            if (isFromGroupPerformance) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", lesson.getModule().getModuleNumber() - 1);
                                bundle.putLong("subjectInfoId", subjectInfoId);

                                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", finalIsLecture ? 1 : 2);
                                bundle.putInt("moduleExpand", lesson.getModule().getModuleNumber() - 1);
                                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInModule.get(String.valueOf(lesson.getModule().getModuleNumber()))
                                        .getStudentPerformanceInSubject().getId());

                                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_student_performance, bundle);
                            }
                        }
                    });
                });
        if (isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentLessonViewModel.deleteStudentLesson(studentLessonId);

                int moduleNumber = studentLessonViewModel.getStudentLesson().getValue().getLesson().getModule().getModuleNumber();

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
            });
        }

        dialog = builder.create();
        dialog.setOnShowListener(dialog -> {
            if (isHasData)
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }
}
