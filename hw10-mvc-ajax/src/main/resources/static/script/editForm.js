// Добавление/редактирование строки. Универсальная функция для всех страниц редактирования сущностей (авторы, жанры, книги).
// Только с комментариями не прокатило, там отдельный код
function save(editForm, id) {
    const formValuesArray = $(editForm).serializeArray();
    let postedObject = {};
    $.map(formValuesArray, function(formValueObject){
        const controlName = formValueObject['name'];
        const controlValue = formValueObject['value'];
        if (document.getElementsByName(controlName)[0].tagName === 'SELECT') {
            postedObject[controlName] = {"id": controlValue};
        }
        else {
            postedObject[controlName] = controlValue;
        }
    });

    if (id) {
        postedObject["id"] = id;
    }

    $.ajax({
        url: editForm.attributes["data-save-url"].value,
        method: 'post',
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(postedObject),
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
                        const lblValidator = $("#label-validator-" + error.fieldName);
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