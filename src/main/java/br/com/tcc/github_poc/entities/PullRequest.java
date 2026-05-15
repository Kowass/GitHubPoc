package br.com.tcc.github_poc.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pull_requests", schema = "public")
public class PullRequest {

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

    @Column(name = "merged_at")
    private LocalDateTime mergedAt;

    @Column(name = "author_login")
    private String authorLogin;
}