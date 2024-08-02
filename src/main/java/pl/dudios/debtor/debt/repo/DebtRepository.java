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
public interface DebtRepository extends JpaRepository<Debt, Long> {

    Page<Debt> findAllByDebtorAndStatusIs(Customer debtor, DebtStatus status, Pageable pageable);

    Page<Debt> findAllByCreditorAndStatusIs(Customer creditor, DebtStatus status, Pageable pageable);

    Page<Debt> findAllByCreditor(Customer creditor, Pageable pageable);

    Page<Debt> findAllByDebtor(Customer creditor, Pageable pageable);

    @Query(value = "SELECT SUM(d.amount)" +
            " FROM debts AS d " +
            "WHERE d.debtor_id = :debtorId AND d.status = 'ACTIVE'",
            nativeQuery = true)
    BigDecimal getDebtAmountSum(Long debtorId);

    @Query(value = "SELECT SUM(d.amount)" +
            " FROM debts AS d " +
            "WHERE d.creditor_id = :creditorId AND d.status = 'ACTIVE'",
            nativeQuery = true)
    BigDecimal getCreditorAmountSum(Long creditorId);

    Long countByDebtorIdAndStatusIs(Long debtorId, DebtStatus status);

    Long countByCreditorIdAndStatusIs(Long creditorId, DebtStatus status);

    @Query(value = "SELECT t.debt_id" +
            " FROM transactions AS t " +
            "WHERE t.id = :transactionId",
            nativeQuery = true)
    Long findByTransactionId(Long transactionId);

    @Query(value = "SELECT SUM(amount) FROM (" +
            " SELECT SUM(d.amount * -1) AS amount" +
            " FROM debts AS d" +
            " WHERE d.debtor_id = :customerId AND d.status = 'ACTIVE' AND d.creditor_id = :friendId" +
            " UNION ALL" +
            " SELECT SUM(d.amount) AS amount" +
            " FROM debts AS d" +
            " WHERE d.debtor_id = :friendId AND d.status = 'ACTIVE' AND d.creditor_id = :customerId" +
            ") AS combined_sums",
            nativeQuery = true)
    BigDecimal getFriendBalance(Long customerId, Long friendId);

    @Query(value = "SELECT * FROM debts WHERE repayment_date IS NOT NULL AND repayment_date < CURRENT_DATE + INTERVAL '7 days' AND status = 'ACTIVE'", nativeQuery = true)
    List<Debt> findAllDebtToRemind();
}
