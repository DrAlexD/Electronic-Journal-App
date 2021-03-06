package com.example.electronicdiary.search.available;

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
import com.example.electronicdiary.search.SubjectsAdapter;

import java.util.ArrayList;

public class SearchAvailableSubjectsFragment extends Fragment {
    private SubjectsAdapter subjectsAdapter;
    private ArrayList<String> subjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_available_subjects, container, false);

        downloadData();

        int actionCode = getArguments().getInt("actionCode");

        View.OnClickListener onItemClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            if (actionCode == 11) {
                Bundle bundle = new Bundle();
                bundle.putInt("actionCode", 11);
                bundle.putString("subjectTitle", subjects.get(position));
                Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_search_all_groups, bundle);
            } else if (actionCode == 12) {
                //TODO удаление предмета из доступных преподавателю
                Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_profile);
            } else if (actionCode == 13) {
                Bundle bundle = new Bundle();
                bundle.putInt("actionCode", 13);
                bundle.putString("subjectTitle", subjects.get(position));
                Navigation.findNavController(view).navigate(R.id.action_search_available_subjects_to_search_available_groups, bundle);
            }
        };
        subjectsAdapter = new SubjectsAdapter(getContext(), subjects, onItemClickListener);

        final RecyclerView recyclerView = root.findViewById(R.id.searchedAvailableSubjectsList);
        recyclerView.setAdapter(subjectsAdapter);
        recyclerView.setHasFixedSize(false);

        final SearchView searchView = root.findViewById(R.id.availableSubjectsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        return root;
    }

    private void downloadData() {
        //TODO поиск доступных предметов
        subjects = new ArrayList<>();
        subjects.add("Матан");
        subjects.add("Мобилки");
        subjects.add("Алгебра");
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