<#include "email_base.ftl">
<#macro title>
  Активация учетной записи
</#macro>

<#macro email_body>
<div>
  <p>Для активации аккаунта проследуйте по <a href="${activationUrl}">ссылке</a>.</p>
</div>
</#macro>
<@display_page />
