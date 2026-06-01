package com.example.truthordare.model;

import javax.persistence.*;

@Entity
@Table(name = "dare_Actions")
public class DareAction {
    private String text;
    private String difficulty;

    public DareAction() {
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public DareAction(String text, Long id,String difficulty) {
        this.text = text;
        this.id = id;
        this.difficulty =difficulty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
