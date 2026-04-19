package br.com.tcc.github_poc.controller;

import br.com.tcc.github_poc.client.GithubClient;
import br.com.tcc.github_poc.dto.GithubRepoResponse;
import br.com.tcc.github_poc.dto.GithubCommitResponse;
import br.com.tcc.github_poc.dto.GithubContributorResponse;
import br.com.tcc.github_poc.dto.GithubPullRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/poc/github")
public class GithubController {

    private final GithubClient githubClient;

    public GithubController(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @GetMapping("/repositorios")
    public ResponseEntity<List<GithubRepoResponse>> buscarRepositorios(
            @RequestHeader("Authorization") String token) {

        List<GithubRepoResponse> repos = githubClient.getUserRepos(token);
        return ResponseEntity.ok(repos);
    }

    @GetMapping("/{owner}/{repo}/commits")
    public ResponseEntity<List<GithubCommitResponse>> buscarCommits(
            @RequestHeader("Authorization") String token,
            @PathVariable String owner,
            @PathVariable String repo) {

        List<GithubCommitResponse> commits = githubClient.getCommits(token, owner, repo);
        return ResponseEntity.ok(commits);
    }

    @GetMapping("/{owner}/{repo}/contribuidores")
    public ResponseEntity<List<GithubContributorResponse>> buscarContribuidores(
            @RequestHeader("Authorization") String token,
            @PathVariable String owner,
            @PathVariable String repo) {

        List<GithubContributorResponse> contribuidores = githubClient.getContributors(token, owner, repo);
        return ResponseEntity.ok(contribuidores);
    }

    @GetMapping("/{owner}/{repo}/prs")
    public ResponseEntity<List<GithubPullRequestResponse>> buscarPullRequests(
            @RequestHeader("Authorization") String token,
            @PathVariable String owner,
            @PathVariable String repo) {

        List<GithubPullRequestResponse> prs = githubClient.getPullRequests(token, owner, repo);
        return ResponseEntity.ok(prs);
    }
}