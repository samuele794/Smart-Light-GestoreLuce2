/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Erroring;
import beans.LampadinaStatus;
import beans.StatusObject;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.ReleModule;

/**
 *
 * @author samuPC
 */
@WebServlet(name = "loop", urlPatterns = {"/loop"})
public class LoopSevlet extends HttpServlet {

    private static String paramTime = "time";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();

        if (request.getQueryString() != null) {

            Scanner scanner = new Scanner(paramTime);

            if (scanner.hasNextInt()) {
                if (ReleModule.isConnected()) {
                    ReleModule.pulseOn(scanner.nextInt());
                } else {
                    int tentative = 0;
                    StatusObject status;
                    do {
                        status = ReleModule.startConnection();
                        if (!status.isException()) {
                            break;
                        }
                        tentative++;
                    } while (tentative > 3);

                    if (status.isException()) {
                        Erroring e = new Erroring(status.getMessage());
                        String error = gson.toJson(e);
                        response.getOutputStream().print(error);
                    } else {
                        ReleModule.pulseOn(scanner.nextInt());
                    }
                }
            } else {
                Erroring e = new Erroring("Parametri non conformi");
                String error = gson.toJson(e);
                response.getOutputStream().print(error);
            }

        } else {
            Erroring e = new Erroring("Parametri non conformi");
            String error = gson.toJson(e);
            response.getOutputStream().print(error);
        }

        response.getOutputStream().flush();
        response.getOutputStream().close();
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
        processRequest(request, response);
    }

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
        processRequest(request, response);
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
