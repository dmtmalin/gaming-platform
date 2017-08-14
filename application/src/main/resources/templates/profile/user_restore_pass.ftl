<#import "/spring.ftl" as spring>
<#include "../base.ftl">
<#macro page_body>
<div class="form__remember_pass">
  <div class="form__main__title">Восстановление пароля
    <p>Укажите Ваш e-mail, на этот адрес будет отправлена ссылка для восстановления пароля</p>
  </div>
  <form class="form__body" action="" method="post" role="form">
    <div class="form__group">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="form__child">
        <div class="labels required">
          <label>Адрес эл. почты</label>
        </div>
        <div class="controls">
          <@spring.formInput "userRestorePasswordForm.email", "class=\"field__type\" required=\"required\"", "email" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <@spring.bind "userRestorePasswordForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
    </div>
    <div class="form__button__group">
      <button class="btn__type btn__type_save">Восстановить</button>
    </div>
  </form>
</div>
</#macro>
<@display_page/>