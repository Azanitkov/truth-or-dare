package com.example.truthordare.Repository;

import com.example.truthordare.model.DareAction;
import com.example.truthordare.model.TruthQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DareActionRepository extends JpaRepository<DareAction, Long> {
    @Query(value = "SELECT * FROM dare_actions ORDER BY RAND() LIMIT 1", nativeQuery = true)
    DareAction findRandomDare();
    @Query(value = "SELECT * FROM truth_questions WHERE difficulty IN (:difficulties) ORDER BY RAND() LIMIT 1", nativeQuery = true)
    DareAction findRandomDareByDifficulties(@Param("difficulties") List<String> difficulties);
}

