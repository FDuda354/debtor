package pl.dudios.debtor.transaction.controller;

import pl.dudios.debtor.files.model.File;

import java.math.BigDecimal;
import java.util.List;

public record TransactionRequest(
        Long debtId,
        BigDecimal amount,
        String description,
        List<File> files
) {
}
