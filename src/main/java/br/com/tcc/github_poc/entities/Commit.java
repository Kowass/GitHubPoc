package br.com.tcc.github_poc.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "commits", schema = "public")
public class Commit {

    @Id
    @Column(name = "sha", nullable = false)
    private String sha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repo_id", nullable = false)
    private GithubRepository repository;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_email")
    private String authorEmail;

    @Column(name = "commit_date")
    private LocalDateTime commitDate;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "author_login")
    private String authorLogin;

    @Column(name = "message_headline", columnDefinition = "text")
    private String messageHeadline;

    @Column(name = "additions")
    private Integer additions = 0;

    @Column(name = "deletions")
    private Integer deletions = 0;

    @Column(name = "total_changes")
    private Integer totalChanges = 0;

    @PrePersist
    @PreUpdate
    public void calculateTotalChanges() {
        int safeAdditions = additions == null ? 0 : additions;
        int safeDeletions = deletions == null ? 0 : deletions;
        this.totalChanges = safeAdditions + safeDeletions;
    }

}