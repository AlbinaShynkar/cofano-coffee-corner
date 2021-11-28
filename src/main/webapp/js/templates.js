const __alertBox = (body, severity) => `
    <div class="alert alert-${severity === 0 ? "warning" : "danger"} alert-dismissible fade show shadow-2-hover" role="alert">
        <strong>${severity === 0 ? "Hmm..." : "Oops!"}</strong> ${body}
        <button type="button" class="close" data-dismiss="alert">
            <span class="material-icons">close</span>
        </button>
    </div>
`;

const __headerNavButton = ({id, text, target, iconUri}) => `
    <li class="cofano-nav-button-wrapper">
        <button class="cofano-nav-button button" id="cofano-nav-button-${id}" onclick="${target}" title="${text}">
            <span class="material-icons cofano-nav-button-icon">${iconUri}</span>
            <span class="cofano-nav-button-text">${text}</span>
        </button>
    </li>
`;

const __iconbarIcon = (obj) => `
    <div class="cofano-iconbar-icon" id="cofano-iconbar-icon-${obj.id}" title="${obj.title}" onclick="toggleIconExtention(${obj.id})">
        <span class="material-icons">${obj.uri}</span>
        <div class="cofano-iconbar-icon-badge"></div>
    </div>
    <div class="cofano-iconbar-extension cofano-popup shadow-3 card" id="cofano-iconbar-extension-${obj.id}">
        ${obj.header == null ? "" : `
        <div class="card-header cofano-iconbar-extension-header">${obj.header}</div>
        `}
        <ul class="list-group list-group-flush"></ul>
        ${obj.footer == null ? "" : `
        <div class="card-footer cofano-iconbar-extension-footer">${obj.footer}</div>
        `}
    </div>
`;

const __iconExtensionItem = (obj) => `
    <li class="list-group-item cofano-iconbar-extension-item" id="${obj.id != undefined ? "cofano-iconbar-extension-item-" + obj.id : ""}" onclick="${obj.target}">
        <div>
            ${obj.value != undefined ? obj.value : ""}
        </div>
        <div>
            ${obj.body != undefined ? obj.body : ""}
        </div>
    </li>
`;

const __iconExtensionItemOU = (obj) => `
    <li class="list-group-item cofano-iconbar-extension-item" title="${obj.email}">
        <img class="profile-img" src="${obj.iconUri}">
        <div>
            ${obj.statusCode === 0 ? 
            "<small class=\"online\">Online</small>" : 
            "<small class=\"dnd\">Do not Disturb</small>"}
            <h6>${obj.name}</h6>
        </div>
    </li>
`;

const __dashboardCard = (obj) => `
    <div class="col-12 col-md-4 cofano-content-card-wrapper" id="cofano-dashboard-card-${obj.id}">
        <div class="card cofano-content-card">
            <div class="cofano-content-card-image-wrapper">
                <div class="cofano-content-card-image rounded shadow-1" style="background-image: linear-gradient(rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.3)), url('${obj.imageUri}')">
                    <h1 class="cofano-content-card-value">${obj.value}</h1>
                    <h6 class="cofano-content-card-description">${obj.description}</h6>
                </div>
            </div>
            <div class="cofano-content-card-data-wrapper shadow-2">
                <h4 class="cofano-content-card-title">${obj.title}</h4>
                <small class="cofano-content-card-updated"></small>
            </div>
        </div>
    </div>
`;

const __dashboardIcon = (iconURI) => `
    <img src="${iconURI}" class="cofano-content-icon"/>
`;

const __chatMessage = (obj) => `
    <li class="cofano-content-chatbox-message" 
        id="cofano-message-${obj.id}"
        starred="${obj.starred}">
        <div class="cofano-content-message-icon">
            <img class="profile-img" src="${obj.author.iconUri}">
        </div>
        <div class="cofano-content-message-text">
            <div class="cofano-content-message-title">
                <h6 class="cofano-content-message-sender">${obj.author.name}</h6>
                <div>
                    <span class="material-icons" 
                        onclick="starMessage(${obj.id})">star</span>
                    <small class="cofano-content-message-time">${moment.utc(obj.time).local().format("HH:mm")}</small>
                </div>
            </div>
            <div class="cofano-content-message-body">${obj.text}</div>
        </div>
    </li>
`;

const __bulletin = (obj) => `
    <div class="card shadow-1-hover" 
        id="cofano-bulletin-${obj.id}" 
        onclick="bulletinOpenView(${obj.id})">
        <img class="card-img-top" src="${obj.imageUri}">
        <div class="card-body">
            <h5 class="card-title">
                ${obj.title}
                <span class="material-icons" onclick="bulletinDelete(${obj.id})">delete</span>
            </h5>
            <p class="card-text">${obj.body}</p>
        </div>
    </div>
`;

const __eventTT = (id, title, time, width, top, left, color) => `
    <div class="cofano-timetable-event shadow-1-hover" 
         id="cofano-tt-event-${id}" onclick="viewHorEvent(${id})" 
         style="width: ${width + "px"}; left: ${left + "px"};  top: ${top + "px"}; background-color: var(${color});">
        <span class="cofano-event-title" style="${width < 200 ? "min-width: 100%" : ""}">${title}</span>
        <small class="cofano-event-time">${time}</small>
    </div>
`;

const __eventEL = (obj) => `
    <li class="list-group-item card cofano-eventlist-event shadow-1-hover" id="cofano-el-event-${obj.id}" onclick="viewVerEvent(${obj.id})">
        <img class="card-img-top cofano-eventlist-event-img" src="${obj.imageUri}">
        <div class="card-header cofano-eventlist-event-header" style="background-color: var(${eventColourMap[obj.type]});">
            ${moment.utc(obj.start).local().format("DD MMM YYYY HH:mm") + " - " + 
              moment.utc(obj.end).local().format("DD MMM YYYY HH:mm")}
        </div>
        <div class="card-body">
            <h5 class="card-title cofano-eventlist-event-title">${obj.title}</h5>
            <p class="description cofano-eventlist-event-body">${obj.body}</p>
            <p class="cofano-eventlist-event-time" style="display: none">${moment.utc(obj.start).local().format("HH:mm") + " - " +
                moment.utc(obj.end).local().format("HH:mm")}</p>
            <div class="seperator">Details</div>
            <div class="cofano-event-details">
                <div class="cofano-event-details-attrs">
                    <ul>
                        <li class="cofano-event-details-attr">
                            <span class="cofano-event-details-attr-title">Type:</span>
                            <span class="cofano-event-details-attr-value">${obj.type}</span>
                        </li>
                        <li class="cofano-event-details-attr">
                            <span class="cofano-event-details-attr-title">Colour:</span>
                            <span class="cofano-event-details-attr-value shadow-2" style="background-color: var(${eventColourMap[obj.type]});"></span>
                        </li>
                        <li class="cofano-event-details-attr">
                            <span class="cofano-event-details-attr-title">Author:</span>
                            <span class="cofano-event-details-attr-value">${obj.author.name}</span>
                        </li>
                        <li class="cofano-event-details-attr">
                            <span class="cofano-event-details-attr-title">Created:</span>
                            <span class="cofano-event-details-attr-value">No clue</span>
                        </li>
                    </ul>
                </div>
                <div class="cofano-event-signedup">
                    <ul class="list-group list-group-flush"> </ul>
                </div>
            </div>
            <div class="cofano-event-details-button-wrapper">
                <button id="signup" class="button shadow-1-hover button-teal cofano-event-details-button" onclick="signUP(${obj.id})">
                    <span class="button-text" style="color: white">Sign Up</span>
                    <span class="material-icons" style="color: white; display: none;">check</span>
                </button>
                <button id="delete" class="button shadow-1-hover button-red cofano-event-details-button" style="display: none" onclick="deleteEvent(${obj.id})">
                    <span class="button-text" style="color: white">Delete</span>
                    <span class="material-icons" style="color: white; display: none;">delete</span>
                </button>
            </div>
        </div>
    </li>
`;

const __eventSignedUp = (obj) => `
    <li class="list-group-item cofano-event-signedup-item">
        <img class="profile-img" src="${obj.iconUri}">
        <div>
            <h6 class="cofano-event-signedup-item-name">${obj.name}</h6>
            <small class="cofano-event-signedup-item-email">${obj.email}</small>
        </div>
    </li>
`;
