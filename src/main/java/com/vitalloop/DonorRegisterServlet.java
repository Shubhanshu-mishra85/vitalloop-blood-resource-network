
package com.vitalloop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/donors/register")
public class DonorRegisterServlet extends HttpServlet {

    private final DonorDAO donorDAO = new DonorDAO();

    private static final List<String> BLOOD_GROUPS = List.of(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    );

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // A real registration must require an authenticated user.
        Object userIdObject = request.getSession(false) == null
            ? null
            : request.getSession(false).getAttribute("userId");

        if (!(userIdObject instanceof Integer)) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please sign in before registering as a donor."
            );
            return;
        }

        String bloodGroup = request.getParameter("bloodGroup");
        String city = request.getParameter("city");
        boolean consent =
            "yes".equals(request.getParameter("consentToContact"));

        if (bloodGroup == null
                || !BLOOD_GROUPS.contains(bloodGroup)
                || city == null
                || city.isBlank()
                || city.trim().length() > 100
                || !consent) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Enter valid details and provide contact consent."
            );
            return;
        }

        try {
            int donorId = donorDAO.registerDonor(
                (Integer) userIdObject,
                bloodGroup,
                city.trim(),
                true
            );

            response.sendRedirect(
                request.getContextPath()
                    + "/donor-registration-success.jsp?id="
                    + donorId
            );

        } catch (SQLException e) {
            throw new ServletException(
                "Registration could not be completed.", e
            );
        }
    }
}
