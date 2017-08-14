<#import "/spring.ftl" as spring>
<#include "../base.ftl">

<#macro page_body>
<div class="login">
  <div class="login__wrap">
    <div class="login__title">&laquo;Игровая платформа&raquo;
      <p>автоматизированная <br> информационная система</p>
    </div>
    <#if error??>
    <div class="login_error" style="display:block;">
      <p>Неверный логин или пароль.</p>
      <p>Пожалуйста, проверьте правильность написания пароля. Возможно, нажата клавиша CAPS-lock?</p>
    </div>
    </#if>
    <#if logout??>
    <div class="login_error" style="display:block;">
      <p>Вы вышли из системы.</p>
    </div>
    </#if>
    <div class="login__form">
      <div class="form__title clearfix">
        <h3>Войти</h3>
        <a class="type__link form__title__link js-show-help-window" href="<@spring.url "/registration"/>" >
          <span>Регистрация</span>
        </a>
      </div>
      <form action="<@spring.url "/login"/>" method="post" role="form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form__item">
          <div class="labels">
            <label for="login">Логин</label>
          </div>
          <div class="controls controls_icon-eye">
            <input class="field__type type__login" type="email" id="email" name="email" required="required">
          </div>
        </div>
        <div class="form__item">
          <div class="labels">
            <label for="password">Пароль</label>
            <a class="type__link form__reset__pass" href="<@spring.url "/restore_password"/>">Забыли пароль?</a>
          </div>
          <div class="controls controls_button-eye">
            <input class="field__type JS-toggle-icon-input" type="password" id="password" name="password" required="required">
            <a class="toggle-icon-eye JS-toggle-icon" data-tooltip="Показывает/скрывает введенный пароль">
              <i class="icon icon-eye-close-active"></i>
            </a>
          </div>
        </div>
        <div class="submit__block">
          <input class="btn__type" type="submit" value="Войти">
        </div>
      </form>
    </div>
  </div>
</div>
</#macro>
<@display_page/>
