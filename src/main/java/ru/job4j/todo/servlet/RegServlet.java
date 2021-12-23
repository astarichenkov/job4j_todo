package ru.job4j.todo.servlet;


import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            HbmStore.instOf().findUserByEmail(email);
            req.setAttribute("register", "E-mail уже существует");
        } catch (NoResultException e) {
            User user = User.of(name, email, password, Role.of("USER", 2));
            HbmStore.instOf().add(user);
            req.setAttribute("register", "Регистрация завершена");
        }
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }
}