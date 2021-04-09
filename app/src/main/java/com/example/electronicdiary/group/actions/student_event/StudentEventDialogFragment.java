package com.example.electronicdiary.group.actions.student_event;

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

import java.util.Date;

public class StudentEventDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_event, null);

        boolean isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");
        int attemptNumber = getArguments().getInt("attemptNumber");
        int eventId = getArguments().getInt("eventId");
        int eventMinPoints = getArguments().getInt("eventMinPoints");
        String eventDeadlineDateString = getArguments().getString("eventDeadlineDate");
        String[] splitedEventDeadlineDate = eventDeadlineDateString.split("\\.");
        Date eventDeadlineDate = new Date(Integer.parseInt(splitedEventDeadlineDate[2]), Integer.parseInt(splitedEventDeadlineDate[1]) - 1,
                Integer.parseInt(splitedEventDeadlineDate[0]));
        String eventTitle = getArguments().getString("eventTitle");
        int studentId = getArguments().getInt("studentId");
        int moduleNumber = getArguments().getInt("moduleNumber");
        int groupId = getArguments().getInt("groupId");
        int subjectId = getArguments().getInt("subjectId");
        int lecturerId = getArguments().getInt("lecturerId");
        int seminarianId = getArguments().getInt("seminarianId");
        int semesterId = getArguments().getInt("semesterId");

        boolean isHasData = attemptNumber != 0;
        StudentEventViewModel studentEventViewModel = new ViewModelProvider(this).get(StudentEventViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentEventIsAttended);
        EditText variantNumber = root.findViewById(R.id.studentEventVariantNumber);
        EditText finishDate = root.findViewById(R.id.studentEventFinishDate);
        EditText earnedPoints = root.findViewById(R.id.studentEventEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentEventBonusPoints);
        CheckBox isHaveCredit = root.findViewById(R.id.studentEventIsHaveCredit);

        if (isHasData) {
            studentEventViewModel.downloadStudentEventById(attemptNumber, eventId, studentId);
            studentEventViewModel.getStudentEvent().observe(this, studentEvent -> {
                if (studentEvent == null) {
                    return;
                }

                isAttended.setChecked(studentEvent.isAttended());
                variantNumber.setText(String.valueOf(studentEvent.getVariantNumber()));

                if (studentEvent.getFinishDate() != null) {
                    finishDate.setText(((studentEvent.getFinishDate().getDate() + 1) < 10 ? "0" + (studentEvent.getFinishDate().getDate() + 1) :
                            (studentEvent.getFinishDate().getDate() + 1)) + "." +
                            ((studentEvent.getFinishDate().getMonth() + 1) < 10 ? "0" + (studentEvent.getFinishDate().getMonth() + 1) :
                                    (studentEvent.getFinishDate().getMonth() + 1)) + "." + studentEvent.getFinishDate().getYear());
                    finishDate.setTextColor(getResources().getColor(studentEvent.getFinishDate().after(eventDeadlineDate) ?
                            R.color.red : R.color.green));
                }

                if (studentEvent.getEarnedPoints() != -1) {
                    earnedPoints.setText(String.valueOf(studentEvent.getEarnedPoints()));
                    if (studentEvent.getEarnedPoints() < eventMinPoints) {
                        earnedPoints.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        earnedPoints.setTextColor(getResources().getColor(R.color.green));
                    }
                }

                if (studentEvent.getBonusPoints() != -1) {
                    bonusPoints.setText(String.valueOf(studentEvent.getBonusPoints()));
                    if (studentEvent.getBonusPoints() < eventMinPoints) {
                        bonusPoints.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        bonusPoints.setTextColor(getResources().getColor(R.color.green));
                    }
                }

                if (studentEvent.getEarnedPoints() != -1 && studentEvent.getBonusPoints() != -1) {
                    if (studentEvent.getEarnedPoints() + studentEvent.getBonusPoints() < eventMinPoints) {
                        earnedPoints.setTextColor(getResources().getColor(R.color.red));
                        bonusPoints.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        earnedPoints.setTextColor(getResources().getColor(R.color.green));
                        bonusPoints.setTextColor(getResources().getColor(R.color.green));
                    }
                }

                isHaveCredit.setChecked(studentEvent.isHaveCredit());
            });
        } else {
            finishDate.setVisibility(View.GONE);
            earnedPoints.setVisibility(View.GONE);
            bonusPoints.setVisibility(View.GONE);
            isHaveCredit.setVisibility(View.GONE);
        }

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentEventViewModel.eventPerformanceDataChanged(variantNumber.getText().toString());
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
        variantNumber.addTextChangedListener(afterTextChangedListener);

        studentEventViewModel.getStudentEventFormState().observe(this, studentEventFormState -> {
            if (studentEventFormState == null) {
                return;
            }

            variantNumber.setError(studentEventFormState.getVariantNumberError() != null ?
                    getString(studentEventFormState.getVariantNumberError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(studentEventFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(root)
                .setTitle("Сдача " + eventTitle)
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    Date date;
                    if (finishDate.getText().toString().isEmpty()) {
                        date = null;
                    } else {
                        String[] splitedFinishDate = finishDate.getText().toString().split("\\.");
                        date = new Date(Integer.parseInt(splitedFinishDate[2]), Integer.parseInt(splitedFinishDate[1]) - 1,
                                Integer.parseInt(splitedFinishDate[0]));
                    }
                    if (isHasData)
                        studentEventViewModel.editStudentEvent(attemptNumber, eventId, studentId, isAttended.isChecked(),
                                Integer.parseInt(variantNumber.getText().toString()), date,
                                Integer.parseInt(earnedPoints.getText().toString().isEmpty() ? "-1" : earnedPoints.getText().toString()),
                                Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()),
                                isHaveCredit.isChecked());
                    else
                        studentEventViewModel.addStudentEvent(attemptNumber, eventId, studentId,
                                moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId, isAttended.isChecked(),
                                Integer.parseInt(variantNumber.getText().toString()));

                    if (isFromGroupPerformance) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("groupId", groupId);
                        bundle.putInt("subjectId", subjectId);
                        bundle.putInt("lecturerId", lecturerId);
                        bundle.putInt("seminarianId", seminarianId);
                        bundle.putInt("semesterId", semesterId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("openPage", 0);
                        bundle.putInt("moduleExpand", moduleNumber - 1);
                        bundle.putInt("studentId", studentId);
                        bundle.putInt("subjectId", subjectId);
                        bundle.putInt("semesterId", semesterId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_student_performance, bundle);
                    }
                });
        if (isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentEventViewModel.deleteStudentEvent(attemptNumber, eventId, studentId);

                if (isFromGroupPerformance) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("groupId", groupId);
                    bundle.putInt("subjectId", subjectId);
                    bundle.putInt("lecturerId", lecturerId);
                    bundle.putInt("seminarianId", seminarianId);
                    bundle.putInt("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 0);
                    bundle.putInt("moduleExpand", moduleNumber - 1);
                    bundle.putInt("studentId", studentId);
                    bundle.putInt("subjectId", subjectId);
                    bundle.putInt("semesterId", semesterId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_student_performance, bundle);
                }
            });
        }

        dialog = builder.create();
        dialog.setOnShowListener(dialog -> {
            if (isHasData)
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isHasData);
        });

        return dialog;
    }
}
