package pl.dudios.debtor.debt.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtStatus;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DeptRepository extends JpaRepository<Debt, Long> {

    Page<Debt> findAllByDebtorAndStatusIs(Customer debtor, DebtStatus status, Pageable pageable);

    Page<Debt> findAllByCreditorAndStatusIs(Customer creditor, DebtStatus status, Pageable pageable);

    @Query(value = "SELECT SUM(d.amount)" +
            " FROM debts AS d " +
            "WHERE d.debtor_id = :debtorId",
            nativeQuery = true)
    BigDecimal getDebtAmountSum(Long debtorId);

    @Query(value = "SELECT SUM(d.amount)" +
            " FROM debts AS d " +
            "WHERE d.creditor_id = :creditorId",
            nativeQuery = true)
    BigDecimal getCreditorAmountSum(Long creditorId);

    Long countByDebtorId(Long debtorId);

    Long countByCreditorId(Long creditorId);
}
