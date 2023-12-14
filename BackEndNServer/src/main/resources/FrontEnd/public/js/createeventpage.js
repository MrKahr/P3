async function addCalendar(title, location, description, date, maxNumberOfPlayers) {
    try {//http://localhost:8080/DateBetween?startDateTime=2023-12-01T15:06:27.299631&endDateTime=2023-12-31T15:06:27.299631
        const response = await fetch("http://localhost:8080/newplaysession?title="+title+"&description=Location: "+location+" Description: "+description+"&dm=test&currentNumberOfPlayers=0&date="+date+"&maxNumberOfPlayers="+maxNumberOfPlayers+"&moduleID=1", {
            method: "POST",
            mode: "cors",
            cache: "no-cache"
        });
        const calendar = await response.text();
        console.log(calendar);
    } catch (error) {
        console.error("Error:", error);
    }
}

const createEventButton = document.getElementById("createEventButton");

createEventButton.addEventListener('click', () => {
  const title = document.querySelector('#eventTitle').value;
  const location = document.querySelector('#eventLocation').value;
  const description = document.querySelector('#eventDescription').value;
  const date = document.querySelector('#eventDate').value;
  const maxNumberOfPlayers = document.querySelector('#eventMaxNumberOfPlayers').value;

  addCalendar(title, location, description, date, maxNumberOfPlayers);
});
