"use strict";
function dragNdrop(event,previewId) {
    var fileName = URL.createObjectURL(event.target.files[0]);


    var preview = document.getElementById(previewId);
    var previewImg = document.createElement("img");
    previewImg.setAttribute("src", fileName);
    preview.innerHTML = "";
    preview.appendChild(previewImg);

}
function drag() {
    document.getElementById('uploadFile').parentNode.className = 'draging dragBox';
}
function drop() {
    document.getElementById('uploadFile').parentNode.className = 'dragBox';
}



// Selecciona todos los elementos input con el atributo data-type='currency'
var currencyInputs = document.querySelectorAll("input[data-type='currency']");

// Asocia los eventos keyup y blur a cada elemento input
currencyInputs.forEach(function (input) {
    input.addEventListener('keyup', function () {
        formatCurrency(this);
    });

    input.addEventListener('blur', function () {
        formatCurrency(this, "blur");
    });
});

function formatNumber(n) {
    // formato del número 1000000 a 1,234,567
    return n.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function formatCurrency(input, blur) {
    // obtén el valor del input
    var input_val = input.value;

    // no validar input vacío
    if (input_val === "") { return; }

    // longitud original
    var original_len = input_val.length;

    // posición inicial del caret
    var caret_pos = input.selectionStart;

    // verifica si hay un decimal
    if (input_val.indexOf(".") >= 0) {
        // obtén la posición del primer decimal
        var decimal_pos = input_val.indexOf(".");

        // divide el número por el punto decimal
        var left_side = input_val.substring(0, decimal_pos);
        var right_side = input_val.substring(decimal_pos);

        // agrega comas al lado izquierdo del número
        left_side = formatNumber(left_side);

        // valida el lado derecho
        right_side = formatNumber(right_side);

        // en blur asegúrate de tener 2 números después del decimal
        if (blur === "blur") {
            right_side += "00";
        }

        // limita el decimal a solo 2 dígitos
        right_side = right_side.substring(0, 2);

        // une el número por .
        input_val = "$" + left_side + "." + right_side;
    } else {
        // no hay decimal ingresado
        // agrega comas al número
        // elimina todos los no dígitos
        input_val = formatNumber(input_val);
        input_val = "$" + input_val;

        // formato final
        if (blur === "blur") {
            input_val += ".00";
        }
    }

    // envía la cadena actualizada al input
    input.value = input_val;

    // coloca el caret en la posición correcta
    var updated_len = input_val.length;
    caret_pos = updated_len - original_len + caret_pos;
    input.setSelectionRange(caret_pos, caret_pos);



}

    var successElement = document.querySelector('[data-success]');
    var errorElement = document.querySelector('[data-error]');


    const iconS = document.querySelector(".iconTS");
    const iconR = document.querySelector(".iconTE");

    var isSuccess = successElement && successElement.getAttribute('data-success') === 'true';
    var isError = errorElement && errorElement.getAttribute('data-error') === 'true';

    if (isSuccess) {
       iconS.classList.add("active");

    }else if(isError){
        iconR.classList.add("active");
    }


