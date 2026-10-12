
package com.vitalloop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/donors/search")
public class DonorSearchServlet extends HttpServlet {

    private final DonorDAO donorDAO = new DonorDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String bloodGroup = request.getParameter("bloodGroup");
        String city = request.getParameter("city");

        request.setAttribute("donors", List.of());
        request.setAttribute("searched", false);

        if (bloodGroup != null && city != null
                && !bloodGroup.isBlank()
                && !city.isBlank()) {

            if (!List.of("A+", "A-", "B+", "B-", "AB+",
                    "AB-", "O+", "O-").contains(bloodGroup)) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid blood group."
                );
                return;
            }

            if (city.length() > 100) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "City name is too long."
                );
                return;
            }

            try {
                List<Donor> donors =
                    donorDAO.searchAvailableDonors(bloodGroup, city);

                request.setAttribute("donors", donors);
                request.setAttribute("searched", true);

            } catch (SQLException e) {
                throw new ServletException(
                    "Donor search is temporarily unavailable.", e);
            }
        }

        request.getRequestDispatcher("/donor-search.jsp")
               .forward(request, response);
    }
}
