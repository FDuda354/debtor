package pl.dudios.debtor.customer.images.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "IMAGES")
public class Image {
    @Id
    private String fileName;
    @Lob
    private byte[] content;
    private String type;

    @Override
    public String toString() {
        return "Image{" +
                "fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
