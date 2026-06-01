package com.example.truthordare.Controller;

import com.example.truthordare.Controller.DTO.TruthUpdateDto;
import com.example.truthordare.Controller.DTO.DareUpdateDto;
import com.example.truthordare.model.DareAction;
import com.example.truthordare.model.Player;
import com.example.truthordare.model.TruthQuestion;
import com.example.truthordare.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("activePlayers", gameService.getActivePlayers());
        model.addAttribute("historyPlayers", gameService.getAllPlayersFromHistory());
        model.addAttribute("newPlayer", new Player());
        return "index";
    }

    @PostMapping("/addPlayer")
    public String addPlayer(@ModelAttribute Player newPlayer) {
        gameService.addPlayer(newPlayer.getName(), newPlayer.getGender());
        return "redirect:/";
    }

    @PostMapping("/addPreviousPlayer")
    public String addPreviousPlayer(@RequestParam Long playerId) {
        gameService.addPreviousPlayer(playerId);
        return "redirect:/";
    }

    @GetMapping("/play")
    public String play(@RequestParam(value = "playerName", required = false) String playerName, Model model) {
        Player selected = null;
        if (playerName != null && !playerName.isEmpty()) {
            selected = gameService.getActivePlayers().stream()
                    .filter(p -> p.getName().equals(playerName))
                    .findFirst().orElse(null);
        }
        if (selected == null) {
            selected = gameService.getRandomActivePlayer();
        }
        model.addAttribute("selectedPlayer", selected);
        model.addAttribute("activePlayers", gameService.getActivePlayers());
        return "play";
    }

    @GetMapping("/getTruth")
    @ResponseBody
    public String getTruth(@RequestParam(required = false) List<String> difficulties) {
        return gameService.getRandomTruth(difficulties);
    }

    @GetMapping("/getDare")
    @ResponseBody
    public String getDare(@RequestParam(required = false) List<String> difficulties) {
        return gameService.getRandomDare(difficulties);
    }

    @GetMapping("/nextPlayer")
    public String nextPlayer(@RequestParam String currentPlayerName) {
        Player next = gameService.getNextActivePlayer(currentPlayerName);
        if (next == null) {
            return "redirect:/";
        }
        String encodedName = URLEncoder.encode(next.getName(), StandardCharsets.UTF_8);
        return "redirect:/play?playerName=" + encodedName;
    }

    @PostMapping("/addQuestion")
    @ResponseBody
    public Map<String, String> addQuestion(@RequestParam String text, @RequestParam String type, @RequestParam String difficulty) {
        Map<String, String> response = new HashMap<>();
        try {
            if ("truth".equals(type)) {
                gameService.addTruth(text, difficulty);
            } else if ("dare".equals(type)) {
                gameService.addDare(text, difficulty);
            }
            response.put("status", "success");
            response.put("message", "Задание добавлено!");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/admin/truth/update")
    public String updateTruth(@RequestParam Long id,
                              @RequestParam String text,
                              @RequestParam String difficulty) {
        gameService.updateTruth(id, text, difficulty);
        return "redirect:/admin";
    }

    @PostMapping("/admin/dare/update")
    public String updateDare(@RequestParam Long id,
                             @RequestParam String text,
                             @RequestParam String difficulty) {
        gameService.updateDare(id, text, difficulty);
        return "redirect:/admin";
    }
    @PostMapping("/admin/truth/update-all")
    @ResponseBody
    public Map<String, String> updateAllTruths(@RequestBody List<TruthUpdateDto> updates) {
        for (TruthUpdateDto dto : updates) {
            gameService.updateTruth(dto.getId(), dto.getText(), dto.getDifficulty());
        }
        return Map.of("status", "success", "message", "Обновлено " + updates.size() + " вопросов");
    }

    @PostMapping("/admin/dare/update-all")
    @ResponseBody
    public Map<String, String> updateAllDares(@RequestBody List<DareUpdateDto> updates) {
        for (DareUpdateDto dto : updates) {
            gameService.updateDare(dto.getId(), dto.getText(), dto.getDifficulty());
        }
        return Map.of("status", "success", "message", "Обновлено " + updates.size() + " действий");
    }

    @PostMapping("/removeActive")
    public String removeActive(@RequestParam String playerName) {
        gameService.removeFromActive(playerName);
        return "redirect:/";
    }

    @PostMapping("/deleteHistory")
    public String deleteHistory(@RequestParam Long playerId) {
        gameService.deleteFromHistory(playerId);
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("truths", gameService.getAllTruthQuestions());
        model.addAttribute("dares", gameService.getAllDareActions());
        return "admin";
    }

    @PostMapping("/admin/truth/delete")
    public String deleteTruth(@RequestParam Long id) {
        gameService.deleteTruth(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/dare/delete")
    public String deleteDare(@RequestParam Long id) {
        gameService.deleteDare(id);
        return "redirect:/admin";
    }
}
