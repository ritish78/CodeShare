function send() {

    let object = {
        "username": document.getElementById("username").value,
        "email": document.getElementById("email").value,
        "password": document.getElementById("password").value
    };

    let json = JSON.stringify(object);


    //Checking if the text boxes are empty or not. If it is empty, just stopping this script
    if (document.getElementById("username").value == null || document.getElementById("username").value == "") {
        return;
    }else if(document.getElementById("email").value == null || document.getElementById("email").value == "") {
        return;
    }else if (document.getElementById("password").value == null || document.getElementById("password".value == "")) {
        return;
    }

    //Then, checking if 'Password' text box and 'Confirm Password' text box have the same value.
    //Only checking 'Password' in previous statement as it must be same as 'Confirm Password'
    if (document.getElementById("password").value !== document.getElementById("confirmPassword").value) {
        alert("Password fields don't match!");
        return;
    }

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/user/new', false);
    xhr.open("POST", '/api/user/new', false)
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    }else{
      alert("Failed! Please try again with correct values!");
    }

}