
window.addEventListener("load", () => {
    console.log("Loaded"); // TODO: disable after testing
    setup();
})

/**
 * Extracts relevant user data from html elements and wraps it in an object.
 * @returns The wrapper object containing the user data. 
 */
function gatherData(){
    const userDetails = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value // TODO: enable password hashing using same encoder as server (bcrypt): https://www.npmjs.com/package/bcryptjs
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
    
    try {
        response = await fetch("/login", {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            credentials: "same-origin",
            mode: "cors",
            cache: "no-cache",
            body: JSON.stringify(userDetails)
        })

        //let explosion = await response.json(); // TODO: This nifty piece of code will make everything explode. Used for testing.

        let status = response.status;
        let message = await response.text();

        switch (status) {
            case 200:
                if(response.redirected){
                    notifyUser("Logged in successfully!");
                    sleep(1000);
                    window.location.href = response.url;
                } 
                else
                    notifyUser("Server is not redirecting properly", status);           
                break;

            case 400:
                notifyUser(message);
                break;

            default:
                notifyUser(message, status);
                break;
        }
    } catch (error) {
        let errorMessage = `Unhandled exception while logging in: " ${error} "`;
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
function notifyUser(message, httpStatus = "", logSeverity = "warn"){
    let loginFieldsContainer = document.getElementById("LoginContainer");

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
        sendData(gatherData());
    }, {once:true})
}


