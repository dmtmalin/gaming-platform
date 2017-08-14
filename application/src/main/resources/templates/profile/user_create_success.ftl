<#import "/spring.ftl" as spring>
<#include "../base.ftl">
<#macro page_body>
<div class="form__popup">
  <div class="message__block">
    <div class="message__title">
      <h1>Регистрация</h1>
    </div>
    <div class="message__content">
      ${flatBlock("user_create_success")}
    </div>
    <div class="message__buttons">
      <form action="<@spring.url "/" />" method="get">
        <button type="submit" class="btn__type btn__type_save" >На главную</button>
      </form>
    </div>
  </div>
</div>
</#macro>
<@display_page />