package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.Repository;

import java.util.Date;

public class LessonEditingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public MutableLiveData<Lesson> getLesson() {
        return lesson;
    }

    public void lessonEditingDataChanged(String lessonAttendPoints) {
        lessonFormState.setValue(new LessonFormState(lessonAttendPoints));
    }

    public void downloadLessonById(int lessonId) {
        this.lesson.setValue(Repository.getInstance().getLessonById(lessonId));
    }

    public void editLesson(int id, int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId,
                           int semesterId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().editLesson(id, moduleNumber, groupId, subjectId, lecturerId, seminarianId,
                semesterId, dateAndTime, isLecture, pointsPerVisit);
    }

    public void deleteLesson(int lessonId) {
        Repository.getInstance().deleteLesson(lessonId);
    }
}