package pl.dudios.debtor.transaction.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.transaction.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByDebtIdOrderByPaymentDateDesc(Long debtId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.debt.id IN (" +
            "SELECT d.id FROM Debt d WHERE d.creditor.id = :customerId OR d.debtor.id = :customerId)" +
            "ORDER BY t.paymentDate DESC")
    Page<Transaction> findByCustomerDebtIds(Long customerId, Pageable pageable);
}
