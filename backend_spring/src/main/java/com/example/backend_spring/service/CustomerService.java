package com.example.backend_spring.service;

import com.example.backend_spring.dto.CustomerDTO;
import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Customer;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;

    public CustomerService(CustomerRepository customerRepository, AppUserRepository appUserRepository) {
        this.customerRepository = customerRepository;
        this.appUserRepository = appUserRepository;
    }

    public boolean setCustomer(CustomerDTO customerDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        Customer customer = customerRepository.findByNameAndAppUser(customerDTO.getName(), appUser);
        boolean created = false;
        if (customer == null) {
            customer = new Customer();
            customer.setAppUser(appUser);
            created = true;
        }
        customer.setName(customerDTO.getName());
        customer.setNote(customerDTO.getNote());
        customerRepository.save(customer);
        return created;
    }

    public List<CustomerDTO> getCustomers(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        List<Customer> customers = customerRepository.findByAppUser(appUser);
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for(Customer customer : customers) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setName(customer.getName());
            customerDTO.setNote(customer.getNote());
            customerDTOs.add(customerDTO);
        }
        return customerDTOs;
    }
}
