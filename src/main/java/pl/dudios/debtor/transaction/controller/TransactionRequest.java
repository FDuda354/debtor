package pl.dudios.debtor.transaction.controller;

import java.math.BigDecimal;

public record TransactionRequest(
        Long debtId,
        BigDecimal amount,
        String description
) {
}
