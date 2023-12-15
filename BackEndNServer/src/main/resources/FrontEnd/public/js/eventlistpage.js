async function getEvents() {
    try {//http://localhost:8080/DateBetween?startDateTime=2023-12-01T15:06:27.299631&endDateTime=2023-12-31T15:06:27.299631
        const response = await fetch("http://localhost:8080/getAllPlaySessions", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        const calendar = await response.text();
        return calendar;
    } catch (error) {
        console.error("Error:", error);
    }
}

const stateEnum = {
    PLANNED: 'Planned',
    CANCELLED: 'Cancelled',
    CONCLUDED: 'Concluded',
    REWARDSRELEASED: 'Rewards Released',
};

async function addEventsToTable() {
    events = [];
    events = await JSON.parse(await getEvents());
    console.log(events);
    const table = document.getElementById("eventTable");

    table.addEventListener("click", function() {
        if(table.contains(event.target)){
            let tablerows = document.querySelectorAll('tr');
            Array.from(tablerows).forEach((tablerow, index) =>{
                if (tablerow.rowIndex != 0) {
                    tablerow.addEventListener('click', () => {
                        let clickedRow = event.currentTarget;
                        let editEventsButton = document.getElementById("editEventsButton");
                        let positionclickedRow = clickedRow.getBoundingClientRect();
                        let buttonTop = positionclickedRow.top + window.scrollY;
                        let buttonLeft = positionclickedRow.right + window.scrollX + 1;
    
                        editEventsButton.style.top = buttonTop + 'px';
                        editEventsButton.style.left = buttonLeft + 'px';
                        editEventsButton.style.display = "block";
                    })
                }
            })
        } else {
            editEventsButton.style.display = "none";
        }
    })
    
    events.forEach(event => {
        const tr = table.insertRow();
        const titleCell = tr.insertCell();
        titleCell.textContent = event.title;

        const [date,time] = event.date.split("T");
        const dateCell = tr.insertCell();
        dateCell.textContent = date;
        

        const timeCell = tr.insertCell();
        timeCell.textContent = time;

        const locationCell = tr.insertCell();
        locationCell.textContent = event.location;

        const dmCell = tr.insertCell();
        dmCell.textContent = event.dm;

        const playersCell = tr.insertCell();
        playersCell.textContent = "";//event.players.join(', ')

        const stateCell = tr.insertCell();
        stateCell.textContent = stateEnum[event.state];
    });
}

document.addEventListener('DOMContentLoaded', () => { addEventsToTable() });


