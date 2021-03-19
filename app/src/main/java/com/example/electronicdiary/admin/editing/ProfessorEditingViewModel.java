package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Professor;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.ProfessorFormState;

public class ProfessorEditingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private final MutableLiveData<Professor> professor = new MutableLiveData<>();

    LiveData<ProfessorFormState> getProfessorFormState() {
        return professorFormState;
    }

    public MutableLiveData<Professor> getProfessor() {
        return professor;
    }

    public void professorEditingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                            String professorPassword, boolean isNameOrSecondNameChanged) {
        professorFormState.setValue(new ProfessorFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void downloadProfessorById(int professorId) {
        this.professor.setValue(Repository.getInstance().getProfessorById(professorId));
    }

    public void editProfessor(String professorName, String professorSecondName, String professorLogin, String professorPassword) {
        Repository.getInstance().editProfessor(professorName, professorSecondName, professorLogin, professorPassword);
    }
}