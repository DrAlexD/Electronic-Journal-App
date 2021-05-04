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

import org.jetbrains.annotations.NotNull;

public class StudentPerformanceInSubjectDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_performance_in_subject, null);

        long studentId = getArguments().getLong("studentId");
        long groupId = getArguments().getLong("groupId");
        long subjectId = getArguments().getLong("subjectId");
        long lecturerId = getArguments().getLong("lecturerId");
        long seminarianId = getArguments().getLong("seminarianId");
        long semesterId = getArguments().getLong("semesterId");

        StudentPerformanceInSubjectViewModel studentPerformanceViewModel =
                new ViewModelProvider(this).get(StudentPerformanceInSubjectViewModel.class);
        studentPerformanceViewModel.downloadSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId);

        EditText earnedPoints = root.findViewById(R.id.studentPerformanceInSubjectEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentPerformanceInSubjectBonusPoints);
        CheckBox isHaveCreditOrAdmission = root.findViewById(R.id.studentPerformanceInSubjectIsHaveCreditOrAdmission);
        EditText examEarnedPoints = root.findViewById(R.id.studentPerformanceInSubjectExamEarnedPoints);
        EditText mark = root.findViewById(R.id.studentPerformanceInSubjectMark);
        studentPerformanceViewModel.getSubjectInfo().observe(this, subjectInfo -> {
            if (subjectInfo == null) {
                return;
            }

            studentPerformanceViewModel.downloadStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId,
                    seminarianId, semesterId);

            isHaveCreditOrAdmission.setText(subjectInfo.isExam() ? "Допуск к экзамену" : "Зачет");
            examEarnedPoints.setVisibility(subjectInfo.isExam() ? View.VISIBLE : View.GONE);
            mark.setVisibility(subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit() ? View.VISIBLE : View.GONE);

            studentPerformanceViewModel.getStudentPerformanceInSubject().observe(this, studentPerformanceInSubject -> {
                if (studentPerformanceInSubject == null) {
                    return;
                }

                if (studentPerformanceInSubject.getEarnedPoints() != -1)
                    earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                if (studentPerformanceInSubject.getBonusPoints() != -1)
                    bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                if (subjectInfo.isExam()) {
                    if (studentPerformanceInSubject.getEarnedExamPoints() != -1)
                        examEarnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
                    if (studentPerformanceInSubject.getMark() != -1)
                        mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }

                if (subjectInfo.isDifferentiatedCredit()) {
                    if (studentPerformanceInSubject.getMark() != -1)
                        mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }
            });
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить успеваемость по предмету")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    if (studentPerformanceViewModel.getSubjectInfo().getValue().isExam()) {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString().isEmpty() ? "-1" : earnedPoints.getText().toString()),
                                Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked(), Integer.parseInt(examEarnedPoints.getText().toString().isEmpty() ? "-1" :
                                        examEarnedPoints.getText().toString()),
                                Integer.parseInt(mark.getText().toString().isEmpty() ? "-1" : mark.getText().toString()));
                    } else if (studentPerformanceViewModel.getSubjectInfo().getValue().isDifferentiatedCredit()) {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString().isEmpty() ? "-1" : earnedPoints.getText().toString()),
                                Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked(),
                                Integer.parseInt(mark.getText().toString().isEmpty() ? "-1" : mark.getText().toString()));
                    } else {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString().isEmpty() ? "-1" : earnedPoints.getText().toString()),
                                Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putLong("groupId", groupId);
                    bundle.putLong("subjectId", subjectId);
                    bundle.putLong("lecturerId", lecturerId);
                    bundle.putLong("seminarianId", seminarianId);
                    bundle.putLong("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_performance_in_subject_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }
}
