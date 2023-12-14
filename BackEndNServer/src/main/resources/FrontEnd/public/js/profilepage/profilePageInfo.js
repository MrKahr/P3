/**
 * Get a username to query in database
 * @returns username to query 
 */
async function getQuiriedUserName() {
    let currentLocation = window.location.href.split("/");

    // Get last element as username 
    let username = currentLocation.pop();

    return username;
}

/**
 * gets the relevant info on a user to display on front end 
 * @param username - username to query in database 
 * @returns - sanitized user object
 */
async function getUserInfo(username) {
    try {
        // fetch call on path to usercontroller
        const response = await fetch(`/api/${username}`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });

        // get text from response and parse information in json object
        let profileInfo = JSON.parse(await response.text());

        return profileInfo;

    } catch (error) {
        console.log(error);
    }


}

/**
 * Input user related info into correct boxes 
 */
async function displayProfileInfo() {
    // Get user name and use that to get user info
    let username = await getQuiriedUserName();
    let userinfo = await getUserInfo(username);

    // Get appropriate element and put user information in text field of element 
    console.log(userinfo);
    let profileContainer = document.querySelector(".profile-container");

    try {
        // Assign information to relevant children
        profileContainer.children[0].innerText = userinfo.basicUserInfo.userName;
        profileContainer.children[2].innerText = userinfo.guestInfo.characterInfo;
        profileContainer.children[3].innerText = userinfo.dmInfo.hostedSessions;
    } catch (error) {
        // Every user has guest info and basic user info
        profileContainer.children[3].innerText = userinfo.dmInfo.hostedSessions;
    }

}

displayProfileInfo();
