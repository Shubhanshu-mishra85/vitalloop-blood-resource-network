
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.vitalloop.Donor" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Find Donors | VitalLoop</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #fff7f7;
            color: #252936;
        }

        main {
            max-width: 760px;
            margin: 50px auto;
            padding: 24px;
        }

        .panel {
            background: white;
            border: 1px solid #f0dfe2;
            border-radius: 14px;
            padding: 24px;
        }

        h1, h2 { color: #bd263a; }

        label {
            display: block;
            margin-top: 16px;
            font-weight: bold;
        }

        input, select, button {
            box-sizing: border-box;
            width: 100%;
            padding: 12px;
            margin-top: 7px;
            border-radius: 7px;
            font: inherit;
        }

        input, select {
            border: 1px solid #d8dbe2;
            background: white;
        }

        button {
            margin-top: 20px;
            border: none;
            background: #c9273b;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        .result {
            margin-top: 16px;
            padding: 14px;
            border: 1px solid #f0dfe2;
            border-radius: 8px;
        }

        .notice {
            color: #69501c;
            background: #fff2d9;
            padding: 12px;
            border-radius: 7px;
            font-size: .9rem;
        }
    </style>
</head>
<body>
<main>
    <div class="panel">
        <h1>VitalLoop</h1>
        <h2>Find potential donors</h2>

        <p>Search demo records by blood group and city.</p>

        <form action="${pageContext.request.contextPath}/donors/search"
              method="get">

            <label for="bloodGroup">Blood group</label>
            <select name="bloodGroup" id="bloodGroup" required>
                <option value="">Select blood group</option>
                <option>A+</option>
                <option>A-</option>
                <option>B+</option>
                <option>B-</option>
                <option>AB+</option>
                <option>AB-</option>
                <option>O+</option>
                <option>O-</option>
            </select>

            <label for="city">City</label>
            <input type="text" id="city" name="city"
                   maxlength="100" required>

            <button type="submit">Search Donors</button>
        </form>

        <div class="notice">
            Search results do not confirm a donor's current availability
            or medical eligibility. For urgent needs, contact a licensed
            blood bank or hospital directly.
        </div>

        <%
            List<Donor> donors =
                (List<Donor>) request.getAttribute("donors");

            Boolean searched =
                (Boolean) request.getAttribute("searched");

            if (Boolean.TRUE.equals(searched)) {
                if (donors != null && !donors.isEmpty()) {
                    for (Donor donor : donors) {
        %>
            <div class="result">
                <strong>Blood group:</strong>
                <%= donor.getBloodGroup() %><br>

                <strong>City:</strong>
                <%= donor.getCity() %>
            </div>
        <%
                    }
                } else {
        %>
            <p>No matching demo records found.</p>
        <%
                }
            }
        %>
    </div>
</main>
</body>
</html>
