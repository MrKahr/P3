
window.addEventListener("load", () => {
    //console.log("Loaded"); // TODO: disable after testing
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

    // Attempt on implementing CSRF protection. MUST be done before deploying to production.
    // const csrfName = "X-XSRF-TOKEN";
    // const csrfToken = getCookie(csrfName);

    // const headers = new Headers({
    //     'Content-Type': 'application/json',
    //     'XSRF-TOKEN': csrfToken,
    // })

    //console.log(headers);

    // Temporary replacement for CSRF protection. Do NOT omit CSRF protection when deploying to production.
    const headers = new Headers({
        'Content-Type': 'application/json'
    })

    try {
        response = await fetch("/login", {
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
                    notifyUser("Logged in successfully!");
                    //sleep(1000);
                    //window.location.href = response.url;
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

/**
 * Get a cookie's token by name.
 * 
 * Courtesy of https://www.w3schools.com/js/js_cookies.asp
 * @param {string} name The name of the cookie.
 * @returns The token of the specified cookie or "" if none found.
 */
function getCookie(name) {
    let tempName = name + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let cookieArray = decodedCookie.split(';');
    for(let i = 0; i <cookieArray.length; i++) {
      let cookie = cookieArray[i];
      while (cookie.charAt(0) == ' ') {
        cookie = cookie.substring(1);
      }
      if (cookie.indexOf(tempName) == 0) {
        return cookie.substring(tempName.length, cookie.length);
      }
    }
    return "";
  }
