package com.demo.dynamodb.service;

import com.demo.dynamodb.dto.CustomerUpdateRequeset;
import com.demo.dynamodb.entity.Customer;
import com.demo.dynamodb.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRepository customerRepository;


    // 저장하기 - PK 생성 후 반환
    public String saveCustomer(String name, String email) {
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer(
                customerId,
                name,
                email,
                System.currentTimeMillis()
        );

        // 저장 : putItem() 메서드
        customerRepository.save(customer);

        // PK 반환
        return customerId;
    }

    // 조회하기
    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId);
    }

    public void deleteCustomer(String customerId) {
       customerRepository.deleteById(customerId);
    }

    public Customer updateCustomer(String customerId, CustomerUpdateRequeset request) {
        Customer existing = customerRepository.findById(customerId);

        if (existing == null) {
            return null;
        }

        if (request.name() != null) {
            existing.setName(request.name());
        }
        if (request.email() != null) {
            existing.setEmail(request.email());
        }
        return customerRepository.update(existing);
    }
}
