package pl.dudios.debtor.debt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.service.DeptService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debt")
public class DebtController {

    private final DeptService deptService;

    @GetMapping("/debtors")
    public ResponseEntity<Page<Debt>> getDebtsByDebtorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                         @RequestParam(value = "debtorId") Long debtorId) {

        return ResponseEntity.ok(deptService.getDebtsByDebtorId(debtorId, page, size));
    }

    @GetMapping("/creditor")
    public ResponseEntity<Page<Debt>> getDebtsByCreditorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "creditorId") Long creditorId) {

        return ResponseEntity.ok(deptService.getDebtsByCreditorId(creditorId, page, size));
    }

    @PostMapping
    public ResponseEntity<Void> addDebt(@RequestBody DeptRequest request) {
        deptService.addDebt(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelDebt(@RequestParam Long debtId) {
        deptService.cancelDebt(debtId);

        return ResponseEntity.noContent().build();
    }
}
