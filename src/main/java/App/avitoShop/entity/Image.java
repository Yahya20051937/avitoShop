package App.avitoShop.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;


@Data
@Entity
@Table(name="imageData")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name = "imageData", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    private Type type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public String getEncodedData(){
        return Base64.getEncoder().encodeToString(this.imageData);
    }

    public enum Type{
        main, optional
    }




}
