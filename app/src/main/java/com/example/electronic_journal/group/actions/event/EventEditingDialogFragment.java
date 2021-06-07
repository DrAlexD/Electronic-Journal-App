package com.example.electronic_journal.group.actions.event;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Event;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private View root;
    private long eventId;
    private EventEditingViewModel eventEditingViewModel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_editing, null);

        eventId = getArguments().getLong("eventId");
        long groupId = getArguments().getLong("groupId");
        String eventTitle = getArguments().getString("eventTitle");

        eventEditingViewModel = new ViewModelProvider(this).get(EventEditingViewModel.class);
        eventEditingViewModel.downloadEventById(eventId);

        EditText startDate = root.findViewById(R.id.eventStartDateEditing);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateEditing);
        EditText minPoints = root.findViewById(R.id.eventMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsEditing);
        EditText numberOfVariants = root.findViewById(R.id.eventNumberOfVariantsEditing);

        eventEditingViewModel.getEvent().observe(this, event -> {
            if (event != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                startDate.setText(dateFormat.format(event.getStartDate()));
                deadlineDate.setText(dateFormat.format(event.getDeadlineDate()));
                minPoints.setText(String.valueOf(event.getMinPoints()));
                maxPoints.setText(String.valueOf(event.getMaxPoints()));
                if (event.getNumberOfVariants() != null)
                    numberOfVariants.setText(String.valueOf(event.getNumberOfVariants()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventEditingViewModel.eventEditingDataChanged(startDate.getText().toString(), deadlineDate.getText().toString(),
                        minPoints.getText().toString(), maxPoints.getText().toString(), numberOfVariants.getText().toString(),
                        eventEditingViewModel.getEvent().getValue() != null ?
                                eventEditingViewModel.getEvent().getValue().getModule().getSubjectInfo().getSemester() : null);
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

        Button addVisitsForStudentsButton = root.findViewById(R.id.eventAddVisitsForStudents);
        addVisitsForStudentsButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("actionCode", 4);
            bundle.putLong("fromGroupId", groupId);
            bundle.putLong("eventId", eventId);
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_editing_to_search_check_students_in_group, bundle);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные " + eventTitle)
                .setPositiveButton("Подтвердить", null)
                .setNegativeButton("Отменить", (dialog, id) -> dismiss())
                .setNeutralButton("Удалить", null).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        EditText startDate = root.findViewById(R.id.eventStartDateEditing);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateEditing);
        EditText minPoints = root.findViewById(R.id.eventMinPointsEditing);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsEditing);
        EditText numberOfVariants = root.findViewById(R.id.eventNumberOfVariantsEditing);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date startDateR = dateFormat.parse(startDate.getText().toString());
                Date deadlineDateR = dateFormat.parse(deadlineDate.getText().toString());

                Event event = eventEditingViewModel.getEvent().getValue();
                eventEditingViewModel.editEvent(eventId, event.getModule(), event.getTypeNumber(), event.getNumber(),
                        startDateR, deadlineDateR,
                        Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()),
                        numberOfVariants.getText().toString().isEmpty() ? null : Integer.parseInt(numberOfVariants.getText().toString()));

                eventEditingViewModel.getAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                    if (answer != null && answer != -2) {
                        if (answer == -1) {
                            Bundle bundle = new Bundle();
                            bundle.putLong("subjectInfoId", eventEditingViewModel.getEvent().getValue().getModule().getSubjectInfo().getId());
                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_editing_to_group_performance_events, bundle);
                        } else {
                            Toast.makeText(getContext(), "Чтобы увеличить значение мин. баллов, сначала увеличьте значение мин. баллов в модуле", Toast.LENGTH_LONG).show();
                            eventEditingViewModel.setAnswer(new MutableLiveData<>());
                        }
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(view -> {
            eventEditingViewModel.deleteEvent(eventId);
            eventEditingViewModel.getSecondAnswer().observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", eventEditingViewModel.getEvent().getValue().getModule().getSubjectInfo().getId());
                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_editing_to_group_performance_events, bundle);
                }
            });
        });
    }
}
