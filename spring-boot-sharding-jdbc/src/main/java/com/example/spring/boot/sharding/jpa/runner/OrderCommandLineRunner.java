package com.example.spring.boot.sharding.jpa.runner;

import com.example.spring.boot.sharding.jpa.entity.Order;
import com.example.spring.boot.sharding.jpa.entity.OrderItem;
import com.example.spring.boot.sharding.jpa.entity.User;
import com.example.spring.boot.sharding.jpa.repository.OrderItemRepository;
import com.example.spring.boot.sharding.jpa.repository.OrderRepository;
import com.example.spring.boot.sharding.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderCommandLineRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommandLineRunner.class);

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    @Resource
    private UserRepository userRepository;

    public static int random() {
        long random = System.nanoTime() % 100;
        // () 强转操作符优先级高于取模操作符
        return (int) random;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("name " + random());
            long userId = userRepository.save(user).getId();
            LOGGER.info("user id : {}", userId);

            Order order = new Order();
            order.setUserId(userId);
            order.setStatus("test " + random());

            long orderId = orderRepository.save(order).getOrderId();
            LOGGER.info("order id : {}", orderId);

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setUserId(userId);
            long orderItemId = orderItemRepository.save(item).getOrderItemId();
            LOGGER.info("order item id : {}", orderItemId);
        }

        LOGGER.info("insert order finished");

        userRepository.findAll().forEach(System.out::println);
        orderItemRepository.findAll().forEach(System.out::println);
    }

}
