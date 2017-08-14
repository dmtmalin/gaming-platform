<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<div class="form__main__title">Изменение пароля</div>
<div class="col-md-7">
  <form class="form__body" action="" method="post" role="form">
    <div class="form__group row">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="form__child form__child_two-column clearfix">
        <div class="form__child">
          <div class="labels required">
            <label>Пароль</label>
          </div>
          <div class="controls controls_button-eye">
            <@spring.formPasswordInput "userChangePasswordForm.password", "class=\"field__type JS-toggle-icon-input\" required=\"required\"" />
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
            <@spring.formPasswordInput "userChangePasswordForm.passwordRepeated", "class=\"field__type JS-toggle-icon-input\" required=\"required\"" />
            <a class="toggle-icon-eye JS-toggle-icon" data-tooltip="Показывает/скрывает введенный пароль">
              <i class="icon icon-eye-close-active"></i>
            </a>
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
          </div>
        </div>
      </div>
      <@spring.bind "userChangePasswordForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
      <#if user_change_pass_success??>
        <div class="form__child JS-close-content">
          <div class="success__block">
          ${flatBlock("user_change_pass_success")}
            <span class="success__close JS-close"></span>
          </div>
        </div>
      </#if>
    </div>
    <div class="form__button__group row">
      <button type="submit" class="btn__type btn__type_save">Изменить</button>
    </div>
  </form>
</div>
</#macro>
<@display_page/>