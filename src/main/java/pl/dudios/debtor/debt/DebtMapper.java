package pl.dudios.debtor.debt;

import lombok.experimental.UtilityClass;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtDTO;

@UtilityClass
public class DebtMapper {

    public DebtDTO mapToDebtDTO(Debt debt, boolean isForCreditor) {
        return new DebtDTO(
                debt.getId(),
                isForCreditor ? debt.getDebtor() : debt.getCreditor(),
                debt.getAmount(),
                debt.getDescription(),
                debt.getRepaymentDate(),
                debt.getStartDate(),
                debt.getStatus()
        );
    }
}
