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

async function joinEvent() {
    console.log("1");
    eventID = await getQueriedEventID();
    console.log("2");
    let currentUser = await getCurrentUser();
    console.log("3"+currentUser);
    try {
        console.log("4");
        // fetch call on path to usercontroller
        await fetch("/api/playsession/play_session/assign?username="+currentUser+"&playSessionID="+eventID+"", {
            method: "POST",
            mode: "cors",
            cache: "no-cache"
        });
        loadEventInfo();
    } catch (error) {
        console.log(error);
    }
}

async function loadEventInfo(){
    eventInfo = await getEventInfo();
    let eventTitleDiv = document.getElementById("eventTitleContent");
    eventTitleDiv.innerHTML = eventInfo.title;
    let eventTimeDiv = document.getElementById("eventTimeContent");
    eventTimeDiv.innerHTML = eventInfo.date;
    let eventPlayersDiv = document.getElementById("eventPlayersContent");
    eventPlayersDiv.innerHTML = eventInfo.users;
    let eventPlayercountDiv = document.getElementById("eventPlayercountContent");
    eventPlayercountDiv.innerHTML = "Players: "+eventInfo.currentNumberOfPlayers+"/"+eventInfo.maxNumberOfPlayers+"";
    let eventDMDiv = document.getElementById("eventDMContent");
    eventDMDiv.innerHTML = eventInfo.dm;
    let eventLocationDiv = document.getElementById("eventLocationContent");
    eventLocationDiv.innerHTML = eventInfo.location;
    let eventDescriptionDiv = document.getElementById("eventDescriptionContent");
    eventDescriptionDiv.innerHTML = eventInfo.description;
    console.log(eventInfo);
}

const joinEventButton = document.getElementById("joinEventButton");
joinEventButton.addEventListener('click', () => {joinEvent()})

document.addEventListener('DOMContentLoaded', () => { loadEventInfo()});
document.addEventListener('DOMContentLoaded', () => { console.log("loaded")});