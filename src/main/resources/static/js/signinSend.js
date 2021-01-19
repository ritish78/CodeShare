function send() {

    let object = {
        "email": document.getElementById("email").value,
        "password": document.getElementById("password").value
    };

    let json = JSON.stringify(object);

    //Checking if the text boxes are empty or not. If it is empty, just stopping this script
    if (document.getElementById("email").value == null || document.getElementById("email").value == "") {
        return;
    }else if(document.getElementById("password").value == null || document.getElementById("password").value == "") {
        return;
    }

    let uuid = document.getElementById("email").value;
    let endPoint = '/user/' + uuid + "/code";

    let xhr = new XMLHttpRequest();
    xhr.open("GET", endPoint, false);
    xhr.open("GET", '/api' + endPoint, false)
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    }else{
      alert("Failed! Please try again with correct values!");
    }

}