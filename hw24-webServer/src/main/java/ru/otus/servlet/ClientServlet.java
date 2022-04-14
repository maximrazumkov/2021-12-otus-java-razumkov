package ru.otus.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dao.ClientDao;
import ru.otus.dao.UserDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final ObjectMapper objectMapper;

    public ClientServlet(ClientDao clientDao, ObjectMapper objectMapper) {
        this.clientDao = clientDao;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = clientDao.findAll();
        List<Map<String, String>> result = clients.stream()
                .map(client -> Map.of(
                        "fullName", client.getName(),
                        "address", client.getAddress() != null ? client.getAddress().getStreet() : "",
                        "phone", client.getPhones().isEmpty() ? "" : client.getPhones().get(0).getNumber()))
                .collect(Collectors.toList());
        response.setContentType("text/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                response.getOutputStream(), StandardCharsets.UTF_8), true);
        writer.println(objectMapper.writeValueAsString(result));
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter("fullName");
        Address address = new Address(req.getParameter("address"));
        Phone phone = new Phone(req.getParameter("phone"));
        Client client = new Client(name, address, List.of(phone));
        clientDao.saveClient(client);
        //response.sendRedirect("/client");
    }

}
