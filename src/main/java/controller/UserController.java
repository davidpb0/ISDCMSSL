/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author davidpb0
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (null == action) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
        else switch (action) {
            case "registerUser":
                registerUser(request, response);
                break;
            case "loginUser":
                loginUser(request, response);
                break;
            case "logoutUser":
                logoutUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                break;
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");
        String username = request.getParameter("username");
        String surnames = request.getParameter("surnames");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User user = new User(nickname, username, surnames, email, password);
        
        if (user.saveUser()){
            HttpSession session = request.getSession(true);
            session.setAttribute("user", nickname);
            response.sendRedirect("home.jsp");
        }
        else {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "The user with that username or email already exists");
            response.sendRedirect("register.jsp");
        }
        
    }
    
    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        if (User.authenticateUser(nickname, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", nickname);
            response.sendRedirect("home.jsp");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid username or password");
            response.sendRedirect("login.jsp");
        }
    }
    
    private void logoutUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
