package com.example.electronicdiary.group.actions.event;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Module;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

public class EventAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_adding, null);

        long subjectInfoId = getArguments().getLong("subjectInfoId");

        EventAddingViewModel eventAddingViewModel = new ViewModelProvider(this).get(EventAddingViewModel.class);

        Spinner module = root.findViewById(R.id.eventModuleAdding);
        Spinner eventType = root.findViewById(R.id.eventTypeAdding);
        EditText startDate = root.findViewById(R.id.eventStartDateAdding);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateAdding);
        EditText minPoints = root.findViewById(R.id.eventMinPointsAdding);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventAddingViewModel.eventAddingDataChanged(startDate.getText().toString(), deadlineDate.getText().toString(),
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

        eventAddingViewModel.getEventFormState().observe(this, eventFormState -> {
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
                .setTitle("Введите данные мероприятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    eventAddingViewModel.downloadLastNumberOfEventType(subjectInfoId, Event.convertTypeToInt((String) eventType.getSelectedItem()));
                    eventAddingViewModel.downloadModules(subjectInfoId);

                    LiveData<Integer> lastNumberOfEventTypeLiveData = eventAddingViewModel.getLastNumberOfEventType();
                    LiveData<Map<String, Module>> modulesLiveData = Transformations.switchMap(lastNumberOfEventTypeLiveData,
                            g -> eventAddingViewModel.getModules());

                    modulesLiveData.observe(this, modules -> {
                        if (modules != null) {
                            String[] splitedStartDate = startDate.getText().toString().split("\\.");
                            String[] splitedDeadlineDate = deadlineDate.getText().toString().split("\\.");
                            eventAddingViewModel.addEvent(modules.get(module.getSelectedItem()),
                                    Event.convertTypeToInt((String) eventType.getSelectedItem()),
                                    eventAddingViewModel.getLastNumberOfEventType().getValue() + 1,
                                    new Date(Integer.parseInt(splitedStartDate[2]), Integer.parseInt(splitedStartDate[1]) - 1,
                                            Integer.parseInt(splitedStartDate[0])), new Date(Integer.parseInt(splitedDeadlineDate[2]),
                                            Integer.parseInt(splitedDeadlineDate[1]) - 1, Integer.parseInt(splitedDeadlineDate[0])),
                                    Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()));
                            Bundle bundle = new Bundle();
                            bundle.putLong("subjectInfoId", subjectInfoId);

                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_adding_to_group_performance, bundle);
                        }
                    });
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}