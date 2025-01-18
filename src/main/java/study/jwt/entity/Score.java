package study.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_scores", schema = "public", catalog = "postgres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "score_id")
    private Long scoreId;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Column(name = "score")
    private BigDecimal score;
    @Basic
    @Column(name = "score_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime scoreDateTime;
}
