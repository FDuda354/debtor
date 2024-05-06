package pl.dudios.debtor.transaction.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.transaction.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByDebtId(Long debtId, Pageable pageable);

    Page<Transaction> findByDebtIdIn(List<Long> debtIds, Pageable pageable);
}
