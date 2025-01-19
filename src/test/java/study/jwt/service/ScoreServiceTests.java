package study.jwt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.jwt.dto.exception.ApplicationError;
import study.jwt.entity.Score;
import study.jwt.entity.User;
import study.jwt.mapper.ScoreMapper;
import study.jwt.repository.ScoreRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTests {
    @Mock
    ScoreRepository scoreRepository;
    @Mock
    UserService userService;
    @Mock
    ScoreMapper scoreMapper;
    @InjectMocks
    ScoreService scoreService;
    @Test
    @DisplayName("Метод ScoreService#getAllScores " +
            "возвращает HTTP-ответ со статусом 200 OK и списком результатов," +
            " если список не пуст")
    void getAllScores_ScoreListIsNotNull_ReturnsValidResponseEntity() {
        // Given
        List<Score> scoreList = List.of(
                Score.builder().scoreId(0L).userId(0L).score(BigDecimal.valueOf(1.01))
                        .scoreDateTime(LocalDateTime.now().minusHours(1)).build(),
                Score.builder().scoreId(1L).userId(1L).score(BigDecimal.valueOf(2.31))
                        .scoreDateTime(LocalDateTime.now().minusMinutes(30)).build()
        );
        doReturn(scoreList).when(scoreRepository).findAll();
        // When
        ResponseEntity<?> responseEntity = scoreService.getAllScores();
        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(scoreList, responseEntity.getBody());
    }
    @DisplayName("Метод ScoreService#getAllScores " +
            "возвращает HTTP-ответ со статусом 404 NOT_FOUND и сообщением о пустом списке," +
            " если список пуст")
    @Test
    void getAllScores_ScoreListIsNull_ReturnsValidResponseEntity() {
        // Given
        doReturn(EMPTY_LIST).when(scoreRepository).findAll();
        // When
        ResponseEntity<?> responseEntity = scoreService.getAllScores();
        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @DisplayName("Метод ScoreService#addNewScore " +
            "возвращает HTTP-ответ со статусом 200 OK и json-объектом созданного результата," +
            " если значение результата не меньше 0")
    @Test
    void addNewScore_ScoreIsValid_ReturnsValidResponseEntity() {
        // Given
        String username = "username_1";
        BigDecimal userScore = BigDecimal.valueOf(1.00);
        doReturn(Optional.of(User.builder().userId(1L).build()))
                .when(userService)
                .findUserByUsername(username);
        // When
        ResponseEntity<?> responseEntity = scoreService.addNewScore(() -> username, userScore);
        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        if (responseEntity.getBody() instanceof Score score) {
            assertEquals(scoreService.getUserIdByUsername(username), score.getUserId());
            assertEquals(userScore, score.getScore());
        }
    }
    @DisplayName("Метод ScoreService#addNewScore " +
            "возвращает HTTP-ответ со статусом 400 BAD_REQUEST и сообщением об ошибочных данных" +
            " если значение результата меньше 0")
    @Test
    void addNewScore_ScoreIsNotValid_ReturnsValidResponseEntity() {
        // Given
        String username = "username_2";
        BigDecimal userScore = BigDecimal.valueOf(-1.00);
        // When
        ResponseEntity<?> responseEntity = scoreService.addNewScore(() -> username, userScore);
        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), "Score shouldn't be less than zero");
    }
}
