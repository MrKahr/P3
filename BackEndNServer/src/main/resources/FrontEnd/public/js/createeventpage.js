async function addCalendar(title, location, description, date, maxNumberOfPlayers) {
    try {
        const response = await fetch("/api/playsession/newplaysession?title="+title+"&description=Location: "+location+" Description: "+description+"&dm=test&currentNumberOfPlayers=0&date="+date+"&maxNumberOfPlayers="+maxNumberOfPlayers+"&moduleID=1", {
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
  if (date == null || date == "" || date < Date.now()){alert("please add a valid date to your event, this can be changed later")};
  const maxNumberOfPlayers = document.querySelector('#eventMaxNumberOfPlayers').value;
  if (maxNumberOfPlayers == null || maxNumberOfPlayers == "" || maxNumberOfPlayers > 7){alert("please add a player maximum (max 7) to your event, this can be changed later")};
  
  if (title != null && location != null && description != null && date != null && maxNumberOfPlayers != null && title != "" && location != "" && description != "" && date != "" && maxNumberOfPlayers != "") {
    try {
        addCalendar(title, location, description, date, maxNumberOfPlayers);
        alert("event created successfully");
      } catch{
        alert("event creation failed, please check your inputs");
      }
  }
  
  
});
