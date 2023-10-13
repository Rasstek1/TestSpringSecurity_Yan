<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Error Page</title>
</head>
<body>
<h1> Page d’erreur</h1>
<h2>Une erreur s’est produite dans l’application. Veuillez contacter le support technique. </h2>
<p>URL source : ${pageContext.errorData.requestURI}
<p>Le message d'erreur :${message}</p>
<p>Le contrôleur source:${trace}</p>

<c:url var="accueilURL" value="/home/accueil"/>
<hr/> <a href="${accueilURL}"> allez à l'accueil</a>

</body>
</html>
