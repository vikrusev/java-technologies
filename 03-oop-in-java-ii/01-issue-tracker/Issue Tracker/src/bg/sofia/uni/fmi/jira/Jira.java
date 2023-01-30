package bg.sofia.uni.fmi.jira;

import bg.sofia.uni.fmi.jira.enums.*;
import bg.sofia.uni.fmi.jira.interfaces.IssueTracker;
import bg.sofia.uni.fmi.jira.enums.IssueType;
import bg.sofia.uni.fmi.jira.issues.Issue;
import bg.sofia.uni.fmi.jira.issues.NewFeature;
import bg.sofia.uni.fmi.jira.issues.Task;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.jira.enums.IssueType.*;

public class Jira implements IssueTracker {

    private Issue[] issues;

    public Jira(Issue[] issues) {
        this.issues = issues;
    }

    @Override
    public Issue[] findAll(Component component, IssueStatus status) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if (issue.getStatus() == status && issue.getComponent() == component) {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }

    @Override
    public Issue[] findAll(Component component, IssuePriority priority) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if (issue.getPriority() == priority && issue.getComponent() == component) {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }

    @Override
    public Issue[] findAll(Component component, IssueType type) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if (issue.getType() == type && issue.getComponent() == component) {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }

    @Override
    public Issue[] findAll(Component component, IssueResolution resolution) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if (issue.getResolution() == resolution && issue.getComponent() == component) {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }

    @Override
    public Issue[] findAllIssuesCreatedBetween(LocalDateTime startTime, LocalDateTime endTime) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if (!issue.getCreatedAt().isBefore(startTime) && !issue.getCreatedAt().isAfter(endTime)) {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }

    @Override
    //find all created before dueTime or all which dueTimes is before dueTime
    public Issue[] findAllBefore(LocalDateTime dueTime) {
        Issue[] found = new Issue[issues.length];
        int counter = 0;
        for (Issue issue : issues) {
            if (issue != null) {
                if ((issue.getType() == NEW_FEATURE && !((NewFeature)issue).getDueTime().isAfter(dueTime))
                    || (issue.getType() == TASK && !((Task)issue).getDueTime().isAfter(dueTime)))
                {
                    found[counter] = issue;
                    counter++;
                }
            }
        }
        return found;
    }
}
