

const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");






function validateForm() {
    var textE = document.getElementById("textEmail").value
    var textP = document.getElementById("textPass").value
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
    var textNR = document.getElementById("textNameR").value
    var textER = document.getElementById("textEmailR").value
    var textPR = document.getElementById("textPassR").value
    var btnLR= document.getElementById("btnRegis")
    const toastEL = document.querySelector(".toast");
    const proEL = document.querySelector(".progress");


    if ( comienzaConArroba(textNR) && textNR.length >= 5  && isValidEmail(textER) && textPR.length >= 8 && btnLR.style.display === "none") {
        btnLR.style.display = "flex";

        setTimeout(() => {
            btnLR.classList.add("Visible");

        }, 10);

        btnLR.addEventListener('click',() =>{
                toastEL.classList.add("active");
                proEL.classList.add("active");

                setTimeout(function(){
                    toastEL.classList.remove("active");
                     proEL.classList.remove("active");
                },5000)
        });



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



signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});









