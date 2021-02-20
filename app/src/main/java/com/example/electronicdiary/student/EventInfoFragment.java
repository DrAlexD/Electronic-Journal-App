package com.example.electronicdiary.student;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.electronicdiary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventInfoFragment extends DialogFragment {
    private final ArrayList<View> changedOptions = new ArrayList<>();
    private ArrayList<String> values;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        downloadData();

        EventInfoViewModel eventInfoViewModel = new ViewModelProvider(this).get(EventInfoViewModel.class);
        eventInfoViewModel.setValues(values);
        eventInfoViewModel.setChangedOptions(changedOptions);

        setData(root);

        builder.setView(root)
                .setTitle(getArguments().getString("eventTitle"))
                .setPositiveButton("Подтвердить", (dialog, id) -> {
                    for (View option : changedOptions) {
                        //TODO отправка новых установленных значений
                    }
                    dismiss();
                });

        return builder.create();
    }

    private void downloadData() {
        //TODO загрузка информации о мероприятии
        values = new ArrayList<>();
        values.add("7");
    }

    private void setData(View root) {
        EditText eventMinPointsText = root.findViewById(R.id.eventMinPoints);
        String value = values.get(0);
        eventMinPointsText.setText(value);
        eventMinPointsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (value.equals(s.toString()))
                    changedOptions.remove(eventMinPointsText);
                else
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
