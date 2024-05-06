package pl.dudios.debtor.debt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.debt.model.DebtDTO;
import pl.dudios.debtor.debt.service.DeptService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debt")
public class DebtController {

    private final DeptService deptService;

    @GetMapping("/debtors")
    public ResponseEntity<Page<DebtDTO>> getDebtsByDebtorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtsByDebtorId(customer.getId(), page, size));
    }

    @GetMapping("/creditors")
    public ResponseEntity<Page<DebtDTO>> getDebtsByCreditorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtsByCreditorId(customer.getId(), page, size));
    }

    @GetMapping("/summary/debt")
    public ResponseEntity<BigDecimal> getDebtAmountSum(@AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtAmountSum(customer.getId()));
    }

    @GetMapping("/summary/credit")
    public ResponseEntity<BigDecimal> getCreditorAmountSum(@AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getCreditorAmountSum(customer.getId()));
    }

    @GetMapping("/summary/debt/count")
    public ResponseEntity<Long> getDebtCount(@AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtCount(customer.getId()));
    }

    @GetMapping("/summary/credit/count")
    public ResponseEntity<Long> getCreditorCount(@AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getCreditorCount(customer.getId()));
    }

//    @PostMapping
//    public ResponseEntity<Void> addDebt(@RequestBody DeptRequest request) {
//        deptService.addDebt(request);
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> cancelDebt(@RequestParam Long debtId) {
//        deptService.cancelDebt(debtId);
//
//        return ResponseEntity.noContent().build();
//    }
}
