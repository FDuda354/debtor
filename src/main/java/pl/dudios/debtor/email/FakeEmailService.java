package pl.dudios.debtor.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.repo.DebtRepository;

import java.util.List;

@Profile("dev")
@Slf4j
@Service
@RequiredArgsConstructor
public class FakeEmailService implements EmailSender {

    private final DebtRepository debtRepository;

    @Async
    public void sendEmail(String to, String subject, String content) {
        log.info("Email sent to {}: Subject: {}, Content: {}", to, subject, content);
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendPayDebtReminderEmail() {
        List<Debt> debtsToRemind = debtRepository.findAllDebtToRemind();
        for (Debt debt : debtsToRemind) {
            sendEmail(debt.getDebtor().getEmail(), "Przypomnienie o spłacie długu", "");
        }

    }

    @Override
    public void sendNewDebtEmail(Debt debt) {
        String to = debt.getDebtor().getEmail();
        String subject = "Nowy dług na twoim koncie";
        String content = "";
        sendEmail(to, subject, content);
        log.info("Informacja o nowym długu wysłana do użytkownika: {}", to);
    }

    @Override
    public void sendCancelDebtEmail(Debt debt) {
        String to = debt.getDebtor().getEmail();
        String subject = "Anulowanie długu";
        String content = "";
        sendEmail(to, subject, content);
        log.info("Informacja o anulowaniu długu wysłana do użytkownika: {}", to);
    }

    @Override
    public void sendPaidDebtEmail(Debt debt) {
        String to = debt.getCreditor().getEmail();
        String subject = "Spłata długu";
        String content = "";
        sendEmail(to, subject, content);
        log.info("Informacja o spłacie długu wysłana do wierzyciela: {}", to);
    }
}