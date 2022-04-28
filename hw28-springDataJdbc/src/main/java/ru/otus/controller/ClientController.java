package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.dto.ClientDto;
import ru.otus.service.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/client")
    public String getClients(Model model) {
        model.addAttribute("client", new ClientDto());
        model.addAttribute("clients", clientService.findAll());
        return "client";
    }

    @PostMapping("/client")
    public String createClient(@ModelAttribute("client") ClientDto client) {
        clientService.save(client);
        return "redirect:/client";
    }
}
