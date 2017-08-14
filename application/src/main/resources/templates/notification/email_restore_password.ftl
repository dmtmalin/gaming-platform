<#include "email_base.ftl">
<#macro title>
  Восстановление пароля
</#macro>

<#macro email_body>
<div>
  <p>Для восстановления пароля проследуйте по <a href="${restorePasswordUrl}">ссылке</a>.</p>
</div>
</#macro>
<@display_page />
