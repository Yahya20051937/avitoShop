package App.avitoShop.entity;

import App.avitoShop.service.CartService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cart")
    private User buyer;


    public List<Product> getProducts(CartService cartService){
        return cartService.getCartProducts(this);
    }

    public void removeProduct(Product product, CartService cartService){
        cartService.deleteProductFromCart(this, product);
    }


    public double getProductsValue(CartService cartService){
        double productsValue = 0;
        for (Product product:this.getProducts(cartService))
            productsValue += product.getPrice();
        return productsValue;
    }
}
