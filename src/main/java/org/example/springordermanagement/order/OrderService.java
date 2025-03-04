package org.example.springordermanagement.order;

import org.example.springordermanagement.customer.Customer;
import org.example.springordermanagement.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerservice;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(long id) {
        return orderRepository.findById(id);
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(long id, Order order) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isPresent()) {
            order.setId(id);
            return orderRepository.save(order);
        }
        else {
            return null;
        }
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByCustomerId(long customerId) {
        Optional<Customer> customerOptional = customerservice.getCustomerById(customerId);

        if (customerOptional.isPresent()) {
            return orderRepository.findByCustomerId(customerOptional.get().getId());
        }
        else {
            return null;
        }
    }

    public List<Order> getOrderByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status);
    }

    public List<Order> getOrderByDateRange(Date start, Date end) {
        return orderRepository.findByOrderDateBetween(start, end);
    }
}
