<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login Page</title>
    <style>
        .error {
            padding: 15px;
            margin-bottom: 20px;
            color: red;
        }

        .msg {
            padding: 15px;
            margin-bottom: 20px;
            color: #31708f;
            background-color:#d9edf7;
        }

        #login-box {
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
<body onload='document.loginForm.username.focus();'>

<div id="login-box">


    <c:if test="${loginError.equals('true')}">
        <div class="error">
            Erreur des informations d'identification. Essayez encore.<br/>
            Cause : ${LoginErrorMessage}
        </div>
    </c:if>
    <h3>Connexion avec le nom d'usager et le mot de passe</h3>
    <form name='loginForm' action="<c:url value='/login/loginpost' />" method='POST'>

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
                <td colspan='2'>
                    <input name="submit" type="submit" value="submit" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </td>
            </tr>
        </table>
    </form>
</div>

</body>
</html>
