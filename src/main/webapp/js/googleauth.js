let AUTH, GUSER, GPROFILE;

function authInit() {

    gapi.load('auth2', () => {

        gapi.auth2.init({
            client_id: "360617665982-cvqbqgvnd2tseqd4mccbejm3hh4blt7r.apps.googleusercontent.com",
            ux_mode: "redirect"
        }).then(() => {
            if (!gapi.auth2.getAuthInstance().isSignedIn.get()) displayLoginPage();
        });

        AUTH = gapi.auth2.getAuthInstance();

    });

    gapi.signin2.render("my-signin2", {
        height: 50,
        longtitle: true,
        theme: "dark",
        onsuccess: onSignIn
    });

}

async function onSignIn() {

    GUSER = AUTH.currentUser.get();
    GPROFILE = GUSER.getBasicProfile();

    $.ajaxSetup({
        beforeSend: (xhr) => {
            xhr.setRequestHeader("Authorization", "Bearer " + GUSER.getAuthResponse().id_token);
        }
    });



    await loadDefault();
    await loadDashboard();
    $(".cofano-login-wrapper").hide();
    $(".cofano-loading").fadeOut(500, () =>
        $(".cofano-panel-wrapper").fadeIn(500, () =>
            $(".cofano-panel").animate({marginTop: '0', opacity: '1'}, 1000)
        )
    );
    sseConnect();
    loadPrevs();

}

function displayLoginPage() {

    $(".cofano-panel-wrapper").hide();
    $(".cofano-loading").hide();
    $(".cofano-login-wrapper").fadeIn(500);

}
