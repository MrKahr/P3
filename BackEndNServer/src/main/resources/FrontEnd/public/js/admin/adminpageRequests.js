
// Module functions
async function addModule() {
    const name = document.getElementById("moduleTitle").value;
    let minLvl = document.getElementById("minLevel").value;
    let maxLvl = document.getElementById("maxLevel").value;
    minLvl = minLvl.length === 1 ? "0" + minLvl : minLvl;
    maxLvl = maxLvl.length === 1 ? "0" + maxLvl : maxLvl;
    const levelRange = minLvl + "-" + maxLvl;
    const description = document.getElementById("moduleTextBox").value;
    if (await requestAddModule(name, description, levelRange) === 200) {
        console.log("successfully added module");
        const modules = await requestGetModules();
        displayModules(modules);
    } else {
        console.log("adding module unsuccessful");
        document.getElementById("moduleUnsuccessful").style.display = "block";
    }
}

async function updateModule() {
    const name = document.getElementById("moduleTitleEdit").value;
    let minLvl = document.getElementById("minLevelEdit").value;
    let maxLvl = document.getElementById("maxLevelEdit").value;
    minLvl = minLvl.length === 1 ? "0" + minLvl : minLvl;
    maxLvl = maxLvl.length === 1 ? "0" + maxLvl : maxLvl;
    const levelRange = minLvl + "-" + maxLvl;
    const description = document.getElementById("moduleTextBoxEdit").value;
    const id = document.getElementById("idModuleEdit").innerText;
    let module = {
        id: id,
        name: name,
        description: description,
        levelRange: levelRange
    }
    if (await requestUpdateModule(module) === 200) {
        console.log("successfully updated module");
        const modules = await requestGetModules();
        displayModules(modules);
    } else {
        console.log("updating module unsuccessful");
        document.getElementById("moduleUnsuccessful").style.display = "block";
    }
}

async function requestAddModule(name, description, levelRange) {
    try {
        const response = await fetch(`/admin/module/add?name=${name}&description=${description}&levelRange=${levelRange}`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache"
        });
        return response.status;
    } catch (error) {
        console.error("Error:", error);
    }
}

async function requestUpdateModule(moduleEdit) {
    try {
        const response = await fetch("/admin/module/editByID", {
            method: "PUT",
            mode: "cors",
            cache: "no-cache",
            headers: {
                "Content-Type": "application/json" // Set type to JSON
            },
            body: JSON.stringify(moduleEdit)
        });
        return response.status;
    } catch (error) {
        console.error("Error:", error);
    }
}

async function requestGetModules() {
    try {
        const response = await fetch("/admin/module/getAll", {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        return await JSON.parse(await response.text());
    } catch (error) {
        console.error("Error:", error);
    }
}

async function deleteModule(ID) {
    try {
        const response = await fetch(`/admin/module/removeByID?id=${ID}`, {
            method: "DELETE",
            mode: "cors",
            cache: "no-cache"
        });
        return await response.text();
    } catch (error) {
        console.error("Error:", error);
    }
}

function displayModules(modules) {
    const moduleTable = document.getElementById("moduleTable");

    // https://stackoverflow.com/questions/12289853/how-can-i-use-a-notfirst-child-selector
    const rowsToRemove = Array.from(moduleTable.querySelectorAll("tr:not(:first-child)"));
    rowsToRemove.forEach(row => row.remove());
    modules.forEach(module => {
        let row = document.createElement("tr");
        document.getElementById("moduleTable").appendChild(row);
        row.innerHTML = innerHTML =
            `<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>`;
        const data = row.querySelectorAll("td");
        data[0].innerText = module.name;
        data[1].innerText = module.levelRange;
        data[2].innerText = module.id;
        data[2].style.display = "none";
        data[3].innerText = module.description;
        data[3].style.display = "none";
        data[4].innerText = module.addedDate.slice(0, 10);

        row.addEventListener("click", () => {
            let clickedRow = event.currentTarget;
            let manageModulesButtons = document.getElementById("manageModulesButtons");
            let positionclickedRow = clickedRow.getBoundingClientRect();
            let buttonTop = positionclickedRow.top + window.scrollY;
            let buttonLeft = positionclickedRow.right + window.scrollX + 1;

            manageModulesButtons.style.top = buttonTop + 'px';
            manageModulesButtons.style.left = buttonLeft + 'px';
            manageModulesButtons.style.display = "block";
            currentRow = clickedRow;
        });
    });
}

function openModuleEdit() {
    document.getElementById("createNewModule").style.display = "none";
    document.getElementById("editModule").style.display = "block";
    const moduleData = currentRow.querySelectorAll("td");
    document.getElementById("moduleTitleEdit").value = moduleData[0].innerText;
    const levelRange = moduleData[1].innerText.split("-");
    document.getElementById("minLevelEdit").value = levelRange[0];
    document.getElementById("maxLevelEdit").value = levelRange[1];
    document.getElementById("idModuleEdit").innerText = moduleData[2].innerText;
    document.getElementById("moduleTextBoxEdit").value = moduleData[3].innerText;
}


// User functions

async function requestGetUsers(min, max) {
    try {
        const response = await fetch(`/api/users_in_range/${min}-${max}`, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        return await JSON.parse(await response.text());
    } catch (error) {
        console.error("Error:", error);
    }
}

async function requestSetRole(username, role) {
    try {
        const response = await fetch(`/api/set_role/${username}?newRole=${role}`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache",
        });
        return await response.text();
    } catch (error) {
        console.error("Error:", error);
    }
}

async function requestRemoveRole(username, selectedRole) {
    try {
        const response = await fetch(`/api/remove_role/${username}?role=${selectedRole}`, {
            method: "DELETE",
            mode: "cors",
            cache: "no-cache",
        });
        return await response.text();
    } catch (error) {
        console.error("Error:", error);
    }
}

async function requestRegisterPayment(username) {
    try {
        const response = await fetch(`/api/register_payment/${username}`, {
            method: "PUT",
            mode: "cors",
            cache: "no-cache",
        });
        return await response.text();
    } catch (error) {
        console.error("Error:", error);
    }
}


async function displayUsers(users) {
    const userTable = document.getElementById("userTable");

    const rowsToRemove = Array.from(userTable.querySelectorAll("tr:not(:first-child)"));
    rowsToRemove.forEach(row => row.remove());
    users.forEach(user => {
        let row = document.createElement("tr");
        userTable.appendChild(row);
        row.innerHTML = innerHTML =
            `<td></td>
                <td></td>
                <td></td>`
        const data = row.querySelectorAll("td");
        data[0].innerText = user.basicUserInfo.userName;
        // https://stackoverflow.com/questions/14379274/how-to-iterate-over-a-javascript-object
        const roleString = Object.keys(user.allRoles).join(' - ');

        data[1].innerText = roleString;
        if (user.memberInfo && user.memberInfo.lastPaymentDate) {
            data[2].innerText = user.memberInfo.lastPaymentDate.slice(0, 10);
        } else {
            data[2].innerText = "None"
        }

        row.addEventListener("click", () => {
            let clickedRow = event.currentTarget;
            let manageUsersButtons = document.getElementById("manageUsersButtons");
            let positionclickedRow = clickedRow.getBoundingClientRect();
            let buttonTop = positionclickedRow.top + window.scrollY;
            let buttonLeft = positionclickedRow.right + window.scrollX + 1;

            manageUsersButtons.style.top = buttonTop + 'px';
            manageUsersButtons.style.left = buttonLeft + 'px';
            manageUsersButtons.style.display = "block";
            currentRow = clickedRow;
        });
    });

}


// ----------------------------------------------------------------------------------------
// Global variable(s)
let currentRow;

// Event listeners

// Module
// Show
document.addEventListener('DOMContentLoaded', async () => displayModules(await requestGetModules()));
// Delete
document.getElementById("deleteModuleButton").addEventListener("click", async () => {
    const id = currentRow.querySelectorAll("td")[2].innerText;
    await deleteModule(id);
    displayModules(await requestGetModules());
    document.getElementById("manageModulesButtons").style.display = "none";
});
// Create
document.getElementById("submitModule").addEventListener("click", () => { addModule() });
// Edit
document.getElementById("editModuleButton").addEventListener("click", () => {
    openModuleEdit();
    document.getElementById("manageModulesButtons").style.display = "none";
});
document.getElementById("cancelModuleEdit").addEventListener("click", () => {
    document.getElementById("createNewModule").style.display = "block";
    document.getElementById("editModule").style.display = "none";
});
document.getElementById("submitModuleEdit").addEventListener("click", async () => {
    await updateModule();
    document.getElementById("createNewModule").style.display = "block";
    document.getElementById("editModule").style.display = "none";
});
document.addEventListener("click", () => document.getElementById("moduleUnsuccessful").style.display = "none");

// User
// Show
document.addEventListener('DOMContentLoaded', async () => displayUsers(await requestGetUsers(1, 20)));

// Change role
document.getElementById("changeRoleButton").addEventListener("click", async () => {
    // https://stackoverflow.com/questions/3301688/how-do-you-get-the-currently-selected-option-in-a-select-via-javascript
    const roleSelect = document.getElementById("managerole");
    const selectedRole = roleSelect.options[roleSelect.selectedIndex].value.toUpperCase();
    const currentRoles = currentRow.querySelectorAll("td")[1].innerText;
    const username = currentRow.querySelectorAll("td")[0].innerText;

    if (currentRoles.includes(selectedRole)) {
        await requestRemoveRole(username, selectedRole);
        await displayUsers(await requestGetUsers(1, 20));
    } else {
        await requestSetRole(username, selectedRole);
        await displayUsers(await requestGetUsers(1, 20));
    }
});

// Register payment
document.getElementById("registerPayment").addEventListener("click", async () => {
    await requestRegisterPayment(currentRow.querySelectorAll("td")[0].innerText);
    await requestGetUsers(1, 20);
});




// ----------------------------------------------------------------------------------------

function showTab(event, manageTab) {
    let tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    document.getElementById('manageUsers').style.display = "none";
    document.getElementById('manageEvents').style.display = "none";
    document.getElementById('blogPosts').style.display = "none";
    document.getElementById('manageModules').style.display = "none";
    document.getElementById('membershipRequests').style.display = "none";

    document.getElementById(manageTab).style.display = "block";
    event.currentTarget.className += " active";
}

let tablerows = document.querySelectorAll('tr');
Array.from(tablerows).forEach((tablerow, index) => {
    tablerow.addEventListener('click', () => {
        let clickedRow = event.currentTarget;
        let manageEventsButtons = document.getElementById("manageEventsButtons");
        let blogPostsButtons = document.getElementById("blogPostsButtons");
        let positionclickedRow = clickedRow.getBoundingClientRect();
        let buttonTop = positionclickedRow.top + window.scrollY;
        let buttonLeft = positionclickedRow.right + window.scrollX + 1;

        manageEventsButtons.style.top = buttonTop + 'px';
        manageEventsButtons.style.left = buttonLeft + 'px';
        manageEventsButtons.style.display = "block";

        blogPostsButtons.style.top = buttonTop + 'px';
        blogPostsButtons.style.left = buttonLeft + 'px';
        blogPostsButtons.style.display = "block";
    })
})

document.getElementById('modifyButton').addEventListener('click', function () {
    let editTitle = document.getElementById("editTitle");
    let editDate = document.getElementById("editDate");
    let editTime = document.getElementById("editTime");
    let editLocation = document.getElementById("editLocation");
    let editDm = document.getElementById("editDm");
    let editPlayers = document.getElementById("editPlayers");
    let editState = document.getElementById("editState");
    if (editTitle.style.display == "none" && editDate.style.display == "none" && editTime.style.display == "none" && editLocation.style.display == "none" && editDm.style.display == "none" && editPlayers.style.display == "none" && editState.style.display == "none") {
        editTitle.style.display = "block";
        editDate.style.display = "block";
        editTime.style.display = "block";
        editLocation.style.display = "block";
        editDm.style.display = "block";
        editPlayers.style.display = "block";
        editState.style.display = "block";
    } else {
        editTitle.style.display = "none";
        editDate.style.display = "none";
        editTime.style.display = "none";
        editLocation.style.display = "none";
        editDm.style.display = "none";
        editPlayers.style.display = "none";
        editState.style.display = "none";
    }

    Array.from(tablerows).forEach((tablerow, index) => {
        if (tablerow.rowIndex != 0) {
            tablerow.addEventListener('click', function editPlacement() {
                let clickedRow = event.currentTarget;
                let editEventInput = document.getElementById("editEventInput");
                let editTitleInput = document.getElementById("editTitleInput");
                let editDateInput = document.getElementById("editDateInput");
                let editTimeInput = document.getElementById("editTimeInput");
                let editLocationInput = document.getElementById("editLocationInput");
                let editDmInput = document.getElementById("editDmInput");
                let editPlayersInput = document.getElementById("editPlayersInput");
                let editStateInput = document.getElementById("editStateInput");
                let editFinished = document.getElementById("editFinished");
                let editCancelled = document.getElementById("editCancelled");

                let editTitlePlace = clickedRow.cells[0];
                editTitlePlace.appendChild(editTitleInput);

                let editDatePlace = clickedRow.cells[1];
                editDatePlace.appendChild(editDateInput);

                let editTimePlace = clickedRow.cells[2];
                editTimePlace.appendChild(editTimeInput);

                let editLocationPlace = clickedRow.cells[3];
                editLocationPlace.appendChild(editLocationInput);

                let editDmPlace = clickedRow.cells[4];
                editDmPlace.appendChild(editDmInput);

                let editPlayersPlace = clickedRow.cells[5];
                editPlayersPlace.appendChild(editPlayersInput);

                let editStatePlace = clickedRow.cells[6];
                editStatePlace.appendChild(editStateInput);

                let editFinishedPlace = clickedRow.cells[6];
                editFinishedPlace.appendChild(editFinished);

                let editCancelledPlace = clickedRow.cells[6];
                editCancelledPlace.appendChild(editCancelled);

                editEventInput.style.display = "block";
            }, { once: true })/* Fix this when solution is found */
        }
    })
})