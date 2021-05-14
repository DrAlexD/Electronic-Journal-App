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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

public class StudentEventDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_event, null);

        boolean isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");
        int attemptNumber = getArguments().getInt("attemptNumber");
        long eventId = getArguments().getLong("eventId");
        String eventTitle = getArguments().getString("eventTitle");
        long studentEventId = getArguments().getLong("studentEventId");
        long studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");
        long subjectInfoId = getArguments().getLong("subjectInfoId");

        boolean isHasData = attemptNumber != 0;
        StudentEventViewModel studentEventViewModel = new ViewModelProvider(this).get(StudentEventViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentEventIsAttended);
        EditText variantNumber = root.findViewById(R.id.studentEventVariantNumber);
        EditText finishDate = root.findViewById(R.id.studentEventFinishDate);
        EditText earnedPoints = root.findViewById(R.id.studentEventEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentEventBonusPoints);
        CheckBox isHaveCredit = root.findViewById(R.id.studentEventIsHaveCredit);

        if (isHasData) {
            studentEventViewModel.downloadStudentEventById(studentEventId);
            studentEventViewModel.getStudentEvent().observe(this, studentEvent -> {
                if (studentEvent != null) {
                    isAttended.setChecked(studentEvent.isAttended());
                    variantNumber.setText(String.valueOf(studentEvent.getVariantNumber()));

                    if (studentEvent.getFinishDate() != null) {
                        finishDate.setText(((studentEvent.getFinishDate().getDate() + 1) < 10 ? "0" + (studentEvent.getFinishDate().getDate() + 1) :
                                (studentEvent.getFinishDate().getDate() + 1)) + "." +
                                ((studentEvent.getFinishDate().getMonth() + 1) < 10 ? "0" + (studentEvent.getFinishDate().getMonth() + 1) :
                                        (studentEvent.getFinishDate().getMonth() + 1)) + "." + studentEvent.getFinishDate().getYear());
                        finishDate.setTextColor(getResources().getColor(studentEvent.getFinishDate().after(studentEvent.getEvent().getDeadlineDate()) ?
                                R.color.red : R.color.green));
                    }

                    if (studentEvent.getEarnedPoints() != -1) {
                        earnedPoints.setText(String.valueOf(studentEvent.getEarnedPoints()));
                        if (studentEvent.getEarnedPoints() < studentEvent.getEvent().getMinPoints()) {
                            earnedPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            earnedPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    if (studentEvent.getBonusPoints() != -1) {
                        bonusPoints.setText(String.valueOf(studentEvent.getBonusPoints()));
                        if (studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                            bonusPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            bonusPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    if (studentEvent.getEarnedPoints() != -1 && studentEvent.getBonusPoints() != -1) {
                        if (studentEvent.getEarnedPoints() + studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                            earnedPoints.setTextColor(getResources().getColor(R.color.red));
                            bonusPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            earnedPoints.setTextColor(getResources().getColor(R.color.green));
                            bonusPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    isHaveCredit.setChecked(studentEvent.isHaveCredit());
                }
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
                    studentEventViewModel.downloadEventById(eventId);
                    studentEventViewModel.downloadStudentPerformanceInModule(studentPerformanceInSubjectId);
                    LiveData<Event> eventLiveData = studentEventViewModel.getEvent();
                    LiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModulesLiveData =
                            Transformations.switchMap(eventLiveData, g -> studentEventViewModel.getStudentPerformanceInModules());

                    studentPerformanceInModulesLiveData.observe(this, studentPerformanceInModule -> {
                        if (studentPerformanceInModule != null) {
                            Event event = studentEventViewModel.getEvent().getValue();
                            Date date;
                            if (finishDate.getText().toString().isEmpty()) {
                                date = null;
                            } else {
                                String[] splitedFinishDate = finishDate.getText().toString().split("\\.");
                                date = new Date(Integer.parseInt(splitedFinishDate[2]), Integer.parseInt(splitedFinishDate[1]) - 1,
                                        Integer.parseInt(splitedFinishDate[0]));
                            }
                            if (isHasData)
                                studentEventViewModel.editStudentEvent(studentEventId, attemptNumber,
                                        studentEventViewModel.getStudentEvent().getValue().getStudentPerformanceInModule(),
                                        studentEventViewModel.getStudentEvent().getValue().getEvent(), isAttended.isChecked(),
                                        Integer.parseInt(variantNumber.getText().toString()), date,
                                        Integer.parseInt(earnedPoints.getText().toString().isEmpty() ? "-1" : earnedPoints.getText().toString()),
                                        Integer.parseInt(bonusPoints.getText().toString().isEmpty() ? "-1" : bonusPoints.getText().toString()),
                                        isHaveCredit.isChecked());
                            else
                                studentEventViewModel.addStudentEvent(attemptNumber,
                                        studentPerformanceInModule.get(String.valueOf(event.getModule().getModuleNumber())), event,
                                        isAttended.isChecked(),
                                        Integer.parseInt(variantNumber.getText().toString()));

                            if (isFromGroupPerformance) {
                                Bundle bundle = new Bundle();
                                bundle.putLong("subjectInfoId", subjectInfoId);

                                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", 0);
                                bundle.putInt("moduleExpand", event.getModule().getModuleNumber() - 1);
                                bundle.putLong("studentPerformanceInSubjectId", studentPerformanceInModule.get(String.valueOf(event.getModule().getModuleNumber()))
                                        .getStudentPerformanceInSubject().getId());

                                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_student_performance, bundle);
                            }
                        }
                    });
                });
        if (isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentEventViewModel.deleteStudentEvent(studentEventId);

                if (isFromGroupPerformance) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", subjectInfoId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                } else {
                    int moduleNumber = studentEventViewModel.getStudentEvent().getValue().getEvent().getModule().getModuleNumber();
                    Bundle bundle = new Bundle();
                    bundle.putInt("openPage", 0);
                    bundle.putInt("moduleExpand", moduleNumber - 1);
                    bundle.putLong("studentPerformanceInSubjectId", studentEventViewModel.getStudentEvent().getValue().getStudentPerformanceInModule().getStudentPerformanceInSubject().getId());

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
