async function loadText(url) { // Basic fetch with no error handling
    try {
        const response = await fetch(url, {
            method: "GET",
            mode: "cors",
            cache: "no-cache"
        });
        const text = await response.text();
        document.getElementById("testButton").innerText = text; // Set innertext of button to response from GET
    } catch (error) {
        console.error("Error:", error);
    }
}