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
  if (title == null || title == ""){alert("please add a title to your event, this can be changed later")};
  const location = document.querySelector('#eventLocation').value;
  if (location == null || location == ""){alert("please add a location to your event, this can be changed later")};
  const description = document.querySelector('#eventDescription').value;
  if (description == null || description == ""){alert("please add a desciption to your event, this can be changed later")};
  const date = document.querySelector('#eventDate').value;
  if (date == null || date == ""){alert("please add a date to your event, this can be changed later")};
  const maxNumberOfPlayers = document.querySelector('#eventMaxNumberOfPlayers').value;
  if (maxNumberOfPlayers == null || maxNumberOfPlayers == ""){alert("please add a player maximum to your event, this can be changed later")};
  
  if (title != null && location != null && description != null && date != null && maxNumberOfPlayers != null && title != "" && location != "" && description != "" && date != "" && maxNumberOfPlayers != "") {
    try {
        addCalendar(title, location, description, date, maxNumberOfPlayers);
        alert("event created successfully");
      } catch{
        alert("event creation failed, please check your inputs");
      }
  }
  
  
});
