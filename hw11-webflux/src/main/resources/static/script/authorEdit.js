// Загрузка свойств на редактирование
window.onload = function() {
    const id = new URLSearchParams(window.location.search).get("id");

    if (id) { //Редактирование автора
        fetch(`/api/v1/authors/${id}`)
            .then(response => response.json())
            .then(author => {
                $("#label-rowId").text(author.id);
                $("#input-fullName").val(author.fullName);
                $("#input-birthYear").val(author.birthYear);
            });

    }
    else {
        var actionHeader = $("#action-header");
        actionHeader.text( actionHeader[0].attributes["data-add-text"].value );
        $("#tr-rowId").hide();
    }
}
