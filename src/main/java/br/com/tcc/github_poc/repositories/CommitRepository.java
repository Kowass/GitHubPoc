package br.com.tcc.github_poc.repositories;

import br.com.tcc.github_poc.entities.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, String> {

    @Query("SELECT c FROM Commit c")
    List<Commit> findAllCommits();

    @Query("SELECT c FROM Commit c WHERE c.sha = :sha")
    Optional<Commit> findCommitBySha(@Param("sha") String sha);

    @Query("""
           SELECT c FROM Commit c
           WHERE c.repository.id = :repoId
           ORDER BY c.commitDate DESC
           """)
    List<Commit> findCommitsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT c FROM Commit c
           WHERE c.authorLogin = :authorLogin
           ORDER BY c.commitDate DESC
           """)
    List<Commit> findCommitsByAuthorLogin(@Param("authorLogin") String authorLogin);

    @Query("""
           SELECT c FROM Commit c
           WHERE c.repository.id = :repoId
           AND c.authorLogin = :authorLogin
           ORDER BY c.commitDate DESC
           """)
    List<Commit> findCommitsByRepositoryIdAndAuthorLogin(
            @Param("repoId") Long repoId,
            @Param("authorLogin") String authorLogin
    );

    @Query("""
           SELECT COUNT(c) FROM Commit c
           WHERE c.repository.id = :repoId
           """)
    Long countCommitsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT COALESCE(SUM(c.additions), 0) FROM Commit c
           WHERE c.repository.id = :repoId
           """)
    Long sumAdditionsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT COALESCE(SUM(c.deletions), 0) FROM Commit c
           WHERE c.repository.id = :repoId
           """)
    Long sumDeletionsByRepositoryId(@Param("repoId") Long repoId);

    @Query("""
           SELECT COALESCE(SUM(c.totalChanges), 0) FROM Commit c
           WHERE c.repository.id = :repoId
           """)
    Long sumTotalChangesByRepositoryId(@Param("repoId") Long repoId);
}