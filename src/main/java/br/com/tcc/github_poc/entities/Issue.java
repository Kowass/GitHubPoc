package br.com.tcc.github_poc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "issues", schema = "public")
public class Issue {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repo_id", nullable = false)
    private GithubRepository repository;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "author_login")
    private String authorLogin;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @JsonIgnore
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<IssueAssignee> assignees = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<IssueLabel> labels = new ArrayList<>();
}