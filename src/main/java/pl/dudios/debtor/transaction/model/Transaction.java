package pl.dudios.debtor.transaction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dudios.debtor.debt.model.Debt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_id_sequence",
            sequenceName = "transaction_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "transaction_id_sequence"
    )
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debt_id", nullable = false)
    private Debt debt;
    private BigDecimal amount;
    private BigDecimal balanceBeforeTransaction;
    private BigDecimal balanceAfterTransaction;
    private String description;
    private LocalDateTime paymentDate;

}
