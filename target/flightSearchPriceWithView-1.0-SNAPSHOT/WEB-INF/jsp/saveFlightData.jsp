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
<h1> Flight Module - Save Flight data ( Flight Repository) </h1>
<hr>

<div class="form">
    <form action="/flight/saveFlightSearchResponse" method="post" onsubmit="return validate()">
        <table>
            <tr>
                <td>Enter origin of Flight &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="origin" name="origin"></td>
            </tr>
            <tr>
                <td>Enter destination of Flight &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="destination" name="destination"></td>
            </tr>
            <tr>
                <td>Is Flight Refundable &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="isRefundable" name="isRefundable"></td>
            </tr>
            <tr>
                <td>Enter Flight origin Depart Date &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="originDepartDate" name="originDepartDate"></td>
            </tr>
            <tr>
                <td>Enter Flight origin Depart Time &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="originDepartTime" name="originDepartTime"></td>
            </tr>
            <tr>
                <td>Enter Flight Destination arrival date &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="destinationArrivalDate" name="destinationArrivalDate"></td>
            </tr>
            <tr>
                <td>Enter Flight Destination arrival time &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="destinationArrivalTime" name="destinationArrivalTime"></td>
            </tr>
            <tr>
                <td>Enter Flight Name &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="flightName" name="flightName"></td>
            </tr>
            <tr>
                <td>Enter Flight Number &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="flightNumber" name="flightNumber"></td>
            </tr>
            <tr>
                <td>Enter seat remaining &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="seatRemain" name="seatRemain"></td>
            </tr>
            <tr>
                <td>Enter Price per Adult &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="pricePerAdult" name="pricePerAdult"></td>
            </tr>
            <tr>
                <td>Enter Price per Child &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="pricePerChild" name="pricePerChild"></td>
            </tr>
            <tr>
                <td>Enter Price per Infant &nbsp;&nbsp;: &nbsp; &nbsp;</td>
                <td><input id="pricePerInfant" name="pricePerInfant"></td>
            </tr>

            <tr>
                <td><input type="submit" value="Submit"></td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>