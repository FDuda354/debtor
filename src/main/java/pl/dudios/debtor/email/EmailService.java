package pl.dudios.debtor.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.repo.DebtRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Profile("!dev")
@RequiredArgsConstructor

@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final DebtRepository debtRepository;

    @Async
    @Scheduled(cron = "0 0 8 * * ?")
    @Override
    public void sendPayDebtReminderEmail() {
        List<Debt> debtsToRemind = debtRepository.findAllDebtToRemind();
        for (Debt debt : debtsToRemind) {
            sendEmail(debt.getDebtor().getEmail(), "Przypomnienie o spłacie długu", generateDebtReminderEmailContent(debt));
        }
    }

    @Async
    @Override
    public void sendNewDebtEmail(Debt debt) {
        sendEmail(debt.getDebtor().getEmail(), "Nałożono na Ciebie nowy dług", generateNewDebtNotificationEmailContent(debt));
    }

    @Async
    @Override
    public void sendCancelDebtEmail(Debt debt) {
        sendEmail(debt.getDebtor().getEmail(), "Anulowano Ci dług", generateDebtCancellationEmailContent(debt));
    }

    @Async
    @Override
    public void sendPaidDebtEmail(Debt debt) {
        sendEmail(debt.getCreditor().getEmail(), "Spłacono dług", generateDebtPaidEmailContent(debt));
    }

    private void sendEmail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("Dłużnik Dudios <postmaster@dudios.pl>");
            helper.setReplyTo("no-reply@dudios.pl");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content + "<br><br><p style='font-size: small; color: grey;'>Prosimy, nie odpowiadaj na tę wiadomość, ponieważ nie jest monitorowana.</p>", true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error while sending email: " + e.getMessage());
        }

    }

    private String generateDebtReminderEmailContent(Debt debt) {
        String dateInfo;
        if (debt.getRepaymentDate() != null) {
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), debt.getRepaymentDate());
            dateInfo = daysRemaining > 0
                    ? "Zostało " + daysRemaining + " dni do terminu spłaty."
                    : "Jesteś " + Math.abs(daysRemaining) + " dni po terminie spłaty.";
        } else {
            dateInfo = "Data spłaty jest nieokreślona.";
        }

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                "<div style='background-color: #f8f9fa; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd;'>" +
                "<h2 style='color: #333333;'>Przypomnienie o spłacie długu</h2>" +
                "<p>Drogi " + debt.getDebtor().getFirstName() + ",</p>" +
                "<p>Przypominamy, że masz zaległy dług wobec " + debt.getCreditor().getFirstName() + ".</p>" +
                "<p><strong>Kwota do spłaty: </strong>" + debt.getAmount() + " PLN</p>" +
                "<p>" + dateInfo + "</p>" +
                "<p><strong>Opis: </strong>" + debt.getDescription() + "</p>" +
                "<p>Prosimy o uregulowanie zaległości jak najszybciej.</p>" +
                "<p>Z poważaniem,<br/>Zespół Dłużnik Dudios</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    private String generateNewDebtNotificationEmailContent(Debt debt) {
        String repaymentDateInfo = debt.getRepaymentDate() != null ? "Masz czas na spłatę do " + debt.getRepaymentDate().toLocalDate() + "." : "Data spłaty jest nieokreślona.";

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                "<div style='background-color: #f8f9fa; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd;'>" +
                "<h2 style='color: #333333;'>Nowy dług na twoim koncie</h2>" +
                "<p>Drogi " + debt.getDebtor().getFirstName() + ",</p>" +
                "<p>Informujemy, że został nałożony nowy dług na Twoje konto.</p>" +
                "<p><strong>Wierzyciel: </strong>" + debt.getCreditor().getFirstName() + " " + debt.getCreditor().getSurname() + "</p>" +
                "<p><strong>Kwota długu: </strong>" + debt.getAmount() + " PLN</p>" +
                "<p><strong>Opis: </strong>" + debt.getDescription() + "</p>" +
                "<p>" + repaymentDateInfo + "</p>" +
                "<p>Z poważaniem,<br/>Zespół Dłużnik Dudios</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String generateDebtCancellationEmailContent(Debt debt) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                "<div style='background-color: #f8f9fa; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd;'>" +
                "<h2 style='color: #333333;'>Anulowanie długu</h2>" +
                "<p>Drogi " + debt.getDebtor().getFirstName() + ",</p>" +
                "<p>Twój dług wobec " + debt.getCreditor().getFirstName() + " " + debt.getCreditor().getSurname() + " został anulowany.</p>" +
                "<p><strong>Kwota: </strong>" + debt.getAmount() + " PLN</p>" +
                "<p><strong>Opis: </strong>" + debt.getDescription() + "</p>" +
                "<p>Z poważaniem,<br/>Zespół Dłużnik Dudios</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    private String generateDebtPaidEmailContent(Debt debt) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                "<div style='background-color: #f8f9fa; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd;'>" +
                "<h2 style='color: #333333;'>Spłata długu</h2>" +
                "<p>Drogi " + debt.getCreditor().getFirstName() + ",</p>" +
                "<p>Informujemy, że dług wobec Ciebie został spłacony przez " + debt.getDebtor().getFirstName() + " " + debt.getDebtor().getSurname() + ".</p>" +
                "<p><strong>Kwota spłaty: </strong>" + debt.getAmount() + " PLN</p>" +
                "<p><strong>Opis: </strong>" + debt.getDescription() + "</p>" +
                "<p>Dług został w pełni uregulowany.</p>" +
                "<p>Z poważaniem,<br/>Zespół Dłużnik Dudios</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


}
