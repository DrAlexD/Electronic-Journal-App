package com.example.electronicdiary.group.actions.student_performance;

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
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;

import org.jetbrains.annotations.NotNull;

public class StudentPerformanceInSubjectDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private long studentPerformanceInSubjectId;
    private StudentPerformanceInSubjectViewModel studentPerformanceViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_performance_in_subject, null);

        studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");

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
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", studentPerformanceViewModel.getStudentPerformanceInSubject().getValue().getSubjectInfo().getId());
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_performance_in_subject_to_group_performance, bundle);
                }
            });
        });
    }
}
