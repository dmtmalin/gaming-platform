$(document).ready(function() {
    initShowHidePassword();
});

function initShowHidePassword() {
    $('.JS-toggle-icon').click(function () {
        var input = $(this).siblings("input.JS-toggle-icon-input");
        var icon = $(this).find("i.icon");
        if(input.attr('type') === 'text') {
            input.prop('type', 'password');
            icon.removeClass("icon-eye-open-default");
            icon.addClass("icon-eye-close-active");
        }
        else {
            input.prop('type', 'text');
            icon.removeClass("icon-eye-close-active");
            icon.addClass("icon-eye-open-default");
        }
    });
}
