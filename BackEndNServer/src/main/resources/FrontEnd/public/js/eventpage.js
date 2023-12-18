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
async function loadEventInfo(){
    eventInfo = await getEventInfo();
    let eventTitleDiv = document.getElementById("eventTitleContent");
    eventTitleDiv.innerHTML = eventInfo.title;
    let eventTimeDiv = document.getElementById("eventTimeContent");
    eventTimeDiv.innerHTML = eventInfo.date;
    let eventPlayercountDiv = document.getElementById("eventPlayercountContent");
    eventPlayercountDiv.innerHTML = "Players: "+eventInfo.currentNumberOfPlayers+"/"+eventInfo.maxNumberOfPlayers+"";
    console.log(eventInfo);
}

document.addEventListener('DOMContentLoaded', () => { loadEventInfo()});
document.addEventListener('DOMContentLoaded', () => { console.log("loaded")});