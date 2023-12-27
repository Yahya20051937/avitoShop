package App.avitoShop.security;

import App.avitoShop.entity.BankAccount;
import App.avitoShop.entity.Cart;
import App.avitoShop.entity.User;
import App.avitoShop.service.ImageService;
import App.avitoShop.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String CIN;

    private static Map<String, User.Role> stringToRole = new HashMap<>();

    static {
        stringToRole.put("client", User.Role.client);
        stringToRole.put("admin", User.Role.admin);
        stringToRole.put("deliveryGuy", User.Role.deliveryGuy);
    }

    public User toUser(PasswordEncoder passwordEncoder, UserService userService){
        //System.out.println("user saved");
        User user = new User(this.username, passwordEncoder.encode(this.password), this.firstName, this.lastName, this.CIN, stringToRole.get(this.role));
        userService.createCartAndBankAccount(user);

        return user;
    }


}
