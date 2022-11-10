package com.example.spring_internet_shop.controller;

import com.example.spring_internet_shop.entity.Order;
import com.example.spring_internet_shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    @RequestMapping("/show-orders")
    public String showAllOrders(Model model) {
        List<Order> orders = orderService.getAll();
        model.addAttribute("allOrders", orders);
        return "all-orders";
    }
}
