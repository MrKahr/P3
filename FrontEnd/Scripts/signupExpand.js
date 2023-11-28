document.getElementById('Password').addEventListener('focus', function() {
    let passwordConditions = document.getElementById("PasswordConditions");
    if(passwordConditions.style.display == "none") {
        passwordConditions.style.display = "block";
    } else {
        passwordConditions.style.display = "none";
    }
})
