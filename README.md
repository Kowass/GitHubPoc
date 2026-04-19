# PoC - Integração GitHub API (TCC)

## Sobre o Projeto
Esta é uma Prova de Conceito (PoC) desenvolvida para a validação técnica inicial do Trabalho de Conclusão de Curso (TCC). O objetivo principal é validar a integração com a API REST do GitHub e testar a extração de métricas de produtividade de programadores, como commits, pull requests e contribuidores de um repositório.

Para agilizar os testes e focar apenas na validação dos dados que integrarão o dashboard oficial, este projeto foi construído de forma *stateless* (sem persistência em base de dados), funcionando como um proxy repassador de requisições.

## Tecnologias Utilizadas
* **Java 21**
* **Spring Boot** (Spring Web)
* **Spring Cloud OpenFeign** (Cliente HTTP declarativo)
* **Maven**

## Estrutura do Projeto
O projeto segue boas práticas de arquitetura em camadas e princípios SOLID:
* `client`: Interfaces Feign para comunicação externa com a API do GitHub, abstraindo a complexidade do HTTP.
* `controller`: Endpoints REST que recebem as chamadas locais e repassam para a camada de cliente.
* `dto`: Objetos de Transferência de Dados (utilizando `Records` do Java) para mapear, filtrar e extrair apenas os dados relevantes dos payloads extensos retornados pelo GitHub.

## Como Executar e Testar

### 1. Requisitos
* Java 21 instalado no sistema.
* Uma IDE (como IntelliJ IDEA ou Eclipse).
* Um [Personal Access Token (PAT)](https://github.com/settings/tokens) do GitHub (Classic ou Fine-grained) com permissões de leitura de repositórios (`repo` ou `public_repo`).
* Postman (ou cliente HTTP similar) para a realização das requisições.

### 2. Rodar a Aplicação
Abra o projeto na sua IDE e execute a classe principal `GithubPocApplication.java` (certifique-se de que a anotação `@EnableFeignClients` está presente). A aplicação iniciará o servidor Tomcat embutido na porta `8080`.

### 3. Autenticação nas Requisições
A API do GitHub exige autenticação. Todas as requisições feitas ao seu `localhost` devem incluir o cabeçalho de autorização contendo o seu token do GitHub. No Postman, configure a aba **Headers**:
* **Key:** `Authorization`
* **Value:** `Bearer SEU_TOKEN_AQUI`

## Endpoints Disponíveis

A API base local está acessível em: `http://localhost:8080/api/poc/github`

### 1. Listar Repositórios do Utilizador Autenticado
* **Método:** `GET`
* **Rota:** `/repositorios`
* **Descrição:** Retorna a lista de repositórios aos quais o dono do token tem acesso. Extrai informações como o ID, nome, URL, quantidade de *stars* e os dados do proprietário (*owner*).

### 2. Listar Histórico de Commits
* **Método:** `GET`
* **Rota:** `/{owner}/{repo}/commits`
* **Descrição:** Retorna o histórico de commits de um repositório específico. O JSON de resposta resolve o aninhamento de dados para trazer detalhes diretos do autor (nome, email, data) e a mensagem de cada commit.

### 3. Listar Contribuidores do Projeto
* **Método:** `GET`
* **Rota:** `/{owner}/{repo}/contribuidores`
* **Descrição:** Retorna os programadores que contribuíram para o repositório. Inclui o `login`, link do avatar e a **quantidade total de contribuições** (commits) de cada um, sendo crucial para a análise de participação relativa.

### 4. Listar Pull Requests (Todas)
* **Método:** `GET`
* **Rota:** `/{owner}/{repo}/prs`
* **Descrição:** Retorna todas as Pull Requests do repositório, incluindo as abertas, fechadas e mergeadas. Detalha o estado atual, o criador, a data de criação e a data de *merge* (se aplicável), permitindo a análise do fluxo de revisão de código.

---
*Projeto elaborado para a validação da estrutura de dados da integração base do TCC.*