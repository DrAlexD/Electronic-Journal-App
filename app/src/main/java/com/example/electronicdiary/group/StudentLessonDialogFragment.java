package com.example.electronicdiary.group;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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

        int position = getArguments().getInt("position");
        int studentId = getArguments().getInt("studentId");
        int lessonId = getArguments().getInt("lessonId");

        boolean isAttended = getArguments().getBoolean("isAttended");
        StudentLessonViewModel studentLessonViewModel = new ViewModelProvider(this).get(StudentLessonViewModel.class);

        EditText lessonEarnedPoints = root.findViewById(R.id.studentLessonEarnedPoints);

        //int lessonId;
        if (isAttended) {
            lessonId = 1;
            studentLessonViewModel.downloadStudentLessonById(lessonId);
            studentLessonViewModel.getLesson().observe(this, lesson -> {
                if (lesson == null) {
                    return;
                }

                lessonEarnedPoints.setText(String.valueOf(lesson.getPoints()));
            });
        } else
            lessonId = -1;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentLessonViewModel.lessonPerformanceDataChanged(lessonEarnedPoints.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        };
        lessonEarnedPoints.addTextChangedListener(afterTextChangedListener);

        studentLessonViewModel.getStudentLessonFormState().observe(this, studentLessonFormState -> {
            if (studentLessonFormState == null) {
                return;
            }

            lessonEarnedPoints.setError(studentLessonFormState.getStudentLessonEarnedPointsError() != null ?
                    getString(studentLessonFormState.getStudentLessonEarnedPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(studentLessonFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(root)
                .setTitle("Посещение " + getArguments().getString("lessonDate"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    if (isAttended)
                        studentLessonViewModel.editStudentLesson(lessonEarnedPoints.getText().toString());
                    else
                        studentLessonViewModel.addStudentLesson(lessonEarnedPoints.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));
                    bundle.putInt("openPage", getArguments().getInt("position") - 1);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
                });
        if (isAttended) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentLessonViewModel.deleteStudentLesson(lessonId);

                Bundle bundle = new Bundle();
                bundle.putString("subject", getArguments().getString("subject"));
                bundle.putString("group", getArguments().getString("group"));
                bundle.putInt("openPage", getArguments().getInt("position") - 1);

                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_lesson_to_group_performance, bundle);
            });
        }

        dialog = builder.create();
        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isAttended);
        });

        return dialog;
    }
}
