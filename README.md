# TCC Dashboard — GitHub Metrics API

Dashboard stateful de métricas de produtividade individual e de equipe baseado em dados do GitHub, com persistência em Supabase/PostgreSQL e documentação interativa via OpenAPI/Swagger.

## Sobre o Projeto

Este é um backend Spring Boot 4.0.5 que:
- **Ingere dados** do GitHub via API REST + GraphQL (Feign + RestClient)
- **Persiste** em Supabase/PostgreSQL com otimizações de batch e N+1 queries
- **Fornece 5 endpoints de métricas** calculadas sobre dados históricos (Cycle Time, Lead Time, TCM, etc.)
- **Expõe documentação interativa** via Swagger UI (OpenAPI 3.0)

O frontend **não consome a GitHub API em runtime** — todas as métricas são calculadas sobre dados persistidos, garantindo performance e auditoria.

## Stack Técnico

| Componente | Tecnologia |
|---|---|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 4.0.5 |
| **HTTP Client** | Spring Cloud OpenFeign (REST) + GraphQL manual |
| **HTTP Paginado** | Spring RestClient |
| **Persistência** | Spring Data JPA + PostgreSQL (Supabase) |
| **Utilitários** | Lombok, Jackson |
| **Validação** | Hibernate Validator (JSR-303) |
| **Documentação** | Springdoc OpenAPI 2.8.8 |

## Estrutura do Projeto

```
br.com.tcc.github_poc
├── client/              # GithubClient (Feign): REST + GraphQL contra api.github.com
├── controller/          # GithubController (PoC original)
├── etl/                 # Motor de seeding assíncrono
│   ├── SeedController
│   ├── SeedOrchestrator (@Async)
│   ├── SeedJobState
│   ├── ingestion/       # 6 ingestion services (Commit, PR, Issue, Review, etc.)
│   ├── extraction/      # PaginatedRestFetcher + GraphQLCommitFetcher
│   ├── mapper/          # DtoToEntityMapper
│   └── ratelimit/       # RateLimitGuard
├── metrics/             # 5 endpoints de métricas + cálculos
│   ├── MetricsController
│   ├── service/         # OverviewMetricsService, FlowMetricsService, etc.
│   ├── dto/             # Response DTOs
│   └── support/         # ConventionalCommitClassifier
├── entities/            # JPA entities (User, Commit, PR, Issue, Review, etc.)
├── repositories/        # Spring Data JPA repositories
├── dto/                 # DTOs de cliente/ingestão
├── config/              # Configuração (OpenAPI, etc.)
└── client/              # Feign clients
```

## Como Executar

### 1. Requisitos

- **Java 21** instalado
- **Maven 3.8+**
- **PostgreSQL/Supabase** com acesso via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- **Token do GitHub** (Classic ou Fine-grained) com `repo` ou `public_repo`

### 2. Configuração de Ambiente

Criar `.env` na raiz do projeto:

```properties
DB_URL=jdbc:postgresql://<host>:<port>/<database>
DB_USERNAME=<username>
DB_PASSWORD=<password>
SEED_REPOS=axios/axios,owner/repo2
SEED_SINCE=2025-05-19
```

### 3. Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação iniciará em `http://localhost:8080`.

## Endpoints Disponíveis

### 📊 Swagger UI (Documentação Interativa)

**URL:** `http://localhost:8080/swagger-ui.html`

Toda a API está auto-documentada com:
- Descrição de cada endpoint
- Parâmetros com exemplos e validação
- Modelos de resposta com detalhes de erros
- Botão "Try it out" para testar
- Respostas de erro documentadas (400, 404, 500)

### 🔌 Endpoints de Métricas

**Base:** `/api/poc/metrics`

Todos requerem `authorLogin` (obrigatório, `@NotBlank`) e opcionalmente `from` / `to` (ISO-8601 dates).

| Endpoint | Descrição | Validação |
|---|---|---|
| `GET /overview` | Volume de commits/PRs, taxa de aceitação e série diária | `repoId` obrigatório e positivo |
| `GET /flow` | Cycle Time, Lead Time, TCM, Time in Review, dias ativos | `repoId` obrigatório e positivo |
| `GET /repos` | Participação relativa por repositório | Sem `repoId` |
| `GET /collaboration` | Distribuição de revisões, comparativo individual vs equipe | `repoId` obrigatório e positivo |
| `GET /insights` | Classificação Conventional Commits (feat/fix/other) | `repoId` obrigatório e positivo |

**Validação aplicada:**
- ✓ `repoId` deve ser positivo (`@Positive`)
- ✓ `authorLogin` não pode ser branco (`@NotBlank`)
- ✓ `from` ≤ `to` obrigatoriamente
- ✓ `repoId` deve existir no banco de dados (404 se não encontrado)

**Exemplo:**
```bash
curl "http://localhost:8080/api/poc/metrics/overview?repoId=23088740&authorLogin=DigitalBrainJS&from=2025-05-19&to=2026-05-19"
```

**Resposta de erro (exemplo):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Repository not found: 999999999",
  "path": "/api/poc/metrics/overview",
  "timestamp": "2026-05-20T10:30:00Z"
}
```

### 🌱 Endpoints de ETL

**Base:** `/api/poc/etl`

| Endpoint | Método | Descrição |
|---|---|---|
| `/seed` | `POST` | Iniciar carga massiva de dados (header: `Authorization: Bearer <token>`) |
| `/status` | `GET` | Status do job de seed |

**Exemplo:**
```bash
curl -X POST http://localhost:8080/api/poc/etl/seed \
  -H "Authorization: Bearer ghp_xxxxxxxxxx"

curl http://localhost:8080/api/poc/etl/status
```

## Testando a API

### Via Swagger UI

1. Abrir `http://localhost:8080/swagger-ui.html`
2. Clicar em qualquer endpoint
3. Clicar em "Try it out"
4. Preencher parâmetros
5. Clicar "Execute"

### Via cURL

```bash
# Obter métricas de overview (sucesso)
curl "http://localhost:8080/api/poc/metrics/overview?repoId=23088740&authorLogin=DigitalBrainJS"

# Erro: authorLogin vazio (400)
curl "http://localhost:8080/api/poc/metrics/overview?repoId=23088740&authorLogin="

# Erro: repoId não existe (404)
curl "http://localhost:8080/api/poc/metrics/overview?repoId=999999999&authorLogin=DigitalBrainJS"

# Erro: from > to (400)
curl "http://localhost:8080/api/poc/metrics/overview?repoId=23088740&authorLogin=DigitalBrainJS&from=2026-01-01&to=2025-01-01"

# Iniciar seed
curl -X POST http://localhost:8080/api/poc/etl/seed \
  -H "Authorization: Bearer ghp_xxxxx"

# Status do seed
curl http://localhost:8080/api/poc/etl/status
```

### Via Postman

1. Importar `http://localhost:8080/api-docs` (OpenAPI JSON)
2. Usar as coleções geradas automaticamente
3. Configurar variáveis de ambiente (`repoId`, `authorLogin`, etc.)

## Repositório de Teste

| Campo | Valor |
|---|---|
| **Repo** | `axios/axios` |
| **repoId** | `23088740` |
| **Usuário de teste** | `DigitalBrainJS` |
| **Período padrão** | `from=2025-05-19&to=2026-05-19` |

## Referência de Métricas

Veja [`metricas-TCC.md`](metricas-TCC.md) para especificação completa de:
- Definição de cada métrica (Cycle Time, Lead Time, TCM, etc.)
- Fórmulas de cálculo
- Visualizações esperadas no dashboard
- Edge cases e tratamento de dados vazios

## Arquitetura de Dados

### ETL (Motor de Seeding)

1. **Extração:** GraphQL para commits (cursor-paginated + `since` nativo), REST paginado para PRs/Issues/Reviews
2. **Rate Limit:** `RateLimitGuard` pausa automaticamente quando `X-RateLimit-Remaining < threshold`
3. **Persistência:** Batch pre-check (`findAllById`) + `Persistable<ID>` para evitar N+1 queries
4. **Idempotência:** Re-runs seguras — entidades duplicadas não são re-inseridas

### Cálculo de Métricas

- **Projeções SQL:** Aggregations nativas (AVG, MIN, MAX, COUNT) via `@Query` e Spring Data projections
- **Série temporal:** Preenchimento de gaps em Java (zero-fill), não em SQL
- **Período:** Defaults a 1 ano atrás / hoje se não especificado

## Decisões Arquiteturais

- ✓ **Persistência stateful:** Todos os dados em Supabase para auditoria e performance
- ✓ **BatchPreCheck:** 1 SELECT em lote + N INSERTs diretos vs N SELECTs + N INSERTs
- ✓ **GraphQL para Commits:** Mais eficiente que REST, retorna `additions`/`deletions` nativamente
- ✓ **Spring Data Projections:** Aggregations SQL reduzem transferência de dados
- ✓ **OpenAPI/Swagger:** Documentação auto-sincronizada com código (sem divergência)
- ✓ **Validação em camada:** JSR-303 (`@NotBlank`, `@Positive`) + validação de negócio (`from ≤ to`, `repoId` existe)
- ✓ **Tratamento centralizado de erros:** `@RestControllerAdvice` com `ErrorResponse` estruturado e documentado no Swagger

---

**Desenvolvido por:** Felipe de Sousa Alves, Matheus de Oliveira Bezerra & Enzo Girão.
