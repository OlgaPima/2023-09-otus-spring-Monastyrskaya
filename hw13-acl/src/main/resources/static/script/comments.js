function editComment(sender) {
    var form = document.getElementById("edit-form");
    var rowId = sender.attributes['data-row-id'].value;
    var bookId = document.getElementById("btn-save").attributes["data-book-id"].value;

    form.setAttribute("action", "/books/comments/edit?bookId=" + bookId + "&commentId=" + rowId);

    document.getElementById("save-comment-label").innerText = "Редактирование отзыва #" + rowId + ":";
    document.getElementById("textarea-comment-text").textContent =
        document.getElementById("td-comment-text-" + rowId).innerText;
}