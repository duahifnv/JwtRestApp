package study.jwt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScoreResponse (BigDecimal score, LocalDate scoreDate) {}
