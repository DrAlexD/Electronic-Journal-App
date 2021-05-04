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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

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
        long studentId = getArguments().getLong("studentId");
        int moduleNumber = getArguments().getInt("moduleNumber");
        long groupId = getArguments().getLong("groupId");
        long subjectId = getArguments().getLong("subjectId");
        long lecturerId = getArguments().getLong("lecturerId");
        long seminarianId = getArguments().getLong("seminarianId");
        long semesterId = getArguments().getLong("semesterId");

        StudentLessonViewModel studentLessonViewModel = new ViewModelProvider(this).get(StudentLessonViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentLessonIsAttended);
        EditText bonusPoints = root.findViewById(R.id.studentLessonBonusPoints);

        if (isHasData) {
            studentLessonViewModel.downloadStudentLessonById(lessonId, studentId);
            studentLessonViewModel.getStudentLesson().observe(this, studentLesson -> {
                if (studentLesson == null) {
                    return;
                }

                isAttended.setChecked(studentLesson.isAttended());
                if (studentLesson.getBonusPoints() != -1)
                    bonusPoints.setText(String.valueOf(studentLesson.getBonusPoints()));
            });
        } else {
            bonusPoints.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        boolean finalIsLecture = isLecture;
        builder.setView(root)
                .setTitle("Посещение " + lessonDate)
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    if (isHasData)
                        studentLessonViewModel.editStudentLesson(lessonId, studentId, isAttended.isChecked(),
                                Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()));
                    else
                        studentLessonViewModel.addStudentLesson(lessonId, moduleNumber, studentId,
                                groupId, subjectId, lecturerId, seminarianId, semesterId, isAttended.isChecked());

                    if (isFromGroupPerformance) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", moduleNumber - 1);
                        bundle.putLong("groupId", groupId);
                        bundle.putLong("subjectId", subjectId);
                        bundle.putLong("lecturerId", lecturerId);
                        bundle.putLong("seminarianId", seminarianId);
                        bundle.putLong("semesterId", semesterId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", finalIsLecture ? 1 : 2);
                        bundle.putInt("moduleExpand", moduleNumber - 1);
                        bundle.putLong("studentId", studentId);
                        bundle.putLong("subjectId", subjectId);
                        bundle.putLong("semesterId", semesterId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_student_performance, bundle);
                    }
                });
        if (isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentLessonViewModel.deleteStudentLesson(lessonId, studentId);

                if (isFromGroupPerformance) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", moduleNumber - 1);
                    bundle.putLong("groupId", groupId);
                    bundle.putLong("subjectId", subjectId);
                    bundle.putLong("lecturerId", lecturerId);
                    bundle.putLong("seminarianId", seminarianId);
                    bundle.putLong("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", finalIsLecture ? 1 : 2);
                    bundle.putInt("moduleExpand", moduleNumber - 1);
                    bundle.putLong("studentId", studentId);
                    bundle.putLong("subjectId", subjectId);
                    bundle.putLong("semesterId", semesterId);

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
