var prevErrorPanel;

// Универсальная функция удаления строки, применяется на всех страницах-списках (авторы, жанры, книги, комментарии)
function deleteWithConfirm(sender) {
    const rowId = sender.attributes['data-row-id'].value;

    if (confirm("Удалить строку с id=" + rowId + "?")) {
        const deleteUrl = sender.attributes['href'].value;

        $.ajax({
            type: 'DELETE',
            url: deleteUrl,
            success: (response) => {
                if (response === "" || response.success) {
                    const deletedTr = document.getElementById("tr-" + rowId);
                    deletedTr.parentNode.removeChild(deletedTr);
                }
                else {
                    // Если были предыдущие неуспешные нажатия на "Удалить", то скрываем ранее отображенную ошибку
                    // (чтобы не висело 10 красных панелей одновременно):
                    if (prevErrorPanel != null) {
                        prevErrorPanel.style.display = "none";
                    }
                    const errorPanel = document.getElementById("errorPanel-" + rowId);
                    if (errorPanel) {
                        errorPanel.style.removeProperty("display"); //показываем панель ошибки, если есть дочерние сущности
                        prevErrorPanel = errorPanel;
                    }
                }
            }
        })
    }
}
