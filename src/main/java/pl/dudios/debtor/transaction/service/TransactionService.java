package pl.dudios.debtor.transaction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtStatus;
import pl.dudios.debtor.debt.repo.DeptRepository;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;
import pl.dudios.debtor.files.model.File;
import pl.dudios.debtor.files.service.FileService;
import pl.dudios.debtor.transaction.controller.TransactionRequest;
import pl.dudios.debtor.transaction.model.Transaction;
import pl.dudios.debtor.transaction.repo.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final DeptRepository deptRepository;
    private final FileService fileService;

    public void addTransaction(TransactionRequest request) {
        Debt debt = deptRepository.findById(request.debtId())
                .orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + request.debtId() + " not found"));

        if (request.amount().compareTo(debt.getAmount()) > 0) {
            log.error("Transaction amount is greater than debt amount");
            throw new RequestValidationException("Transaction amount is greater than debt amount");
        }

        if (debt.getTransactions() == null) {
            debt.setTransactions(new ArrayList<>());
        }

        Transaction transaction = Transaction.builder()
                .debt(debt)
                .balanceBeforeTransaction(debt.getAmount())
                .amount(request.amount())
                .balanceAfterTransaction(debt.getAmount().subtract(request.amount()))
                .description(request.description())
                .paymentDate(LocalDateTime.now())
                .files(request.files())
                .build();

        debt.getTransactions().add(transaction);
        debt.setAmount(transaction.getBalanceAfterTransaction());

        if (debt.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            debt.setStatus(DebtStatus.FINISHED);
        }

        transactionRepository.save(transaction);
    }

    private boolean debtIsPaid(Debt debt) {
        BigDecimal transactionsAmount = debt.getTransactions().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return transactionsAmount.compareTo(debt.getAmount()) >= 0;
    }

    public void addTransactionFiles(Long transactionId, List<MultipartFile> files) {
        List<File> filesToSave = new ArrayList<>();
        for (MultipartFile file : files) {
            filesToSave.add(fileService.uploadFile(file));
        }
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id: " + transactionId + " not found"));

        if (transaction.getFiles() == null) {
            transaction.setFiles(new ArrayList<>());
        }
        transaction.getFiles().addAll(filesToSave);

    }

    public Page<Transaction> getTransactionsByDebtId(Long debtId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAllByDebtIdOrderByPaymentDateDesc(debtId, pageable);
    }

    public Page<Transaction> getTransactionsByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByCustomerDebtIds(customerId, pageable);
    }

    @Transactional
    public void rollbackTransaction(Long transactionId) {
        Long debtId = deptRepository.findByTransactionId(transactionId);

        Debt debt = deptRepository.findById(debtId)
                .orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        if (debt.getStatus().equals(DebtStatus.ACTIVE) || debt.getStatus().equals(DebtStatus.FINISHED)) {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Transaction with id: " + transactionId + " not found"));
            debt.getTransactions().remove(transaction);
            debt.setAmount(debt.getAmount().add(transaction.getAmount()));
            debt.setStatus(debt.getAmount().equals(BigDecimal.ZERO) ? DebtStatus.FINISHED : DebtStatus.ACTIVE);
            transactionRepository.deleteById(transactionId);
        }

    }
}
