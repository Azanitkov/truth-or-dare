package com.example.truthordare.service;

import com.example.truthordare.Repository.DareActionRepository;
import com.example.truthordare.Repository.PlayerRepository;
import com.example.truthordare.Repository.TruthQuestionRepository;
import com.example.truthordare.model.DareAction;
import com.example.truthordare.model.Player;
import com.example.truthordare.model.TruthQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final PlayerRepository playerRepository;
    private final TruthQuestionRepository truthRepository;
    private final DareActionRepository dareRepository;


    public GameService(PlayerRepository playerRepository,
                       TruthQuestionRepository truthRepository,
                       DareActionRepository dareRepository) {
        this.playerRepository = playerRepository;
        this.truthRepository = truthRepository;
        this.dareRepository = dareRepository;
    }

    private final List<Player> activePlayers = new ArrayList<>();

    public void addPlayer(String name, String gender) {
        Optional<Player> existing = playerRepository.findByName(name);
        Player player;
        if (existing.isPresent()) {
            player = existing.get();
            if (gender != null && !gender.isEmpty() && (player.getGender() == null || player.getGender().isEmpty())) {
                player.setGender(gender);
                playerRepository.save(player);
            }
        } else {
            player = new Player(name, gender);
            playerRepository.save(player);
        }
        if (activePlayers.stream().noneMatch(p -> p.getName().equals(player.getName()))) {
            activePlayers.add(player);
        }
    }

    public void addPreviousPlayer(Long playerId) {
        playerRepository.findById(playerId).ifPresent(player -> {
            if (activePlayers.stream().noneMatch(p -> p.getName().equals(player.getName())))
                activePlayers.add(player);
        });

    }

    public List<Player> getAllPlayersFromHistory() {
        return playerRepository.findAll();
    }

    public List<Player> getActivePlayers() {
        return new ArrayList<>(activePlayers);
    }

    public Player getRandomActivePlayer() {
        if (activePlayers.isEmpty()) return null;
        return activePlayers.get((int) (Math.random() * activePlayers.size()));
    }

    public Player getRandomOtherActivePlayer(String currentName) {
        List<Player> others = activePlayers.stream()
                .filter(p -> !p.getName().equals(currentName))
                .collect(Collectors.toList());
        if (others.isEmpty()) return activePlayers.isEmpty() ? null : activePlayers.get(0);
        return others.get((int) (Math.random() * others.size()));
    }

    public String getRandomTruth(List<String> difficulties) {
        if (difficulties == null || difficulties.isEmpty()) {
            difficulties = Arrays.asList("EASY", "MEDIUM", "HARD");
        }
        TruthQuestion q = truthRepository.findRandomTruthByDifficulties(difficulties);
        if (q == null) {
            return "Нет вопросов для выбранной сложности. Добавьте через кнопку 'Добавить задание'.";
        }
        return q.getText();
    }

    public String getRandomDare(List<String> difficulties) {
        if (difficulties == null || difficulties.isEmpty()) {
            difficulties = Arrays.asList("EASY", "MEDIUM", "HARD");
        }
        DareAction a = dareRepository.findRandomDareByDifficulties(difficulties);
        if (a == null) {
            return "Нет действий для выбранной сложности. Добавьте через кнопку 'Добавить задание'.";
        }
        return a.getText();
    }

    public void addTruth(String text, String difficulty) {
        TruthQuestion q = new TruthQuestion();
        q.setText(text);
        q.setDifficulty(difficulty);
        truthRepository.save(q);
    }

    public void addDare(String text, String difficulty) {
        DareAction a = new DareAction();
        a.setText(text);
        a.setDifficulty(difficulty);
        dareRepository.save(a);
    }

    public void removeFromActive(String playerName) {
        activePlayers.removeIf(p -> p.getName().equals(playerName));
    }


    public void deleteFromHistory(Long playerId) {
        playerRepository.deleteById(playerId);
    }

    public List<TruthQuestion> getAllTruthQuestions() {
        return truthRepository.findAll();
    }

    public List<DareAction> getAllDareActions() {
        return dareRepository.findAll();
    }

    public void updateTruth(Long id, String text, String difficulty) {
        TruthQuestion q = truthRepository.findById(id).orElseThrow();
        q.setText(text);
        q.setDifficulty(difficulty);
        truthRepository.save(q);
    }


    public void updateDare(Long id, String newText, String difficulty) {
        DareAction a = dareRepository.findById(id).orElseThrow();
        a.setText(newText);
        a.setDifficulty(difficulty);
        dareRepository.save(a);
    }

    public Player getNextActivePlayer(String currentName) {
        List<Player> list = new ArrayList<>(activePlayers);
        if (list.isEmpty()) return null;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(currentName)) {
                int nextIndex = (i + 1) % list.size();
                return list.get(nextIndex);
            }
        }

        return list.get(0);
    }

    public void deleteTruth(Long id) {
        truthRepository.deleteById(id);
    }


    public void deleteDare(Long id) {
        dareRepository.deleteById(id);
    }
}

