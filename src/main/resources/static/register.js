const email = document.getElementById("")

function post() {
    return fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: json.stringify({

        })
    })
}