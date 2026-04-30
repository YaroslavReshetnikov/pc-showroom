package com.example.showroom.controller;

import com.example.showroom.entity.OrderRecord;
import com.example.showroom.model.Product;
import com.example.showroom.repository.OrderRecordRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private final ProductController productController;
    private final OrderRecordRepository orderRecordRepository;

    public CartController(ProductController productController,
                          OrderRecordRepository orderRecordRepository) {
        this.productController = productController;
        this.orderRecordRepository = orderRecordRepository;
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        return cart == null ? 0 : cart.size();
    }

    @SuppressWarnings("unchecked")
    private List<Product> getCart(HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        List<Product> cart = getCart(session);

        double total = calculateTotal(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.setAttribute("cart", new ArrayList<Product>());
        return "redirect:/cart";
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        List<Product> products = productController.getAllProducts();
        List<Product> cart = getCart(session);

        Product selected = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            cart.add(selected);
        }

        return "redirect:/periphery";
    }

    @GetMapping("/cart/remove/{index}")
    public String removeFromCart(@PathVariable int index, HttpSession session) {
        List<Product> cart = getCart(session);

        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkout(Model model, HttpSession session) {
        List<Product> cart = getCart(session);

        double total = calculateTotal(cart);

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "cart-checkout";
    }

    @PostMapping("/cart/checkout")
    public String confirmCheckout(@RequestParam String customerName,
                                  @RequestParam String phone,
                                  @RequestParam String city,
                                  @RequestParam String deliveryMethod,
                                  @RequestParam(required = false) String novaType,
                                  @RequestParam String branchNumber,
                                  @RequestParam(required = false) String comment,
                                  Model model,
                                  HttpSession session) {

        List<Product> cart = getCart(session);

        double total = calculateTotal(cart);

        String deliveryInfo;
        if ("Нова пошта".equals(deliveryMethod)) {
            deliveryInfo = city + ", " + deliveryMethod + ", " + novaType + ", " + branchNumber;
        } else {
            deliveryInfo = city + ", " + deliveryMethod + ", " + branchNumber;
        }

        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOrderType("PERIPHERY");
        orderRecord.setCustomerName(customerName);
        orderRecord.setPhone(phone);
        orderRecord.setCity(city);
        orderRecord.setDeliveryMethod(deliveryMethod);
        orderRecord.setBranchInfo(branchNumber);
        orderRecord.setComment(comment);
        orderRecord.setTotalPrice(total);
        orderRecord.setCreatedAt(LocalDateTime.now());
        orderRecord.setStatus("У роботі");

        StringBuilder items = new StringBuilder();
        for (Product product : cart) {
            items.append(product.getName())
                    .append(" - ")
                    .append(product.getPrice())
                    .append(" $\n");
        }
        orderRecord.setItemsSummary(items.toString());

        if ("Нова пошта".equals(deliveryMethod)) {
            orderRecord.setShowroomAddress(city + ", Нова пошта, " + novaType + ", " + branchNumber);
        } else {
            orderRecord.setShowroomAddress(city + ", Укрпошта, " + branchNumber);
        }

        orderRecordRepository.save(orderRecord);

        model.addAttribute("customerName", customerName);
        model.addAttribute("phone", phone);
        model.addAttribute("city", city);
        model.addAttribute("deliveryMethod", deliveryMethod);
        model.addAttribute("novaType", novaType);
        model.addAttribute("branchNumber", branchNumber);
        model.addAttribute("deliveryInfo", deliveryInfo);
        model.addAttribute("comment", comment);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        session.setAttribute("cart", new ArrayList<Product>());

        return "cart-success";
    }

    private double calculateTotal(List<Product> cart) {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice();
        }
        return total;
    }
}