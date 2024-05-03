package pl.dudios.debtor.debt.model;

import pl.dudios.debtor.customer.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DebtDTO(
        Long id,
        Customer customer,
        BigDecimal amount,
        String description,
        LocalDateTime repaymentDate,
        LocalDateTime startDate,
        DebtStatus status
) {
}
