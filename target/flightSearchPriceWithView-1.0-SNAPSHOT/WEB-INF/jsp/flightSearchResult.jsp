<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.Map" %>
<%@ page import ="java.util.List" %>
<%@ page import="com.coviam.entity.FlightSearchResponse" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Test</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 50%;
    }

    td, th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #dddddd;
    }
    </style>
</head>
<body>
<form>

   <% Map <String,List<FlightSearchResponse>> flightMap = (Map<String,List<FlightSearchResponse>>)request.getAttribute("flightResult"); %>
    <%for(int i = 0; i < flightMap.size(); i++){ %>
    <%List<FlightSearchResponse> flightList  = (List<FlightSearchResponse>)flightMap.get(i); %>
    <% if(i ==0){%>
        <td> Ongoing Flights &nbsp;&nbsp;:&nbsp;&nbsp; </td>
      <% }else{%>
          <td> Return Flights &nbsp;&nbsp;: &nbsp;&nbsp;</td>
         <% }%>
    <br><br><br>

      <% for(int j = 0; j < flightList.size(); j++) { %>
      <table>
      <tr>
        <tr><td> Origin &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getOrigin()%></td></tr>
        <tr><td> Destination &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getDestination()%></td></tr>
        <tr><td> FlightName &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getFlightName()%></td></tr>
        <tr><td> FlightNumber &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getFlightNumber()%></td></tr>
        <tr><td> DepartDate &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getOriginDepartDate()%></td></tr>
        <tr><td> DepartTime &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getOriginDepartTime()%></td></tr>
        <tr><td> ArrivalDate &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getDestinationArrivalDate()%></td></tr>
        <tr><td> ArrivalTime &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getDestinationArrivalTime()%></td></tr>
        <tr><td> AdultSeatPrice &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getPricePerAdult()%></td></tr>
        <tr><td> ChildSeatPrice &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getPricePerChild()%></td></tr>
        <tr><td> InfantSeatPrice &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getPricePerInfant()%></td></tr>
        <tr><td> SeatRemain &nbsp;&nbsp;: &nbsp;&nbsp;</td> <td><%=flightList.get(j).getSeatRemain()%></td></tr>
      </tr>
      </table>
      <% } %>
      </br></br></br>
    <% } %>
</form>

</body>
</html>
