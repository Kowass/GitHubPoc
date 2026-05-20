package br.com.tcc.github_poc.metrics;

import br.com.tcc.github_poc.metrics.dto.CollaborationMetricsResponse;
import br.com.tcc.github_poc.metrics.dto.FlowMetricsResponse;
import br.com.tcc.github_poc.metrics.dto.InsightsMetricsResponse;
import br.com.tcc.github_poc.metrics.dto.OverviewMetricsResponse;
import br.com.tcc.github_poc.metrics.dto.RepoMetricsResponse;
import br.com.tcc.github_poc.metrics.service.CollaborationMetricsService;
import br.com.tcc.github_poc.metrics.service.FlowMetricsService;
import br.com.tcc.github_poc.metrics.service.InsightsMetricsService;
import br.com.tcc.github_poc.metrics.service.OverviewMetricsService;
import br.com.tcc.github_poc.metrics.service.RepoMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/poc/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final OverviewMetricsService overviewService;
    private final FlowMetricsService flowService;
    private final RepoMetricsService repoService;
    private final CollaborationMetricsService collaborationService;
    private final InsightsMetricsService insightsService;

    /**
     * Dashboard principal: volume de commits/PRs, taxa de aceitação e série diária.
     *
     * GET /api/poc/metrics/overview?repoId=123&authorLogin=Kowass&from=2025-05-19&to=2026-05-19
     */
    @GetMapping("/overview")
    public ResponseEntity<OverviewMetricsResponse> overview(
            @RequestParam Long repoId,
            @RequestParam String authorLogin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(overviewService.compute(repoId, authorLogin, from, to));
    }

    /**
     * Atividade e Fluxo: Cycle Time, Lead Time, TCM, Time in Review, dias ativos e recentes.
     *
     * GET /api/poc/metrics/flow?repoId=123&authorLogin=Kowass&from=2025-05-19&to=2026-05-19
     */
    @GetMapping("/flow")
    public ResponseEntity<FlowMetricsResponse> flow(
            @RequestParam Long repoId,
            @RequestParam String authorLogin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(flowService.compute(repoId, authorLogin, from, to));
    }

    /**
     * Análise por repositório: participação relativa e totais por repo.
     *
     * GET /api/poc/metrics/repos?authorLogin=Kowass&from=2025-05-19&to=2026-05-19
     */
    @GetMapping("/repos")
    public ResponseEntity<RepoMetricsResponse> repos(
            @RequestParam String authorLogin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(repoService.compute(authorLogin, from, to));
    }

    /**
     * Colaboração: distribuição de revisões, contribuidores ativos e comparativo individual vs equipe.
     *
     * GET /api/poc/metrics/collaboration?repoId=123&authorLogin=Kowass&from=2025-05-19&to=2026-05-19
     */
    @GetMapping("/collaboration")
    public ResponseEntity<CollaborationMetricsResponse> collaboration(
            @RequestParam Long repoId,
            @RequestParam String authorLogin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(collaborationService.compute(repoId, authorLogin, from, to));
    }

    /**
     * Insights: proporção feat/fix nos commits (Conventional Commits).
     *
     * GET /api/poc/metrics/insights?repoId=123&authorLogin=Kowass&from=2025-05-19&to=2026-05-19
     */
    @GetMapping("/insights")
    public ResponseEntity<InsightsMetricsResponse> insights(
            @RequestParam Long repoId,
            @RequestParam String authorLogin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(insightsService.compute(repoId, authorLogin, from, to));
    }
}
