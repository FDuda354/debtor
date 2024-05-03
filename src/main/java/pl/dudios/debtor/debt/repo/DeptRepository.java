package pl.dudios.debtor.debt.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtStatus;

@Repository
public interface DeptRepository extends JpaRepository<Debt, Long> {

    Page<Debt> findAllByDebtorAndStatusIs(Customer debtor, DebtStatus status, Pageable pageable);

    Page<Debt> findAllByCreditorAndStatusIs(Customer creditor, DebtStatus status, Pageable pageable);
}
