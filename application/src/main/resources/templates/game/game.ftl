<#import "/spring.ftl" as spring>
<#include "../main.ftl">
<#macro page_content>
<h1 class="pull-left">${game.name}
  <a class="small pen" href="<@spring.url "/game/change/" + game.id/>"><img src="<@spring.url "/img/pen.png"/>"></a>
  <small>
    <div class="game-description__block">${game.description}</div>
  </small>
</h1>
<h1 class="pull-right">
  <a class="btn btn-success" href="<@spring.url "/game/" + game.id + "/version/create"/>">+ Добавить билд</a></h1>
<div style="clear: both"></div>
<#--
<div class="game-card__block">
  <p><b>
    <#if game.published && game.currentGameVersion??>
      Опубликовано
    <#else>
      Не опубликовано
    </#if>
  </b></p>
  <p>Категория ${game.gameCategory.name}</p>
  <img src="<@spring.url mediaUrl + game.icon />">
  <#if game.currentGameVersion??>
    <br><br>
    <p>Текущий билд ${game.currentGameVersion.build}</p>
  </#if>
</div>
<a class="JS-show-hide">Показать описание</a>
<br>
<div class="game-description__block JS-show-hide-content">
  <p>${game.description}</p>
</div>
-->
<#if game.gameVersions?? && game.gameVersions?size gt 0 >
<div class="table-responsive">
  <div class="table__wrap">
    <div class="header__block">
      <div class="row__item row__item_head clearfix">
        <div class="col__item col__item_head col__width__default">Билд</div>
        <div class="col__item col__item_head col__width__default">Статус публикации</div>
        <div class="col__item col__item_head col__width__small">Что нового</div>
        <div class="col__item col__item_head col__width__default">Превью</div>
      </div>
    </div>
    <#list game.gameVersions as version>
    <div class="table__item table__item_one-row has__border">
      <div class="table__item__body">
        <div class="row__item clearfix">
          <div class="col__item col__width__default">
            <div class="curator-teachers__item">Билд ${version.build}
              <span>загружен ${version.createdAt?string["dd.MM.yyyy, HH:mm"]}</span>
            </div>
          </div>
          <div class="col__item col__width__default">
            <#if version.approve??>
              <#if version.approve>
                <p>Одобрен</p>
              <#else>
                <p>Отклонен причина ${version.reason}</p>
              </#if>
            <#else>
              <p>Модерация</p>
            </#if>
          </div>
          <div class="col__item col__width__small">
            <p>${version.whatNew}</p>
          </div>
          <div class="col__item col__width__default">
            <form method="post" class="JS-preview-game-form">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <input type="hidden" name="uri" value="<@spring.url mediaUrl + version.uri/>"/>
              <button type="button" class="btn btn-primary">Запустить</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    </#list>
  </div>
</div>
<#else>
${flatBlock("user_no_game_versions")}
</#if>
<div class="JS-preview-game"></div>
</#macro>
<@display_page />
