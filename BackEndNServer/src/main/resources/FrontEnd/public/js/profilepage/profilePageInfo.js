async function getSessionUser() {
    try {
        const response = await fetch("http://localhost:8080/currentsessionuser", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
    } catch (error) {
        console.log(error);
    }

    return response;
}

async function getUserInfo(username) {
    try {
        const response = await fetch("http://localhost:8080/" + username, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
    } catch (error) {
        console.log(error);
    }

    return response;
}

getUserInfo()

function displayProfileInfo(){
    let username;
    let userinfo =  getSessionUser().then(getUserInfo(username))


}
