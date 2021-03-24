package com.example.electronicdiary.group;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

public class EventAddingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_adding, null);

        int semesterId = getArguments().getInt("semesterId");
        int groupId = getArguments().getInt("groupId");
        int subjectId = getArguments().getInt("subjectId");

        EventAddingViewModel eventAddingViewModel = new ViewModelProvider(this).get(EventAddingViewModel.class);

        Spinner eventType = root.findViewById(R.id.eventTypeAdding);

        EditText eventMinPoints = root.findViewById(R.id.eventMinPointsAdding);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventAddingViewModel.eventAddingDataChanged(eventMinPoints.getText().toString());
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
        eventMinPoints.addTextChangedListener(afterTextChangedListener);

        eventAddingViewModel.getEventFormState().observe(this, eventFormState -> {
            if (eventFormState == null) {
                return;
            }

            eventMinPoints.setError(eventFormState.getEventMinPointsError() != null ?
                    getString(eventFormState.getEventMinPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(eventFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Введите данные мероприятия")
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    eventAddingViewModel.addEvent(eventMinPoints.getText().toString(), eventType.getSelectedItemPosition());

                    Bundle bundle = new Bundle();
                    bundle.putInt("semesterId", semesterId);
                    bundle.putInt("groupId", groupId);
                    bundle.putInt("subjectId", subjectId);

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_adding_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }
}