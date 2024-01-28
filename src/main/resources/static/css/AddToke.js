document.getElementById("add").addEventListener("click",function(){
document.getElementsByClassName("container2CT")[0].classList.add("active");

});

document.getElementById("closeadd").addEventListener("click",function(){
document.getElementsByClassName("container2CT")[0].classList.remove("active");

});





const daysContainer = document.getElementById("daysContainer");
const prevBtn = document.getElementById("prevBtn");
const nextBtn = document.getElementById("nextBtn");
const monthYear = document.getElementById("monthYear");
const dateInput = document.getElementById("dateInput");
const calendar = document.getElementById("calendar");
var menu = document.getElementById('menu');
var displayValue = menu.style.display;


let currentDate = new Date();
let selectedDate = null;

function handleDayClick(day) {
  selectedDate = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth(),
    day
  );

  const formattedDate = `${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}/${selectedDate.getDate().toString().padStart(2, '0')}/${selectedDate.getFullYear()}`;
  dateInput.value = formattedDate;
  console.log(selectedDate.getTime());
  calendar.style.display = "none";
  renderCalendar();
}

function createDayElement(day) {
  const date = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
  const dayElement = document.createElement("div");
  dayElement.classList.add("day");

  if (date.toDateString() === new Date().toDateString()) {
    dayElement.classList.add("current");
  }
  if (selectedDate && date.toDateString() === selectedDate.toDateString()) {
    dayElement.classList.add("selected");
  }

  // Deshabilita visualmente los días anteriores a la fecha actual
  if (date < new Date()) {
    dayElement.classList.add("disabled");
    dayElement.textContent = day;
  } else {
    dayElement.textContent = day;
    dayElement.addEventListener("click", () => {
      handleDayClick(day);
    });
  }

  daysContainer.appendChild(dayElement);
}
function renderCalendar() {
  daysContainer.innerHTML = "";
  const firstDay = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth(),
    1
  );
  const lastDay = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth() + 1,
    0
  );

  monthYear.textContent = `${currentDate.toLocaleString("default", {
    month: "long"
  })} ${currentDate.getFullYear()}`;

  for (let day = 1; day <= lastDay.getDate(); day++) {
    createDayElement(day);
  }
}

prevBtn.addEventListener("click", () => {
  currentDate.setMonth(currentDate.getMonth() - 1);
  renderCalendar();
});

nextBtn.addEventListener("click", () => {
  currentDate.setMonth(currentDate.getMonth() + 1);
  renderCalendar();
});

dateInput.addEventListener("click", () => {
  calendar.style.display = "block";
  positionCalendar();
});

document.addEventListener("click", (event) => {
  if (!dateInput.contains(event.target) && !calendar.contains(event.target)) {
    calendar.style.display = "none";
  }
});

function positionCalendar() {
  const inputRect = dateInput.getBoundingClientRect();
  calendar.style.top = 70 + "px"
  calendar.style.left = 484 + "px";
}

window.addEventListener("resize", positionCalendar);

renderCalendar();

function mostrarMenu(boton) {

       if (displayValue === 'none') {

            console.log('display:none');
            menu.style.display = 'grid';

           var rect = boton.getBoundingClientRect();
           menu.style.left = 1150.05 + 'px';
           menu.style.top = rect.bottom + 'px';

      }

       var tokenId = boton.getAttribute('data-tokenid');
       console.log('Valor del token.id:', tokenId);
       document.getElementById('tokenid').value = tokenId;

}

function ocultarMenu() {
   var menu = document.getElementById('menu');
   menu.style.display = 'none';
}


window.onclick = function(event) {

    if (!event.target.matches('.buttonCopy') && !event.target.closest('.buttonCopy') && !event.target.matches('.copy') && !event.target.closest('.copy')) {

        ocultarMenu();
    }
}










