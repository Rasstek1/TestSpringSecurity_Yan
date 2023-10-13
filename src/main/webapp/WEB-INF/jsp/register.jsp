<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login Page</title>
    <style>
        .error {
            padding: 15px;
            margin-bottom: 20px;
            color:red;
        }

        .msg {
            padding: 15px;
            margin-bottom: 20px;
            color: #31708f;
            background-color:#d9edf7;
        }

        #register-box {
            width: 300px;
            padding: 20px;
            margin: 100px auto;
            background: #fff;
            -webkit-border-radius: 2px;
            -moz-border-radius: 2px;
            border: 1px solid #000;
        }
    </style>
</head>
<body onload='document.registerForm.username.focus();'>
<div id="register-box">

    <c:if test="${not empty registerError}" >
        <div class="error">${registerErrorMessage}</div>
    </c:if>
    <h2>Spring Security Register Form</h2>
    <form name='registerForm' action="<c:url value='/login/register' />" method='POST'>
        <table>
            <tr>
                <td>Le nom de l'usager:</td>
                <td><input type='text' name='username' value=''></td>
            </tr>
            <tr>
                <td>Le mot de passe:</td>
                <td><input type='password' name='password' /></td>
            </tr>
            <tr>
                <td>L'adresse courriel:</td>
                <td><input type='email' name='courriel' /></td>
            </tr>
            <tr>
                <td colspan='2'>
                    <input name="submit" type="submit"  value="submit" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                </td>
            </tr>
        </table>



    </form>
</div>

</body>
</html>
