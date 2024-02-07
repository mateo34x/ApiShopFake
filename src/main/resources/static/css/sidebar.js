const OP = document.getElementById("btnO");
const sections = document.querySelectorAll('.sidebard, .titleside, .containerside, .ItemName, .containerProfile, .dataUserP, .svgside, .btnLogout');


OP.addEventListener("click", () =>{

    const svgElement = OP.querySelector("svg");
    var classSVG = svgElement.classList.value;

    if (classSVG === 'open') {

        svgElement.classList.add("close");
         sections.forEach(section => {
                section.classList.add('open');
         });


    } else {
        svgElement.classList.remove("close");
         sections.forEach(section => {
                section.classList.remove('open');
         });


    }
})