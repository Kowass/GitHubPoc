package br.com.tcc.github_poc.etl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/poc/etl")
@Tag(name = "ETL", description = "Ingestão de dados do GitHub no Supabase. Executa seed assíncrono via GraphQL + REST paginado.")
public class SeedController {

    private final SeedOrchestrator orchestrator;
    private final SeedJobState jobState;

    @Operation(
        summary = "Iniciar seed do ETL",
        description = "Inicia a carga assíncrona dos dados do GitHub (commits, PRs, issues, reviews) " +
                      "para os repositórios configurados em `etl.seed.repos`. Retorna 409 se já estiver em execução."
    )
    @PostMapping("/seed")
    public ResponseEntity<String> seed(
            @Parameter(description = "Token do GitHub no formato 'Bearer ghp_...'", required = true, example = "Bearer ghp_xxxxxxxxxxxx")
            @RequestHeader("Authorization") String token) {
        if (jobState.isRunning()) {
            return ResponseEntity.status(409).body("ETL já está em execução. Consulte GET /api/poc/etl/status.");
        }
        orchestrator.start(token);
        return ResponseEntity.accepted().body("ETL iniciado. Acompanhe em GET /api/poc/etl/status.");
    }

    @Operation(
        summary = "Status do ETL",
        description = "Retorna o snapshot do estado atual do job de seed: progresso por entidade, erros e status geral."
    )
    @GetMapping("/status")
    public ResponseEntity<SeedJobState.Snapshot> status() {
        return ResponseEntity.ok(jobState.snapshot());
    }
}
