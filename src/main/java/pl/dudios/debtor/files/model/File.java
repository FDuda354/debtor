package pl.dudios.debtor.files.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dudios.debtor.transaction.model.Transaction;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "FILES")
public class File {
    @Id
    private String fileName;
    @Lob
    private byte[] content;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
