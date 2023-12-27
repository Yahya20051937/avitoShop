package App.avitoShop.web;

import App.avitoShop.data.*;
import App.avitoShop.entity.Image;
import App.avitoShop.entity.Product;
import App.avitoShop.entity.Purchase;
import App.avitoShop.entity.User;
import App.avitoShop.service.CartService;
import App.avitoShop.service.ImageService;
import App.avitoShop.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/app")
public class AppController {

    private static final String UploadingDirectory = "C:\\avitoImages\\";

    private ProductRepository productRepository;
    private ImageService imageService;
    private ImageRepository imageRepository;
    private UserService userService;
    private PurchaseRepository purchaseRepository;

    private CartRepository cartRepository;
    private UserRepository userRepository;
    private CartService cartService;

    private String Error = "";

    public void checkError(Model model){
        if (!this.Error.isEmpty()) {
            model.addAttribute("Error", this.Error);
            System.out.println("Error ...");
            this.Error = "";
            }
        else
            model.addAttribute("Error", "");
    }


    @Autowired
    public AppController(ProductRepository productRepository, ImageService imageService, ImageRepository imageRepository, UserService userService, PurchaseRepository purchaseRepository, CartRepository cartRepository, UserRepository userRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.purchaseRepository = purchaseRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        this.checkError(model);
        Iterable<Product> allProducts = this.productRepository.findAll();
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getCart().getId());
        model.addAttribute("products", allProducts);
        model.addAttribute("user", user);
        model.addAttribute("userImage", user.getImage());
        return "appHomePage";
    }

    @GetMapping("/addProduct")
    public String addProductGet(Model model) {
        this.checkError(model);
        return "addProductPage";

    }

    @GetMapping("/getCart")
    public String getCart(Authentication authentication, Model model) {
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        List<Product> products = user.getCart().getProducts(cartService);
        model.addAttribute("products", products);

        if (products.size() > 0)
            model.addAttribute("validateOption", true);
        else
            model.addAttribute("validateOption", false);
        return "getCartPage";
    }

    @GetMapping("/getPurchases")
    public String getPurchases(Authentication authentication, Model model){
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        model.addAttribute("purchases", user.getPurchases());
        return "getPurchasesPage";
    }

    @ModelAttribute(name = "product")
    public Product product() {
        return new Product();
    }

    @PostMapping("/addProduct")
    public String addProductPost(Product product, Authentication authentication, @RequestParam("mainImage") MultipartFile mainFile, @RequestParam("optionalImages") MultipartFile[] optionalFiles, Model model) {
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        product.setSeller(user);
        this.imageService.saveProductImage(mainFile, product, Image.Type.main);
        for (MultipartFile file : optionalFiles)
            this.imageService.saveProductImage(file, product, Image.Type.optional);

        this.productRepository.save(product);

        for (Image image : product.getImages())
            this.imageRepository.save(image);

        return "redirect:/app/home";
    }

    @GetMapping("/browseProduct/{productId}")
    public String browseProduct(@PathVariable Long productId, Model model, Authentication authentication) {
        this.checkError(model);
        Product product = this.productRepository.findById(productId).get();
        System.out.println("product Id : " + product.getId());
        model.addAttribute("images", product.getImages());
        model.addAttribute("product", product);
        // if the product's seller is the user, or if the product is already sold, we make the add to cart button not clickable, or if the product is already in the cart.

        User user = (User) authentication.getPrincipal();
        if (user.getId() == product.getSeller().getId() || product.isSold() || cartService.checkIfProductInCart(user.getCart(), product)) {
            model.addAttribute("disableButton", true);
            System.out.println("disabled");
        } else
            model.addAttribute("disableButton", false);
        return "browseProductPage";
    }

    @GetMapping("/addProductToCart/{productId}")
    public String addProductToCart(@PathVariable Long productId, Authentication authentication, Model model) {
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        System.out.println("product Id : " + productId);
        Product product = this.productRepository.findById(productId).get();
        this.cartService.addProductToCart(user.getCart(), product);
        return "redirect:/app/home";
    }

    @GetMapping("/removeProductFromCart/{productId}")
    public String removeProductFromCart(@PathVariable Long productId, Authentication authentication, Model model){
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        Product product = this.productRepository.findById(productId).get();
        user.getCart().removeProduct(product, cartService);
        //this.cartRepository.deleteById(user.getCart().getId());
        System.out.println(user.getCart().getProducts(cartService).size());
        this.cartRepository.save(user.getCart());
        return "redirect:/app/getCart";
    }

    @GetMapping("/validateOrder")
    public String validateOrder(Authentication authentication, Model model) {
        this.checkError(model);
        User user = (User) authentication.getPrincipal();
        List<Product> productsToValidate = new ArrayList<>();
        double priceSum = 0.0;
        for (Product product : user.getCart().getProducts(cartService)) {
            if (!product.isSold()){
                productsToValidate.add(product);
                priceSum += product.getPrice();
            }

        }
        model.addAttribute("productsToValidate", productsToValidate);
        model.addAttribute("priceSum", priceSum);
        return "validateOrderPage";
    }

    @GetMapping("/payOrder")
    public String payOrder(Authentication authentication, Model model){
        this.checkError(model);
        User user = (User) authentication.getPrincipal();

        Purchase purchase = new Purchase();
        purchase.setBuyer(user);
        purchase.setProducts(user.getCart().getProducts(cartService));


        if (user.payCart(this.userRepository, cartService)){
            this.purchaseRepository.save(purchase);
            user.addPurchase(purchase);
            System.out.println("purchase saved...");
            return "redirect:/app/home";

            }
        else {
            this.Error = "You can't afford all these products .Please review your cart";
            return "redirect:/app/getCart";
        }




    }

}
