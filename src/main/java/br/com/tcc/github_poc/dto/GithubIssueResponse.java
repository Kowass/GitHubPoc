package br.com.tcc.github_poc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GithubIssueResponse(
        Long id,
        int number,
        String title,
        String state,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("closed_at") String closedAt,
        List<LabelInfo> labels,
        List<OwnerInfo> assignees
) {}

record LabelInfo(String name) {}