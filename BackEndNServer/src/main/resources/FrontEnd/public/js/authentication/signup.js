let SIGNUP_AS_MEMBER = false;

window.addEventListener("load", () => {
    //console.log("Loaded"); // TODO: disable after testing
    setup();
})

/**
 * Extracts relevant user data from html elements and wraps it in an object.
 * @returns The wrapper object containing the user data. 
 */
function gatherData(){

    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let confirmPassword = document.getElementById("confirmPassword").value;
    let email = document.getElementById("email").value;
    let fullName = document.getElementById("fullName").value;
    let address = document.getElementById("address").value;
    let phone = document.getElementById("phone").value;
    let postalCode = document.getElementById("postalCode").value;
    let signUpAsMember = SIGNUP_AS_MEMBER;
    
    const userDetails = {
        username: username,
        password: password,
        confirmPassword: confirmPassword,
        email: email,
        fullName: fullName,
        address: address,
        phone: phone,
        postalCode: postalCode,
        signUpAsMember: signUpAsMember
    };

    console.log(userDetails);
    return userDetails;
}

/**
 * Sends the object to the server and parses the response.
 * @param {object} userDetails 
 */
async function sendData(userDetails){
    let response;

    // Temporary replacement for CSRF protection. Do NOT omit CSRF protection when deploying to production.
    const headers = new Headers({
        'Content-Type': 'application/json'
    })

    try {
        response = await fetch("/signup", {
            method: "POST",
            headers,
            credentials: "include",
            body: JSON.stringify(userDetails)
        })

        //let explosion = await response.json(); // This nifty piece of code will make everything explode. Used for testing.

        let status = response.status;
        let message = await response.text();

        switch (status) {
            case 200:
                if(response.redirected){
                    notifyUser("Signed up successfully!");
                    //sleep(1500);
                    window.location.href = response.url;
                } 
                else
                    notifyUser("Server is not redirecting properly", status, "warn");           
                break;

            case 400:
                notifyUser(message);
                break;

            default:
                notifyUser(message, status);
                break;
        }
    } catch (error) {
        let errorMessage = `Exception ocurred while logging in: " ${error} "`;
        notifyUser(errorMessage, "", "error");
    }
}

/**
 * Notifies the user of all errors that might occur during login.
 * Both in the html and the console.
 * This function uses default values for httpstatus and logSeverity.
 * @param {string} message 
 * @param {int} httpStatus 
 * @param {string} logSeverity 
 */
function notifyUser(message, httpStatus = "", logSeverity = "log"){
    let loginFieldsContainer = document.getElementById("SignupContainer");

    // Set custom message if httpstatus is defined
    if(httpStatus !== ""){
        message = `Received HTTP response: ${httpStatus} with message: ${message}`;
    }

    let messageDiv = document.createElement("div");
    let messageNode = document.createTextNode(message);

    messageDiv.id = "messageDiv";

    messageDiv.appendChild(messageNode);
    loginFieldsContainer.appendChild(messageDiv);

    // Console logging
    if(logSeverity === "error")
        console.error(message);
    else if(logSeverity === "log")
        console.log(message);
    else
        console.warn(message);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

function setup(){
    document.getElementById("submit").addEventListener("click", (event) => {
        
        // TODO: Validate input before sending
        sendData(gatherData());
    })

    document.getElementById('signUpMember').addEventListener('click', (event) => {
        SIGNUP_AS_MEMBER ? SIGNUP_AS_MEMBER = false : SIGNUP_AS_MEMBER = true;
    })
}