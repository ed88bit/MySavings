package my.mys.service;

import lombok.AllArgsConstructor;
import my.mys.repository.MovementsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class MovementsService {

    private final MovementsRepository movementsRepository;

    public String outflowsAtMonth(Integer month) {

        String outflowAmount = movementsRepository.getOutflowsAtMonth(month);

        return outflowAmount;
    }

    public String incomeAtMonth(Integer month) {

        String incomeAmount = movementsRepository.getIncomeAtMonth(month);

        return incomeAmount;

    }

    public String balanceAtMonth(Integer month) {

        double incomeAmount = Double.parseDouble(incomeAtMonth(month));
        double outflowAmount = Double.parseDouble(outflowsAtMonth(month));

        return String.valueOf(BigDecimal.valueOf(incomeAmount + outflowAmount).setScale(2, RoundingMode.HALF_EVEN));

    }
}
