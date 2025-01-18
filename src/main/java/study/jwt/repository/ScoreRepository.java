package study.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.jwt.entity.Score;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query("select s from Score s inner join User as u " +
            "on s.userId = u.userId where u.username = ?1")
    List<Score> findScoresByUsername(String username);

    Integer countScoresByUserId(Long userId);
    List<Score> findTopByUserIdOrderByScoreDesc(Long userId);
}
