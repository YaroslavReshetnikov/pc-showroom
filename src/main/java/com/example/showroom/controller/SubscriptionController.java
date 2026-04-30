package com.example.showroom.controller;

import com.example.showroom.entity.Subscriber;
import com.example.showroom.repository.SubscriberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}