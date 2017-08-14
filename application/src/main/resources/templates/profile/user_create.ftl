<#import "/spring.ftl" as spring>
<#include "../base.ftl">
<#macro page_body>
<div class="form__add__new__user">
  <div class="form__main__title">Регистрация
    <p>Звездочкой обозначены обязательные для заполнения поля</p>
  </div>
  <form class="form__body" action="" method="post" role="form">
    <div class="form__group">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="form__group__title">Персональные данные</div>
      <div class="form__child">
        <div class="labels required">
          <label>Отображаемое имя или наименование организации</label>
        </div>
        <div class="controls">
          <@spring.formInput "userCreateForm.fullName", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child">
        <div class="form__child">
          <div class="labels required">
            <label>Адрес эл. почты</label>
          </div>
          <div class="controls">
            <@spring.formInput "userCreateForm.email", "class=\"field__type\" required=\"required\"", "email" />
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
          </div>
        </div>
      </div>
      <div class="form__child form__child_two-column clearfix">
        <div class="form__child">
          <div class="labels required">
            <label for="password">Пароль</label>
          </div>
          <div class="controls controls_button-eye">
            <@spring.formPasswordInput "userCreateForm.password", "class=\"field__type JS-toggle-icon-input\" required=\"required\"" />
            <a class="toggle-icon-eye JS-toggle-icon" data-tooltip="Показывает/скрывает введенный пароль">
              <i class="icon icon-eye-close-active"></i>
            </a>
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
          </div>
        </div>
        <div class="form__child">
          <div class="labels required">
            <label for="password_2">Повторите пароль</label>
          </div>
          <div class="controls controls_button-eye">
            <@spring.formPasswordInput "userCreateForm.passwordRepeated", "class=\"field__type JS-toggle-icon-input\" required=\"required\"" />
            <a class="toggle-icon-eye JS-toggle-icon" data-tooltip="Показывает/скрывает введенный пароль">
              <i class="icon icon-eye-close-active"></i>
            </a>
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
          </div>
        </div>
      </div>
      <@spring.bind "userCreateForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
    </div>
    <div class="form__button__group">
      <button type="submit" class="btn__type btn__type_save">Регистрация</button>
    </div>
  </form>
</div>
</#macro>
<@display_page/>
