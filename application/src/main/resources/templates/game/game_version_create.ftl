<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<div class="col-md-7">
  <form class="form__body" action="" method="post" role="form" enctype="multipart/form-data">
    <div class="form__group row">
      <div class="note__block">
        <p>Добавте в zip-архив Вашу html5 игру и загрузите еe. Архив может содержать только html, js, css и файлы мультимедиа.</p>
        <p>Либо Вы можете указать ссылку на внешний ресурс с приложением.</p>
        <@spring.bind "gameVersionCreateForm.uri" />
        <p><input type="checkbox" class="JS-show-hide-input" ${spring.status.value?has_content?then("checked", "")} /> <b>Разместить на внешнем ресурсе.</b></p>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <@spring.formInput "gameVersionCreateForm.game", "", "hidden" />
      <div class="form__child JS-hide-on-checked">
        <div class="labels">
          <label>Архив</label>
        </div>
        <div class="controls">
          <@spring.formInput "gameVersionCreateForm.archive", "class=\"field__type JS-file-input\" style=\"display: none\"", "file" />
          <label for="archive" class="btn btn-default btn__preview JS-file-input-show">
            Выбрать файл
          </label>
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child JS-show-on-checked">
        <div class="labels">
          <label>Ссылка на приложение</label>
        </div>
        <div class="controls">
          <@spring.formInput "gameVersionCreateForm.uri", "class=\"field__type\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child">
        <div class="labels required">
          <label>Что нового</label>
        </div>
        <div class="controls">
          <@spring.formTextarea "gameVersionCreateForm.whatNew", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <@spring.bind "gameVersionCreateForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
    </div>
    <div class="form__button__group row">
      <input type="submit" class="btn__type btn__type_save" value="Добавить">
    </div>
  </form>
</div>
</#macro>
<@display_page />