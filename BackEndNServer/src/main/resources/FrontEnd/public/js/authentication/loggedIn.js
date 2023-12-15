window.addEventListener("load", () => {
    //console.log("Loaded"); // TODO: disable after testing
    checkCurrentSession();
})

function displayLoggedIn(username){
    let splitClass = "split";
    let logoClass = "logo";

    try {
        let authenticationElements = document.getElementsByClassName(splitClass);
        let elemArray = Array.from(authenticationElements);

        elemArray.forEach(element => {
            //console.log(element);
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
        
        //profilePic.src = "../images/AalborgTableTop_Logo.png" // TODO: Placeholder pic. Should be user profile pic.
        
        anchorProfile.appendChild(profilePic)
        divProfile.appendChild(anchorProfile);
    
        let topNav = document.getElementsByClassName("topnav")[0];
        topNav.append(logout, anchorProfileText, divProfile);        
    } catch (error) {
        console.error(error);
    }

}

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

        // Testing
        //console.log(message[0]);
        
        // The user has logged in
        if(message[0] === "true"){
            displayLoggedIn(message[1]);
        }
        // The user has NOT logged in
        else if(message[0] === "false"){
            // Possible to do stuff here
        }

    } catch (error) {
        console.error(error);
    }
}