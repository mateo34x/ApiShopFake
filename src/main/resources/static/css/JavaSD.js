function toggleSection(sectionName) {
    deactivateAllSections();

    const activeSection = document.querySelector('.' + sectionName);
    if (activeSection) {
        activeSection.classList.add('active');
    }
}



function deactivateAllSections() {
    const sections = document.querySelectorAll('.dashboard, .token, .addProduct, .ViewAllP, .Photo');
    sections.forEach(section => {
        section.classList.remove('active');
    });
}

const tokenDIV = document.querySelector(".token");

 var successElement = document.querySelector('[data-successT]');
 var isSuccess = successElement && successElement.getAttribute('data-successT') === 'true';
 if(isSuccess){
    deactivateAllSections();
    tokenDIV.classList.add('active')

 }
document.addEventListener('keydown', function(event) {
    if (event.key === ' ' && event.target.tagName !== 'INPUT' && event.target.tagName !== 'TEXTAREA') {
        // Si la tecla presionada es un espacio y no está enfocada en un INPUT o TEXTAREA
        var tokenNameElement = document.getElementById('tokenName-<ID_DEL_TOKEN>');
        if (tokenNameElement) {
            // Crear un área de texto temporal y copiar el contenido al portapapeles
            var tempTextArea = document.createElement('textarea');
            tempTextArea.value = tokenNameElement.textContent;
            document.body.appendChild(tempTextArea);
            tempTextArea.select();
            document.execCommand('copy');
            document.body.removeChild(tempTextArea);

            // Opcional: Notificar al usuario que el contenido se ha copiado
            alert('Contenido copiado al portapapeles: ' + tokenNameElement.textContent);
        }
    }
});


window.onload = function () {
            if (window.history && window.history.pushState) {
                window.history.pushState('', null, window.location.href);
                window.onpopstate = function () {
                    window.history.pushState('', null, window.location.href);
                };
            }
        };


var mensajeMostrado = false;

// Función para mostrar el mensaje personalizado
function mostrarMensaje() {
    return '¡Espera! ';
}

// Agregar un listener para el evento beforeunload
window.addEventListener('beforeunload', function (e) {
    // Si el mensaje personalizado aún no se ha mostrado, mostrarlo
    if (!mensajeMostrado) {
        // Cancelar el evento de cierre predeterminado
        e.preventDefault();
        // Mostrar el mensaje personalizado
        e.returnValue = mostrarMensaje();
        // Marcar el mensaje como mostrado
        mensajeMostrado = true;
    }
});

// Agregar un listener para el evento unload
window.addEventListener('unload', function (e) {
    // Restablecer la variable mensajeMostrado al salir de la página
    mensajeMostrado = false;
});