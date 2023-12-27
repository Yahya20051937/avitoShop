package App.avitoShop.web;

import App.avitoShop.data.UserRepository;
import App.avitoShop.entity.User;
import App.avitoShop.security.RegistrationForm;
import App.avitoShop.service.ImageService;
import App.avitoShop.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/authenticate")
public class AuthenticationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private ImageService imageService;



    @Autowired
    public AuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, ImageService imageService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.imageService = imageService;

    }

    @GetMapping("/register")
    public String registerForm() {return "registrationPage";};

    @PostMapping("/register")
    public String processRegistration(RegistrationForm form,@RequestParam("image") MultipartFile imageFile){
        System.out.println(form);
        User user = form.toUser(passwordEncoder, userService);
        if (this.userRepository.findByUsername(user.getUsername()) == null)  { // this to avoid having duplicated user_names.
                this.imageService.saveUserImage(imageFile, user);
                this.userRepository.save(user);
            }
        else
            return "redirect:/authenticate/register";
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {return "loginPage";}

}
