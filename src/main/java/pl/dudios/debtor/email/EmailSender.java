package pl.dudios.debtor.email;

import pl.dudios.debtor.debt.model.Debt;

public interface EmailSender {

    void sendPayDebtReminderEmail();
    void sendNewDebtEmail(Debt debt);
    void sendCancelDebtEmail(Debt debt);
    void sendPaidDebtEmail(Debt debt);

}
