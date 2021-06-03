package com.example.electronic_journal.group.actions.student_performance;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;

import org.jetbrains.annotations.NotNull;

public class StudentPerformanceInSubjectDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private long studentPerformanceInSubjectId;
    private boolean isFromGroupPerformance;
    private StudentPerformanceInSubjectViewModel studentPerformanceViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_performance_in_subject, null);

        studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");
        isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");

        studentPerformanceViewModel =
                new ViewModelProvider(this).get(StudentPerformanceInSubjectViewModel.class);
        studentPerformanceViewModel.downloadStudentPerformanceInSubject(studentPerformanceInSubjectId);

        EditText earnedPoints = root.findViewById(R.id.studentPerformanceInSubjectEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentPerformanceInSubjectBonusPoints);
        CheckBox isHaveCreditOrAdmission = root.findViewById(R.id.studentPerformanceInSubjectIsHaveCreditOrAdmission);
        EditText examEarnedPoints = root.findViewById(R.id.studentPerformanceInSubjectExamEarnedPoints);
        EditText mark = root.findViewById(R.id.studentPerformanceInSubjectMark);

        studentPerformanceViewModel.getStudentPerformanceInSubject().observe(this, studentPerformanceInSubject -> {
            if (studentPerformanceInSubject != null) {
                isHaveCreditOrAdmission.setText(studentPerformanceInSubject.getSubjectInfo().isExam() ? "Допуск к экзамену" : "Зачет");
                examEarnedPoints.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ? View.VISIBLE : View.GONE);
                mark.setVisibility(studentPerformanceInSubject.getSubjectInfo().isExam() ||
                        studentPerformanceInSubject.getSubjectInfo().isDifferentiatedCredit() ? View.VISIBLE : View.GONE);

                if (studentPerformanceInSubject.getEarnedPoints() != null)
                    earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                if (studentPerformanceInSubject.getBonusPoints() != null)
                    bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null)
                    isHaveCreditOrAdmission.setChecked(studentPerformanceInSubject.isHaveCreditOrAdmission());

                if (studentPerformanceInSubject.getSubjectInfo().isExam()) {
                    if (studentPerformanceInSubject.getEarnedExamPoints() != null)
                        examEarnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
                    if (studentPerformanceInSubject.getMark() != null)
                        mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }

                if (studentPerformanceInSubject.getSubjectInfo().isDifferentiatedCredit()) {
                    if (studentPerformanceInSubject.getMark() != null)
                        mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean isExamEarnedPointsValid = true;
                if (!examEarnedPoints.getText().toString().isEmpty()) {
                    isExamEarnedPointsValid = Integer.parseInt(examEarnedPoints.getText().toString()) <= 30;
                    if (!isExamEarnedPointsValid)
                        examEarnedPoints.setError(getString(R.string.invalid_max_exam_points_field));
                    else
                        examEarnedPoints.setError(null);
                }

                boolean isMarkValid = true;
                if (!mark.getText().toString().isEmpty()) {
                    isMarkValid = Integer.parseInt(mark.getText().toString()) >= 2 && Integer.parseInt(mark.getText().toString()) <= 5;
                    if (!isMarkValid)
                        mark.setError(getString(R.string.invalid_mark_field));
                    else
                        mark.setError(null);
                }

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isExamEarnedPointsValid && isMarkValid);
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
        examEarnedPoints.addTextChangedListener(afterTextChangedListener);
        mark.addTextChangedListener(afterTextChangedListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить успеваемость по предмету")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText earnedPoints = root.findViewById(R.id.studentPerformanceInSubjectEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentPerformanceInSubjectBonusPoints);
        CheckBox isHaveCreditOrAdmission = root.findViewById(R.id.studentPerformanceInSubjectIsHaveCreditOrAdmission);
        EditText examEarnedPoints = root.findViewById(R.id.studentPerformanceInSubjectExamEarnedPoints);
        EditText mark = root.findViewById(R.id.studentPerformanceInSubjectMark);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            StudentPerformanceInSubject studentPerformanceInSubject = studentPerformanceViewModel.getStudentPerformanceInSubject().getValue();

            if (studentPerformanceInSubject.getSubjectInfo().isExam()) {
                studentPerformanceViewModel.editStudentPerformance(studentPerformanceInSubjectId, studentPerformanceInSubject.getSubjectInfo(),
                        studentPerformanceInSubject.getStudent(), earnedPoints.getText().toString().isEmpty() ? null : Integer.parseInt(earnedPoints.getText().toString()),
                        bonusPoints.getText().toString().isEmpty() ? null : Integer.parseInt(bonusPoints.getText().toString()),
                        isHaveCreditOrAdmission.isChecked(), examEarnedPoints.getText().toString().isEmpty() ? null : Integer.parseInt(examEarnedPoints.getText().toString()),
                        mark.getText().toString().isEmpty() ? null : Integer.parseInt(mark.getText().toString()));
            } else if (studentPerformanceInSubject.getSubjectInfo().isDifferentiatedCredit()) {
                studentPerformanceViewModel.editStudentPerformance(studentPerformanceInSubjectId, studentPerformanceInSubject.getSubjectInfo(),
                        studentPerformanceInSubject.getStudent(), earnedPoints.getText().toString().isEmpty() ? null : Integer.parseInt(earnedPoints.getText().toString()),
                        bonusPoints.getText().toString().isEmpty() ? null : Integer.parseInt(bonusPoints.getText().toString()),
                        isHaveCreditOrAdmission.isChecked(),
                        mark.getText().toString().isEmpty() ? null : Integer.parseInt(mark.getText().toString()));
            } else {
                studentPerformanceViewModel.editStudentPerformance(studentPerformanceInSubjectId, studentPerformanceInSubject.getSubjectInfo(),
                        studentPerformanceInSubject.getStudent(), earnedPoints.getText().toString().isEmpty() ? null : Integer.parseInt(earnedPoints.getText().toString()),
                        bonusPoints.getText().toString().isEmpty() ? null : Integer.parseInt(bonusPoints.getText().toString()),
                        isHaveCreditOrAdmission.isChecked());
            }

            studentPerformanceViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    if (isFromGroupPerformance) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("subjectInfoId", studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getId());
                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_performance_in_subject_to_group_performance_events, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", 0);
                        bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInSubjectId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_performance_in_subject_to_student_performance, bundle);
                    }
                }
            });
        });
    }
}
