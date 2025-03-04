package org.example.springordermanagement.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(long id) {
        return customerRepository.findById(id);
    }

    public Customer updateCustomer(long id, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            customer.setId(id);
            return customerRepository.save(customer);
        }
        else {
            return null;
        }
    }

    public void deleteCustomer(long id) {
        customerRepository.deleteById(id);
    }
}
