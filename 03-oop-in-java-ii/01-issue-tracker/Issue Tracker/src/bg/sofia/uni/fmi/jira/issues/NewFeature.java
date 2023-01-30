package bg.sofia.uni.fmi.jira.issues;

import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.jira.enums.IssueType.NEW_FEATURE;

public final class NewFeature extends Issue {
    private LocalDateTime dueTime;

    public NewFeature(IssuePriority priority, Component component,
                      User reporter, String description, LocalDateTime dueTime)
            throws InvalidReporterException
    {
        super(priority, component, reporter, description);

        this.type = NEW_FEATURE;
        this.dueTime = dueTime;
    }

    public LocalDateTime getDueTime() {
        return this.dueTime;
    }
}
