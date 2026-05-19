package br.com.tcc.github_poc.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "repository_contributors", schema = "public")
public class RepositoryContributor {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repo_id", nullable = false)
    private GithubRepository repository;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "contributions")
    private Integer contributions = 0;
}