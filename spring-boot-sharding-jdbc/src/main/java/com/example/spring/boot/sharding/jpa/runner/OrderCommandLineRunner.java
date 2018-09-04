/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.example.spring.boot.sharding.jpa.runner;

import com.example.spring.boot.sharding.jpa.entity.Order;
import com.example.spring.boot.sharding.jpa.entity.OrderItem;
import com.example.spring.boot.sharding.jpa.repository.OrderItemRepository;
import com.example.spring.boot.sharding.jpa.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderCommandLineRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommandLineRunner.class);

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    public static int random100() {
        Long random = System.nanoTime() % 100;
        return random.intValue();
    }

    @Override
    public void run(String... args) throws Exception {
        List<Long> orderIds = new ArrayList<>(10);
        List<Long> orderItemIds = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            int userId = random100();
            LOGGER.info("user id : {}", userId);

            Order order = new Order();
            order.setUserId(userId);
            order.setStatus("test at " + LocalDateTime.now());

            long orderId = orderRepository.save(order).getOrderId();
            LOGGER.info("order id : {}", orderId);

            orderIds.add(orderId);
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setUserId(userId);
            orderItemIds.add(orderItemRepository.save(item).getOrderItemId());
        }

        LOGGER.info("insert order finished");

        List<OrderItem> orderItems = orderItemRepository.findAll();
        orderItems.stream().forEach(System.out::println);
    }

}
