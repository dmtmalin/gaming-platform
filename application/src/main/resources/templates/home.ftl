<#import "/spring.ftl" as spring>
<#include "main.ftl">

<#macro page_content>
<h1 class="pull-left">Мои приложения</h1>
<h1 class="pull-right">
  <a class="btn btn-success" href="<@spring.url "/game/create" />">+ Новое приложение</a>
</h1>
<div style="clear: both"></div>
<#if games?size gt 0 >
<div class="table__wrap">
  <div class="header__block">
    <div class="row__item row__item_head clearfix">
      <div class="col__item col__item_head col__width__default">Приложение</div>
      <div class="col__item col__item_head col__width__default">Иконка</div>
      <div class="col__item col__item_head col__width__small">Опубликовано</div>
      <div class="col__item col__item_head col__width__default">Текущий билд</div>
    </div>
  </div>
  <#list games as game>
  <div class="table__item table__item_one-row has__border">
    <div class="table__item__body">
      <div class="row__item clearfix JS-game-open-click">
        <div class="col__item col__width__default">
          <div class="curator-teachers__item">${game.name}
            <span>
            <#if game.description?length gt 30>
              ${game.description[0..<30]}...
            <#else>
              ${game.description}
            </#if>
            </span>
          </div>
        </div>
        <div class="col__item col__width__default">
          <img src="<@spring.url mediaUrl + game.icon />"/>
        </div>
        <div class="col__item col__width__small">
        <#if game.published && game.currentGameVersion??>
          <i class="icon icon-green-round"></i>Да
        <#else>
          <i class="icon icon-red-round"></i>Нет
        </#if>
        </div>
        <div class="col__item col__width__default">
        <#if game.currentGameVersion??>
          <div class="curator-teachers__item">
            <p>Билд ${game.currentGameVersion.build} загружен ${game.currentGameVersion.createdAt?string["dd.MM.yyyy, HH:mm"]}</p>
            <span>
              Что нового: ${game.currentGameVersion.whatNew}
            </span>
          </div>
        <#else>
          <p>Нет</p>
        </#if>
        </div>
        <form class="JS-game-open-form" method="get" action="<@spring.url "/game/" + game.id/>"></form>
      </div>
    </div>
  </div>
  </#list>
</div>
<#else>
${flatBlock("user_no_games")}
</#if>
</#macro>
<@display_page/>
