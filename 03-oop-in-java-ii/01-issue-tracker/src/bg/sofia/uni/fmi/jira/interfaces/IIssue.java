package bg.sofia.uni.fmi.jira.interfaces;

import bg.sofia.uni.fmi.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.jira.enums.IssueStatus;

import java.time.LocalDateTime;

public interface IIssue {

    void resolve(IssueResolution resolution);

    void setStatus(IssueStatus status);

    String getId();

    LocalDateTime getCreatedAt();

    LocalDateTime getLastModifiedAt();

}