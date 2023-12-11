async function postResource(string, url){
    await fetch(url,{
        method: "POST",
        mode: "cors",
        cache: "no-cache"
    });
}