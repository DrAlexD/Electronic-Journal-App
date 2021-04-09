package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.ProfessorFormState;
import com.example.electronicdiary.data_classes.Professor;

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

    public void downloadProfessorByIdWithLogin(int professorId) {
        this.professor.setValue(Repository.getInstance().getProfessorByIdWithLogin(professorId));
    }

    public void editProfessor(int professorId, String professorName, String professorSecondName, String professorLogin, String professorPassword) {
        Repository.getInstance().editProfessor(professorId, professorName, professorSecondName, professorLogin, professorPassword);
    }
}