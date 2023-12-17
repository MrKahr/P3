async function loadCalendar(startDateTime, endDateTime) {
    try {
        const response = await fetch("/api/playsession/datebetween?startDateTime="+startDateTime+"&endDateTime="+endDateTime, {
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

// Beautified by https://jsonformatter.org/
// In an actual implementation this would be a GET to the server
let eventsData = [];

let currentDate = new Date();
// For event color we cycle through this array
const colors = ["#add8e6", "#559374", "#fca9e0", "#84ba5e"];
const NUMCOLORS = colors.length;

async function addEventsToCalendar(events) {
    if (currentDate.getMonth() === 11) {
        events = await JSON.parse(await loadCalendar(""+currentDate.getFullYear()+"-"+(currentDate.getMonth()+1)+"-01T00:01",""+(currentDate.getFullYear()+1)+"-01-01T00:01"));
        console.log(events);
    } else if (currentDate.getMonth() >= 9){
        events = await JSON.parse(await loadCalendar(""+currentDate.getFullYear()+"-"+(currentDate.getMonth()+1)+"-01T00:02",""+currentDate.getFullYear()+"-"+(currentDate.getMonth()+2)+"-01T00:02"));
        console.log(events);
    } else if (currentDate.getMonth() == 8){
        events = await JSON.parse(await loadCalendar(""+currentDate.getFullYear()+"-0"+(currentDate.getMonth()+1)+"-01T00:02",""+currentDate.getFullYear()+"-"+(currentDate.getMonth()+2)+"-01T00:02"));
        console.log(events);
    } else {
        events = await JSON.parse(await loadCalendar(""+currentDate.getFullYear()+"-0"+(currentDate.getMonth()+1)+"-01T00:03",""+currentDate.getFullYear()+"-0"+(currentDate.getMonth()+2)+"-01T00:03"));
        console.log(events);
    }
    let colorNum = 0; // Event color cycles through an array
    events.forEach(event => {
        const eventDate = new Date(event.date);
        console.log(event.id);
        if (event.module === null) {
            event.module = {
                "name": "",
                "description": "",
                "levelRange": "",
                "addedDate": null,
                "removedDate": null
            }
        }

        if (eventDate.getMonth() === currentDate.getMonth() && eventDate.getFullYear() === currentDate.getFullYear()) {
            const eventElement = document.createElement('div');
            eventElement.classList.add('event');

            // Newline in string counts as newline character
            eventElement.textContent = `${event.title}
            ${event.currentNumberOfPlayers} players
            ${event.module.name}`;
            eventElement.style.backgroundColor = colorPicker(colorNum);
            colorNum = colorNum >= NUMCOLORS - 1 ? 0 : colorNum + 1;

            addModal(event, eventElement); // Add modal

            eventElement.onclick = function () {
                showModal(eventElement);
            }

            document.getElementById(`${eventDate.getDate()}`).appendChild(eventElement);
        }
    });
}

// https://www.w3schools.com/howto/howto_css_modals.asp
function addModal(event, eventElement) {
    const modal = document.createElement('div');
    modal.classList.add('modal');
    eventElement.appendChild(modal);

    // Put content in modal
    const modalContent = document.createElement('div');
    const modalHeader = document.createElement('div');
    const span = document.createElement('span');

    modalHeader.innerHTML = `<h2>${event.title}</h2>`

    modalContent.appendChild(modalHeader);
    modalContent.classList.add('modal-content');

    span.classList.add('close');
    span.innerText = '×'; // Symbol that isn't the letter x

    // https://stackoverflow.com/questions/618089/can-i-insert-elements-to-the-beginning-of-an-element-using-appendchild
    modalHeader.insertBefore(span, modalHeader.firstChild);
    const textContent = document.createElement('p');
    modalContent.appendChild(textContent);

    const date = new Date(event.date);
    dateStr = `${date.getDate()}-${date.getMonth()}-${date.getFullYear()}
    ${date.getHours()}:${date.getMinutes()}`;

    textContent.innerText = `${dateStr}

    ${event.module.name}

    Level range: ${event.module.levelRange}
    ${event.currentNumberOfPlayers} / ${event.maxNumberOfPlayers} players
    
    ${event.module.description}`;

    // Options to close modal
    span.onclick = function (event) {
        event.stopPropagation();
        modal.style.display = "none";
    }
    modal.onclick = function (event) {
        // Probably not a great way of doing this
        if (event.target == modal) {
            event.stopPropagation();
            modal.style.display = "none";
        }
    }

    // Link to event or 404
    const link = document.createElement('a');
    link.href = `events/eventPage/${event.id}.html`;
    link.innerText = "Go to event";
    modalContent.appendChild(link);

    modal.appendChild(modalContent);

    return modal;
}

function showModal(eventElement) {
    const modal = eventElement.querySelector('div');
    modal.style.display = "block";
}

// Returns color from global array
function colorPicker(colorNum) {
    return colors[colorNum];
}


// https://stackoverflow.com/questions/1184334/get-number-days-in-a-specified-month-using-javascript
function generateCalendar(year, month) {
    const daysInMonth = new Date(year, month, 0).getDate();
    let firstDay = new Date(year, month - 1, 1).getDay();

    // js weekdays sets sunday as 0 and monday as 1 for some reason
    firstDay = (firstDay === 0) ? 6 : firstDay - 1;

    let calendarHtml = '<table class="calendar-table"><thead><tr><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th><th>Fri</th><th>Sat</th><th>Sun</th></tr></thead><tbody>';
    let day = 1;

    // Weeks (rows)
    for (let i = 0; i < 6; i++) {
        calendarHtml += '<tr>';
        // Weekdays (cells)
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < firstDay) {
                calendarHtml += '<td></td>'; // empty cell
            } else if (day > daysInMonth) {
                break; // exit if all days are rendered
            } else {
                calendarHtml += `<td id="${day}">${day}</td>`;
                day++;
            }
        }
        calendarHtml += '</tr>';
        if (day > daysInMonth) {
            break; // exit if all days are rendered
        }
    }

    calendarHtml += '</tbody></table>';
    return calendarHtml;
}

function updateCalendarHeader() {
    const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    document.getElementById('current-month-year').innerText = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
}

async function updateCalendar() {
    document.getElementById('calendar').innerHTML = generateCalendar(currentDate.getFullYear(), currentDate.getMonth() + 1);
    updateCalendarHeader();
}

function changeMonth(diff) {
    currentDate.setMonth(currentDate.getMonth() + diff);
    updateCalendar();
}

document.getElementById('prev-month').addEventListener('click', () => {
    changeMonth(-1);
    addEventsToCalendar(eventsData);
});
document.getElementById('next-month').addEventListener('click', () => {
    changeMonth(1);
    addEventsToCalendar(eventsData);
});

// Generate Calendar
updateCalendar();

document.addEventListener('DOMContentLoaded', () => { addEventsToCalendar(eventsData) });
