<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<h1 class="pull-right">
  <a class="btn btn-success" href="<@spring.url "/change_password"/>">Изменить пароль</a></h1>
<div style="clear: both"></div>
<div class="form__main__title">Данные профиля</div>
<form class="form__body" action="" method="post" role="form" enctype="multipart/form-data">
  <div class="form__group row">
    <div class="col-md-3 col-md-push-7">
      <div class="form__child form__child_two-column clearfix">
        <div class="form__child">
          <div class="labels">
            <label>Аватар</label>
          </div>
          <div class="controls">
            <#if userChangeForm.avatarUrl??>
              <#assign avatarUrl = mediaUrl + userChangeForm.avatarUrl />
            <#else>
              <#assign avatarUrl = "/img/not-avatar.png" />
            </#if>
            <div id="iconPreview" class="jumbotron file-preview-frame">
              <p>	<img class="JS-preview-image" src="<@spring.url avatarUrl />"></p>
            </div>
            <@spring.formInput "userChangeForm.avatar", "class=\"field__type JS-preview-file\" style=\"display: none\"", "file" />
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
            <label for="avatar" class="btn btn-default btn__preview">
              Загрузить изображение
            </label>
            <br>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-7 col-md-pull-3">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <@spring.formInput "userChangeForm.avatarUrl", "", "hidden" />
      <div class="form__child form__child_two-column clearfix">
        <div class="form__child">
          <div class="labels required">
            <label>Отображаемое имя или наименование организации</label>
          </div>
          <div class="controls">
            <@spring.formInput "userChangeForm.fullName", "class=\"field__type\" required=\"required\"" />
            <#list spring.status.errorMessages as error>
              <div class="error__block">${error}</div>
            </#list>
          </div>
        </div>
      </div>
      <@spring.bind "userChangeForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
      <#if user_change_success??>
        <div class="form__child JS-close-content">
          <div class="success__block">
          ${flatBlock("user_change_success")}
            <span class="success__close JS-close"></span>
          </div>
        </div>
      </#if>
    </div>
  </div>
  <div class="form__button__group row">
    <button type="submit" class="btn__type btn__type_save">Сохранить изменения</button>
  </div>
</form>
</#macro>
<@display_page />