async function loadCalendar() {
    try {
        const response = await fetch("http://localhost:8080/DateBetween?startDateTime=2023-12-01T15:06:27.299631&endDateTime=2023-12-31T15:06:27.299631", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        const calendar = await response.text();
        console.log(calendar);
    } catch (error) {
        console.error("Error:", error);
    }
}
loadCalendar();

// Beautified by https://jsonformatter.org/
// In an actual implementation this would be a GET to the server
const eventsData = [
    // {
    //     "id": "2",
    //     "title": "Cool Event På DiceNDrinks",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 2,
    //     "date": "2023-11-12T10:10:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Dungeons And Dargons",
    //         "description": "Module description that describes the module Dungeons And Dargons",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // },
    // {
    //     "id": "10",
    //     "title": "My Sick Ass Homebrew",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 4,
    //     "date": "2023-11-12T10:20:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Dimwits and Dummies",
    //         "description": "Module description that describes the module Dimwits and Dummies",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // },
    // {
    //     "id": "11",
    //     "title": "My Wacky Event",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 4,
    //     "date": "2023-11-13T10:30:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Dandies and Debonairs",
    //         "description": "Module description that describes the module Dandies and Debonairs",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // },
    // {
    //     "id": "12",
    //     "title": "kun for hundeejere",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 6,
    //     "date": "2023-11-13T10:45:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Dudes and Doggies",
    //         "description": "Module description that describes the module Dudes and Doggies",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // },
    // {
    //     "id": "13",
    //     "title": "Please Join Me",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 0,
    //     "date": "2023-11-13T10:50:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Downers and Deadbeats",
    //         "description": "Module description that describes the module Downers and Deadbeats. This description shows and example of a description that is more than a single line long.\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nPlease",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // },
    // {
    //     "id": "1",
    //     "title": "Name Me",
    //     "maxNumberOfPlayers": 6,
    //     "currentNumberOfPlayers": 3,
    //     "date": "2023-12-13T10:10:00",
    //     "state": "Not yet",
    //     "moduleSetEvents": [],
    //     "module": {
    //         "name": "Dunkins and Donuts",
    //         "description": "Module description that describes the module Dunkins and Donuts",
    //         "levelRange": "2-10",
    //         "addedDate": null,
    //         "removedDate": null
    //     }
    // }
];


let currentDate = new Date();
// For event color we cycle through this array
const colors = ["#add8e6", "#559374", "#fca9e0", "#84ba5e"];
const NUMCOLORS = colors.length;

function addEventsToCalendar(events) {
    let colorNum = 0; // Event color cycles through an array
    events.forEach(event => {
        const eventDate = new Date(event.date);

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

function updateCalendar() {
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
