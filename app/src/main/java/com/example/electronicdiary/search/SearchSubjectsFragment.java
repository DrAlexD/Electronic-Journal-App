package com.example.electronicdiary.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import java.util.ArrayList;

public class SearchSubjectsFragment extends Fragment {
    private SubjectsAdapter subjectsAdapter;
    private ArrayList<String> subjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_subjects, container, false);

        downloadData();

        int actionCode = getArguments().getInt("actionCode");

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("subjectTitle", subjects.get(position));
                Navigation.findNavController(view).navigate(R.id.action_search_subjects_to_dialog_subject_editing, bundle);
            } else if (actionCode == 2) {
                //TODO удаление предмета из базы

                Bundle bundle = new Bundle();
                bundle.putInt("openPage", 2);
                Navigation.findNavController(view).navigate(R.id.action_search_subjects_to_admin_actions, bundle);
            }
        };
        subjectsAdapter = new SubjectsAdapter(getContext(), subjects, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedSubjectsList);
        recyclerView.setAdapter(subjectsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.subjectsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск всех предметов
        subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Линал");
        subjects.add("Операционки");
        subjects.add("Моделирование");
        subjects.add("Методы оптимизации");
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subjectsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}