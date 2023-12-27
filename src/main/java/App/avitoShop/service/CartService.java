package App.avitoShop.service;

import App.avitoShop.data.CartProductRepository;
import App.avitoShop.data.CartRepository;
import App.avitoShop.data.ProductRepository;
import App.avitoShop.entity.Cart;
import App.avitoShop.entity.Product;
import App.avitoShop.relationship.CartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    @Autowired
    CartProductRepository cartProductRepository;
    @Autowired
    ProductRepository productRepository;

    public void addProductToCart(Cart cart, Product product){
        CartProduct cart_product = new CartProduct(cart, product);
        this.cartProductRepository.save(cart_product);

    }

    public void deleteProductFromCart(Cart cart, Product product){
        Iterable<CartProduct> cartProductsRelationships = this.cartProductRepository.findAll();
        for (CartProduct relationship:cartProductsRelationships) {
            if (Objects.equals(relationship.getCart_id(), cart.getId()) && Objects.equals(relationship.getProduct_id(), product.getId())) {
                System.out.println("Deleting from cart .... ");
                this.cartProductRepository.deleteById(relationship.getId());
                break;
            }
        }
    }

    public boolean checkIfProductInCart(Cart cart, Product product){
        List<Product> cartProducts = this.getCartProducts(cart);
        for (Product p:cartProducts)
            if (p.getId().equals(product.getId()))
                return true;
        return false;

    }


    public List<Product> getCartProducts(Cart cart){
        Iterable<CartProduct> cartProductsRelationships = this.cartProductRepository.findAll();
        List<Product> cartProducts = new ArrayList<>();

        for (CartProduct relationship:cartProductsRelationships){
            if (Objects.equals(relationship.getCart_id(), cart.getId())){
                Product product = this.productRepository.findById(relationship.getProduct_id()).get();
                cartProducts.add(product);
            }
        }
        return cartProducts;
    }
}
