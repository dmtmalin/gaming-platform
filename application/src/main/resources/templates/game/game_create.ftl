<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<div class="col-md-7">
  <form id="game-create-form" action="" method="post" role="form" enctype="multipart/form-data">
    <div class="form__group row">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <div class="form__child">
        <div class="labels required">
          <label>Имя приложения</label>
        </div>
        <div class="controls">
          <@spring.formInput "gameCreateForm.name", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child">
        <div class="labels required">
          <label>Иконка</label>
        </div>
        <div class="controls">
          <@spring.formInput "gameCreateForm.icon", "class=\"field__type JS-file-input\" style=\"display: none\"", "file" />
          <label for="icon" class="btn btn-default btn__preview JS-file-input-show">
            Выбрать файл
          </label>
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child">
        <div class="labels required">
          <label>Описание</label>
        </div>
        <div class="controls">
          <@spring.formTextarea "gameCreateForm.description", "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <div class="form__child">
        <div class="labels required">
          <label>Категория</label>
        </div>
        <div class="controls">
          <@spring.formSingleSelect "gameCreateForm.categoryId", categories, "class=\"field__type\" required=\"required\"" />
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </div>
      <@spring.bind "gameCreateForm" />
      <#if spring.status.error>
        <div class="form__child">
          <#list spring.status.errorMessages as error>
            <div class="error__block">${error}</div>
          </#list>
        </div>
      </#if>
    </div>
    <div class="form__button__group row">
      <button type="submit" class="btn__type btn__type_save">Сохранить изменения</button>
    </div>
  </form>
</div>
</#macro>
<@display_page/>
