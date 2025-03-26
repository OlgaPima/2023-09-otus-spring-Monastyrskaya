fetch('/static/components/btnAdd.html')
    .then(res => res.text())
    .then(text => {
        let btnAddPlaceholder = document.getElementById("btnAdd-placeholder");
        btnAddPlaceholder.innerHTML = text;
    })

var prevErrorPanel;

function deleteWithConfirm(sender) {
    const rowId = sender.attributes['data-row-id'].value;

    if (confirm("Удалить строку с id=" + rowId + "?")) {
        const deleteUrl = sender.attributes['href'].value;

        $.ajax({
            type: 'DELETE',
            url: deleteUrl,
            success: (deleteIsOk) => {
                if (deleteIsOk === true || deleteIsOk === "") {
                    window.location.href = sender.attributes['redirect-href'].value;
                } else {
                    window.location.href = sender.attributes['redirect-href'].value + "?cannotDelId=" + rowId;
                }
            }
        })
    }
}