/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Model.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mariam
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {
    public static ArrayList<User> userList = new ArrayList<>();
    public static ArrayList<User> onlineUsers = new ArrayList<>();
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gsonMessage = new Gson();
        String activeUsersObject = gsonMessage.toJson(onlineUsers);
        PrintWriter out = response.getWriter();
        out.write(activeUsersObject);
        out.close();
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("requestPage").equals("login")){//case of login
            String name = request.getParameter("userName");
            String pass = request.getParameter("pass");
            boolean found = false;
            for(User user : userList){
                if(user.getUser().equals(name) && user.getPassword().equals(pass)){
                    found = true;
                    onlineUsers.add(user);
                    break;
                }
            }
            if(found){
                HttpSession session = request.getSession(true);
                session.setAttribute("user",name);
                response.sendRedirect("ChatHome.jsp");
            }
            else{
                RequestDispatcher rd = request.getRequestDispatcher("index.html");
                rd.include(request, response);
                response.getOutputStream().print("invalid username or password");
                
            }
        }
        else{//case of logOut
            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");
            for(User user : onlineUsers){
                if(user.getUser().equals(name)){
                    onlineUsers.remove(user);
                    break;
                }
            }
            response.sendRedirect("index.html");
            session.invalidate();
        }
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
