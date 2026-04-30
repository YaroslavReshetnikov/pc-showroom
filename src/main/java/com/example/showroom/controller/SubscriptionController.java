package com.example.showroom.controller;

import com.example.showroom.entity.Subscriber;
import com.example.showroom.repository.SubscriberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscriptionController {

    private final SubscriberRepository subscriberRepository;

    public SubscriptionController(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String email,
                            @RequestParam(required = false, defaultValue = "/") String redirectTo,
                            RedirectAttributes redirectAttributes) {

        String normalizedEmail = email.trim().toLowerCase();

        if (!subscriberRepository.existsByEmail(normalizedEmail)) {
            subscriberRepository.save(new Subscriber(normalizedEmail));
        }

        redirectAttributes.addFlashAttribute("subscribed", true);
        return "redirect:" + redirectTo;
    }

    @GetMapping("/admin/subscribers")
    public String subscribers(Model model) {
        model.addAttribute("subscribers", subscriberRepository.findAll());
        return "admin-subscribers";
    }

    @PostMapping("/admin/subscribers/delete/{id}")
    public String deleteSubscriber(@PathVariable Long id) {
        subscriberRepository.deleteById(id);
        return "redirect:/admin/subscribers";
    }
}