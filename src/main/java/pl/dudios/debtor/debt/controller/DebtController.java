package pl.dudios.debtor.debt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtDTO;
import pl.dudios.debtor.debt.service.DeptService;
import pl.dudios.debtor.exception.UnauthorizedAccessException;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debt")
public class DebtController {

    private final DeptService deptService;

    @GetMapping
    public ResponseEntity<Debt> getDebtById(@RequestParam(value = "debtId") Long debtId,
                                            @AuthenticationPrincipal Customer customer) {
        Debt debt = deptService.getDebtById(debtId);
        if (debt.getCreditor().getId().equals(customer.getId()) || debt.getDebtor().getId().equals(customer.getId())) {
            return ResponseEntity.ok(debt);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to view this debt");
        }
    }


    @GetMapping("/debtors")
    public ResponseEntity<Page<DebtDTO>> getDebtsByDebtorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @RequestParam(value = "onlyActive") boolean onlyActive,
                                                            @AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtsByDebtorId(customer.getId(), page, size, onlyActive));
    }

    @GetMapping("/creditors")
    public ResponseEntity<Page<DebtDTO>> getDebtsByCreditorId(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "onlyActive") boolean onlyActive,
                                                              @AuthenticationPrincipal Customer customer) {

        return ResponseEntity.ok(deptService.getDebtsByCreditorId(customer.getId(), page, size, onlyActive));
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

    @PostMapping
    public ResponseEntity<Void> addDebt(@RequestBody DebtRequest request) {
        deptService.addDebt(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelDebt(@RequestParam Long debtId) {
        deptService.cancelDebt(debtId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reactive")
    public ResponseEntity<Void> reactiveDebt(@RequestParam Long debtId) {
        deptService.reactiveDebt(debtId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/friend-balance")
    public ResponseEntity<BigDecimal> getFriendBalance(@AuthenticationPrincipal Customer customer,
                                                       @RequestParam("friendId") Long friendId) {

        return ResponseEntity.ok(deptService.getFriendBalance(customer.getId(), friendId));
    }
}
