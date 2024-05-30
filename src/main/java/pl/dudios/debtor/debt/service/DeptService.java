package pl.dudios.debtor.debt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.repository.CustomerDao;
import pl.dudios.debtor.debt.DebtMapper;
import pl.dudios.debtor.debt.controller.DebtRequest;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtDTO;
import pl.dudios.debtor.debt.repo.DeptRepository;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


    public Debt addDebt(DebtRequest request) {
        if (request.debtorEmail() == null || request.debtorEmail().equalsIgnoreCase(request.creditorEmail())) {
            throw new RequestValidationException("debtor mail and creditor email are the same");
        }
        Customer debtor = customerDao.getCustomerByEmail(request.debtorEmail().toLowerCase());
        Customer creditor = customerDao.getCustomerByEmail(request.creditorEmail().toLowerCase());

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

    public Page<DebtDTO> getDebtsByDebtorId(Long debtorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Customer debtor = customerDao.getCustomerById(debtorId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with Id: " + debtorId + " not found"));
        Page<Debt> debts = deptRepository.findAllByDebtorAndStatusIs(debtor, ACTIVE, pageable);
        List<DebtDTO> debtDTOs = debts.getContent()
                .stream()
                .map(d -> DebtMapper.mapToDebtDTO(d, false))
                .collect(Collectors.toList());

        return new PageImpl<>(debtDTOs, PageRequest.of(page, size), debts.getTotalElements());
    }

    public Page<DebtDTO> getDebtsByCreditorId(Long creditorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Customer creditor = customerDao.getCustomerById(creditorId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with Id: " + creditorId + " not found"));
        Page<Debt> debts = deptRepository.findAllByCreditorAndStatusIs(creditor, ACTIVE, pageable);
        List<DebtDTO> debtDTOs = debts.getContent()
                .stream()
                .map(d -> DebtMapper.mapToDebtDTO(d, true))
                .collect(Collectors.toList());

        return new PageImpl<>(debtDTOs, PageRequest.of(page, size), debts.getTotalElements());
    }

    public BigDecimal getDebtAmountSum(Long debtorId) {
        return deptRepository.getDebtAmountSum(debtorId);
    }

    public BigDecimal getCreditorAmountSum(Long creditorId) {
        return deptRepository.getCreditorAmountSum(creditorId);
    }

    public Long getDebtCount(Long debtorId) {
        return deptRepository.countByDebtorId(debtorId);
    }

    public Long getCreditorCount(Long creditorId) {
        return deptRepository.countByCreditorId(creditorId);
    }
}
