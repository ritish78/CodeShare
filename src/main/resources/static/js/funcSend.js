function send() {
    //Getting the uuid from url from:
    //localhost:8080/api/code/{uuid}/new
    var url = window.location.pathname;
    var stuff = url.split('/');
    var uuid = stuff[stuff.length-2];

    let object = {
        "body": document.getElementById("code_snippet").value,
        "timeInSeconds": document.getElementById("time_restriction").value,
        "viewsLeft": document.getElementById("views_restriction").value,
        "user_id": uuid
    };

    let json = JSON.stringify(object);

    if (document.getElementById("code_snippet").value == null || document.getElementById("code_snippet").value == ""){
        return;
    }else if(document.getElementById("views_restriction").value == null || document.getElementById("views_restriction").value == ""){
        return;
    }


    let xhr = new XMLHttpRequest();
    let endPoint = '/api/code/' + uuid + '/new';
    xhr.open("POST", '/code/new', false);
    xhr.open("POST", endPoint, false)
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(json);

    if (xhr.status == 201) {
      alert("Success!");
    }else{
      alert("Failed! Please try again with correct values! Email might have already used before.");
    }

}
