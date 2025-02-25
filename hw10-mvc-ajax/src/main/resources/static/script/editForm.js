// Добавление/редактирование строки. Универсальная функция для всех страниц редактирования сущностей (авторы, жанры, книги).
// Только с комментариями не прокатило, там отдельный код
function save(editForm, id) {
    var formArray = $(editForm).serializeArray();
    var jsonData = {};
    $.map(formArray, function(i){
        jsonData[i['name']] = i['value'];
    });

    if (id) {
        jsonData["id"] = id;
    }

    $.ajax({
        url: editForm.attributes["data-save-url"].value,
        method: 'post',
        dataType: 'json',
        data: jsonData,
        // Этот блок сработает при каких-то сетевых ошибках, если вызов не дошел до контроллера
        error: function(data) {
            alert("Ошибка сохранения"); // TODO: отображать текст ошибки
            return false;
        },
        success: function(data) { // Сюда прилетят результаты из контроллера: как успешные сохранения, так и с ошибками
            if (data.status === "success") { // возврат на страницу списка
                const returnAttr = editForm.attributes["data-return-url"];
                if (returnAttr) {
                    window.location.href = returnAttr.value;
                }
                return true;
            }
            else { // "error" - отображаем сообщения об ошибках
                hideAllValidators();
                $("#label-server-error").hide();
                data.errors.forEach(error => {
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

function hideAllValidators() {
    $.each( $("label[id^='label-validator-']"), function() {
        $(this).hide();
    });
}