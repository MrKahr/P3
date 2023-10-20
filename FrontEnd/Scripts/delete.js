async function deletePage(url) {
    try {
        const reponse = await fetch(url, {
            method: "DELETE",
            mode: "cors",
            cache: "no-cache"
        }).then(url => console.log("Deleted resource: " + url));
    } catch (error) {
        console.error("Error:", error);
    }
}