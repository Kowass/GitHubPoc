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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Metrics", description = "Métricas de produtividade individual e de equipe calculadas sobre dados ingeridos do GitHub")
public class MetricsController {

    private final OverviewMetricsService overviewService;
    private final FlowMetricsService flowService;
    private final RepoMetricsService repoService;
    private final CollaborationMetricsService collaborationService;
    private final InsightsMetricsService insightsService;

    @Operation(
        summary = "Dashboard principal",
        description = "Volume de commits/PRs, taxa de aceitação de PRs e série diária de atividade do usuário vs equipe."
    )
    @GetMapping("/overview")
    public ResponseEntity<OverviewMetricsResponse> overview(
            @Parameter(description = "ID do repositório no Supabase", example = "23088740", required = true)
            @RequestParam Long repoId,
            @Parameter(description = "Login do usuário no GitHub", example = "DigitalBrainJS", required = true)
            @RequestParam String authorLogin,
            @Parameter(description = "Início do período (ISO-8601)", example = "2025-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fim do período (ISO-8601)", example = "2026-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(overviewService.compute(repoId, authorLogin, from, to));
    }

    @Operation(
        summary = "Atividade e Fluxo",
        description = "Cycle Time, Lead Time, TCM (linhas/commit), Time in Review, dias ativos e últimas atividades."
    )
    @GetMapping("/flow")
    public ResponseEntity<FlowMetricsResponse> flow(
            @Parameter(description = "ID do repositório no Supabase", example = "23088740", required = true)
            @RequestParam Long repoId,
            @Parameter(description = "Login do usuário no GitHub", example = "DigitalBrainJS", required = true)
            @RequestParam String authorLogin,
            @Parameter(description = "Início do período (ISO-8601)", example = "2025-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fim do período (ISO-8601)", example = "2026-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(flowService.compute(repoId, authorLogin, from, to));
    }

    @Operation(
        summary = "Análise por repositório",
        description = "Participação relativa e totais de commits/PRs por repositório para o usuário informado. Não requer repoId."
    )
    @GetMapping("/repos")
    public ResponseEntity<RepoMetricsResponse> repos(
            @Parameter(description = "Login do usuário no GitHub", example = "DigitalBrainJS", required = true)
            @RequestParam String authorLogin,
            @Parameter(description = "Início do período (ISO-8601)", example = "2025-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fim do período (ISO-8601)", example = "2026-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(repoService.compute(authorLogin, from, to));
    }

    @Operation(
        summary = "Colaboração",
        description = "Distribuição de revisões por revisor, número de contribuidores ativos e comparativo individual vs média da equipe."
    )
    @GetMapping("/collaboration")
    public ResponseEntity<CollaborationMetricsResponse> collaboration(
            @Parameter(description = "ID do repositório no Supabase", example = "23088740", required = true)
            @RequestParam Long repoId,
            @Parameter(description = "Login do usuário no GitHub", example = "DigitalBrainJS", required = true)
            @RequestParam String authorLogin,
            @Parameter(description = "Início do período (ISO-8601)", example = "2025-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fim do período (ISO-8601)", example = "2026-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(collaborationService.compute(repoId, authorLogin, from, to));
    }

    @Operation(
        summary = "Insights de commits",
        description = "Classificação dos commits por padrão Conventional Commits (feat/fix/other) para o usuário e para a equipe."
    )
    @GetMapping("/insights")
    public ResponseEntity<InsightsMetricsResponse> insights(
            @Parameter(description = "ID do repositório no Supabase", example = "23088740", required = true)
            @RequestParam Long repoId,
            @Parameter(description = "Login do usuário no GitHub", example = "DigitalBrainJS", required = true)
            @RequestParam String authorLogin,
            @Parameter(description = "Início do período (ISO-8601)", example = "2025-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fim do período (ISO-8601)", example = "2026-05-19")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(insightsService.compute(repoId, authorLogin, from, to));
    }
}
