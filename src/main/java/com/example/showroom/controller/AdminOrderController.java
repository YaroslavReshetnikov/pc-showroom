package com.example.showroom.controller;

import com.example.showroom.entity.OrderRecord;
import com.example.showroom.repository.BuildRecordRepository;
import com.example.showroom.repository.OrderRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminOrderController {

    private final OrderRecordRepository orderRecordRepository;
    private final BuildRecordRepository buildRecordRepository;

    public AdminOrderController(OrderRecordRepository orderRecordRepository,
                                BuildRecordRepository buildRecordRepository) {
        this.orderRecordRepository = orderRecordRepository;
        this.buildRecordRepository = buildRecordRepository;
    }

    @GetMapping("/admin/orders")
    public String allOrders(Model model) {
        model.addAttribute("orders",
                orderRecordRepository.findAll()
                        .stream()
                        .sorted((a, b) -> b.getId().compareTo(a.getId()))
                        .toList());
        return "admin-orders";
    }

    @PostMapping("/admin/orders/status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String status) {

        OrderRecord order = orderRecordRepository.findById(id).orElse(null);

        if (order != null) {
            order.setStatus(status);
            orderRecordRepository.save(order);
        }

        return "redirect:/admin/orders";
    }

    @PostMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {

        OrderRecord order = orderRecordRepository.findById(id).orElse(null);

        if (order != null) {

            if ("BUILD".equals(order.getOrderType()) && order.getBuildRecordId() != null) {
                if (buildRecordRepository.existsById(order.getBuildRecordId())) {
                    buildRecordRepository.deleteById(order.getBuildRecordId());
                }
            }

            orderRecordRepository.deleteById(id);
        }

        return "redirect:/admin/orders";
    }
}