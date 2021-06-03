package com.example.electronic_journal.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.ProfessorFormState;
import com.example.electronic_journal.data_classes.Professor;

public class ProfessorEditingViewModel extends ViewModel {
    private final MutableLiveData<ProfessorFormState> professorFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<Professor>> professor = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
    }

    LiveData<ProfessorFormState> getProfessorFormState() {
        return professorFormState;
    }

    public LiveData<Result<Professor>> getProfessor() {
        return professor;
    }

    public void professorEditingDataChanged(String professorName, String professorSecondName, String professorLogin,
                                            String professorPassword, boolean isNameOrSecondNameChanged) {
        professorFormState.setValue(new ProfessorFormState(professorName, professorSecondName, professorLogin,
                professorPassword, isNameOrSecondNameChanged));
    }

    public void downloadProfessorByIdWithLogin(long professorId) {
        Repository.getInstance().getProfessorById(professorId, professor);
    }

    public void editProfessor(long professorId, String professorName, String professorSecondName, String professorLogin, String professorPassword, String role) {
        Repository.getInstance().editProfessor(professorId, new Professor(professorName, professorSecondName, professorLogin, professorPassword, role), answer);
    }
}