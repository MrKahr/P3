
window.addEventListener("load", () => {
    console.log("Loaded");
    setup();
})

function gatherData(){
    const userDetails = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value // TODO: enable password hashing using same as server (bcrypt): https://www.npmjs.com/package/bcryptjs
    };

    console.log(userDetails);
    return userDetails;
}

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

        if(response.redirected){
            window.location.href = response.url;    
        }


        // const result = response.url;
        // const redirect = response.url;
        // console.log(`Success: ${result} | redirect: ${redirect}`);
    } catch (error) {
        console.error(`Error sending data: ${error}`);
    }

    // try {
    //     if(response.redirected){
    //         window.location.href = response.url;    
    //     }
    //     // The server responded with an error (e.g. bad credentials)
    //     else{
    //         let json = await response.json();
    //         errorObject = JSON.parse(json);
    //         console.log(errorObject);
    //     }
    //     // Unknown error during processing.
    // } catch (error) {
    //     console.log(`Unknown error occured: ${error}`);
    // }




}

function setup(){
    document.getElementById("submit").addEventListener("click", (event) => {
        sendData(gatherData());
    }, {once:true})
}


