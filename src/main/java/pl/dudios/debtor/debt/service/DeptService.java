package pl.dudios.debtor.debt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.repository.CustomerDao;
import pl.dudios.debtor.debt.controller.DeptRequest;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.repo.DeptRepository;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

import static pl.dudios.debtor.debt.model.DebtStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    private final CustomerDao customerDao;

    public Debt getDebtById(Long debtId) {
        return deptRepository.findById(debtId)
                .orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
    }

    public Debt addDebt(DeptRequest request) {
        if (request.debtorEmail().equals(request.creditorEmail())) {
            throw new RequestValidationException("debtor mail and creditor email are the same");
        }
        Customer debtor = customerDao.getCustomerByEmail(request.debtorEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + request.debtorEmail() + " not found"));
        Customer creditor = customerDao.getCustomerByEmail(request.creditorEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + request.creditorEmail() + " not found"));

        Debt debt = Debt.builder()
                .debtor(debtor)
                .creditor(creditor)
                .amount(request.amount())
                .description(request.description())
                .repaymentDate(request.repaymentDate())
                .startDate(LocalDateTime.now())
                .status(ACTIVE)
                .build();

        return deptRepository.save(debt);
    }

    public void cancelDebt(Long debtId) {
        Debt debt = deptRepository.findById(debtId).orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        debt.setStatus(CANCELLED);
    }

    public void archiveDebt(Long debtId) {
        Debt debt = deptRepository.findById(debtId).orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        debt.setStatus(ARCHIVED);
    }

    public void reActiveDebt(Long debtId) {
        Debt debt = deptRepository.findById(debtId).orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        debt.setStatus(ACTIVE);
    }

    public Page<Debt> getDebtsByDebtorId(Long debtorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deptRepository.findAllByDebtorIdAndStatusIs(debtorId, ACTIVE, pageable);
    }

    public Page<Debt> getDebtsByCreditorId(Long creditorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deptRepository.findAllByCreditorIdAndStatusIs(creditorId, ACTIVE, pageable);
    }
}
