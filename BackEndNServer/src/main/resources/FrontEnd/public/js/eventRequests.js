// Event pages are created when new events are created and given a unique ID, which is used for the url

// Event data retrieved via GET to retrieve possible changes
const eventData = {
    "id": "1",
    "title": "D'n'D'n'D'n'D",
    "maxNumberOfPlayers": 6,
    "currentNumberOfPlayers": 3,
    "date": "2023-12-13T10:10:00",
    "state": "Not yet",
    "moduleSetEvents": [],
    "description": "It is where it is.", // new
    "players": ["XxShadowMasterxX", "swaglord69", "SomeGuy2"], // new
    "dm": "Dragonslayer9001", // new
    "module": { // https://forgottenrealms.fandom.com/wiki/Icewind_Dale:_Rime_of_the_Frostmaiden
        "name": "Icewind Dale: Rime of the Frostmaiden",
        "description": "Feel the cold touch of death in this adventure for the world’s greatest roleplaying game.\n\nIn Icewind Dale, adventure is a dish best served cold.\n\nBeneath the unyielding night sky, you stand before a towering glacier and recite an ancient rhyme, causing a crack to form in the great wall of ice. Beyond this yawning fissure, the Caves of Hunger await. And past this icy dungeon is a secret so old and terrifying that few dare speak of it. The mad wizards of the Arcane Brotherhood long to possess that which the god of winter’s wrath has so coldly preserved—as do you! What fantastic secrets and treasures are entombed in the sunless heart of the glacier, and what will their discovery mean for the denizens of Icewind Dale? Can you save Ten-Towns from the Frostmaiden's everlasting night?\nIcewind Dale: Rime of the Frostmaiden is a tale of dark terror that revisits the forlorn, flickering candlelights of civilization known as Ten-Towns and sheds light on the many bone-chilling locations that surround these frontier settlements.",
        "levelRange": "1-5",
        "addedDate": null,
        "removedDate": null
    },
    "rewards": [] // new + we want to hide these
};

function displayEvent() {
    if(eventData.currentNumberOfPlayers >= eventData.maxNumberOfPlayers) {
        document.getElementById('event-button').style.display = 'none';
    }

    document.getElementById('event-title').innerText = eventData.title;

    const date = new Date(eventData.date);
    dateStr = `${date.getDate()}-${date.getMonth()}-${date.getFullYear()}

    ${date.getHours()}:${date.getMinutes()}`;
    document.getElementById('event-date-time').innerText = dateStr;

    document.getElementById('dm').innerText = eventData.dm;

    document.getElementById('signups').innerText = `${eventData.currentNumberOfPlayers} / ${eventData.maxNumberOfPlayers}`;

    let players = '';
    for (username of eventData.players) {
        players = players.concat('<li>' + username + '</li>');
    }
    document.getElementById('user-list').innerHTML = players;

    document.getElementById('event-description').innerText = eventData.description;

    document.getElementById('module-name').innerText = eventData.module.name;

    document.getElementById('level-range').innerText = eventData.module.levelRange;

    document.getElementById('module-description').innerText = eventData.module.description;
}

// https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dialog
const dialog = document.querySelector("dialog");
const showButton = document.querySelector("dialog + button");
const inside = document.getElementById("inside-dialog")

showButton.addEventListener("click", () => {
  dialog.showModal();
});



// https://stackoverflow.com/questions/50037663/how-to-close-a-native-html-dialog-when-clicking-outside-with-javascript
dialog.addEventListener("click", () => {
    dialog.close();
})

inside.addEventListener("click", (event) => {
    event.stopPropagation();
});

document.addEventListener('DOMContentLoaded', () => { displayEvent() });