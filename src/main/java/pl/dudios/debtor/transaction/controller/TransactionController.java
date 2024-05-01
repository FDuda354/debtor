package pl.dudios.debtor.transaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.transaction.model.Transaction;
import pl.dudios.debtor.transaction.service.TransactionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<Transaction>> getTransactionByDebtId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                    @RequestParam(value = "debtId") Long debtId) {

        return ResponseEntity.ok(transactionService.getTransactionByDebtId(debtId, page, size));
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionRequest request) {
        transactionService.addTransaction(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file")
    public ResponseEntity<Void> addTransactionFile(@RequestParam("transactionId") Long transactionId,
                                                   @RequestParam("file") MultipartFile file) {

        transactionService.addTransactionFiles(transactionId, List.of(file));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/files")
    public ResponseEntity<Void> addTransactionFiles(@RequestParam("transactionId") Long transactionId,
                                                    @RequestParam("files") List<MultipartFile> files) {
        transactionService.addTransactionFiles(transactionId, files);

        return ResponseEntity.noContent().build();
    }
}
