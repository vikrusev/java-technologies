package bg.sofia.uni.fmi.jira.issues;

import bg.sofia.uni.fmi.jira.Component;
import bg.sofia.uni.fmi.jira.User;
import bg.sofia.uni.fmi.jira.enums.*;
import bg.sofia.uni.fmi.jira.interfaces.IIssue;
import bg.sofia.uni.fmi.jira.issues.exceptions.InvalidReporterException;

import java.time.LocalDateTime;

public class Issue implements IIssue {

    private String issueID = null;
    private static int idNumber = 0;
    private String description = null;

    protected IssueType type;
    private IssuePriority priority;
    private IssueStatus status = IssueStatus.OPEN;
    private IssueResolution resolution = IssueResolution.UNRESOLVED;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime modifiedAt;

    private Component component = null;
    private User reporter = null;

    public Issue(IssuePriority priority, Component component, User reporter, String description)
            throws InvalidReporterException
    {
        validateReporter(reporter);
        validatePriority(priority);
        validateComponent(component);
        validateDescription(description);

        this.priority = priority;
        this.component = component;
        this.reporter = reporter;
        this.description = description;

        this.issueID = component.getShortName() + idNumber;
        idNumber++;
    }

    @Override
    public void resolve(IssueResolution resolution) {
        this.resolution = resolution;
    }

    @Override
    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    @Override
    public String getId() {
        return this.issueID;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public LocalDateTime getLastModifiedAt() {
        return this.modifiedAt;
    }

    public String getDescription() {
        return this.description;
    }

    public IssueType getType() {
        return this.type;
    }

    public IssuePriority getPriority() {
        return this.priority;
    }

    public IssueStatus getStatus() {
        return this.status;
    }

    public IssueResolution getResolution() {
        return this.resolution;
    }

    public Component getComponent() {
        return this.component;
    }

    public User getReporter() {
        return this.reporter;
    }

    private void validatePriority(IssuePriority priority) throws InvalidReporterException {
        if (priority == null) {
            throw new InvalidReporterException("Invalid reporter");
        }
    }

    private void validateComponent(Component component) throws InvalidReporterException {
        if (component == null) {
            throw new InvalidReporterException("Invalid component");
        }
    }

    private void validateReporter(User reporter) throws InvalidReporterException {
        if (reporter == null) {
            throw new InvalidReporterException("Invalid reporter");
        }
    }

    private void validateDescription(String description) throws InvalidReporterException {
        if (description == null) {
            throw new InvalidReporterException("Invalid description");
        }
    }
}
