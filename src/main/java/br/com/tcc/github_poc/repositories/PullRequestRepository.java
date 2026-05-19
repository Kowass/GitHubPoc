package br.com.tcc.github_poc.repositories;

import br.com.tcc.github_poc.entities.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

    @Query("SELECT p FROM PullRequest p")
    List<PullRequest> findAllPullRequests();

    @Query("SELECT p FROM PullRequest p WHERE p.id = :id")
    Optional<PullRequest> findPullRequestById(@Param("id") Long id);

    @Query("""
           SELECT p FROM PullRequest p
           WHERE p.repository.id = :repoId
           ORDER BY p.createdAt DESC
           """)
    List<PullRequest> findPullRequestsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT p FROM PullRequest p
           WHERE p.repository.id = :repoId
           AND p.state = :state
           ORDER BY p.createdAt DESC
           """)
    List<PullRequest> findPullRequestsByRepositoryIdAndState(
            @Param("repoId") Long repoId,
            @Param("state") String state
    );

    @Query("""
           SELECT p FROM PullRequest p
           WHERE p.authorLogin = :authorLogin
           ORDER BY p.createdAt DESC
           """)
    List<PullRequest> findPullRequestsByAuthorLogin(@Param("authorLogin") String authorLogin);

    @Query("""
           SELECT p FROM PullRequest p
           WHERE p.repository.id = :repoId
           AND p.mergedAt IS NOT NULL
           ORDER BY p.mergedAt DESC
           """)
    List<PullRequest> findMergedPullRequestsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT COUNT(p) FROM PullRequest p
           WHERE p.repository.id = :repoId
           """)
    Long countPullRequestsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT COUNT(p) FROM PullRequest p
           WHERE p.repository.id = :repoId
           AND p.state = :state
           """)
    Long countPullRequestsByRepositoryIdAndState(
            @Param("repoId") Long repoId,
            @Param("state") String state
    );

    @Query("""
           SELECT COUNT(p) FROM PullRequest p
           WHERE p.repository.id = :repoId
           AND p.mergedAt IS NOT NULL
           """)
    Long countMergedPullRequestsByRepositoryId(@Param("repoId") Long repoId);
}