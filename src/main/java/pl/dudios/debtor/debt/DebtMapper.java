package pl.dudios.debtor.debt;

import lombok.experimental.UtilityClass;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.model.DebtDTO;
import pl.dudios.debtor.utils.mappers.CustomerMapper;

@UtilityClass
public class DebtMapper {

    public DebtDTO mapToDebtDTO(Debt debt, boolean isForCreditor) {
        return new DebtDTO(
                debt.getId(),
                isForCreditor ?
                        CustomerMapper.mapToCustomerDTO(debt.getDebtor()) :
                        CustomerMapper.mapToCustomerDTO(debt.getCreditor()),
                debt.getAmount(),
                debt.getStaredAmount(),
                debt.getDescription(),
                debt.getRepaymentDate(),
                debt.getStartDate(),
                debt.getStatus()
        );
    }
}
