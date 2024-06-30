//package com.example.productservice.controller;
//
//import com.example.productservice.config.RabbitMQConfig;
//import com.example.productservice.exception.InvalidException;
//import com.example.productservice.payload.ActorRequest;
//import com.example.productservice.service.CustomerService;
//import com.example.productservice.service.EmployeeService;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class Consumer {
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @RabbitHandler
//    @RabbitListener(queues = RabbitMQConfig.QUEUE)
//    public void receiveMessage(ActorRequest actorRequest) throws InvalidException {
//        if (actorRequest.getRole().equals("EMPLOYEE")) {
//            employeeService.create(actorRequest);
//        }
//        else {
//            customerService.create(actorRequest);
//        }
//    }
//}
