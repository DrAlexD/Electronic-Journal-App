package com.example.electronicdiary.group.actions.event;

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

import java.util.Date;

public class EventEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_editing, null);

        int eventId = getArguments().getInt("eventId");

        EventEditingViewModel eventEditingViewModel = new ViewModelProvider(this).get(EventEditingViewModel.class);
        eventEditingViewModel.downloadEventById(eventId);

        EditText startDate = root.findViewById(R.id.eventStartDateEditing);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateEditing);
        EditText minPoints = root.findViewById(R.id.eventMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsEditing);
        eventEditingViewModel.getEvent().observe(this, event -> {
            if (event == null) {
                return;
            }

            startDate.setText(((event.getStartDate().getDate() + 1) < 10 ? "0" + (event.getStartDate().getDate() + 1) :
                    (event.getStartDate().getDate() + 1)) + "." +
                    ((event.getStartDate().getMonth() + 1) < 10 ? "0" + (event.getStartDate().getMonth() + 1) :
                            (event.getStartDate().getMonth() + 1)) + "." + event.getStartDate().getYear());
            deadlineDate.setText(((event.getDeadlineDate().getDate() + 1) < 10 ? "0" + (event.getDeadlineDate().getDate() + 1) :
                    (event.getDeadlineDate().getDate() + 1)) + "." +
                    ((event.getDeadlineDate().getMonth() + 1) < 10 ? "0" + (event.getDeadlineDate().getMonth() + 1) :
                            (event.getDeadlineDate().getMonth() + 1)) + "." + event.getDeadlineDate().getYear());
            minPoints.setText(String.valueOf(event.getMinPoints()));
            maxPoints.setText(String.valueOf(event.getMaxPoints()));
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventEditingViewModel.eventEditingDataChanged(startDate.getText().toString(), deadlineDate.getText().toString(),
                        minPoints.getText().toString(), maxPoints.getText().toString());
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
        startDate.addTextChangedListener(afterTextChangedListener);
        deadlineDate.addTextChangedListener(afterTextChangedListener);
        minPoints.addTextChangedListener(afterTextChangedListener);
        maxPoints.addTextChangedListener(afterTextChangedListener);

        eventEditingViewModel.getEventFormState().observe(this, eventFormState -> {
            if (eventFormState == null) {
                return;
            }

            startDate.setError(eventFormState.getStartDateError() != null ?
                    getString(eventFormState.getStartDateError()) : null);
            deadlineDate.setError(eventFormState.getDeadlineDateError() != null ?
                    getString(eventFormState.getDeadlineDateError()) : null);
            minPoints.setError(eventFormState.getMinPointsError() != null ?
                    getString(eventFormState.getMinPointsError()) : null);
            maxPoints.setError(eventFormState.getMaxPointsError() != null ?
                    getString(eventFormState.getMaxPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(eventFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные " + eventEditingViewModel.getEvent().getValue().getTitle())
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    String[] splitedStartDate = startDate.getText().toString().split("\\.");
                    String[] splitedDeadlineDate = deadlineDate.getText().toString().split("\\.");
                    eventEditingViewModel.editEvent(eventId, new Date(Integer.parseInt(splitedStartDate[2]), Integer.parseInt(splitedStartDate[1]) - 1,
                                    Integer.parseInt(splitedStartDate[0])), new Date(Integer.parseInt(splitedDeadlineDate[2]),
                                    Integer.parseInt(splitedDeadlineDate[1]) - 1, Integer.parseInt(splitedDeadlineDate[0])),
                            Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()));

                    dismiss();
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    eventEditingViewModel.deleteEvent(eventId);

                    Bundle bundle = new Bundle();
                    bundle.putInt("groupId", eventEditingViewModel.getEvent().getValue().getGroupId());
                    bundle.putInt("subjectId", eventEditingViewModel.getEvent().getValue().getSubjectId());
                    bundle.putInt("lecturerId", eventEditingViewModel.getEvent().getValue().getLecturerId());
                    bundle.putInt("seminarianId", eventEditingViewModel.getEvent().getValue().getSeminarianId());
                    bundle.putInt("semesterId", eventEditingViewModel.getEvent().getValue().getSemesterId());

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_editing_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }
}
