package my.mys.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import my.mys.service.MovementsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MovementsController {

    private final MovementsService movementsService;

    @GetMapping("/outflowsAtMonth")
    public ResponseEntity<String> getOutflowsAtMonth(@RequestParam Integer month) {
        return ResponseEntity.ok(movementsService.outflowsAtMonth(month));
    }

    @GetMapping("/incomeAtMonth")
    public ResponseEntity<String> getIncomeAtMonth(@RequestParam Integer month) {
        return ResponseEntity.ok(movementsService.incomeAtMonth(month));
    }

    @GetMapping("/balanceAtMonth")
    public ResponseEntity<String> getBalanceAtMonth(@RequestParam Integer month) {
        return ResponseEntity.ok(movementsService.balanceAtMonth(month));
    }

}
