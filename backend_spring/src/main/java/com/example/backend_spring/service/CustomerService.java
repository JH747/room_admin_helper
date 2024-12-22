package com.example.backend_spring.service;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Customer;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;

    public CustomerService(CustomerRepository customerRepository, AppUserRepository appUserRepository) {
        this.customerRepository = customerRepository;
        this.appUserRepository = appUserRepository;
    }

    public Customer createCustomer(Customer customer, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        customer.setAppUser(appUser);
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return customerRepository.findByAppUser(appUser);
    }
}
