package com.example.spring_internet_shop.service;

import com.example.spring_internet_shop.entity.Order;
import com.example.spring_internet_shop.entity.OrderItem;
import com.example.spring_internet_shop.entity.Product;
import com.example.spring_internet_shop.exception.BadRequestException;
import com.example.spring_internet_shop.exception.ResourceNotFoundException;
import com.example.spring_internet_shop.repository.OrderRepository;
import com.example.spring_internet_shop.repository.ProductRepository;
import com.example.spring_internet_shop.service.enums.ErrorsMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order get(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorsMessageEnum.NOT_FOUND.getMessage()));
    }


    @Override
    public void save(Order order) {
        if (order.getId() != null) {
            throw new BadRequestException(ErrorsMessageEnum.BAD_POST_ID_REQUEST.getMessage());
        }
        if (order.getStatus().equals("paid")) {
            throw new BadRequestException(ErrorsMessageEnum.STATUS_ALREADY_PAID.getMessage());
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void complete(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorsMessageEnum.NOT_FOUND.getMessage()));

        completeOrder(order);
    }


    private void completeOrder(Order order) {
        order.setOrderDate(LocalDate.now());
        order.setStatus("paid");

        List<OrderItem> orderItem = order.getOrderItems();

        for (OrderItem item : orderItem) {
            Product product = item.getProduct();
            product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());
        }
    }

    @Override
    public void update(Long id, Order receivedOrder) {
        var currentOrder = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorsMessageEnum.NOT_FOUND.getMessage()));

        updateOrder(currentOrder, receivedOrder.getOrderItems());
        orderRepository.save(currentOrder);
    }

    private void updateOrder(Order curOrder, List<OrderItem> items) {
        curOrder.getOrderItems().clear();
        curOrder.getOrderItems().addAll(ofNullable(items).orElse(emptyList()));
    }


    @Override
    public void delete(Long id) {
        orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorsMessageEnum.NOT_FOUND.getMessage()));

        orderRepository.deleteById(id);
    }
}