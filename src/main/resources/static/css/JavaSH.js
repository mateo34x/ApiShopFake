var spanRedireccion = document.getElementById('enlaceRedireccion');

spanRedireccion.addEventListener('click', () => {
        window.location.href = 'login.html';
});

document.addEventListener("DOMContentLoaded", function() {
    animateText();
});





document.addEventListener("DOMContentLoaded", function() {

        animateText();

});

function animateText() {
    var animatedTexts = document.querySelectorAll(".v1_4, .v1_22, .v1_36, .button-27, .button-28, .v1_25, .hideen");


    if(window.innerWidth >= 999){
        animatedTexts.forEach(function(text) {
            if(text.classList.contains("hideen")){
                text.style.zIndex = "0";
            }else{
                text.style.opacity = "1";
                text.style.transform = "translateY(0)";
            }


            });

    }


}