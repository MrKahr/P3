/**
 * Get username of user currently on page
 * @returns username to query 
 */
async function getCurrentUserName() {
    try {
        // Api gets the current user based on authentication object
        const response = await fetch("/api/currentUser", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });

        let username = await response.text();
        return username;

    } catch (error) {
        console.log(error);
    }
}

async function getUserInfo(username) {
    try {
        const response = await fetch(`/api/${username}`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        // Return response once promise has been resolved 
        return await JSON.parse(await response.text());
    } catch (error) {
        console.log(error);
    }
}

async function getUserEvents(){

}

async function inputUserInfo() {
    let username = await getCurrentUserName();
    let userinfo = await getUserInfo(username);

    // Find and place element in username 
    let nameContainer = document.querySelector(".username");
    nameContainer.innerText = username;

    // Find and place element in infobox 
    let guestInfoContainer = document.getElementById("profileText");
    guestInfoContainer.innerText = userinfo.guestInfo.characterInfo;

    // Find and place element future events

    // Find and place info if user is member - we don't care about strict equality also don't want an undefined user object
    if (userinfo.memberInfo != null) {
        inputMemberInfo(userinfo);
    }

}

function inputMemberInfo(userinfo) {
    let infoColumns = document.querySelectorAll(".info-column2");
    infoColumns.forEach(column => {

        let columnInput = column.querySelector("input");

        switch (columnInput.id) {
            case "fullName":
                columnInput.value = userinfo.memberInfo.realName;
                break;
            case "phoneNumber":
                columnInput.value = userinfo.memberInfo.phoneNumber;
                break;
            case "postalCode":
                columnInput.value = userinfo.memberInfo.postalCode;
                break;
            case "adress":
                columnInput.value = userinfo.memberInfo.address;
                break;
            case "email":
                columnInput.value = userinfo.memberInfo.email;
                break;
            default:
                break;
        }
    });
}

async function saveGuestInfo() { };

async function saveMemberInfo() { };

function requestMembership() { };

function changeActivationStatus(){
    let currentButtonStatus = document.getElementById("activationButton").value; 
    let promptmsg = "You are about to " + currentButtonStatus + "are you sure you want to do this?";
    let activationPrompt = prompt(promptmsg);
    currentButtonStatus = "HELLO";
    if(activationPrompt === "yes"){
        console.log(currentButtonStatus);
        if(currentButtonStatus === "Deactivate Account"){
            currentButtonStatus = "Activate Account";
        } else {
            currentButtonStatus = "Deactivate Account";
        }
    } else {
        console.warn("Operation unsuccessful!");
    }
}

function generatePageContent(){
    inputUserInfo();
    document.getElementById("activationButton").addEventListener("click", changeActivationStatus);
}

generatePageContent();
