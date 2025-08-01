package com.lala.jwt.service;

import com.lala.jwt.repository.CustomerRepository;
import com.lala.jwt.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private  CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        UserDetails user =  User.withUsername(customer.getEmail())
                        .password(customer.getPwd())
                        .roles(customer.getRole())   //If u use it like this, it will automatically append ROLE_ prefix to the role
                        .build();

        return user;
        /*
        Whereas if you use like this you have to handle it separately
         */
    }


}
