package App.avitoShop;

import App.avitoShop.data.ImageRepository;
import App.avitoShop.data.ProductRepository;
import App.avitoShop.data.UserRepository;
import App.avitoShop.entity.User;
import App.avitoShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    UserRepository userRepository;
    ProductRepository productRepository;
    ImageRepository imageRepository;
    UserService userService;

    @Autowired
    public CustomCommandLineRunner(UserRepository userRepository, ProductRepository productRepository, ImageRepository imageRepository, UserService userService){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }


    @Override
    public void run(String ... args) throws Exception{
        //this.imageRepository.deleteAll();
        //for (User user:this.userRepository.findAll())
            //this.userService.deleteCart(user);
        //this.productRepository.deleteAll();
        //this.userRepository.deleteAll();

        //System.out.println("Deleted");
        //Long id = Long.valueOf(2352);

        //User ADMIN = this.userRepository.findById(id).get();



        //this.userService.createCartAndBankAccount(ADMIN);
        //this.userRepository.save(ADMIN);
        //System.out.println("user id : " +  ADMIN.getId());



    }
}
