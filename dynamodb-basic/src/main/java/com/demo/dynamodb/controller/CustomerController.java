package com.demo.dynamodb.controller;

import com.demo.dynamodb.dto.CustomerCreateRequest;
import com.demo.dynamodb.dto.CustomerResponse;
import com.demo.dynamodb.entity.Customer;
import com.demo.dynamodb.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/customers")
@RequiredArgsConstructor
@RestController
public class CustomerController {

    private final CustomerService customerService;

    // [저장하기]
    @PostMapping
    public ResponseEntity<String> save(@RequestBody CustomerCreateRequest request) {
        String customerId = customerService.saveCustomer(
                request.name(),
                request.email()
        );

        // 201, 등록된 customerId 반환
        return ResponseEntity.status(201).body(customerId);
    }

    // 단건 조회
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> findCustomer(
            @PathVariable("customerId") String customerId
    ) {
        // 조회
        Customer foundCustomer = customerService.getCustomer(customerId);


        if ( foundCustomer == null){
            return ResponseEntity.notFound().build();
        }
        CustomerResponse response = new CustomerResponse(
                foundCustomer.getCustomerId(),
                foundCustomer.getName(),
                foundCustomer.getEmail(),
                foundCustomer.getCreateAt()
        );
        return ResponseEntity.ok(response);
    }


}

