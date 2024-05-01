package pl.dudios.debtor.debt.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtStatus;

@Repository
public interface DeptRepository extends JpaRepository<Debt, Long> {

    Page<Debt> findAllByDebtorIdAndStatusIs(Long debtId, DebtStatus status, Pageable pageable);

    Page<Debt> findAllByCreditorIdAndStatusIs(Long creditorId, DebtStatus status, Pageable pageable);
}
