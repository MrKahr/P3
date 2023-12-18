window.addEventListener("load", () => {
    checkCurrentSession();
})

function displayLoggedIn(message){
    let username = message[1];
    let authorities = message[2];
    
    createLoggedInBar(username);

    if(document.getElementById("profileMenu")){
        showProfileMenu(username, authorities);
    }
}

/**
 * Updates links and removes HTML elements in the profile menu to match the user's authority level.
 * @param {string} username 
 * @param {array<string>} authorities Array of user's authorities. 
 */
function showProfileMenu(username, authorities){
    let admin = "admin";
    let dm = "dm";
    let hasAdminRights = false;
    let hasDmRights = false;

    authorities.forEach(role => {
        role = role.toLowerCase();

        if(role === admin){
            hasAdminRights = true;
        }
        else if(role === dm){
            hasDmRights = true;
        }
    })

    let adminLinks = document.getElementById(admin);
    // let dmLinks = document.getElementById(dm); // Add when DM menu has been added

    if(hasAdminRights){
        // You can do stuff here if user is Admin.
    }
    else{
        adminLinks.remove();
    }

    if(hasDmRights){
        // You can do stuff here if user if DM.
    }
    else{
        //dmLinks.remove();
    }

    let profileEventsLink = document.getElementById("profileEvents").firstElementChild;
    let profileSettingsLink = document.getElementById("profileSettings").firstElementChild;

    profileEventsLink.href = `/eventlistpage`; // TODO: Add event link after merging event pages.
    profileSettingsLink.href = `/profile/${username}/settings`;
}

/**
 * Removes all elements with the "split" class, i.e authentication related elements 
 * and replaces them with corresponding elements telling the user they're logged in. 
 * @param {string} username 
 */
function createLoggedInBar(username){
    let splitClass = "split";
    let logoClass = "logo";

    try {
        let authenticationElements = document.getElementsByClassName(splitClass);
        let elemArray = Array.from(authenticationElements);

        elemArray.forEach(element => {
            element.remove();
        });
    
        let logout = document.createElement("a");
        logout.href = "/logout";
        logout.classList.add(splitClass);
        logout.text = "Log Out";
    
        // Container div for a user's profile pic.
        let divProfile = document.createElement("div");
        divProfile.classList.add(splitClass);
    
        // Temp anchor for the text telling the user they're logged in.
        let anchorProfileText = document.createElement("a");
        anchorProfileText.text = `Logged in as: ${username}`;
        anchorProfileText.classList.add(splitClass);

        // Anchor with link to a user's profile
        let anchorProfile = document.createElement("a");
        anchorProfile.href = `/profile/${username}`;
        anchorProfile.classList.add(splitClass);
    
        // The user's profile pic.
        let profilePic = document.createElement("img");
        profilePic.classList.add(logoClass);
        profilePic.src = "/images/AalborgTableTop_Logo.png" // TODO: Placeholder pic. Should be user profile pic.
        
        anchorProfile.appendChild(profilePic)
        divProfile.appendChild(anchorProfile);
    
        let topNav = document.getElementsByClassName("topnav")[0];
        topNav.append(logout, anchorProfileText, divProfile);        
    } catch (error) {
        console.error(error);
    }
}

/**
 * Wrapper function to clean up the UI when user does not have authority to view elements.
 */
function cleanUpDisplay(){
    cleanUpLogindDisplay();
    cleanUpProfileMenuDisplay();
}

/**
 * Cleans up any hardcoded HTML elements with the "split" class, i.e. authentication related elements. 
 */
function cleanUpLogindDisplay(){
    let splitClass = "split";

    let authenticationElements = document.getElementsByClassName(splitClass);
    let elemArray = Array.from(authenticationElements);
    elemArray.forEach(element => {
        element.remove();
    });

    let signup = document.createElement("a");
    let login = document.createElement("a");

    signup.href = "/signup";
    signup.text = "Sign Up"
    signup.classList.add(splitClass)

    login.href = "/login";
    login.text = "Log In";
    login.classList.add(splitClass);

    let topNav = document.getElementsByClassName("topnav")[0];
    topNav.append(login, signup);
}

/**
 * Removes the profile menu from HTML.
 */
function cleanUpProfileMenuDisplay(){
    let profileMenu = document.getElementById("profileMenu");
    profileMenu.remove();
}

/**
 * Requests the server for the user's session to determine if they're logged in or not.
 */
async function checkCurrentSession(){
    let response;
    
    try {
        response = await fetch("/api/user?" + new URLSearchParams({
            getLoginSession: true
        }), {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            },
            mode: "cors",
        });

        let message = JSON.parse(await response.text());
        
        // The user has logged in
        if(message[0] === "true"){
            displayLoggedIn(message);
        }
        // The user has NOT logged in
        else if(message[0] === "false"){
            cleanUpDisplay();
        }

    } catch (error) {
        console.error(error);
    }
}