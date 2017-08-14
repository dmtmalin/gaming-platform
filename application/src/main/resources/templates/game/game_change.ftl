<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<div class="form__main__title">Изменить приложение</div>
<form class="form__body" action="" method="post" role="form" enctype="multipart/form-data">
  <div class="form__group row">
    <div class="col-md-3 col-md-push-7"><div class="form__child form__child_two-column clearfix">
      <div class="form__child">
        <div class="labels">
          <label>Иконка</label>
        </div>
        <div class="controls">
          <div id="iconPreview" class="jumbotron file-preview-frame">
            <p>	<img class="JS-preview-image" src="<@spring.url mediaUrl + gameChangeForm.iconUrl/>"></p>
          </div>
          <@spring.formInput "gameChangeForm.icon", "class=\"field__type JS-preview-file\" style=\"display: none\"", "file" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
          <label for="icon" class="btn btn-default btn__preview">
            Загрузить изображение
          </label>
          <br>
        </div>
      </div>
    </div>
  </div>
  <div class="col-md-7 col-md-pull-3">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <@spring.formInput "gameChangeForm.id", "", "hidden" />
    <@spring.formInput "gameChangeForm.iconUrl", "", "hidden" />
    <div class="form__child form__child_two-column clearfix">
      <div class="form__child">
        <div class="labels required">
          <label>Имя</label>
        </div>
        <div class="controls">
          <@spring.formInput "gameChangeForm.name", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
    </div>
    <div class="form__child form__child_two-column clearfix">
      <div class="form__child">
        <div class="labels required">
          <label>Описание</label>
        </div>
        <div class="controls">
          <@spring.formTextarea "gameChangeForm.description", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
    </div>
    <div class="form__child form__child_two-column clearfix">
      <div class="form__child">
        <div class="labels required">
          <label>Категория</label>
        </div>
        <div class="controls">
          <@spring.formSingleSelect "gameChangeForm.categoryId", categories, "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
    </div>
    <div class="form__child form__child_two-column clearfix">
      <div class="form__child">
        <div class="labels">
          <label>Текущий билд</label>
        </div>
        <div class="controls">
          <@spring.formSingleSelect "gameChangeForm.currentGameVersionId", versions, "class=\"field__type\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
          </div>
        </div>
      </div>
    <@spring.bind "gameChangeForm" />
    <#if spring.status.error>
      <div class="form__child">
        <#list spring.status.errorMessages as error>
          <div class="error__block">${error}</div>
        </#list>
      </div>
    </#if>
    <#if game_change_success??>
      <div class="form__child JS-close-content">
        <div class="success__block">
          ${flatBlock("game_change_success")}
          <span class="success__close JS-close"></span>
        </div>
      </div>
    </#if>
    </div>
  </div>
  <div class="form__button__group">
    <button type="submit" class="btn__type btn__type_save">Сохранить изменения</button>
  </div>
</form>
</#macro>
<@display_page />
