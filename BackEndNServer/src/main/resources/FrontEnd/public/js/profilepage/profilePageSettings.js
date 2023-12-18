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

async function getGuestInfo(username) {
    try {
        const response = await fetch(`/api/${username}/getGuestInfo`, {
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

async function setGuestInfo(username, newInfo) {
    try {
        const response = await fetch(`/api/${username}/${newInfo}`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache"
        });
        // Return response once promise has been resolved 
        return await response.text();
    } catch (error) {
        console.log(error);
    }
}

async function getUserEvents() {

}

async function inputUserInfo(username) {
    let userinfo = await getUserInfo(username);

    // Find and place element in username 
    let nameContainer = document.querySelector(".username");
    nameContainer.innerText = username;

    // Find and place element in infobox 
    let guestInfoContainer = document.getElementById("profileText");
    guestInfoContainer.innerText = userinfo.guestInfo.characterInfo;

    // Find and place element future events
    //displayFutureSessions(username);

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

async function saveGuestInfo() {
    let username = await getCurrentUserName();
    try {
        // Get info in infobox
        let currentTextArea = document.getElementById("profileText");
        let guestInfo = {
            characterInfo: currentTextArea.value,
            "@class":".Guest"
        };
        // Jacksonserializer complains about missing id attribut @class - fix: https://stackoverflow.com/questions/31665620/is-jacksons-jsonsubtypes-still-necessary-for-polymorphic-deserialization
        const response = await fetch(`/api/${username}/saveGuestInfo`,
            {
                method: "PUT",
                mode: "cors",
                cache: "no-cache",
                headers: {
                    "Content-Type": "application/json" // Set type to JSON
                },
                body: JSON.stringify(guestInfo)
            })
            let currentInfoContainer = document.getElementsByClassName("Save Changes Message");
            currentInfoContainer[0].innerText = await response.text();
            currentInfoContainer[0].visibility = "visible";
    } catch (error) {
        console.log(error);
    }
}

async function saveMemberInfo() { };

async function changeActivationStatus() {
    let username = await getCurrentUserName();
    let currentButton = document.getElementById("activationButton");

    // Setup prompt to make sure user deactivates/activates account correctly
    let promptmsg = "You are about to" + currentButton.innerText + ". Your account can be restored in the next six months. \n Are you sure you want to " + currentButton.innerText + " [yes/no]";
    let activationPrompt = prompt(promptmsg);

    let reponseMessage;

    if (activationPrompt === "yes") {
        // Check whether button is currently an activation or deactivation button
        if (currentButton.innerText.split(" ")[0] === "Deactivate") {
            reponseMessage = await deactivateAccount(username);
        } else {
            reponseMessage = await activateAccount(username);
        }

        changeActivationButtonText(currentButton);

        // Display message to user 
        let statusMessage = document.getElementsByClassName("Activation Message");
        statusMessage[0].visibility = "visible";
        statusMessage[0].innerText = reponseMessage;
    } else {
        console.warn("Operation unsuccessful!");
    }
}

function changeActivationButtonText(activationButton) {
    if (activationButton.innerText === "Deactivate Account") {
        activationButton.innerText = "Activate Account";
    } else {
        activationButton.innerText = "Deactivate Account";
    }
}

async function deactivateAccount(username) {
    try {
        const response = await fetch(`/api/${username}/deactivate`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache"
        });
        // Return response once promise has been resolved 
        return await response.text();
    } catch (error) {
        console.log(error);
    }
}

async function activateAccount(username) {
    try {
        const response = await fetch(`/api/${username}/reactivate`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache"
        });
        // Return response once promise has been resolved 
        return await response.text();
    } catch (error) {
        console.log(error);
    }
}

async function getFutureSessions(username){
    try {
        const response = await fetch(`/api/${username}/futureEvents`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        // Return response once promise has been resolved 
        return response;
    } catch (error) {
        console.log(error);
    }
}

/* async function displayFutureSessions(username){
    let futureSessions = await getFutureSessions(username);
    let sessionContainer = document.getElementsByClassName("eventlist");

    futureSessions.forEach(session => {
        let newParagraph = document.createElement("p");
        newParagraph.innerText = session;
        sessionContainer.appendChild(newParagraph);
        
    });
}
 */
/**
 * This is dummy, because we don't just want to check two passwords, we also want to check whether the person remembers their own password
 * @returns 
 */
async function changePassword(){
    // TODO: implement hashing, and checking the old password with the password in the database
    let username = await getCurrentUserName();
    let oldPassword = document.getElementById("oldPassword");
    let newPassword = document.getElementById("newPassword");
    let confirmedPassword = document.getElementById("confirmPassword");
    let statusContainer = document.getElementById("passwordChangeMessage");

    if(!(newPassword.value === confirmedPassword.value)){
        statusContainer.innerText = "Passwords do not match";
    } else {
        statusContainer.innerText = "";
        try {
            const validationResponse = await fetch(`/api/${username}/validatePassword`, {
                method: "PUT",
                mode: "cors",
                cache: "no-cache",
                headers: {
                    "Content-Type": "application/json" // Set type to JSON
                },
                body: JSON.stringify(confirmedPassword)
            })

            if(validationResponse){
                try {
                    const savingReponse = await fetch(`/api/${username}/savePassword`, {
                        method: "PUT",
                        mode: "cors",
                        cache: "no-cache",
                        headers: {
                            "Content-Type": "application/json" // Set type to JSON
                        },
                        body: JSON.stringify(confirmedPassword)
                    })
                    //console.log(statusContainer);
                    statusContainer.innerText = savingReponse;
                } catch (error) {
                    console.log(error);
                }
            }
        } catch (error) {
            console.log(error);
        }
    }
    
}

async function makeMembershipRequest(){
    let memberfields = document.getElementsByClassName("info-column2");
    let validFields = true;
    let username = await getCurrentUserName();

    for (const field of memberfields) {
        if(field.children[0].value === ""){
            validFields = false;
        };
    }

    if(validFields){
        let memberInfo = {
            realName: memberfields[0].children[0].value,
            phoneNumber: memberfields[1].children[0].value,
            postalCode: memberfields[2].children[0].value,
            address: memberfields[3].children[0].value,
            email: memberfields[4].children[0].value,
            "@class":".Member"
        };
        //console.log(memberInfo);
        try {
            const response = await fetch(`/api/${username}/sendUpgradeToMemberRequest`, {
                method: "PUT",
                mode: "cors",
                cache: "no-cache",
                headers: {
                    "Content-Type": "application/json" // Set type to JSON
                },
                body: JSON.stringify(memberInfo)
            });
            // Return response once promise has been resolved 
            let statusContainer = document.getElementById("membershipRequestStatus");
            statusContainer.innerText = await response.text();
        } catch (error) {
            console.log(error);
        }
        
    }


}
async function generatePageContent() {
    let username = await getCurrentUserName();
    await inputUserInfo(username);
    document.getElementById("activationButton").addEventListener("click", changeActivationStatus);
    document.getElementById("guestInfoSave").addEventListener("click", saveGuestInfo);
    document.getElementById("changePasswordButton").addEventListener("click", changePassword);
    document.getElementById("requestMembershipButton").addEventListener("click", makeMembershipRequest);
}

generatePageContent();
