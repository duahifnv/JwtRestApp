package study.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import study.jwt.service.ScoreService;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;
    @GetMapping()
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getAllScores() {
        return scoreService.getAllScores();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getScoreById(@PathVariable Long id) {
        return scoreService.getScoreById(id);
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        return scoreService.getUserProfile(principal);
    }
    @PostMapping()
    public ResponseEntity<?> addNewScore(Principal principal, @RequestParam BigDecimal score) {
        return scoreService.addNewScore(principal, score);
    }
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScoreById(@PathVariable Long id) {
        return scoreService.deleteScoreById(id);
    }
}
