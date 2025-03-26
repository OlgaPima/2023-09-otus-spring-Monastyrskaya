fetch('/static/components/menu.html')
    .then(res => res.text())
    .then(text => {
        let menuPlaceholder = document.getElementById("menu-placeholder");
        menuPlaceholder.innerHTML = text;
    })