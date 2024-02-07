

const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");
const signUpGoogle = document.getElementById("btnGoogle");

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

signUpGoogle.addEventListener('click', () => {

    document.getElementById('tokenid').value = tokenId;

});







function validateForm() {
    var textE = document.getElementById("inputEmail").value
    var textP = document.getElementById("inputPassword").value
    var btnL= document.getElementById("btnLogin")




    if (isValidEmail(textE) && textP.length >= 8 && btnL.style.display === "none") {
        btnL.style.display = "flex";

        setTimeout(() => {
            btnL.classList.add("Visible","hover");

        }, 10);




    } else if (!(isValidEmail(textE) && textP.length >= 8)) {



        setTimeout(() => {
            btnL.classList.remove("Visible","hover");
            btnL.style.display = "none";
        }, 10);
    }
}

function validateFormR() {
    var textNR = document.getElementById("name").value
    var textER = document.getElementById("email").value
    var textPR = document.getElementById("password").value
    var btnLR= document.getElementById("btnRegis")



    if ( comienzaConArroba(textNR) && textNR.length >= 5  && isValidEmail(textER) && textPR.length >= 8 && btnLR.style.display === "none") {
        btnLR.style.display = "flex";

        setTimeout(() => {
            btnLR.classList.add("Visible");

        }, 10);





    } else if (!(isValidEmail(textER) && textPR.length >= 8)) {



        setTimeout(() => {
            btnLR.classList.add("Btn");
            btnLR.style.display = "none";
        }, 10);
    }
}


function isValidEmail(email) {
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function comienzaConArroba(input) {
    return input.startsWith('@');
}









document.getElementById('iniciar').addEventListener('keydown', function(event) {

      if (event.key === 'Enter') {

        event.preventDefault();

      }
    });


document.getElementById('registro').addEventListener('keydown', function(event) {

      if (event.key === 'Enter') {

        event.preventDefault();

      }
    });


    var successElement = document.querySelector('[data-success]');
    var errorElement = document.querySelector('[data-error]');
    var errorElementEmail = document.querySelector('[data-email]');

    const toastEL = document.querySelector(".toast");
    const toastER = document.querySelector(".toast_error");
    const proEL = document.querySelector(".progress");
    const proER = document.querySelector(".progressError");


    var isSuccess = successElement && successElement.getAttribute('data-success') === 'true';
    var isError = errorElement && errorElement.getAttribute('data-error') === 'true';
    var isEmailError = errorElementEmail && errorElementEmail.getAttribute('data-email') === 'true';

    if (isSuccess) {
        toastEL.classList.add("active");
        proEL.classList.add("active");

        setTimeout(function(){
            toastEL.classList.remove("active");
            proEL.classList.remove("active");
        },5000)

    }else if(isEmailError){
        toastER.classList.add("active");
        proER.classList.add("active");



        setTimeout(function(){
            toastER.classList.remove("active");
            proER.classList.remove("active");
        },5000)

    }else if(isError){

        document.getElementById("title").innerText = "Error";
        document.getElementById("message").innerText = "Bad email or password";
        toastER.classList.add("active");
        proER.classList.add("active");


        setTimeout(function(){
           toastER.classList.remove("active");
           proER.classList.remove("active");
        },5000)

    }





function guardarDatoLocal(valor) {

    // Guardar el dato en localStorage

    var xhr = new XMLHttpRequest();
        xhr.open('POST', '/google', true);
        xhr.setRequestHeader('Content-Type', 'text/plain');

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                console.log('Respuesta del servidor:', xhr.responseText);

            }
        };

        xhr.send(valor);
}


