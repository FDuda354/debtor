package pl.dudios.debtor.files.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dudios.debtor.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.util.Arrays;

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
    private LocalDateTime uploadDate;
    private Long size;

    @Override
    public String toString() {
        return "File{" +
                "fileName='" + fileName + '\'' +
                ", content=" + Arrays.toString(content) +
                ", type='" + type + '\'' +
                ", uploadDate=" + uploadDate +
                ", size=" + size +
                '}';
    }
}
