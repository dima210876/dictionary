<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title></title>
    </head>
        <body bgcolor="lightskyblue">
        <Font color="#e9967" size="12">
            Русско-английский (англо-русский) словарь
        </Font>
        <br>
        <br>
        <form name="frm" method="Get" action="MyServlet">
            <Font color="blue" size="6"> Введите слово: </Font><input type="Text" name="txt"/>
            <br>
            <br>
            <Font color="blue" size="6">Перевод: ${word} ${translation}</Font><br>
            <h4><Input type="submit" value="Перевести"/>
            </h4>
        </form>
    </body>
</html>