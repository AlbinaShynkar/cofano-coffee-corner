CREATE TABLE page (
    id character varying(32) NOT NULL PRIMARY KEY,
    dashboard character varying(255),
    chat character varying(255),
    bulletin character varying(255),
    event character varying(255),
    login character varying(255)
);

CREATE TABLE dashboard (
    attribute character varying(32) NOT NULL PRIMARY KEY,
    value character varying(255) NOT NULL
);

CREATE TABLE chat_page (
    attribute character varying(32) NOT NULL PRIMARY KEY,
    value character varying(255) NOT NULL
);

CREATE TABLE bulletin_page (
    attribute character varying(32) NOT NULL PRIMARY KEY,
    value character varying(255) NOT NULL
);

CREATE TABLE event_page (
    attribute character varying(32) NOT NULL PRIMARY KEY,
    value character varying(255) NOT NULL
);

CREATE TABLE login_page (
    attribute character varying(32) NOT NULL PRIMARY KEY,
    value text NOT NULL
);

CREATE TABLE nav_button (
    id smallint NOT NULL PRIMARY KEY,
    icon_uri varchar(255) NOT NULL,
    text varchar(32) NOT NULL,
    target_uri varchar(255) NOT NULL
);

CREATE TABLE icon (
    id smallint NOT NULL PRIMARY KEY,
    title varchar(32) NOT NULL,
    uri varchar(255) NOT NULL,
    header varchar(128),
    footer varchar(128)
);

CREATE TABLE icon_extension (
    id smallint NOT NULL,
    value varchar(255),
    body text,
    target varchar(64),
    icon_id smallint,
    PRIMARY KEY (id, icon_id),
    FOREIGN KEY (icon_id) REFERENCES icon(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE card (
    id smallint NOT NULL PRIMARY KEY,
    title varchar(64) NOT NULL,
    image_uri varchar(255) NOT NULL,
    description text NOT NULL,
    page_id varchar(16) NOT NULL
);

CREATE TABLE "user" (
    id varchar(64) NOT NULL PRIMARY KEY,
    email varchar(64) NOT NULL,
    name varchar(32) NOT NULL,
    icon_uri varchar(255) NOT NULL
);

CREATE TABLE event (
    id serial NOT NULL PRIMARY KEY,
    title varchar(128) NOT NULL,
    body text,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL,
    image_uri varchar(255),
    type varchar(16) NOT NULL,
    author_id varchar(64) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE bulletin (
    id serial NOT NULL PRIMARY KEY,
    title varchar(128),
    body text NOT NULL,
    image_uri varchar(255),
    author_id varchar(64),
    FOREIGN KEY (author_id) REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE message (
    id serial NOT NULL PRIMARY KEY,
    text text NOT NULL,
    time timestamp NOT NULL,
    starred boolean NOT NULL DEFAULT FALSE,
    author_id varchar(64) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE event_participant (
    user_id varchar(64) NOT NULL,
    event_id int NOT NULL,
    PRIMARY KEY (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES event(id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO page (id, dashboard, chat, bulletin, event, login) VALUES
('header',

    NULL,

    '<span class="material-icons" style="margin-right: 40px;" onclick="toggleChatView()">arrow_back_ios</span>
     Lets Chat
	 <span class="material-icons" style=" margin-left: 40px;" onclick="toggleChatView()">arrow_forward_ios</span>',

	'The Bulletin Board',

	'<span class="material-icons" style="margin-right: 40px;" onclick="toggleEventView()">arrow_back_ios</span>
     Upcoming Events
	 <span class="material-icons" style="margin-left: 40px;" onclick="toggleEventView()">arrow_forward_ios</span>',

	 'Welcome!'
 ),
('title',
    'Dashboard | Cofano Coffee Corner',
    'Company Chat | Cofano Coffee Corner',
    'Bulletin Board | Cofano Coffee Corner',
    'Upcoming Events | Cofano Coffee Corner',
    'Login | Cofano Coffee Corner'
),
('background_uri',
    'img/cofano_coffeecorner_bg_1.jpg',
    'img/cofano_coffeecorner_bg_1.jpg',
    'img/cofano_coffeecorner_bg_1.jpg',
    'img/cofano_coffeecorner_bg_1.jpg',
    'img/cofano_coffeecorner_login_bg_1.jpg'
);

INSERT INTO dashboard (attribute, value) VALUES
('separator', 'What''s new'),
('greeting', 'Welcome, '),
('sub_greeting', 'Let''s take a break...'),
('icon_uri', 'img/cofano_coffeecorner_dashboard_icon.svg');

INSERT INTO chat_page (attribute, value) VALUES
('placeholder', 'Type here to send a message...'),
('send_button_value', 'Send'),
('load_more_value', 'Load more'),
('load_more_target', 'chatLoadMore()');

INSERT INTO bulletin_page (attribute, value) VALUES
('add_button_uri', 'add'),
('add_button_target', 'bulletinAdd()'),
('load_more_value', 'Load more'),
('load_more_target', 'bulletinLoadMore()');

INSERT INTO event_page (attribute, value) VALUES
('add_button_uri', 'add'),
('add_button_target', 'eventAdd()'),
('load_more_value', 'Load more'),
('load_more_target', 'eventLoadMore()');

INSERT INTO login_page (attribute, value) VALUES
('logo_uri', 'img/cofano_coffeecorner_login_logo.png'),
('welcome_message', 'This is the Digital Coffee Corner for Cofano Software Solutions. Before the situation regarding COVID-19, the employees at Cofano would share their creative minds with each other, whilst enjoying a cup of coffee. That''s why, in times of Corona, this digital meeting ground has been set up.'),
('separator', 'Sign In');

INSERT INTO nav_button (id, icon_uri, text, target_uri) VALUES
(1, 'dashboard', 'Dashboard', 'loadDashboard()'),
(2, 'chat', 'Company Chat', 'loadChat()'),
(3, 'emoji_objects', 'Bulletin Board', 'loadBulletin()'),
(4, 'event', 'Upcoming Events', 'loadEvent()');

INSERT INTO icon (id, title, uri, header, footer) VALUES
(1, 'Notifications', 'notifications_active', 'Notifications', NULL),
(2, 'Other Users', 'people', 'Other Users', NULL),
(3, 'Settings', 'account_circle', 'Settings', '<button onclick="{ AUTH.disconnect(); location.reload(); }">Sign Out</button>');

INSERT INTO icon_extension (id, value, body, target, icon_id) VALUES 
(1, '<span class="material-icons">chat</span>Unread Messages', '<span class="badge badge-pill"></span>', 'loadChat()', '1'),
(2, '<span class="material-icons">star</span>Starred Messages', '<span class="badge badge-pill"></span>', NULL, '1'),
(3, '<span class="material-icons">emoji_objects</span>New Bulletins', '<span class="badge badge-pill"></span>', 'loadBulletin()', '1'),
(4, '<span class="material-icons">event</span>New Events', '<span class="badge badge-pill"></span>', NULL, '1'),
(1, '', '<img class="profile-img"/>
         <h5></h5>
         <small></small>', NULL, '3'),
(2, 'Status', '<select class="custom-select">
				   <option name="online" selected>Online</option>
				   <option name="dnd">Do not disturb</option>
				   <option name="something">Something else</option>
               </select>', NULL, '3'),
(3, 'Theme', '<a id="theme-light">Light</a>
              <a id="theme-dark">Dark</a>', NULL, '3');

INSERT INTO card (id, title, image_uri, description, page_id) VALUES
(1, 'New Chat Messages', 'img/cofano_coffeecorner_card_social.jpg', 'Unread messages', 'dashboard'),
(2, 'Latest Bulletin', 'img/cofano_coffeecorner_card_sunny.jpg', 'title here', 'dashboard'),
(3, 'Upcoming Break', 'img/cofano_coffeecorner_card_coffee.jpg', 'Today', 'dashboard');