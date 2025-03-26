// Загрузка свойств книги для редактирования
window.onload = function() {
    fillAuthorsList();
    fillGenresList();

    const id = new URLSearchParams(window.location.search).get("id");
    if (id) { //Редактирование книги
        fetch(`/api/v1/books/${id}`)
            .then(response => response.json())
            .then(book => {
                $("#label-rowId").text(book.id);
                $("#input-title").val(book.title);
                $("#select-author").val(book.author.id);
                $("#select-genre").val(book.genre.id);
            });
    }
    else { // создание новой книги
        var actionHeader = $("#action-header");
        actionHeader.text( actionHeader[0].attributes["data-add-text"].value );
        $("#tr-rowId").hide();
    }
}

function fillAuthorsList() {
    fetch(`/api/v1/authors`)
        .then(response => response.json())
        .then(authorsList => {
            authorsList.forEach(author => {
                $("#select-author").append(
                    $('<option>', {
                    value: author.id,
                    text: author.fullName
                }));
            });
        });
}

function fillGenresList() {
    fetch(`/api/v1/genres`)
        .then(response => response.json())
        .then(genresList => {
            genresList.forEach(genre => {
                $("#select-genre").append(
                    $('<option>', {
                        value: genre.id,
                        text: genre.name
                    }));
            });
        });
}