package com.example.electronic_journal.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

class EventsAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;

    private final List<Integer> modulesNumbers;
    private final Map<String, Module> moduleByModules;
    private final Map<String, StudentPerformanceInModule> studentPerformanceByModules;
    private final Map<String, List<Event>> eventsByModules;
    private final Map<String, List<StudentEvent>> studentEventsByModules;

    EventsAdapter(Context context, List<Integer> modulesNumbers, Map<String, Module> moduleByModules,
                  Map<String, StudentPerformanceInModule> studentPerformanceByModules,
                  Map<String, List<Event>> eventsByModules,
                  Map<String, List<StudentEvent>> studentEventsByModules) {
        this.inflater = LayoutInflater.from(context);
        this.modulesNumbers = modulesNumbers;
        this.moduleByModules = moduleByModules;
        this.studentPerformanceByModules = studentPerformanceByModules;
        this.eventsByModules = eventsByModules;
        this.studentEventsByModules = studentEventsByModules;
    }

    @Override
    public int getGroupCount() {
        return modulesNumbers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (eventsByModules.get(String.valueOf(modulesNumbers.get(groupPosition))) != null)
            return eventsByModules.get(String.valueOf(modulesNumbers.get(groupPosition))).size();
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return modulesNumbers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return eventsByModules.get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_module_performance, null);
        }

        String moduleTitle = "Модуль " + modulesNumbers.get(groupPosition);
        Module module = moduleByModules.get(String.valueOf(modulesNumbers.get(groupPosition)));
        StudentPerformanceInModule studentPerformanceInModule = studentPerformanceByModules.get(String.valueOf(modulesNumbers.get(groupPosition)));

        TextView moduleTitleWithPointsView = view.findViewById(R.id.moduleTitleWithPoints);
        if (module.getMaxAvailablePoints() != 0) {
            moduleTitleWithPointsView.setText(moduleTitle + " (" + module.getMinPoints() +
                    "-" + module.getMaxAvailablePoints() + ")");
        } else {
            moduleTitleWithPointsView.setText(moduleTitle);
        }
        if (studentPerformanceInModule.getEarnedPoints() != null) {
            moduleTitleWithPointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() >= module.getMinPoints() ?
                    inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));

            TextView earnedModulePointsView = view.findViewById(R.id.earnedModulePoints);
            earnedModulePointsView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
            earnedModulePointsView.setTextColor(studentPerformanceInModule.getEarnedPoints() >= module.getMinPoints() ?
                    inflater.getContext().getColor(R.color.green) : inflater.getContext().getColor(R.color.red));
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.holder_event_performance, null);
        }

        Event event = eventsByModules.get(String.valueOf(modulesNumbers.get(groupPosition))).get(childPosition);

        List<StudentEvent> studentEvents = null;
        if (studentEventsByModules != null) {
            studentEvents = studentEventsByModules.get(String.valueOf(modulesNumbers.get(groupPosition)));
        }

        int lastAttempt = 0;
        StudentEvent studentEvent = null;
        if (studentEvents != null) {
            for (int i = 0; i < studentEvents.size(); i++) {
                if (event.getId() == studentEvents.get(i).getEvent().getId() && studentEvents.get(i).getAttemptNumber() > lastAttempt) {
                    studentEvent = studentEvents.get(i);
                    lastAttempt = studentEvents.get(i).getAttemptNumber();
                }
            }
        }


        TextView eventTitleView = view.findViewById(R.id.eventTitle);
        TextView attemptNumberView = view.findViewById(R.id.attemptNumber);
        TextView allPointsView = view.findViewById(R.id.allPoints);
        TextView finishDateView = view.findViewById(R.id.finishDate);

        if (lastAttempt == 0) {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText("");
            allPointsView.setText("Не сдано");
            finishDateView.setText("");
        } else if (!studentEvent.isAttended()) {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText("");
            allPointsView.setText("Не посещено");
            finishDateView.setText("");
        } else {
            eventTitleView.setText(event.getTitle());
            attemptNumberView.setText(studentEvent.getAttemptNumber() + " попытка");
            if (studentEvent.getEarnedPoints() == null && studentEvent.getBonusPoints() == null) {
                allPointsView.setText("-");
            } else if (studentEvent.getEarnedPoints() == null) {
                allPointsView.setText(String.valueOf(studentEvent.getBonusPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            } else if (studentEvent.getBonusPoints() == null) {
                allPointsView.setText(String.valueOf(studentEvent.getEarnedPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            } else {
                allPointsView.setText(String.valueOf(studentEvent.getEarnedPoints() + studentEvent.getBonusPoints()));
                allPointsView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ?
                        R.color.green : R.color.red));
            }

            if (studentEvent.getFinishDate() != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                finishDateView.setText(dateFormat.format(studentEvent.getFinishDate()));
                finishDateView.setTextColor(inflater.getContext().getColor(studentEvent.getFinishDate().after(event.getDeadlineDate())
                        ? R.color.red : R.color.green));
            } else
                finishDateView.setText("-");

            if (studentEvent.isHaveCredit() != null) {
                eventTitleView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ? R.color.green : R.color.red));
                attemptNumberView.setTextColor(inflater.getContext().getColor(studentEvent.isHaveCredit() ? R.color.green : R.color.red));
            }
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

