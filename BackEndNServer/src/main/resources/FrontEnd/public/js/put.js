async function sendText(data) {
    try {
        const response = await fetch("http://localhost:8080/responder", {
            method: "PUT",
            body: JSON.stringify({ value: data }),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const result = await response.json();
        document.getElementById("postButton").innerText = result;
    } catch (error) {
        console.error("Error:", error);
    }
}