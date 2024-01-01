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
    let joinEventButton = document.getElementById("joinEventButton");
    let eventInfo = await getEventInfo();
    let currentUser = await getCurrentUser();
    if (eventInfo.users.includes(currentUser)) {
        console.log("includes");
        joinEventButton.innerHTML = '<input type="submit" value="Leave Event"></input>';
        joinEventButton.removeEventListener('click',joinEvent);
        joinEventButton.addEventListener('click',leaveEvent);
    } else if(eventInfo.dm != currentUser){
        joinEventButton.innerHTML = '<input type="submit" value="Join Event"></input>';
        joinEventButton.removeEventListener('click',leaveEvent);
        joinEventButton.addEventListener('click',joinEvent);
        console.log("does not include");
    }
    
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

document.addEventListener('DOMContentLoaded', loadEventInfo);
document.addEventListener('DOMContentLoaded', () => { console.log("loaded")});