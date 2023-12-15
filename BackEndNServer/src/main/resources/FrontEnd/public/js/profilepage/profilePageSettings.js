/**
 * Get username of user currently on page
 * @returns username to query 
 */
async function getCurrentUserName() {
    try {
        const response = await fetch("/currentuser", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });

        // get text from response and parse information in json object
        let username = response;

        return username;

    } catch (error) {
        console.log(error);
    }
}

getCurrentUserName();