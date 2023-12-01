
window.addEventListener("load", () => {
    console.log("Loaded");
    setup();
})

function gatherData(){
    const userDetails = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value // TODO: enable password hashing using same as server (bcrypt): https://www.npmjs.com/package/bcryptjs
    };

    if(userDetails.username === ""){
        console.warn(`Empty username! Setting to null`);
        userDetails.username = null;
    }
    if(userDetails.password === ""){
        console.warn(`Empty Password! Setting to null`);
        userDetails.password = null;
    }

    console.log(userDetails);
    return userDetails;
}

async function sendData(userDetails){

    if(userDetails.username === null || userDetails.password === null){
        console.warn(`userDetails incorrect! Username: ${userDetails.username} | Password: ${userDetails.password}`);
        return;
    }


    try {
        const response = await fetch("/login", {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            credentials: "same-origin",
            mode: "cors",
            cache: "no-cache",
            body: JSON.stringify(userDetails)
        })
        //const result = await response.json();
        //console.log(`Success: ${result}`);
    } catch (error) {
        console.error(`Error sending data: ${error}`);
    }


}

function setup(){
    document.getElementById("submit").addEventListener("click", (event) => {
        sendData(gatherData());
        
    }, {once:true})
}


