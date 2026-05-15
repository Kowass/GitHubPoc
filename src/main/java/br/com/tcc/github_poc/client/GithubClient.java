package br.com.tcc.github_poc.client;

import br.com.tcc.github_poc.dto.GithubRepoResponse;
import br.com.tcc.github_poc.dto.GithubCommitResponse;
import br.com.tcc.github_poc.dto.GithubContributorResponse;
import br.com.tcc.github_poc.dto.GithubPullRequestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.tcc.github_poc.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "githubClient", url = "https://api.github.com")
public interface GithubClient {

    @GetMapping("/user/repos")
    List<GithubRepoResponse> getUserRepos(
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/repos/{owner}/{repo}/commits")
    List<GithubCommitResponse> getCommits(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

    @GetMapping("/repos/{owner}/{repo}/contributors")
    List<GithubContributorResponse> getContributors(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

    @GetMapping("/repos/{owner}/{repo}/pulls?state=all")
    List<GithubPullRequestResponse> getPullRequests(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

    @GetMapping("/repos/{owner}/{repo}/issues?state=all")
    List<GithubIssueResponse> getIssues(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

    @GetMapping("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews")
    List<GithubReviewResponse> getPullRequestReviews(
            @RequestHeader("Authorization") String token,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("pullNumber") int pullNumber
    );

    @PostMapping(value = "/graphql", consumes = "application/json")
    GraphQLCommitStatsResponse executeGraphQL(
            @RequestHeader("Authorization") String token,
            @RequestBody GraphQLRequest request
    );
}