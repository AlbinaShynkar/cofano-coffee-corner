const incr = 50;
let cmin = 0, bmin = 0, emin = 0;
let cmax = incr, bmax = incr, emax = incr;

let CURRENTPAGE;
let STATUS;

$(document).ajaxError(function(event, jqXHR, settings, thrownError) {
    switch (jqXHR.status) {
        case 403:
            renderAlertBox("You can't access that! 🤬 (403)", 1); break;
        case 404:
            renderAlertBox("Nothing there 🔍 (404)", 0); break;
        case 500:
            renderAlertBox("Something went horribly wrong 😭 (500)", 1); break;
        case 515:
            const error = $.parseJSON(jqXHR.responseText);
            renderAlertBox(error.message, error.severity);
            break;
        default:
            renderAlertBox("Something went wrong 😕", 0);
    }
});

$(document).ready(function () {

    $("#cofano-chat .cofano-content-send-wrapper form").submit(function (e) {

        e.preventDefault();

        let message = {
            text: $(".cofano-content-send-wrapper input[type=text]").val()
        };

        $.ajax({
            type: "POST",
            url: "rest/chat?author=" + GPROFILE.getId(),
            data: JSON.stringify(message),
            contentType: "application/json"
        });

        scrollChatBox(false);
        $(this).trigger("reset");

    });

    $("#cofano-overlay-bulletin-new form").submit(function (e) {

        e.preventDefault();

        let bulletin = {
            title: $("#cofano-input-bulletin-tl").val(),
            body: $("#cofano-input-bulletin-dc").val(),
            imageUri: $("#cofano-input-bulletin-img").val()
        };

        $.ajax({
            type: "POST",
            url: "rest/bulletins?author=" + GPROFILE.getId(),
            data: JSON.stringify(bulletin),
            contentType: "application/json"
        });

        overlayClose();
        $(this).trigger("reset");

    });

    $("#cofano-overlay-event-new form").submit(function (e) {

        e.preventDefault();

        let event = {
            title: $("#cofano-input-event-tl").val(),
            body: $("#cofano-input-event-ds").val(),
            start: moment(
                    $("#cofano-input-event-sd").val() + " "
                  + $("#cofano-input-event-st").val(), "YYYY-MM-DD HH:mm")
                  .format("x"),
            end: moment(
                    $("#cofano-input-event-ed").val() + " "
                  + $("#cofano-input-event-et").val(), "YYYY-MM-DD HH:mm")
                  .format("x"),
            type: $("#cofano-input-event-ty").val(),
            imageUri: $("#cofano-input-event-img").val()
        };

        $.ajax({
            type: "POST",
            url: "rest/events?author=" + GPROFILE.getId(),
            data: JSON.stringify(event),
            contentType: "application/json"
        });

        overlayClose();
        $(this).trigger("reset");

    });

});





async function loadDefault() {

    $(".cofano-popup").hide();

    await $.ajax("rest/page/default").done((response) => {

        $(".cofano-nav-list")
            .html(response.buttons.map(__headerNavButton).join(''));
        $(".cofano-nav-button-wrapper:first .cofano-nav-button")
            .addClass("active");

        $(".cofano-body-iconbar")
            .append(response.icons.map(__iconbarIcon).join(''));

        $("#cofano-iconbar-extension-1 .list-group")
            .append(response.icons[0].extensions.map(__iconExtensionItem).join(""));

        $("#cofano-iconbar-extension-3 .list-group")
            .append(response.icons[2].extensions.map(__iconExtensionItem).join(""));
        $("#cofano-iconbar-extension-3 .profile-img")
            .attr("src", GPROFILE.getImageUrl());
        $("#cofano-iconbar-extension-3 .cofano-iconbar-extension-userdata-name")
            .text(GPROFILE.getName());
        $("#cofano-iconbar-extension-3 .cofano-iconbar-extension-userdata-email")
            .text(GPROFILE.getEmail());


        updateNC();
        $(".cofano-iconbar-extension").hide();

        $("#cofano-iconbar-extension-3 #theme-light").click(function () {
            $("body").removeClass("dark").addClass("light");
            localStorage.setItem("theme", "light");
        });
        $("#cofano-iconbar-extension-3 #theme-dark").click(function () {
            $("body").removeClass("light").addClass("dark");
            localStorage.setItem("theme", "dark");
        });
        $("#cofano-iconbar-extension-3 select").change(function () {
            STATUS = $("#cofano-iconbar-extension-3 select option:selected").text();
            handleStatusUpdate($(this).children(":selected").attr("name"))
        });

    });

}

function loadPrevs() {

    // LOAD PREV CHAT MSGS
    $.ajax("rest/chat/messages?min=" + cmin + "&max=" + cmax).done((response) => {

        $("#cofano-chat .cofano-content-chatbox-load-wrapper")
            .after(response.map(__chatMessage).join(''));
        $(".cofano-content-chatbox-date")
            .html(moment.utc(response[response.length - 1].time).local().format("DD MMM YYYY"));
        $.each(response, (i, obj) => {
            if (obj.starred) addStarredMsg(obj.id);
        });

    });

    // LOAD PREV BLTS
    $.ajax("rest/bulletins?min=" + bmin + "&max=" + bmax).done((response) => {
        $("#cofano-bulletin .cofano-content-masonry")
            .append(response.map(__bulletin).join(''));
    });

    // LOAD PREV EVTS
    $.ajax("rest/events/daily").done((response) => {
        $.each(response, (i, obj) => renderEvent(obj));
    });

    $.ajax("rest/events?min=" + emin + "&max=" + emax).done((response) => {
        $.each(response, (i, obj) => {
            $(".cofano-content-eventlist > ul").append(__eventEL(obj));
            $(".cofano-eventlist-event .card-body > div").hide();
        });
    });

}

function loadCards() {

    $("#cofano-dashboard-card-1 .cofano-content-card-value").text(nwMsg);

    $.ajax("rest/events/next").done((response) => {

        if (response !== null) {
            $("#cofano-dashboard-card-3 .cofano-content-card-value").html(response.title);
        } else {
            $("#cofano-dashboard-card-3 .cofano-content-card-value").text("Nothing");
            $("#cofano-dashboard-card-3 .cofano-content-card-description").text("No events found");
        }

    });

    $.ajax("rest/bulletins?min=0&max=1").done((response) => {

        if (response.length > 0) {
            $("#cofano-dashboard-card-2 .cofano-content-card-value").html(response[0].title);
            $("#cofano-dashboard-card-2 .cofano-content-card-description").html(response[0].body);
        } else {
            $("#cofano-dashboard-card-2 .cofano-content-card-value").text("Nothing");
            $("#cofano-dashboard-card-2 .cofano-content-card-description").text("No bulletins found");
        }

    });

}

async function loadDashboard() {

    activateNavButton($("#cofano-nav-button-1"));
    $(".cofano-popup").hide();
    CURRENTPAGE = "dashboard";

    await $.ajax("rest/page/dashboard").done((response) => {

        document.title = response.title;
        $(".cofano-panel-wrapper")
            .css("background-image", "var(--c-img-wash), url(" + response.backgroundUri + ")");
        $("#cofano-dashboard .cofano-content-header-text")
            .html(response.greeting + GPROFILE.getGivenName());
        $("#cofano-dashboard .cofano-content-header-subtext")
            .html(response.subGreeting);
        $("#cofano-dashboard .seperator")
            .html(response.separator);
        $("#cofano-dashboard .cofano-content-icon-wrapper")
            .html(__dashboardIcon(response.iconUri));
        $("#cofano-dashboard .cofano-content-cards")
            .html(response.cards.map(__dashboardCard).join(''));

        SINCELOAD = moment();
        updateCards();
        loadCards();
        setInterval(updateCards, 60000);

        $("#cofano-dashboard").siblings().not(".cofano-body-iconbar").hide();
        $("#cofano-dashboard").show();

    });


}

function loadChat() {

    activateNavButton($("#cofano-nav-button-2"));
    $(".cofano-popup").hide();
    CURRENTPAGE = "chat";
    cmin = 0;
    cmax = incr;

    $.ajax("rest/page/chat").done((response) => {

        document.title = response.title;
        $(".cofano-panel-wrapper")
            .css("background-image", "var(--c-img-wash), url(" + response.backgroundUri + ")");
        $("#cofano-chat .cofano-content-header-text")
            .html(response.header);
        $("#cofano-chat .cofano-content-send input[type=text]")
            .attr("placeholder", response.placeholder);
        $("#cofano-chat .cofano-content-send input[type=submit]")
            .attr("value", response.sendButtonValue);
        $("#cofano-chat .cofano-content-chatbox-load")
            .html(response.loadMoreValue);
        $("#cofano-chat .cofano-content-chatbox-load")
            .attr("onclick", response.loadMoreTarget);

        $("#cofano-chat").siblings().not(".cofano-body-iconbar").hide();
        $("#cofano-chat").show();
        $("#cofano-chat .cofano-content-starredmsg-wrapper").hide()
        $("#cofano-chat .cofano-content-chatbox-wrapper").show()

        nwMsg = 0;
        stMsg = 0;
        updateNC();
        scrollChatBox(false);

    });

}

function loadBulletin() {

    activateNavButton($("#cofano-nav-button-3"));
    $(".cofano-popup").hide();
    CURRENTPAGE = "bulletin";
    bmin = 0;
    bmax = incr;
    nwBlt = 0;
    updateNC();

    $.ajax("rest/page/bulletin").done((response) => {

        document.title = response.title;
        $("#cofano-bulletin .cofano-content-header-text")
            .html(response.header);
        $("#cofano-bulletin .cofano-panel-wrapper")
            .css("background-image", "var(--c-img-wash), url(" + response.backgroundUri + ")");
        $("#cofano-bulletin .cofano-content-masonry-add span")
            .html(response.addButtonUri);
        $("#cofano-bulletin .cofano-content-masonry-add")
            .attr("onclick", response.addButtonTarget);
        $("#cofano-bulletin .cofano-content-masonry-load")
            .html(response.loadMoreValue);
        $("#cofano-bulletin .cofano-content-masonry-load")
            .attr("onclick", response.loadMoreTarget);

        $("#cofano-bulletin").siblings().not(".cofano-body-iconbar").hide();
        $("#cofano-bulletin").show();

    });


}

function loadEvent() {

    activateNavButton($("#cofano-nav-button-4"));
    $(".cofano-popup").hide();
    CURRENTPAGE = "event";

    if ($("#cofano-event .cofano-timetable-segment").length === 0) {

        const header = $(".cofano-content-timetable-header");
        const body = $(".cofano-content-timetable-body");
        let h, q;
        for (let i = 0; i < 24*4; i++) {
            h = Math.floor(i/4);
            q = (i % 4) * 15;
            if (i > 0) header.append("<small class='cofano-timetable-segment-label' style='width: " + W + "px'>" + (h < 10 ? "0" + h : h) + ":" + (q === 0 ? "00" : q) + "</small>");
            body.append("<div class='cofano-timetable-segment' style='width: " + W + "px'></div>")
        }

        setInterval(scrollTimetable, 60000);

    }

    $.ajax("rest/page/event").done((response) => {

        document.title = response.title;
        $("#cofano-event .cofano-content-header-text")
            .html(response.header);
        $("#cofano-event .cofano-panel-wrapper")
            .css("background-image", "var(--c-img-wash), url(" + response.backgroundUri + ")");

        if ($(".cofano-content-event-wrapper").children().length > 0) {
            viewHorEvent($(".cofano-content-event-wrapper")
                .children(":first").attr("id").match(/\d+/)[0]);
        }

        nwEvt = 0;
        updateNC();

        $("#cofano-event").siblings().not(".cofano-body-iconbar").hide();
        $("#cofano-event").show();
        $("#cofano-event .cofano-content-eventlist-wrapper").hide();
        $("#cofano-event .cofano-content-timetable-wrapper").show();
        $("#cofano-event .cofano-content-timetable-data-wrapper").show();
        $("#cofano-event .cofano-timetable-event").each((i, obj) => eventCheckCollapsed(obj));
        scrollTimetable();

    });


}

function chatLoadMore() {

    cmin += incr;
    cmax += incr;

    $.ajax("rest/chat/messages?min=" + cmin + "&max=" + cmax).done((response) => {
        $("#cofano-chat .cofano-content-chatbox-load-wrapper")
            .after(response.map(__chatMessage).join(''));
    });

}

function starMessage(id) {

    $.ajax({
        url: "rest/chat?id=" + id,
        type: "PUT"
    });

}

function bulletinLoadMore() {

    bmin += incr;
    bmax += incr;

    $.ajax("rest/bulletins?min=" + bmin + "&max=" + bmax).done((response) => {
        $("#cofano-bulletin .cofano-content-masonry")
            .append(response.map(__bulletin).join(''));
    });

}

function bulletinDelete(id) {

    $.ajax({
        url: "rest/bulletins?id=" + id,
        type: "DELETE"
    }).done((response) => {
        $("#cofano-bulletin #cofano-bulletin-" + id).remove();
        bulletinCloseView();
    });

}

function deleteEvent(id) {

    $.ajax({
        url: "rest/events?id=" + id,
        type: "DELETE"
    }).done((response) => {
        $(" #cofano-tt-event-" + id).remove();
        $(" #cofano-el-event-" + id).remove();
    });

    if ($(".cofano-content-event-wrapper").children().length > 0) {
        viewHorEvent($(".cofano-content-event-wrapper")
            .children(":first").attr("id").match(/\d+/)[0]);
    } else {
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-details-title").empty();
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-details-time").empty();
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-details-body").empty();
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-signedup").remove();
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-details-button-wrapper").remove();
        $("#cofano-event .cofano-content-timetable-data-wrapper .cofano-event-details-attrs").remove();
    }

}