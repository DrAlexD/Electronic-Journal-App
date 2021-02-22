package com.example.electronicdiary.group;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventInfoDialogFragment extends DialogFragment {
    private final ArrayList<String> values = new ArrayList<>();
    private final ArrayList<View> changedOptions = new ArrayList<>();

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_event_info, null);

        downloadData();

        setData(root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(root)
                .setTitle(getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    for (View option : changedOptions) {
                        //TODO отправка новых установленных значений
                    }
                    dismiss();
                }).create();
    }

    private void downloadData() {
        //TODO загрузка информации о мероприятии
        values.add("7");
    }

    private void setData(View root) {
        String value = values.get(0);
        EditText eventMinPointsText = root.findViewById(R.id.eventMinPoints);
        eventMinPointsText.setText(value);
        eventMinPointsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (value.equals(s.toString()))
                    changedOptions.remove(eventMinPointsText);
                else if (!changedOptions.contains(eventMinPointsText))
                    changedOptions.add(eventMinPointsText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
        });
    }
}
