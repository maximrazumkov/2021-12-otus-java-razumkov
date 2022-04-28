package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        List<ClientDto> clientsDto = clients.stream().map(ClientDto::new).toList();
        Map<String, Object> paramsMap = Map.of("clients", clientsDto);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage("client.html", paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter("fullName");
        Address address = new Address(req.getParameter("address"));
        Phone phone = new Phone(req.getParameter("phone"));
        Client client = new Client(name, address, List.of(phone));
        dbServiceClient.saveClient(client);
        response.sendRedirect("/client");
    }

}
