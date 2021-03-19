<!DOCTYPE html>
<html lang="de">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <title>Night-Chat</title>

    <link rel="stylesheet" href="assets/styles/index.css">
    <link rel="stylesheet" href="assets/styles/chat.css">
    <script type="text/javascript" src="ws.js"></script>
</head>
<body class="chat">
<div class="container">
    <div class="header vcenter">CatsHostel - Night chat</div>
    <div class="mainbody">
        <!-- The message container that will hold all the events, messages from the server and other users -->
        <div class="messages" id="messages"></div>

        <!-- The form with the text input and the button to send messages -->
        <div class="form">
            <label for="commandInput">Enter message or command (starting from slash /):</label>
            <input type="button" id="sendButton" value="send">
            <div class="input-container">
                <input type="text" id="commandInput">
            </div>
        </div>
    </div>
    <div class="panel right content">Right panel</div>
    <div class="footer vcenter">Footer</div>
</div>
</body>
</html>
