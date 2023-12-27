package App.avitoShop.data;

import App.avitoShop.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository  extends CrudRepository<Cart, Long> {
}
