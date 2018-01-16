<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <!-- Static content -->
    <link rel="stylesheet" href="/resources/css/style.css">
    <script type="text/javascript" src="/resources/js/app.js"></script>

    <title>Spring Boot</title>
</head>
<body>
<h1> Flight Module - request for Flight Details</h1>
<hr>

<div class="form">
    <form action="/flight/price" method="post">
        <table>
            <tr>
                <td>Enter the FlightId &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><form:input path="id" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="Submit"></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>