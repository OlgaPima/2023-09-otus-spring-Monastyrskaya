function deleteWithConfirm(sender) {
    var rowId = sender.attributes['data-row-id'].value;
    if (confirm("Удалить строку с id=" + rowId + "?")) {
        location.href = sender.attributes['href'].value;
    }
}
