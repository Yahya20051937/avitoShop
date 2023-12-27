package App.avitoShop.security;

import App.avitoShop.data.UserRepository;
import App.avitoShop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        final User user = this.userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        return user;
    }
}
