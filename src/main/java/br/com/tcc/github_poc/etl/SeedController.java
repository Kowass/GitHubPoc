package br.com.tcc.github_poc.etl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/poc/etl")
public class SeedController {

    private final SeedOrchestrator orchestrator;
    private final SeedJobState jobState;

    @PostMapping("/seed")
    public ResponseEntity<String> seed(@RequestHeader("Authorization") String token) {
        if (jobState.isRunning()) {
            return ResponseEntity.status(409).body("ETL já está em execução. Consulte GET /api/poc/etl/status.");
        }
        orchestrator.start(token);
        return ResponseEntity.accepted().body("ETL iniciado. Acompanhe em GET /api/poc/etl/status.");
    }

    @GetMapping("/status")
    public ResponseEntity<SeedJobState.Snapshot> status() {
        return ResponseEntity.ok(jobState.snapshot());
    }
}
