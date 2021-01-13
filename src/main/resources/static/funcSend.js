function send() {

    let object = {
        "body": document.getElementById("code_snippet").value,
        "timeInSeconds": document.getElementById("time_restriction").value,
        "viewsLeft": document.getElementById("views_restriction").value
    };

    let json = JSON.stringify(object);

    if (document.getElementById("code_snippet").value == null || document.getElementById("code_snippet").value == ""){
        return;
    }else if(document.getElementById("views_restriction").value == null || document.getElementById("views_restriction").value == ""){
        return;
    }

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/code/new', false);
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    }else{
      alert("Failed! Please try again with correct values!");
    }

}
