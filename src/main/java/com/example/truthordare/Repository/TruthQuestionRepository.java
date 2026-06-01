package com.example.truthordare.Repository;

import com.example.truthordare.model.TruthQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TruthQuestionRepository extends JpaRepository<TruthQuestion, Long> {
    @Query(value = "SELECT * FROM truth_questions ORDER BY RAND() LIMIT 1", nativeQuery = true)
    TruthQuestion findRandomTruth();

    @Query(value = "SELECT * FROM truth_questions WHERE difficulty IN (:difficulties) ORDER BY RAND() LIMIT 1", nativeQuery = true)
    TruthQuestion findRandomTruthByDifficulties(@Param("difficulties") List<String> difficulties);
}
