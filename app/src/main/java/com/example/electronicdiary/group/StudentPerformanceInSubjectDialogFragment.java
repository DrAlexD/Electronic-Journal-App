package com.example.electronicdiary.group;

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

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class StudentPerformanceInSubjectDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_performance_in_subject, null);

        int studentId = getArguments().getInt("studentId");
        int groupId = getArguments().getInt("groupId");
        int subjectId = getArguments().getInt("subjectId");
        int lecturerId = getArguments().getInt("lecturerId");
        int seminarianId = getArguments().getInt("seminarianId");
        int semesterId = getArguments().getInt("semesterId");

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

            isHaveCreditOrAdmission.setHint(subjectInfo.isExam() ? "Допуск к экзамену" : "Зачет");
            examEarnedPoints.setVisibility(subjectInfo.isExam() ? View.VISIBLE : View.GONE);
            mark.setVisibility(subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit() ? View.VISIBLE : View.GONE);

            studentPerformanceViewModel.getStudentPerformanceInSubject().observe(this, studentPerformanceInSubject -> {
                if (studentPerformanceInSubject == null) {
                    return;
                }

                earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                if (subjectInfo.isExam()) {
                    examEarnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
                    mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }

                if (subjectInfo.isDifferentiatedCredit()) {
                    mark.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }
            });

            TextWatcher afterTextChangedListener = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (subjectInfo.isExam()) {
                        studentPerformanceViewModel.studentPerformanceDataChanged(earnedPoints.getText().toString(),
                                bonusPoints.getText().toString(), examEarnedPoints.getText().toString(), mark.getText().toString());
                    } else if (subjectInfo.isDifferentiatedCredit()) {
                        studentPerformanceViewModel.studentPerformanceDataChanged(earnedPoints.getText().toString(),
                                bonusPoints.getText().toString(), mark.getText().toString());
                    } else {
                        studentPerformanceViewModel.studentPerformanceDataChanged(earnedPoints.getText().toString(),
                                bonusPoints.getText().toString());
                    }
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

            earnedPoints.addTextChangedListener(afterTextChangedListener);
            bonusPoints.addTextChangedListener(afterTextChangedListener);
            examEarnedPoints.addTextChangedListener(afterTextChangedListener);
            mark.addTextChangedListener(afterTextChangedListener);

            studentPerformanceViewModel.getStudentPerformanceInSubjectFormState().observe(this, studentPerformanceFormState -> {
                if (studentPerformanceFormState == null) {
                    return;
                }

                earnedPoints.setError(studentPerformanceFormState.getEarnedPointsError() != null ?
                        getString(studentPerformanceFormState.getEarnedPointsError()) : null);
                bonusPoints.setError(studentPerformanceFormState.getBonusPointsError() != null ?
                        getString(studentPerformanceFormState.getBonusPointsError()) : null);
                if (subjectInfo.isExam()) {
                    examEarnedPoints.setError(studentPerformanceFormState.getExamEarnedPointsError() != null ?
                            getString(studentPerformanceFormState.getExamEarnedPointsError()) : null);
                }
                if (subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit()) {
                    mark.setError(studentPerformanceFormState.getMarkError() != null ?
                            getString(studentPerformanceFormState.getMarkError()) : null);
                }

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(studentPerformanceFormState.isDataValid());
            });
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить успеваемость по предмету")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    if (studentPerformanceViewModel.getSubjectInfo().getValue().isExam()) {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString()), Integer.parseInt(bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked(), Integer.parseInt(examEarnedPoints.getText().toString()),
                                Integer.parseInt(mark.getText().toString()));
                    } else if (studentPerformanceViewModel.getSubjectInfo().getValue().isDifferentiatedCredit()) {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString()), Integer.parseInt(bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked(), Integer.parseInt(mark.getText().toString()));
                    } else {
                        studentPerformanceViewModel.editStudentPerformance(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                                Integer.parseInt(earnedPoints.getText().toString()), Integer.parseInt(bonusPoints.getText().toString()),
                                isHaveCreditOrAdmission.isChecked());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("groupId", groupId);
                    bundle.putInt("subjectId", subjectId);
                    bundle.putInt("lecturerId", lecturerId);
                    bundle.putInt("seminarianId", seminarianId);
                    bundle.putInt("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_performance_in_subject_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true));

        return dialog;
    }
}
