package study.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.jwt.dto.UserProfileResponse;
import study.jwt.dto.exception.ApplicationError;
import study.jwt.entity.Score;
import study.jwt.entity.User;
import study.jwt.mapper.ScoreMapper;
import study.jwt.repository.ScoreRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final UserService userService;
    private final ScoreMapper scoreMapper;
    public ResponseEntity<?> getAllScores() {
        List<Score> scoreList = scoreRepository.findAll();
        return scoreList.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApplicationError(HttpStatus.NOT_FOUND.value(),
                                "Score list is empty")) :
                ResponseEntity.ok(scoreList);
    }
    public ResponseEntity<?> getAllScoresByUsername(String username) {
        List<Score> scoreList = scoreRepository.findScoresByUsername(username);
        return scoreList.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(scoreList);
    }
    public ResponseEntity<?> getScoreById(Long id) {
        return ResponseEntity.ofNullable(scoreRepository.findById(id).orElse(null));
    }
    public ResponseEntity<?> getUserProfile(Principal principal) {
        Long userId = getUserIdByUsername(principal.getName());
        return ResponseEntity.ok(
                UserProfileResponse.builder()
                .username(principal.getName())
                .amountOfScores(
                        scoreRepository.countScoresByUserId(userId)
                )
                .bestScore(
                        scoreRepository.findTopByUserIdOrderByScoreDesc(userId)
                        .stream().findFirst().map(Score::getScore).orElse(BigDecimal.ZERO)
                )
                .scoreList(
                        scoreMapper.toScoreResponses(
                                scoreRepository.findScoresByUsername(principal.getName())
                        )
                )
                .build()
        );
    }
    public ResponseEntity<?> addNewScore(Principal principal, BigDecimal score) {
        if (score.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Score shouldn't be less than zero");
        }
        return ResponseEntity.ok(
                scoreRepository.save(
                        scoreMapper.toScore(score, getUserIdByUsername(principal.getName()))
                )
        );
    }
    public ResponseEntity<?> deleteScoreById(Long id) {
        if (scoreRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scoreRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    private Long getUserIdByUsername(String username) {
        Optional<User> userByUsername = userService.findUserByUsername(username);
        return userByUsername.get().getUserId();
    }
}
