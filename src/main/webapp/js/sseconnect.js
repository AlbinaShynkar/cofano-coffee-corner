let nwMsg = 0;
let stMsg = 0;
let nwBlt = 0;
let nwEvt = 0;
updateNC();


function sseConnect() {

    let doNotifications = Notification.permission === "granted";

    if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function (permission) {
            doNotifications = permission === "granted";
        });
    }

	const user = {
        id : GUSER.getId(),
        email : GPROFILE.getEmail(),
        name : GPROFILE.getName(),
        iconUri : GPROFILE.getImageUrl(),
        statusCode : 0
    }

    let source = new EventSource("rest/broadcaster?" +
        "user=" + encodeURIComponent(JSON.stringify(user)) + "&" +
        "id_token=" + GUSER.getAuthResponse().id_token);

    source.onopen = function () {
        const status = localStorage.getItem("status");
        if (status === null) handleStatusUpdate("online");
        else handleStatusUpdate(status)
    }
        
    source.addEventListener("message",  function (event) {

        let message = $.parseJSON(event.data);

        if (CURRENTPAGE !== "chat") {

            nwMsg++;
            updateNC();

            if (doNotifications && !DND) {
                new Notification("New message in Company Chat", {
                    body: message.author.name + ": " + message.text,
                    icon: "img/cofano_smallnav_logo.png",
                    silent: nwMsg > 10
                }).onclick = function (e) {
                    e.preventDefault();
                    console.log("You clicked the notification!")
                    //loadChat();
                };
            }

        }

        $(".cofano-content-chatbox-messagelist").append(__chatMessage(message));
        $(".cofano-content-chatbox-date").html(moment.utc(message.time).local().format("DD MMM YYYY"));
        scrollChatBox(true);

    });

    source.addEventListener("bulletin", function (event) {

        let bulletin = $.parseJSON(event.data);

        if (CURRENTPAGE !== "bulletin") {

            nwBlt++;
            updateNC();

            if (doNotifications && !DND) {
                new Notification("New Bulletin: " + bulletin.title, {
                    body: bulletin.body,
                    image: bulletin.imageURI,
                    icon: "img/cofano_smallnav_logo.png",
                    silent: nwBlt > 10
                }).onclick = function (e) {
                    e.preventDefault();
                    console.log("You clicked the notification!")
                    //loadBulletin();
                };
            }

        }

        $("#cofano-bulletin .cofano-content-masonry")
            .prepend(__bulletin(bulletin));
        $("#cofano-dashboard-card-2 .cofano-content-card-value").text(bulletin.title);
        $("#cofano-dashboard-card-2 .cofano-content-card-description").text(bulletin.body);

    });

    source.addEventListener("events", function (event) {

        let eventIn = $.parseJSON(event.data);

        if (CURRENTPAGE !== "event") {

            nwEvt++;
            updateNC();

            if (doNotifications && !DND) {
                new Notification("New event by " + eventIn.author.name, {
                    body: eventIn.text,
                    icon: eventIn.imageUri,
                    silent: nwEvt > 10
                }).onclick = function (e) {
                    e.preventDefault();
                    console.log("You clicked the notification!")
                    //loadEvent();
                };
            }

        }

        $(".cofano-content-eventlist > ul").append(__eventEL(eventIn));
        $(".cofano-eventlist-event .card-body > div").hide();

        if (moment.utc(eventIn.start).local().isSame(moment(), "d")) renderEvent(eventIn);
        eventCheckCollapsed($("#cofano-tt-event-" + eventIn.id))

    });

    source.addEventListener("users", function(event) {

        let users = $.parseJSON(event.data);
        $("#cofano-iconbar-extension-2 .list-group")
            .html(users.map(__iconExtensionItemOU).join(''))

    });

    source.addEventListener("starred", function (event) {

        let starred = $.parseJSON(event.data);

        if (CURRENTPAGE !== "chat") {
            stMsg++;
            updateNC();
        }

        $("#cofano-message-" + starred.id).attr("starred", starred.starred);
        if (starred.starred) addStarredMsg(starred.id);
        else $(".cofano-starredmsg-card").has("#cofano-message-" + starred.id).remove();

    })

}