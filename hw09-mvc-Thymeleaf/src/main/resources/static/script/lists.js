fetch('/static/components/btnAdd.html')
    .then(res => res.text())
    .then(text => {
        let btnAddPlaceholder = document.getElementById("btnAdd-placeholder");
        btnAddPlaceholder.innerHTML = text;
    })

function deleteWithConfirm(sender) {
    var rowId = sender.attributes['data-row-id'].value;
    if (confirm("Удалить строку с id=" + rowId + "?")) {
        location.href = sender.attributes['href'].value;
    }
}
