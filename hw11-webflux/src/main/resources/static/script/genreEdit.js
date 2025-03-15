// Загрузка свойств на редактирование
window.onload = function() {
    const id = new URLSearchParams(window.location.search).get("id");

    if (id) { //Редактирование жанра
        fetch(`/api/v1/genres/${id}`)
            .then(response => response.json())
            .then(genre => {
                $("#label-rowId").text(genre.id);
                $("#input-name").val(genre.name);
            });

    }
    else {
        var actionHeader = $("#action-header");
        actionHeader.text( actionHeader[0].attributes["data-add-text"].value );
        $("#tr-rowId").hide();
    }
}
