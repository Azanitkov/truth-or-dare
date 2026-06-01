package com.example.truthordare.model;

import javax.persistence.*;

@Entity
@Table(name = "truth_Questions")
public class TruthQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String difficulty;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public TruthQuestion() {

    }

    public TruthQuestion(Long id, String text, String difficulty) {
        this.id = id;
        this.text = text;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
