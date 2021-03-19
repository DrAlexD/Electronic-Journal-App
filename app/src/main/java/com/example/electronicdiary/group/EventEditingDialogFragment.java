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

public class EventEditingDialogFragment extends DialogFragment {
    private AlertDialog dialog;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_editing, null);

        EventEditingViewModel eventEditingViewModel = new ViewModelProvider(this).get(EventEditingViewModel.class);
        int eventId = 1;
        eventEditingViewModel.downloadEventById(eventId);

        EditText eventMinPoints = root.findViewById(R.id.eventMinPointsEditing);

        eventEditingViewModel.getEvent().observe(this, event -> {
            if (event == null) {
                return;
            }

            eventMinPoints.setText("7");
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                eventEditingViewModel.eventEditingDataChanged(eventMinPoints.getText().toString());
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

        eventEditingViewModel.getEventFormState().observe(this, eventFormState -> {
            if (eventFormState == null) {
                return;
            }

            eventMinPoints.setError(eventFormState.getEventMinPointsError() != null ?
                    getString(eventFormState.getEventMinPointsError()) : null);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(eventFormState.isDataValid());
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog = builder.setView(root)
                .setTitle("Изменить данные " + getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    eventEditingViewModel.editEvent(eventMinPoints.getText().toString());

                    dismiss();
                })
                .setNegativeButton("Отменить", (dialog, id) -> {
                    dismiss();
                })
                .setNeutralButton("Удалить", (dialog, id) -> {
                    eventEditingViewModel.deleteEvent(eventId);

                    Bundle bundle = new Bundle();
                    bundle.putString("subject", getArguments().getString("subject"));
                    bundle.putString("event", getArguments().getString("event"));

                    Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_dialog_event_editing_to_group_performance, bundle);
                }).create();

        dialog.setOnShowListener(dialog -> {
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.red));
            ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        });

        return dialog;
    }
}
