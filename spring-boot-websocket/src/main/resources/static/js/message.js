var websocket = new SockJS("/sockjs/webSocketServer");
var count = 1;
websocket.onopen = function (evnt) {
    window.console.log(evnt);
};
websocket.onmessage = function (evnt) {
    window.console.log(evnt.data);
    $("#message").append('<p>' + evnt.data + '</p>');
    if (count > 3) {
        websocket.close();
    } else {
        window.console.log(count);
        count = count + 1;
    }
};
websocket.onerror = function (evnt) {
    window.console.log(evnt);
};
websocket.onclose = function (evnt) {
    window.console.log(evnt);
}