package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.pojo.Admin;
import com.zpq.pojo.User;
import com.zpq.service.LoginService;
import com.zpq.service.LoginServletImpl;

/**
 * Servlet implementation class CheckLoginServlet
 */
@WebServlet("/CheckLoginServlet")
public class CheckLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginServletImpl();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // response.getWriter().append("Served at: ").append(request.getContextPath());
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String entity = request.getParameter("entity");
        if (entity.equals("管理员")) {
            Admin admin;
            try {
                admin = loginService.selectAdmin(name, password);
                if (admin != null) {
                    response.getWriter().write("success");
                } else {
                    System.out.println("fail");
                    response.getWriter().write("fail");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (entity.equals("用户")) {
            User user;
            try {
                user = loginService.selectUser(name, password);
                if (user != null) {
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("fail");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}