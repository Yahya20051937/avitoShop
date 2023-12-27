package App.avitoShop.data;

import App.avitoShop.relationship.CartProduct;
import org.springframework.data.repository.CrudRepository;

public interface CartProductRepository extends CrudRepository<CartProduct, Long> {
}
