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

public class StudentEventDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_student_event, null);

        boolean isHasData = getArguments().getString("earnedPoints") != null;
        StudentEventViewModel studentEventViewModel = new ViewModelProvider(this).get(StudentEventViewModel.class);

        EditText eventEarnedPoints = root.findViewById(R.id.studentEventEarnedPoints);

        int eventId;
        if (isHasData) {
            eventId = 1;
            studentEventViewModel.downloadStudentEventById(eventId);
            studentEventViewModel.getEvent().observe(this, event -> {
                if (event == null) {
                    return;
                }

                eventEarnedPoints.setText(String.valueOf(event.getPoints()));
            });
        } else
            eventId = -1;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                studentEventViewModel.eventPerformanceDataChanged(eventEarnedPoints.getText().toString());
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
        eventEarnedPoints.addTextChangedListener(afterTextChangedListener);

        studentEventViewModel.getStudentEventFormState().observe(this, studentEventFormState -> {
            if (studentEventFormState == null) {
                return;
            }

            eventEarnedPoints.setError(studentEventFormState.getStudentEventEarnedPointsError() != null ?
                    getString(studentEventFormState.getStudentEventEarnedPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(studentEventFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(root)
                .setTitle("Сдача " + getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    if (isHasData)
                        studentEventViewModel.editStudentEvent(eventEarnedPoints.getText().toString());
                    else
                        studentEventViewModel.addStudentEvent(eventEarnedPoints.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("group", getArguments().getString("group"));

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
                });
        if (isHasData) {
            builder.setNegativeButton("Отменить", (dialog, id) -> {
                dismiss();
            }).setNeutralButton("Удалить", (dialog, id) -> {
                studentEventViewModel.deleteStudentEvent(eventId);

                Bundle bundle = new Bundle();
                bundle.putString("subject", getArguments().getString("subject"));
                bundle.putString("group", getArguments().getString("group"));

                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_student_event_to_group_performance, bundle);
            });
        }

        dialog = builder.create();
        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isHasData);
        });

        return dialog;
    }
}
