window.onload = setTimeout(loadMenu, 100); //без таймаута менюшка иногда отваливается (похоже, в момент загрузки еще не сформирован DOM)

function loadMenu() {
    fetch('/static/components/menu.html')
        .then(res => res.text())
        .then(text => {
            let menuPlaceholder = document.getElementById("menu-placeholder");
            menuPlaceholder.innerHTML = text;
        });
}