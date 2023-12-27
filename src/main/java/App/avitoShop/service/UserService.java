package App.avitoShop.service;

import App.avitoShop.data.BankAccountRepository;
import App.avitoShop.data.CartRepository;
import App.avitoShop.data.UserRepository;
import App.avitoShop.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void createCartAndBankAccount(User user){
        Cart cart = new Cart();
        this.cartRepository.save(cart);
        user.setCart(cart);
        System.out.println("Cart created");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setMoney(1000.0);
        this.bankAccountRepository.save(bankAccount);
        user.setBankAccount(bankAccount);
        System.out.println("Bank account created");
    }

    public void addProductMoneyInAdminBankAccount(Purchase purchase){
        Long adminId = Long.valueOf(2352);
        User ADMIN = this.userRepository.findById(adminId).get();
        for (Product product:purchase.getProducts()){
            ADMIN.getBankAccount().addMoney(product.getPrice());
            product.setSold(true);
        }
        purchase.setPhase(Purchase.Phase.gathering);
    }



}