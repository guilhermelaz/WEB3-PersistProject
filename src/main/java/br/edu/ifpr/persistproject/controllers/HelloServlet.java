package br.edu.ifpr.persistproject.controllers;

import java.io.*;
import java.util.List;

import br.edu.ifpr.persistproject.model.Seller;
import br.edu.ifpr.persistproject.repository.SellerRepository;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/")
public class HelloServlet extends HttpServlet {

    SellerRepository repository = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        repository = new SellerRepository();
        List<Seller> sellers = repository.getSellers();



    }

    public void destroy() {
    }
}