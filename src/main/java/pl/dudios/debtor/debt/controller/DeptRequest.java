package pl.dudios.debtor.debt.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeptRequest(
        String debtorEmail,
        String creditorEmail,
        BigDecimal amount,
        String description,
        LocalDateTime repaymentDate
) {
}
