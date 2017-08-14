$("document").ready(function(){
    initLogout();
    initOpenGame();
    initShowPreviewGame();
    initBlockClose();
    initShowHideOnChecked();
    initShowFilenameInBlock();
    initShowHideContent();
    initShowPreviewImage();
});

/*
Выход main.ftl
 */
function initLogout() {
    $(".JS-logout-button").click(function () {
        $(".JS-logout-form").submit();
    })
}

/*
Переход на страницу с игрой game.ftl
 */
function initOpenGame() {
    $("form.JS-game-open-form").each(function () {
        var form = $(this);
        form.closest(".JS-game-open-click").click(function () {
            form.submit();
        })
    });
}

/*
Функция показа превью картинки
 */
function initShowPreviewImage() {
    $("input.JS-preview-file").change(function() {
        var formData = new FormData();
        formData.set('image', this.files[0], $(this).val());
        formData.set('_csrf', $('input[name=_csrf]').val());
        var preview = $("img.JS-preview-image");
        $.ajax({
            url: "/preview/image",
            type: "post",
            dataType: 'text',
            contentType: false,
            processData: false,
            cache: false,
            data: formData,
            success: function(img) {
                preview.attr("src","data:image/jpeg;base64," + img);
            },
            error: function () {
                preview.attr("src","/img/not-available.png");
            }
        });
    });
}

/*
Функция для показа превью игры game.ftl
 */
function initShowPreviewGame() {
    $("form.JS-preview-game-form").each(function () {
        var form = $(this);
        $(this).find("button").click(function () {
            var formData = new FormData(form[0]);
            $.ajax({
                url: "/game/preview",
                type: "post",
                dataType: 'text',
                contentType: false,
                processData: false,
                cache: false,
                data: formData,
                success: function (html) {
                    $(".JS-preview-game").append(html);
                    $(".JS-close").click(function () {
                        $(".JS-preview-game").empty();
                    });
                }
            });
        });
    });
}

/*
Функция закрытия блока
 */
function initBlockClose() {
    $(".JS-close").click(function () {
        var block = $(this).closest(".JS-close-content");
        block.empty();
    });
}

/*
Фкнция скрывает/показывает элементы
 */
function initShowHideOnChecked() {
    var isChecked = $(".JS-show-hide-input").attr("checked");
    showHideOnChecked(isChecked);
    clearInputIfElementIsHide(".JS-hide-on-checked");
    clearInputIfElementIsHide(".JS-show-on-checked");

    $(".JS-show-hide-input").click(function () {
        showHideOnChecked(this.checked);
        clearInputIfElementIsHide(".JS-hide-on-checked");
        clearInputIfElementIsHide(".JS-show-on-checked");
    });
}

function showHideOnChecked(isChecked) {
    if (isChecked) {
        $(".JS-hide-on-checked").hide();
        $(".JS-show-on-checked").show();
    }
    else {
        $(".JS-hide-on-checked").show();
        $(".JS-show-on-checked").hide();
    }
}

/*
Функция очищает input если элемент скрыт
 */
function clearInputIfElementIsHide(element) {
    var isVisible = $(element).is(":visible");
    if (!isVisible) {
        $(element).find("input").each(function () {
           $(this).val("");
        });
    }
}

/*
Функция показывает имя выбранного файла
 */
function initShowFilenameInBlock() {
    $("input.JS-file-input").change(function () {
        var filename = $(this).val();
        $(".JS-file-input-show").html(filename);
    });
}

/*
Функция показывает/скрывает котнент
 */
function initShowHideContent() {
    $(".JS-show-hide").click(function () {
        $(".JS-show-hide-content").toggle();
    });
    
}
