package pl.dudios.debtor.debt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
@Table(name = "DEBTS")
public class Debt {

    @Id
    @SequenceGenerator(
            name = "debt_id_sequence",
            sequenceName = "debt_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "debt_id_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    private Customer debtor;
    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
    private Customer creditor;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "debt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transaction> transactions;
    private BigDecimal amount;
    private String description;
    private LocalDateTime repaymentDate;
    private LocalDateTime startDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DebtStatus status;
}
