package study.jwt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import study.jwt.dto.ScoreResponse;
import study.jwt.entity.Score;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface ScoreMapper {
    @Mapping(target = "scoreDateTime", expression = "java(LocalDateTime.now())")
    Score toScore(BigDecimal score, Long userId);
    @Mapping(source = "scoreDateTime", target = "scoreDate", qualifiedByName = "toLocalDate")
    ScoreResponse toScoreResponse(Score score);
    List<ScoreResponse> toScoreResponses(List<Score> scores);
    @Named("toLocalDate")
    default LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }
}
