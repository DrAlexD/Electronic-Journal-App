package com.example.electronicdiary.group.actions.student_event;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class StudentEventDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private StudentEventViewModel studentEventViewModel;
    private LiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModulesLiveData;
    private boolean isHasData;
    private boolean isFromGroupPerformance;
    private int attemptNumber;
    private long subjectInfoId;
    private boolean isProfessor;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_event, null);

        isFromGroupPerformance = getArguments().getBoolean("isFromGroupPerformance");
        attemptNumber = getArguments().getInt("attemptNumber");
        long eventId = getArguments().getLong("eventId");
        String eventTitle = getArguments().getString("eventTitle");
        long studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");
        subjectInfoId = getArguments().getLong("subjectInfoId");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isProfessor = sharedPreferences.getBoolean("isUserProfessor", true);
        isHasData = attemptNumber != 0;
        studentEventViewModel = new ViewModelProvider(this).get(StudentEventViewModel.class);

        CheckBox isAttended = root.findViewById(R.id.studentEventIsAttended);
        EditText variantNumber = root.findViewById(R.id.studentEventVariantNumber);
        EditText finishDate = root.findViewById(R.id.studentEventFinishDate);
        EditText earnedPoints = root.findViewById(R.id.studentEventEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentEventBonusPoints);
        CheckBox isHaveCredit = root.findViewById(R.id.studentEventIsHaveCredit);
        isAttended.setEnabled(isProfessor);
        variantNumber.setEnabled(isProfessor);
        finishDate.setEnabled(isProfessor);
        earnedPoints.setEnabled(isProfessor);
        bonusPoints.setEnabled(isProfessor);
        isHaveCredit.setEnabled(isProfessor);

        if (isHasData) {
            studentEventViewModel.downloadStudentEventById(getArguments().getLong("studentEventId"));
            if (isProfessor) {
                studentEventViewModel.downloadEventById(eventId);
                studentEventViewModel.downloadStudentPerformanceInModule(studentPerformanceInSubjectId);
                LiveData<StudentEvent> studentEventLiveData = studentEventViewModel.getStudentEvent();
                LiveData<Event> eventLiveData = Transformations.switchMap(studentEventLiveData, studentEvent -> {
                    isAttended.setChecked(studentEvent.isAttended());
                    variantNumber.setText(String.valueOf(studentEvent.getVariantNumber()));

                    if (studentEvent.getFinishDate() != null) {
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        finishDate.setText(dateFormat.format(studentEvent.getFinishDate()));
                        finishDate.setTextColor(getResources().getColor(studentEvent.getFinishDate().after(studentEvent.getEvent().getDeadlineDate()) ?
                                R.color.red : R.color.green));
                    }

                    if (studentEvent.getEarnedPoints() != null) {
                        earnedPoints.setText(String.valueOf(studentEvent.getEarnedPoints()));
                        if (studentEvent.getEarnedPoints() < studentEvent.getEvent().getMinPoints()) {
                            earnedPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            earnedPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    if (studentEvent.getBonusPoints() != null) {
                        bonusPoints.setText(String.valueOf(studentEvent.getBonusPoints()));
                        if (studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                            bonusPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            bonusPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    if (studentEvent.getEarnedPoints() != null && studentEvent.getBonusPoints() != null) {
                        if (studentEvent.getEarnedPoints() + studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                            earnedPoints.setTextColor(getResources().getColor(R.color.red));
                            bonusPoints.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            earnedPoints.setTextColor(getResources().getColor(R.color.green));
                            bonusPoints.setTextColor(getResources().getColor(R.color.green));
                        }
                    }

                    if (studentEvent.isHaveCredit() != null) {
                        isHaveCredit.setChecked(studentEvent.isHaveCredit());
                    }

                    return studentEventViewModel.getEvent();
                });

                studentPerformanceInModulesLiveData =
                        Transformations.switchMap(eventLiveData, g -> studentEventViewModel.getStudentPerformanceInModules());
            } else {
                studentEventViewModel.getStudentEvent().observe(this, studentEvent -> {
                    if (studentEvent != null) {
                        isAttended.setChecked(studentEvent.isAttended());
                        variantNumber.setText(String.valueOf(studentEvent.getVariantNumber()));

                        if (studentEvent.getFinishDate() != null) {
                            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            finishDate.setText(dateFormat.format(studentEvent.getFinishDate()));
                            finishDate.setTextColor(getResources().getColor(studentEvent.getFinishDate().after(studentEvent.getEvent().getDeadlineDate()) ?
                                    R.color.red : R.color.green));
                        }

                        if (studentEvent.getEarnedPoints() != null) {
                            earnedPoints.setText(String.valueOf(studentEvent.getEarnedPoints()));
                            if (studentEvent.getEarnedPoints() < studentEvent.getEvent().getMinPoints()) {
                                earnedPoints.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                earnedPoints.setTextColor(getResources().getColor(R.color.green));
                            }
                        }

                        if (studentEvent.getBonusPoints() != null) {
                            bonusPoints.setText(String.valueOf(studentEvent.getBonusPoints()));
                            if (studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                                bonusPoints.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                bonusPoints.setTextColor(getResources().getColor(R.color.green));
                            }
                        }

                        if (studentEvent.getEarnedPoints() != null && studentEvent.getBonusPoints() != null) {
                            if (studentEvent.getEarnedPoints() + studentEvent.getBonusPoints() < studentEvent.getEvent().getMinPoints()) {
                                earnedPoints.setTextColor(getResources().getColor(R.color.red));
                                bonusPoints.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                earnedPoints.setTextColor(getResources().getColor(R.color.green));
                                bonusPoints.setTextColor(getResources().getColor(R.color.green));
                            }
                        }

                        if (studentEvent.isHaveCredit() != null) {
                            isHaveCredit.setChecked(studentEvent.isHaveCredit());
                        }
                    }
                });
            }
        } else {
            finishDate.setVisibility(View.GONE);
            earnedPoints.setVisibility(View.GONE);
            bonusPoints.setVisibility(View.GONE);
            isHaveCredit.setVisibility(View.GONE);

            if (isProfessor) {
                studentEventViewModel.downloadEventById(eventId);
                studentEventViewModel.downloadStudentPerformanceInModule(studentPerformanceInSubjectId);

                LiveData<Event> eventLiveData = studentEventViewModel.getEvent();
                studentPerformanceInModulesLiveData =
                        Transformations.switchMap(eventLiveData, g -> studentEventViewModel.getStudentPerformanceInModules());
            }
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
                .setTitle("Сдача " + eventTitle);
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
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isHasData);
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        CheckBox isAttended = root.findViewById(R.id.studentEventIsAttended);
        EditText variantNumber = root.findViewById(R.id.studentEventVariantNumber);
        EditText finishDate = root.findViewById(R.id.studentEventFinishDate);
        EditText earnedPoints = root.findViewById(R.id.studentEventEarnedPoints);
        EditText bonusPoints = root.findViewById(R.id.studentEventBonusPoints);
        CheckBox isHaveCredit = root.findViewById(R.id.studentEventIsHaveCredit);

        if (isProfessor) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                LiveData<Boolean> answerLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                        studentPerformanceInModule -> {
                            Event event = studentEventViewModel.getEvent().getValue();
                            Date date;
                            if (finishDate.getText().toString().isEmpty()) {
                                date = null;
                            } else {
                                try {
                                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    date = dateFormat.parse(finishDate.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    date = null;
                                }
                            }
                            if (isHasData)
                                studentEventViewModel.editStudentEvent(getArguments().getLong("studentEventId"), attemptNumber,
                                        studentEventViewModel.getStudentEvent().getValue().getStudentPerformanceInModule(),
                                        studentEventViewModel.getStudentEvent().getValue().getEvent(), isAttended.isChecked(),
                                        Integer.parseInt(variantNumber.getText().toString()), date,
                                        earnedPoints.getText().toString().isEmpty() ? null : Integer.parseInt(earnedPoints.getText().toString()),
                                        bonusPoints.getText().toString().isEmpty() ? null : Integer.parseInt(bonusPoints.getText().toString()),
                                        isHaveCredit.isChecked());
                            else
                                studentEventViewModel.addStudentEvent(attemptNumber + 1,
                                        studentPerformanceInModule.get(String.valueOf(event.getModule().getModuleNumber())), event,
                                        isAttended.isChecked(),
                                        Integer.parseInt(variantNumber.getText().toString()));

                            return studentEventViewModel.getAnswer();
                        });

                answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        if (isFromGroupPerformance) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("subjectInfoId", subjectInfoId);

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                        } else {
                            int moduleNumber = studentEventViewModel.getEvent().getValue().getModule().getModuleNumber();
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", 0);
                            bundle.putInt("moduleExpand", moduleNumber - 1);
                            bundle.putLong("studentPerformanceInSubjectId", studentEventViewModel.getStudentPerformanceInModules().getValue()
                                    .get(String.valueOf(moduleNumber))
                                    .getStudentPerformanceInSubject().getId());

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_student_performance, bundle);
                        }
                    }
                });
            });
        }

        if (isProfessor && isHasData) {
            dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(view -> {
                LiveData<Boolean> answerLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                        studentPerformanceInModules -> {
                            studentEventViewModel.deleteStudentEvent(getArguments().getLong("studentEventId"));
                            return studentEventViewModel.getAnswer();
                        });

                answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null) {
                        if (isFromGroupPerformance) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("subjectInfoId", subjectInfoId);

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                        } else {
                            int moduleNumber = studentEventViewModel.getEvent().getValue().getModule().getModuleNumber();
                            Bundle bundle = new Bundle();
                            bundle.putInt("openPage", 0);
                            bundle.putInt("moduleExpand", moduleNumber - 1);
                            bundle.putLong("studentPerformanceInSubjectId", studentEventViewModel.getStudentEvent().getValue()
                                    .getStudentPerformanceInModule().getStudentPerformanceInSubject().getId());

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_student_performance, bundle);
                        }
                    }
                });
            });
        }
    }
}
