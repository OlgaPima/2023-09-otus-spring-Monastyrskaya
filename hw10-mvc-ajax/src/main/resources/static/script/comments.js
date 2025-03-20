window.onload = function() {
    $("#edit-form")[0].setAttribute("onsubmit", "saveComment(this, null); return false;");
}

function editComment(sender) {
    const form = $("#edit-form")[0];
    const commentId = sender.attributes['data-row-id'].value;

    // Переключаем форму в режим редактирования существующего комментария
    form.setAttribute("onsubmit", "saveComment(this, " + commentId + "); return false;");
    $("#save-comment-label").text("Редактирование отзыва #" + commentId + ":");
    $("#textarea-comment-text").val( $("#td-comment-text-" + commentId).text() );
}

function saveComment(editForm, commentId) {
    var jsonData = {};
    if (commentId) {
        jsonData["id"] = commentId;
    }
    jsonData["commentText"] = $("#textarea-comment-text").val();

    $.ajax({
        url: '/api/v1/books/comments?bookId=' + bookId, // переменная bookId определяется в файле bookComments.html
        method: 'post',
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(jsonData),
        // Этот блок сработает при каких-то сетевых ошибках, если вызов не дошел до контроллера
        error: function(responseData) {
            alert("Ошибка сохранения"); // TODO: отображать текст ошибки
            return false;
        },
        success: function(responseData) { // Сюда прилетят результаты из контроллера: как успешные сохранения, так и с ошибками
            if (responseData.status === "success") {
                refreshGridAndSwitchToAddMode();
                return false;
            }
            else { // "error" - отображаем сообщения об ошибках
                $("#label-validator-commentText").hide();
                $("#label-server-error").hide();
                responseData.errors.forEach(error => {
                    if (error.fieldName) { // ошибка валидации поля - отображаем валидатор и передаем в него текст
                        var lblValidator = $("#label-validator-" + error.fieldName);
                        lblValidator.text(error.errorMessage);
                        lblValidator.show();
                    }
                    else { // какая-то другая серверная ошибка сохранения (например, НЛО прилетело...)
                        $("#label-server-error").show();                    }
                });
                return false;
            }
        }
    });
}

function refreshGridAndSwitchToAddMode() {
    // очищаем грид
    const tbody = $("#comments-tbody")[0];
    $.each( $("tr[id^='tr-']"), function() {
        tbody.removeChild(this);
    });

    //освежаем список отзывов, перечитывая его с сервера
    refreshGrid();

    // (либо в случае редактирования можно проще - просто скорректировать содержимое ячейки td джаваскриптом без обращения на сервер,
    // мы же знаем, что все успешно сохранилось). Но в случае добавления коммента так не прокатит - придется тут
    // дублировать верстку из bookComments.html для создания новой строки tr. Либо как-то еще выкручиваться, чтобы она не дублировалась.
    // Поэтому просто всегда обращаемся на сервер во избежание лишних извращений

    // переключаем форму в режим добавления нового комментария
    $("#save-comment-label").text("Добавьте новый отзыв:");
    $("#textarea-comment-text").val("");
    $("#edit-form")[0].setAttribute("onsubmit", "saveComment(this, null); return false;");
}
