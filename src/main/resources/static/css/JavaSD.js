function cargarPagina(url, titulo) {
    fetch(url)
        .then(response => response.text())
        .then(data => {
            // Utiliza DOMParser para convertir la cadena de texto en un documento HTML
            const parser = new DOMParser();
            const doc = parser.parseFromString(data, 'text/html');

            // Extrae el contenido del cuerpo y el título del documento analizado
            const nuevoContenido = doc.body.innerHTML;
            const nuevoTitulo = doc.title;

            // Actualiza el contenido y el título en el DOM
            document.getElementById('contenido').innerHTML = nuevoContenido;
            document.getElementById('tituloPagina').innerText = nuevoTitulo || titulo;

            // Carga y aplica los estilos
            const estilos = doc.head.querySelectorAll('style, link[rel="stylesheet"]');
            estilos.forEach(estilo => {
                document.head.appendChild(estilo.cloneNode(true));
            });

            // Extrae los scripts
            const scripts = doc.body.querySelectorAll('script');

            // Crea y ejecuta nuevos scripts
            scripts.forEach(script => {
                const nuevoScript = document.createElement('script');
                nuevoScript.src = script.src;
                nuevoScript.text = script.text;
                document.body.appendChild(nuevoScript);
            });
        })
        .catch(error => {
            console.error('Error al cargar la página:', error);
        });
}
