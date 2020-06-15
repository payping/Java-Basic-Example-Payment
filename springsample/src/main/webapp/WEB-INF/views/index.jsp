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
<h1>PayPing Spring Sample</h1>
<hr>

<div class="form">
    <form action="CreatePay" method="post"   onsubmit="return validate()">
        <table>
            <tr>
                <td>Name</td>
                <td><input id="payerName" name="payerName" ></td>
            </tr>
            <tr>
                <td>Amount*</td>
                <td><input id="amount" name="amount" type="number" value="1000" ></td>
            </tr>
            <tr>
                <td>PhoneOrEmail</td>
                <td><input id="payerIdentity" name="payerIdentity" ></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><input id="description" name="description" ></td>
                <td><input type="submit" value="pay"></td>
            </tr>

        </table>
    </form>
</div>

</body>
</html>