async function loadText(url) { // Basic fetch with no error handling
    const response = await fetch("http://localhost:8080/something",{
        method: "GET",
        mode: "cors",
        cache: "no-cache"
    });
    const text = await response.text();
    document.getElementById("testButton").innerText = text; // Set innertext of button to response from GET
}