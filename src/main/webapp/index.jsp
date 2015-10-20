<%--
  Created by IntelliJ IDEA.
  User: xiayingjie
  Date: 15/6/9
  Time: 下午1:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Testing websockets</title>
</head>
<body>
<div>
    <input type="text" name="nick" id="nick" />
    <input type="text" name="message" id="message" />
    <input type="button" value="Start" onclick="start()"/>
    <input type="button" value="send" onclick="send()"/>
</div>
<div id="messages"></div>
<script type="text/javascript">
    var webSocket;
    function start() {
        var name  =document.getElementById("nick").value;
        webSocket =
                new WebSocket('ws://localhost:8080/ws/'+name);
        webSocket.onerror = function (event) {
            onError(event)
        };

        webSocket.onopen = function (event) {
            onOpen(event)
        };

        webSocket.onmessage = function (event) {
            onMessage(event)
        };

    }
    function send(){
        var message  =document.getElementById("message").value;
        alert(message);
        webSocket.send(message);

    }


    function onMessage(event) {

        document.getElementById('messages').innerHTML
                += '<br />' + event.data;
    }

    function onOpen(event) {
        document.getElementById('messages').innerHTML
                = 'Connection established';
    }

    function onError(event) {
        alert(event.data);
    }


</script>
</body>
</html>
