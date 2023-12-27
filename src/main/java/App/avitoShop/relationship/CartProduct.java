package App.avitoShop.relationship;

import App.avitoShop.entity.Cart;
import App.avitoShop.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="cart_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long cart_id;
    private Long product_id;

    public CartProduct(Cart cart, Product product){
        this.cart_id = cart.getId();
        this.product_id = product.getId();
    }
}
