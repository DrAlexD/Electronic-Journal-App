package com.example.electronic_journal.search.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.search.ProfessorsAdapter;

public class SearchProfessorsFragment extends Fragment {
    private ProfessorsAdapter professorsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_professors, container, false);

        SearchProfessorsViewModel searchProfessorsViewModel = new ViewModelProvider(this).get(SearchProfessorsViewModel.class);
        searchProfessorsViewModel.downloadProfessors();

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedProfessorsList);
        searchProfessorsViewModel.getProfessors().observe(getViewLifecycleOwner(), professors -> {
            if (professors != null) {
                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    if (actionCode == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("professorId", professorsAdapter.getProfessors().get(position).getId());
                        Navigation.findNavController(view).navigate(R.id.action_search_professors_to_dialog_professor_editing, bundle);
                    } else if (actionCode == 2) {
                        searchProfessorsViewModel.deleteProfessor(professorsAdapter.getProfessors().get(position).getId());
                        searchProfessorsViewModel.getAnswer().observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешное удаление преподавателя", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", 2);
                                Navigation.findNavController(view).navigate(R.id.action_search_professors_to_admin_actions, bundle);
                            }
                        });
                    }
                };

                professorsAdapter = new ProfessorsAdapter(getContext(), professors, onItemClickListener);
                recyclerView.setAdapter(professorsAdapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        final SearchView searchView = root.findViewById(R.id.professorsSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());

        return root;
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                professorsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}