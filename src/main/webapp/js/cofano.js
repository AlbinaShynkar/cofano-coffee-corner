let OVERLAY, SINCELOAD, DND;
const W = 100;

const eventColourMap = {
    "Unspecified": "--gray-dark",
    "Break": "--c-colour-orange",
    "Meeting": "--c-colour-red",
    "Stand-up Meeting": "--purple",
    "Lunch": "--yellow",
    "Corporate Event": "--green"
}


$(document).ready(function () {

    OVERLAY = $(".cofano-panel-overlay");
    SINCELOAD = moment();

    OVERLAY.hide();
    $(".cofano-login-wrapper").hide();
    $(".cofano-panel-wrapper").hide();

    $("body").addClass(localStorage.getItem("theme"));

});


/**
 *  RESPONSIVENESS OF BUTTONS AND FEATURES
 */

function renderAlertBox(body, severity) {
    $(".cofano-alert-wrapper").append(__alertBox(body, severity));
}

function activateNavButton(obj) {
    $(".cofano-nav-button").removeClass("button-orange active");
    $(obj).addClass("button-orange active");
}

function toggleNavBar() {
    $(".cofano-panel-nav-wrapper").toggleClass("active");
    $(".cofano-body-iconbar").toggleClass("active");
    $(".hamburger").toggleClass("is-active");
}

function updateCards() {
    $(".cofano-content-card-updated").text("updated " + SINCELOAD.fromNow());
}

function toggleIconExtention(id) {
    $(".cofano-popup").not("#cofano-iconbar-extension-" + id).hide();
    $("#cofano-iconbar-extension-" + id).toggle();
}

function updateNC() {

    $("#cofano-dashboard-card-1 .cofano-content-card-value").text(nwMsg);
    $("#cofano-iconbar-extension-item-1 .badge").text(nwMsg);
    $("#cofano-iconbar-extension-item-2 .badge").text(stMsg);
    $("#cofano-iconbar-extension-item-3 .badge").text(nwBlt);
    $("#cofano-iconbar-extension-item-4 .badge").text(nwEvt);

    $("#cofano-iconbar-extension-1 .cofano-iconbar-extension-item .badge").each(function () {
        let t = $(this);
        t.text() == 0 ? t.removeClass("badge-orange") : t.addClass("badge-orange");
    });

    let sum = nwMsg + stMsg + nwBlt + nwEvt;
    let iconBadge = $("#cofano-iconbar-icon-1 .cofano-iconbar-icon-badge");
    if (sum === 0) iconBadge.hide()
    else {
        iconBadge.show()
        if (sum > 99) iconBadge.text("99+");
        else iconBadge.text(sum);
    }

}

function scrollChatBox(doCheck) {

    let chatbox = $(".cofano-content-chatbox");
    let scrollHeight = chatbox.prop("scrollHeight");

    if (doCheck) {
        if (scrollHeight - chatbox.scrollTop() - chatbox.height() < 100)
            chatbox.scrollTop(scrollHeight);
    } else chatbox.scrollTop(scrollHeight);

}

function handleStatusUpdate(name) {

    localStorage.setItem("status", name)
    $("#cofano-iconbar-extension-3 select option[name=" + name + "]").attr("selected", "")

    let code;
    if (name === "online") code = 0;
    else if (name === "dnd") code = 1;

    $.ajax({
        url: "rest/user?id=" + GPROFILE.getId() + "&status=" + code,
        type: "PUT",
        success: () => {

            if (name === "online") {
                DND = false;
                $("#cofano-iconbar-extension-3 .profile-img").css("border-color", "var(--green)")
                $("#cofano-iconbar-icon-3 .cofano-iconbar-icon-badge").css("background-color", "var(--green)")
            } else if (name === "dnd") {
                DND = true;
                $("#cofano-iconbar-extension-3 .profile-img").css("border-color", "var(--c-colour-red)")
                $("#cofano-iconbar-icon-3 .cofano-iconbar-icon-badge").css("background-color", "var(--c-colour-red)")
            } else if (name === "something") {
                $("#cofano-iconbar-extension-3 .profile-img").css("border-color", "var(--c-colour-orange)")
                $("#cofano-iconbar-icon-3 .cofano-iconbar-icon-badge").css("background-color", "var(--c-colour-orange)")
            }

        }
    });

}

function scrollTimetable() {

    let qrts = 0;
    qrts += moment().format("HH") * 4;
    qrts += moment().format("mm") / 15;
    $(".cofano-content-timetable-indicator").css("left", W * qrts);
    $(".cofano-content-timetable").scrollLeft(W * (qrts - 1));

}

function renderEvent(obj) {

    let b = moment.utc(obj.start).local();
    let e = moment.utc(obj.end).local();
    const time = b.format("HH:mm") + " - " + e.format("HH:mm");
    const width = e.diff(b, "minutes") * W / 15;
    const left = b.diff(moment().startOf("day"), "minutes") * W / 15;
    const top = 47;

    let succeeded = true;
    let Bn = left;
    let En = left + width;
    let Be, Ee;

    function getRow(row) {
        return $("#cofano-event .cofano-timetable-event").filter(function () {
            return $(this).css("top") === 15 + row * top + "px";
        })
    }


    function testRow(currentrow) {
        if (currentrow.length !== 0) {

            currentrow.each(function () {
                Be = parseFloat($(this).css("left"));
                Ee = Be + parseFloat($(this).css("width"));

                if ((Bn < Be && En > Be) || (Bn >= Be && En <= Ee) || (Bn < Ee && En > Ee)) {
                    rownumber++;
                    testRow(getRow(rownumber));
                    succeeded = false;
                    return false;
                }

            });
            if (succeeded) $(".cofano-content-event-wrapper")
                .append(__eventTT(obj.id, obj.title, time, width, 15 + top * rownumber, left, eventColourMap[obj.type]));

        } else $(".cofano-content-event-wrapper")
            .append(__eventTT(obj.id, obj.title, time, width, 15 + top * rownumber, left, eventColourMap[obj.type]));
    }

    let rownumber = 0;
    testRow(getRow(rownumber));

}

function eventCheckCollapsed(obj) {

    if ($(obj).width() === $(obj).find(" .cofano-event-title").width() +
        $(obj).find(".cofano-event-time").width() +
        parseInt($(obj).find(".cofano-event-time").css("margin-left")) ||
        $(obj).width() === $(obj).find(".cofano-event-title").width()) {

        $(obj).addClass("collapsed");

    }

}

function addStarredMsg(id) {
    $("#cofano-chat .cofano-starredmsg-masonry")
        .append("<div class=\"card shadow-1-hover cofano-starredmsg-card\"></div>")
        .find(".cofano-starredmsg-card:empty")
        .append($("#cofano-message-" + id).clone());
}

function toggleChatView() {
    $("#cofano-chat .row:not(.cofano-content-header, .cofano-content-send-wrapper)").toggle();
}

function toggleEventView() {
    $("#cofano-event .row:not(.cofano-content-header)").toggle();
}

function viewVerEvent(id) {
    $.ajax("rest/events/participants?id=" + id).done((response) => {

        determineEventButtonVal(response, id);

        let event = $("#cofano-el-event-" + id).clone();
        event.removeAttr("onclick");
        event.find(".cofano-event-signedup > ul")
            .html(response.map(__eventSignedUp).join(''));
        event.find(".card-body > div").show();
        $(".cofano-content-eventlist-details").html(event);
        event.find(".cofano-eventlist-event-img").show();

        $.each(event.find(".cofano-event-details-attr-value"), (i, obj) => {
            if ($(obj).text() === GPROFILE.getName()) event.find("#delete").show();
        });

    });
}

function viewHorEvent(id) {

    $.ajax("rest/events/participants?id=" + id).done((response) => {

        let event = $("#cofano-el-event-" + id).clone();
        $.each(event.find(".cofano-event-details-attr-value"), (i, obj) => {
            if ($(obj).text() === GPROFILE.getName()) event.find("#delete").show();
        })

        $(".cofano-content-timetable-data-wrapper .cofano-event-details-metadata .cofano-event-details-button-wrapper").remove()
        $(".cofano-content-timetable-data-wrapper .cofano-event-details-metadata")
            .append(event.find(".cofano-event-details-button-wrapper").show());

        $(".cofano-content-timetable-data-wrapper .cofano-event-details-title")
            .text(event.find(".cofano-eventlist-event-title").text());

        $(".cofano-content-timetable-data-wrapper .cofano-event-details-time")
            .text(event.find(".cofano-eventlist-event-time").text());

        $(".cofano-content-timetable-data-wrapper .cofano-event-details-body")
            .text(event.find(".cofano-eventlist-event-body").text());

        $(".cofano-content-timetable-data-wrapper .cofano-event-details .cofano-event-details-attrs").remove();
        $(".cofano-content-timetable-data-wrapper .cofano-event-details")
            .append(event.find(".cofano-event-details-attrs"));

        $(".cofano-content-timetable-data-wrapper .cofano-event-signedup-wrapper")
            .html(event.find(".cofano-event-signedup").show());

        $(".cofano-content-timetable-data-wrapper .cofano-event-signedup > ul")
            .html(response.map(__eventSignedUp).join(''));
        determineEventButtonVal(response, id);

    });


}

function determineEventButtonVal(response, id) {

    if (response.length === 0) setSignUpButtonValue(id, "Sign Up", "check");
    else $.each(response, (i, obj) => {

        if (GPROFILE.getId() === obj.id) {
            setSignUpButtonValue(id, "Leave", "clear");
            return false;
        } else setSignUpButtonValue(id, "Sign Up", "check");

    });
}

function setSignUpButtonValue(id, text, icon) {
    $("#cofano-el-event-" + id + " #signup .button-text").text(text);
    $(".cofano-content-timetable-data-wrapper #signup .button-text").text(text);
    $("#cofano-el-event-" + id + " #signup .material-icons").text(icon);
    $(".cofano-content-timetable-data-wrapper #signup .material-icons").text(icon);
}

function signUP(id) {

    $.ajax({
        url: "rest/events/subscribe?userId=" + GPROFILE.getId() + "&eventId=" + id,
        type: "POST",
        async: false
    });

    $.ajax("rest/events/participants?id=" + id).done((response) => {
        $(".cofano-event-signedup > ul")
            .html(response.map(__eventSignedUp).join(''));
        determineEventButtonVal(response, id)
    });

}









/**
 *  CONTROL OF THE OVERLAY
 */

function overlayOpen() {
    OVERLAY.show();
}

function overlayClose() {
    OVERLAY.hide();
    OVERLAY.children().hide();
}

function bulletinOpenView(id) {
    let bulletin = $("#cofano-bulletin #cofano-bulletin-" + id).clone();
    let bin = bulletin.find(".card-title span");
    $("#cofano-overlay-bulletin-view .cofano-overlay-button-wrapper .button-red")
        .attr("onclick", bin.attr("onclick"));
    bin.remove();
    bulletin.removeAttr("onclick");
    $("#cofano-overlay-bulletin-view").prepend(bulletin);
    $("#cofano-overlay-bulletin-view").css("display", "block");
    overlayOpen();
}

function bulletinCloseView() {
    OVERLAY.find("#cofano-overlay-bulletin-view")
        .children().not(".cofano-overlay-button-wrapper").remove();
    overlayClose();
}

function bulletinAdd() {
    $("#cofano-overlay-bulletin-new").show();
    overlayOpen();
}

function eventAdd() {
    $("#cofano-overlay-event-new").show();
    overlayOpen();
}



