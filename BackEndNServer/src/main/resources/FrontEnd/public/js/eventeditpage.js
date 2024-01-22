async function getQueriedEventID() {
    let currentLocation = window.location.href.split("/");

    // Get last element as username 
    let eventID = currentLocation.pop();

    return eventID;
}

async function getEventInfo() {
    eventID = await getQueriedEventID();
    try {
        // fetch call on path to usercontroller
        const response = await fetch(`/api/playsession/play_session/${eventID}`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });

        // get text from response and parse information in json object
        let eventInfo = JSON.parse(await response.text());

        console.log(eventInfo);

        return eventInfo;

    } catch (error) {
        console.log(error);
    }
}

async function getCurrentUser() {
    try {
        // fetch call on path to usercontroller
        const response = await fetch(`/api/currentUser`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        const username = await response.text();
        console.log(username);
        return username;

    } catch (error) {
        console.log(error);
    }
}

async function loadEventInfo(){
    console.log("load");
    let eventInfo = await getEventInfo();
    let currentUser = await getCurrentUser();
    
    let eventTitle = document.getElementById("eventTitle");
    eventTitle.value = eventInfo.title;
    let eventLocation = document.getElementById("eventLocation");
    eventLocation.value = eventInfo.location;
    let eventDescription = document.getElementById("descriptionText");
    eventDescription.value = eventInfo.description;
    let eventDate = document.getElementById("eventDate");
    let eventTime = document.getElementById("eventTime");
    let eventDateSplit = eventInfo.date.split("T");
    eventTime.value = eventDateSplit.pop();
    eventDate.value = eventDateSplit.pop();

    
    // let eventPlayers = document.getElementById("eventPlayers");
    // eventPlayersDiv.innerHTML = eventInfo.users;
    // let eventPlayercountDiv = document.getElementById("eventPlayercountContent");
    // eventPlayercountDiv.innerHTML = "Players: "+eventInfo.currentNumberOfPlayers+"/"+eventInfo.maxNumberOfPlayers+"";
    // let eventDMDiv = document.getElementById("eventDMContent");
    // eventDMDiv.innerHTML = eventInfo.dm;
    
    console.log(eventInfo);
}

document.addEventListener('DOMContentLoaded', loadEventInfo);
document.addEventListener('DOMContentLoaded', () => { console.log("loaded")});
