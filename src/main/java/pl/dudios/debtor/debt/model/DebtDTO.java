package pl.dudios.debtor.debt.model;

import pl.dudios.debtor.customer.model.CustomerDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DebtDTO(
        Long id,
        CustomerDTO customer,
        BigDecimal amount,
        BigDecimal startedAmount,
        String description,
        LocalDateTime repaymentDate,
        LocalDateTime startDate,
        DebtStatus status
) {
}
