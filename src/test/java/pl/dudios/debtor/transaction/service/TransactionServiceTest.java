package pl.dudios.debtor.transaction.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dudios.debtor.debt.model.Debt;
import pl.dudios.debtor.debt.repo.DebtRepository;
import pl.dudios.debtor.email.EmailSender;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.notification.service.NotificationService;
import pl.dudios.debtor.transaction.controller.TransactionRequest;
import pl.dudios.debtor.transaction.model.Transaction;
import pl.dudios.debtor.transaction.repo.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService underTest;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private DebtRepository debtRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EmailSender emailSender;

    @ParameterizedTest
    @ValueSource(strings = {"0", "-10"})
    void itShouldThrowWhenAmountIsNotPositive(String amount) {
        // given
        TransactionRequest request = new TransactionRequest(1L, new BigDecimal(amount), "desc");
        Debt debt = Debt.builder().amount(new BigDecimal("100")).build();
        given(debtRepository.findById(any(Long.class))).willReturn(Optional.of(debt));

        // when
        assertThatThrownBy(() -> underTest.addTransaction(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("greater than zero");

        // then
        then(transactionRepository).should(never()).save(any(Transaction.class));
        then(notificationService).shouldHaveNoInteractions();
        then(emailSender).shouldHaveNoInteractions();
    }
}
