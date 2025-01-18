package study.jwt.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UserProfileResponse (String username,
                                   Integer amountOfScores,
                                   BigDecimal bestScore,
                                   List<ScoreResponse> scoreList) {}
