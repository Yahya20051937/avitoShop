package App.avitoShop.entity;


import App.avitoShop.data.UserRepository;
import App.avitoShop.service.CartService;
import jakarta.persistence.*;
import jakarta.security.enterprise.credential.Password;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table(name = "User_")
public class User implements  UserDetails{
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String CIN;
    private Role role;

    public User(String username, String password, String firstName, String lastName, String CIN, Role role){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.CIN = CIN;
        this.role = role;
    }

    public User(String username, String password, Role role){
        this.username = username;
        this.password = password;
        this.role = role;
    }



    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Product> productsToSell;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "cart_id", referencedColumnName = "id")
   private Cart cart;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
   private BankAccount bankAccount;

   @OneToOne(cascade = CascadeType.REMOVE)
   @JoinColumn(name = "image_id", referencedColumnName = "id")
   private Image image;

   @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
   private List<Purchase> purchases;

   public void addProductToSell(Product product){
        this.productsToSell.add(product);
    }


    public enum Role{
        admin, client, deliveryGuy
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addPurchase(Purchase purchase){this.purchases.add(purchase);}

    public void addMoney(double value){this.bankAccount.setMoney(this.bankAccount.getMoney() + value); }

    public boolean payCart(UserRepository userRepository, CartService cartService){
        if (this.bankAccount.getMoney() >= this.cart.getProductsValue(cartService)) {
            for (Product product : this.cart.getProducts(cartService)) {
                this.bankAccount.setMoney(this.bankAccount.getMoney() - product.getPrice());
                product.getSeller().addMoney(product.getPrice());
                userRepository.save(product.getSeller());
            }
        userRepository.save(this);
        return true;
        }

    return false;
    }
}
