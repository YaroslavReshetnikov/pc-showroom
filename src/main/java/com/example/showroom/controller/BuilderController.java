package com.example.showroom.controller;

import com.example.showroom.entity.BuildRecord;
import com.example.showroom.entity.OrderRecord;
import com.example.showroom.model.Build;
import com.example.showroom.model.BuildView;
import com.example.showroom.model.PickupOrder;
import com.example.showroom.model.Product;
import com.example.showroom.repository.BuildRecordRepository;
import com.example.showroom.repository.OrderRecordRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/builder")
public class BuilderController {

    private final ProductController productController;
    private final OrderRecordRepository orderRecordRepository;
    private final BuildRecordRepository buildRecordRepository;

    public BuilderController(ProductController productController,
                             OrderRecordRepository orderRecordRepository,
                             BuildRecordRepository buildRecordRepository) {
        this.productController = productController;
        this.orderRecordRepository = orderRecordRepository;
        this.buildRecordRepository = buildRecordRepository;
    }

    @GetMapping
    public String builder(Model model, HttpSession session) {

        Build build = (Build) session.getAttribute("build");

        if (build == null) {
            build = new Build();
            session.setAttribute("build", build);
        }

        double total = calculateTotal(build);
        model.addAttribute("total", total);

        List<Product> products = productController.getAllProducts();

        model.addAttribute("cpuList",
                products.stream().filter(p -> p.getCategory().equals("CPU")).toList());

        model.addAttribute("gpuList",
                products.stream().filter(p -> p.getCategory().equals("GPU")).toList());

        model.addAttribute("ramList",
                products.stream().filter(p -> p.getCategory().equals("RAM")).toList());

        model.addAttribute("motherList",
                products.stream().filter(p -> p.getCategory().equals("Mother")).toList());

        model.addAttribute("otherList",
                products.stream().filter(p -> p.getCategory().equals("Other")).toList());

        model.addAttribute("psuList",
                products.stream().filter(p -> p.getCategory().equals("PSU")).toList());

        model.addAttribute("coolingList",
                products.stream().filter(p -> p.getCategory().equals("Cooling")).toList());

        model.addAttribute("build", build);

        Long editBuildId = (Long) session.getAttribute("editBuildId");
        model.addAttribute("editBuildId", editBuildId);

        return "builder";
    }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable Long id, Model model) {

        BuildRecord selectedBuild = buildRecordRepository.findById(id).orElse(null);

        if (selectedBuild == null) {
            return "redirect:/profile";
        }

        model.addAttribute("buildId", id);
        model.addAttribute("selectedBuild", selectedBuild);

        return "checkout";
    }

    @PostMapping("/checkout/{id}")
    public String confirmCheckout(@PathVariable Long id,
                                  @RequestParam String customerName,
                                  @RequestParam String phone,
                                  @RequestParam String city,
                                  @RequestParam String showroomAddress,
                                  @RequestParam(required = false) String comment,
                                  Model model) {

        BuildRecord selectedBuild = buildRecordRepository.findById(id).orElse(null);

        if (selectedBuild == null) {
            return "redirect:/profile";
        }

        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOrderType("BUILD");
        orderRecord.setCustomerName(customerName);
        orderRecord.setPhone(phone);
        orderRecord.setCity(city);
        orderRecord.setDeliveryMethod("SHOWROOM");
        orderRecord.setShowroomAddress(showroomAddress);
        orderRecord.setBranchInfo(showroomAddress);
        orderRecord.setComment(comment);
        orderRecord.setTotalPrice(selectedBuild.getTotal());
        orderRecord.setCreatedAt(LocalDateTime.now());
        orderRecord.setStatus("У роботі");
        orderRecord.setBuildRecordId(selectedBuild.getId());

        StringBuilder items = new StringBuilder();

        if (selectedBuild.getCpu() != null && !selectedBuild.getCpu().isBlank()) {
            items.append("CPU: ").append(selectedBuild.getCpu()).append("\n");
        }
        if (selectedBuild.getGpu() != null && !selectedBuild.getGpu().isBlank()) {
            items.append("GPU: ").append(selectedBuild.getGpu()).append("\n");
        }
        if (selectedBuild.getRam() != null && !selectedBuild.getRam().isBlank()) {
            items.append("RAM: ").append(selectedBuild.getRam()).append("\n");
        }
        if (selectedBuild.getMother() != null && !selectedBuild.getMother().isBlank()) {
            items.append("Mother: ").append(selectedBuild.getMother()).append("\n");
        }
        if (selectedBuild.getPsu() != null && !selectedBuild.getPsu().isBlank()) {
            items.append("PSU: ").append(selectedBuild.getPsu()).append("\n");
        }
        if (selectedBuild.getCooling() != null && !selectedBuild.getCooling().isBlank()) {
            items.append("Cooling: ").append(selectedBuild.getCooling()).append("\n");
        }
        if (selectedBuild.getOther() != null && !selectedBuild.getOther().isBlank()) {
            items.append("Other: ").append(selectedBuild.getOther()).append("\n");
        }

        orderRecord.setItemsSummary(items.toString());
        orderRecordRepository.save(orderRecord);

        PickupOrder order = new PickupOrder();
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setShowroomAddress(showroomAddress);

        model.addAttribute("order", order);
        model.addAttribute("customerName", customerName);
        model.addAttribute("phone", phone);
        model.addAttribute("city", city);
        model.addAttribute("showroomAddress", showroomAddress);
        model.addAttribute("comment", comment);

        return "order-success";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, HttpSession session) {

        BuildRecord saved = buildRecordRepository.findById(id).orElse(null);

        if (saved != null) {
            Build copy = new Build();

            List<Product> products = productController.getAllProducts();

            if (saved.getCpu() != null) {
                products.stream()
                        .filter(p -> saved.getCpu().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setCpu);
            }

            if (saved.getGpu() != null) {
                products.stream()
                        .filter(p -> saved.getGpu().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setGpu);
            }

            if (saved.getRam() != null) {
                products.stream()
                        .filter(p -> saved.getRam().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setRam);
            }

            if (saved.getMother() != null) {
                products.stream()
                        .filter(p -> saved.getMother().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setMother);
            }

            if (saved.getOther() != null) {
                products.stream()
                        .filter(p -> saved.getOther().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setOther);
            }

            if (saved.getPsu() != null) {
                products.stream()
                        .filter(p -> saved.getPsu().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setPsu);
            }

            if (saved.getCooling() != null) {
                products.stream()
                        .filter(p -> saved.getCooling().equals(p.getName()))
                        .findFirst()
                        .ifPresent(copy::setCooling);
            }

            session.setAttribute("build", copy);
            session.setAttribute("editBuildId", id);
        }

        return "redirect:/builder";
    }

    @GetMapping("/save")
    public String save(HttpSession session) {

        Build build = (Build) session.getAttribute("build");

        if (build == null) {
            return "redirect:/builder";
        }

        double total = calculateTotal(build);

        Long editBuildId = (Long) session.getAttribute("editBuildId");

        BuildRecord buildRecord;
        if (editBuildId != null) {
            buildRecord = buildRecordRepository.findById(editBuildId).orElse(new BuildRecord());
        } else {
            buildRecord = new BuildRecord();
        }

        buildRecord.setCpu(build.getCpu() != null ? build.getCpu().getName() : null);
        buildRecord.setGpu(build.getGpu() != null ? build.getGpu().getName() : null);
        buildRecord.setRam(build.getRam() != null ? build.getRam().getName() : null);
        buildRecord.setMother(build.getMother() != null ? build.getMother().getName() : null);
        buildRecord.setPsu(build.getPsu() != null ? build.getPsu().getName() : null);
        buildRecord.setCooling(build.getCooling() != null ? build.getCooling().getName() : null);
        buildRecord.setOther(build.getOther() != null ? build.getOther().getName() : null);
        buildRecord.setTotal(total);

        buildRecordRepository.save(buildRecord);

        session.setAttribute("build", new Build());
        session.removeAttribute("editBuildId");

        return "redirect:/profile";
    }

    @GetMapping("/my-builds")
    public String myBuilds(Model model) {

        List<BuildRecord> buildRecords = buildRecordRepository.findAll();
        List<OrderRecord> buildOrders = orderRecordRepository.findAll().stream()
                .filter(order -> "BUILD".equals(order.getOrderType()) && order.getBuildRecordId() != null)
                .toList();

        List<BuildView> builds = new ArrayList<>();

        for (BuildRecord buildRecord : buildRecords) {
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

            String status = "Не оформлено";

            for (OrderRecord order : buildOrders) {
                if (buildRecord.getId().equals(order.getBuildRecordId())) {
                    status = order.getStatus();
                    break;
                }
            }

            buildView.setStatus(status);
            builds.add(buildView);
        }

        model.addAttribute("builds", builds);
        return "my-builds";
    }

    @GetMapping("/new")
    public String newBuild(HttpSession session) {
        session.setAttribute("build", new Build());
        session.removeAttribute("editBuildId");
        return "redirect:/builder";
    }

    @GetMapping("/add/{type}/{id}")
    public String add(@PathVariable String type,
                      @PathVariable Long id,
                      HttpSession session) {

        Build build = (Build) session.getAttribute("build");

        if (build == null) {
            build = new Build();
            session.setAttribute("build", build);
        }

        List<Product> products = productController.getAllProducts();

        Product selected = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            switch (type) {
                case "cpu" -> build.setCpu(selected);
                case "gpu" -> build.setGpu(selected);
                case "ram" -> build.setRam(selected);
                case "mother" -> build.setMother(selected);
                case "other" -> build.setOther(selected);
                case "psu" -> build.setPsu(selected);
                case "cooling" -> build.setCooling(selected);
            }
        }

        return "redirect:/builder";
    }

    @GetMapping("/remove/{type}")
    public String remove(@PathVariable String type,
                         HttpSession session) {

        Build build = (Build) session.getAttribute("build");

        if (build == null) {
            return "redirect:/builder";
        }

        switch (type) {
            case "cpu" -> build.setCpu(null);
            case "gpu" -> build.setGpu(null);
            case "ram" -> build.setRam(null);
            case "mother" -> build.setMother(null);
            case "other" -> build.setOther(null);
            case "psu" -> build.setPsu(null);
            case "cooling" -> build.setCooling(null);
        }

        return "redirect:/builder";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        if (buildRecordRepository.existsById(id)) {
            buildRecordRepository.deleteById(id);
        }
        return "redirect:/profile";
    }

    private double calculateTotal(Build build) {
        double total = 0;

        if (build.getCpu() != null) total += build.getCpu().getPrice();
        if (build.getGpu() != null) total += build.getGpu().getPrice();
        if (build.getRam() != null) total += build.getRam().getPrice();
        if (build.getMother() != null) total += build.getMother().getPrice();
        if (build.getOther() != null) total += build.getOther().getPrice();
        if (build.getPsu() != null) total += build.getPsu().getPrice();
        if (build.getCooling() != null) total += build.getCooling().getPrice();

        return total;
    }
}