async function addCalendar(title, location, description, date, maxNumberOfPlayers, dm, requiredRole) {
    try {
        const response = await fetch("/api/playsession/newplaysession?" + new URLSearchParams({
          title: title,
          description: description,
          location: location,
          dm: dm,
          date: date,
          maxNumberOfPlayers: maxNumberOfPlayers,
          moduleID: 1,
          requiredRole: requiredRole
        }), {
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
  validateEvent();
});

async function validateEvent(){
  const title = document.querySelector('#eventTitle').value;
  const location = document.querySelector('#eventLocation').value;
  const description = document.querySelector('#eventDescription').value;
  const date = document.querySelector('#eventDate').value;
  const maxNumberOfPlayers = document.querySelector('#eventMaxNumberOfPlayers').value;
  const dm = await getUsername();
  const requiredRole = document.getElementById("eventPlayers").value;

  //TODO: validate max length
  if(title == null || title == ""){
    alert("Please add a valid title (max 40 characters) to your event, this can be changed later");
  }
  
  if(location == null || location == ""){
    alert("Please add a location to your event, this can be changed later");
  }
  
  if(description == null || description == ""){
    alert("Please add a desciption to your event, this can be changed later");
  };
  
  if(date == null || date == ""){
    alert("Please add a valid date to your event, this can be changed later");
  }
  
  if (maxNumberOfPlayers == null || maxNumberOfPlayers == "" || maxNumberOfPlayers > 7){
    alert("Please add valid a player maximum (max 7) to your event, this can be changed later");
  }
  
  if (title != null && location != null && description != null && date != null && maxNumberOfPlayers != null &&
      title != "" && location != "" && description != "" && date != "" && maxNumberOfPlayers != "" && maxNumberOfPlayers <= 7) {
    try {
        addCalendar(title, location, description, date, maxNumberOfPlayers, dm, requiredRole);
        alert("event created successfully");
      } catch{
        alert("event creation failed, please check your inputs");
      }
  }
}

/**
 * Requests the server for the user's session to determine if they're logged in or not.
 */
async function getUsername(){
  let response;
  
  try {
      response = await fetch("/api/user?" + new URLSearchParams({
          getLoginSession: true
      }), {
          method: "GET",
          headers: {
              'Content-Type': 'application/json'
          },
          mode: "cors",
      });

      let message = JSON.parse(await response.text());
      
      // The user has logged in
      if(message[0] === "true"){
        return message[1];
      }
      // The user has NOT logged in
      else if(message[0] === "false"){
          console.warn("Cannot fetch username as you're not logged in.");
          return "DM not found";
      }

  } catch (error) {
      console.error(error);
  }
}