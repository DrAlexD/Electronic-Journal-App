package com.example.electronicdiary.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.electronicdiary.R;
import com.example.electronicdiary.admin.adding.GroupAddingDialogFragment;
import com.example.electronicdiary.admin.adding.ProfessorAddingDialogFragment;
import com.example.electronicdiary.admin.adding.SemesterAddingDialogFragment;
import com.example.electronicdiary.admin.adding.StudentAddingDialogFragment;
import com.example.electronicdiary.admin.adding.SubjectAddingDialogFragment;

import java.util.ArrayList;

public class AdminOneTypeActionsFragment extends Fragment {
    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_one_type_actions, container, false);

        position = getArguments().getInt("position");

         /*TODO Админ действия
            1. Изменение всего сверху
            2. Удаление всего сверху
         */

        /*TODO Действия преподавателя
            1. Добавление к себе предмета
            2. Добавление группы к своему предмету
            3. Выбор семестра в настройках из доступных
            4. Добавление/удаление/изменение контрольных мероприятий
            5. Добавление/удаление/изменение оценок по контрольным мероприятиям
            6. Добавление/удаление/изменение пар
            7. Добавление/удаление/изменение посещаемости пар
         */

        if (position == 1) {
            addActions(root);
        } else if (position == 2) {

        } else {
            deleteActions(root);
        }

        return root;
    }

    private void addActions(View root) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Добавить группу");
        actions.add("Добавить студента к группе");
        actions.add("Добавить предмет");
        actions.add("Добавить преподавателя");
        actions.add("Добавить семестр");

        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_admin_action, R.id.adminActionTitle, actions);
        final ListView listView = root.findViewById(R.id.adminActionsList);
        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                new GroupAddingDialogFragment().show(getChildFragmentManager(), "groupAdding");
            } else if (position == 1) {
                new StudentAddingDialogFragment().show(getChildFragmentManager(), "studentAdding");
            } else if (position == 2) {
                new SubjectAddingDialogFragment().show(getChildFragmentManager(), "subjectAdding");
            } else if (position == 3) {
                new ProfessorAddingDialogFragment().show(getChildFragmentManager(), "professorAdding");
            } else if (position == 4) {
                new SemesterAddingDialogFragment().show(getChildFragmentManager(), "semesterAdding");
            }
        });
    }

    private void deleteActions(View root) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Удалить группу");
        actions.add("Удалить студента из группы");
        actions.add("Удалить предмет");
        actions.add("Удалить преподавателя");
        actions.add("Удалить семестр");

        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getContext(), R.layout.holder_admin_action, R.id.adminActionTitle, actions);
        final ListView listView = root.findViewById(R.id.adminActionsList);
        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("actionCode", 2);
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_groups, bundle);
            } else if (position == 1) {
                Bundle bundle = new Bundle();
                bundle.putInt("actionCode", 2);
                Navigation.findNavController(root).navigate(R.id.action_admin_actions_to_search_all_groups, bundle);
            } else if (position == 2) {
                new SubjectAddingDialogFragment().show(getChildFragmentManager(), "subjectAdding");
            } else if (position == 3) {
                new ProfessorAddingDialogFragment().show(getChildFragmentManager(), "professorAdding");
            } else if (position == 4) {
                new SemesterAddingDialogFragment().show(getChildFragmentManager(), "semesterAdding");
            }
        });
    }
}