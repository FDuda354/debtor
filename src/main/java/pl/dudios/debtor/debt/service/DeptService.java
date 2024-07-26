package pl.dudios.debtor.debt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.repository.CustomerRepo;
import pl.dudios.debtor.debt.DebtMapper;
import pl.dudios.debtor.debt.controller.DebtRequest;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtDTO;
import pl.dudios.debtor.debt.repo.DeptRepository;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;
import pl.dudios.debtor.notification.model.Notification;
import pl.dudios.debtor.notification.service.NotificationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pl.dudios.debtor.debt.model.DebtStatus.ACTIVE;
import static pl.dudios.debtor.debt.model.DebtStatus.CANCELLED;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;
    private final CustomerRepo customerRepo;
    private final NotificationService notificationService;

    public Debt getDebtById(Long debtId) {
        return deptRepository.findById(debtId)
                .orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
    }


    public void addDebt(DebtRequest request) {
        if (request.debtorEmail() == null || request.debtorEmail().equalsIgnoreCase(request.creditorEmail())) {
            throw new RequestValidationException("debtor mail and creditor email are the same");
        }
        Customer debtor = customerRepo.findByEmail(request.debtorEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + request.debtorEmail() + " not found"));
        Customer creditor = customerRepo.findByEmail(request.creditorEmail().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + request.creditorEmail() + " not found"));

        Debt debt = Debt.builder()
                .debtor(debtor)
                .creditor(creditor)
                .amount(request.amount())
                .staredAmount(request.amount())
                .description(request.description())
                .repaymentDate(request.repaymentDate())
                .startDate(LocalDateTime.now())
                .status(ACTIVE)
                .build();

        deptRepository.save(debt);
        notificationService.notifyUser(request.debtorEmail(), new Notification("Masz nowy dług u "+ request.creditorEmail()));
        notificationService.notifyUser(request.creditorEmail(), new Notification(request.debtorEmail()+ " ma u ciebie nowy dług"));

    }

    @Transactional
    public void cancelDebt(Long debtId) {
        Debt debt = deptRepository.findById(debtId).orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        if (debt.getStatus().equals(ACTIVE)) {
            debt.setStatus(CANCELLED);
            notificationService.notifyUser(debt.getDebtor().getEmail(), new Notification("Anulowano twój dług u " + debt.getCreditor().getEmail()));
        }
    }

    @Transactional
    public void reactiveDebt(Long debtId) {
        Debt debt = deptRepository.findById(debtId).orElseThrow(() -> new ResourceNotFoundException("Debt with id: " + debtId + " not found"));
        debt.setStatus(ACTIVE);
        notificationService.notifyUser(debt.getDebtor().getEmail(), new Notification("Anulowano twój dług u " + debt.getCreditor().getEmail() + " został reaktywowany."));

    }

    public Page<DebtDTO> getDebtsByDebtorId(Long debtorId, int page, int size, boolean onlyActive) {
        Pageable pageable = PageRequest.of(page, size);
        Customer debtor = customerRepo.findById(debtorId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with Id: " + debtorId + " not found"));
        Page<Debt> debts = onlyActive ? deptRepository.findAllByDebtorAndStatusIs(debtor, ACTIVE, pageable) : deptRepository.findAllByDebtor(debtor, pageable);
        List<DebtDTO> debtDTOs = debts.getContent()
                .stream()
                .map(d -> DebtMapper.mapToDebtDTO(d, false))
                .collect(Collectors.toList());

        return new PageImpl<>(debtDTOs, PageRequest.of(page, size), debts.getTotalElements());
    }

    public Page<DebtDTO> getDebtsByCreditorId(Long creditorId, int page, int size, boolean onlyActive) {
        Pageable pageable = PageRequest.of(page, size);
        Customer creditor = customerRepo.findById(creditorId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with Id: " + creditorId + " not found"));
        Page<Debt> debts = onlyActive ? deptRepository.findAllByCreditorAndStatusIs(creditor, ACTIVE, pageable) : deptRepository.findAllByCreditor(creditor, pageable);
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
        return deptRepository.countByDebtorIdAndStatusIs(debtorId, ACTIVE);
    }

    public Long getCreditorCount(Long creditorId) {
        return deptRepository.countByCreditorIdAndStatusIs(creditorId, ACTIVE);
    }

    public BigDecimal getFriendBalance(Long id, Long friendId) {
        BigDecimal friendBalance = deptRepository.getFriendBalance(id, friendId);
        return friendBalance == null ? BigDecimal.ZERO : friendBalance;
    }
}
