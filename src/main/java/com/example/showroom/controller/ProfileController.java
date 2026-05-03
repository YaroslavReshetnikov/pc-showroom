package com.example.showroom.controller;

import com.example.showroom.entity.BuildRecord;
import com.example.showroom.entity.OrderRecord;
import com.example.showroom.model.BuildView;
import com.example.showroom.repository.BuildRecordRepository;
import com.example.showroom.repository.OrderRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    private final OrderRecordRepository orderRecordRepository;
    private final BuildRecordRepository buildRecordRepository;

    public ProfileController(OrderRecordRepository orderRecordRepository,
                             BuildRecordRepository buildRecordRepository) {
        this.orderRecordRepository = orderRecordRepository;
        this.buildRecordRepository = buildRecordRepository;
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        List<OrderRecord> orders = orderRecordRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .toList();

        List<BuildRecord> buildRecords = buildRecordRepository.findAll();

        List<OrderRecord> buildOrders = orders.stream()
                .filter(order -> "BUILD".equals(order.getOrderType()) &&
                        order.getBuildRecordId() != null)
                .toList();

        List<BuildView> builds = new ArrayList<>();

        for (BuildRecord buildRecord : buildRecords) {

            boolean alreadyOrdered = false;

            for (OrderRecord order : buildOrders) {
                if (buildRecord.getId().equals(order.getBuildRecordId())) {
                    alreadyOrdered = true;
                    break;
                }
            }

            if (alreadyOrdered) {
                continue;
            }

            BuildView buildView = new BuildView();

            buildView.setId(buildRecord.getId());
            buildView.setCpu(buildRecord.getCpu());
            buildView.setGpu(buildRecord.getGpu());
            buildView.setRam(buildRecord.getRam());
            buildView.setMother(buildRecord.getMother());
            buildView.setPsu(buildRecord.getPsu());
            buildView.setCooling(buildRecord.getCooling());
            buildView.setOther(buildRecord.getOther());
            buildView.setTotal(buildRecord.getTotal());
            buildView.setStatus("Не оформлено");

            builds.add(buildView);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("builds", builds);

        return "profile";
    }

    @PostMapping("/profile/delete/{id}")
    public String deleteCompletedOrderFromProfile(@PathVariable Long id) {
        OrderRecord order = orderRecordRepository.findById(id).orElse(null);

        if (order != null && "Виконано".equals(order.getStatus())) {


            if ("BUILD".equals(order.getOrderType()) && order.getBuildRecordId() != null) {
                if (buildRecordRepository.existsById(order.getBuildRecordId())) {
                    buildRecordRepository.deleteById(order.getBuildRecordId());
                }
            }

            orderRecordRepository.deleteById(id);
        }

        return "redirect:/profile";
    }
}