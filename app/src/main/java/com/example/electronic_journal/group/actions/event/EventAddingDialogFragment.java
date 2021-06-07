package com.example.electronic_journal.group.actions.event;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Module;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EventAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;
    private EventAddingViewModel eventAddingViewModel;
    private View root;
    private LiveData<Map<String, Module>> modulesLiveData;
    private long subjectInfoId;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_adding, null);

        subjectInfoId = getArguments().getLong("subjectInfoId");

        eventAddingViewModel = new ViewModelProvider(this).get(EventAddingViewModel.class);

        EditText startDate = root.findViewById(R.id.eventStartDateAdding);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateAdding);
        EditText minPoints = root.findViewById(R.id.eventMinPointsAdding);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsAdding);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventAddingViewModel.eventAddingDataChanged(startDate.getText().toString(), deadlineDate.getText().toString(),
                        minPoints.getText().toString(), maxPoints.getText().toString(),
                        eventAddingViewModel.getModules().getValue() != null ?
                                eventAddingViewModel.getModules().getValue().get("1").getSubjectInfo().getSemester() : null);
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

        eventAddingViewModel.downloadModules(subjectInfoId);
        modulesLiveData = eventAddingViewModel.getModules();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные мероприятия")
                .setPositiveButton("Подтвердить", null).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        Spinner module = root.findViewById(R.id.eventModuleAdding);
        Spinner eventType = root.findViewById(R.id.eventTypeAdding);
        EditText startDate = root.findViewById(R.id.eventStartDateAdding);
        EditText deadlineDate = root.findViewById(R.id.eventDeadlineDateAdding);
        EditText minPoints = root.findViewById(R.id.eventMinPointsAdding);
        EditText maxPoints = root.findViewById(R.id.eventMaxPointsAdding);
        EditText numberOfVariants = root.findViewById(R.id.eventNumberOfVariantsAdding);

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            if (eventAddingViewModel.getLastNumberOfEventType().getValue() == null)
                eventAddingViewModel.downloadLastNumberOfEventType(subjectInfoId, Event.convertTypeToInt((String) eventType.getSelectedItem()));
            LiveData<Integer> lastNumberOfEventTypeLiveData = Transformations.switchMap(modulesLiveData,
                    g -> eventAddingViewModel.getLastNumberOfEventType());

            LiveData<Integer> answerLiveData = Transformations.switchMap(lastNumberOfEventTypeLiveData, lastNumberOfEventType -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date startDateR = null;
                Date deadlineDateR = null;
                try {
                    startDateR = sdf.parse(startDate.getText().toString());
                    deadlineDateR = sdf.parse(deadlineDate.getText().toString());

                    eventAddingViewModel.addEvent(eventAddingViewModel.getModules().getValue().get(module.getSelectedItem()),
                            Event.convertTypeToInt((String) eventType.getSelectedItem()),
                            eventAddingViewModel.getLastNumberOfEventType().getValue() + 1,
                            startDateR, deadlineDateR,
                            Integer.parseInt(minPoints.getText().toString()), Integer.parseInt(maxPoints.getText().toString()),
                            numberOfVariants.getText().toString().isEmpty() ? null : Integer.parseInt(numberOfVariants.getText().toString()));

                    return eventAddingViewModel.getAnswer();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            answerLiveData.observe(getParentFragment().getViewLifecycleOwner(), answer -> {
                if (answer != null && answer != -2) {
                    if (answer == -1) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("subjectInfoId", subjectInfoId);

                        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_adding_to_group_performance_events, bundle);
                    } else {
                        Toast.makeText(getContext(), "Доступное значение мин. баллов: " + answer + ", увеличьте значение мин. баллов в модуле", Toast.LENGTH_LONG).show();
                        eventAddingViewModel.setAnswer(new MutableLiveData<>());
                    }
                }
            });
        });
    }
}