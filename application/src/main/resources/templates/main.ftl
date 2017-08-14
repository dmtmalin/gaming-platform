<#import "/spring.ftl" as spring>
<#include "base.ftl">

<#macro page_content>
</#macro>

<#macro page_body>
<nav class="navbar navbar-fixed-top">
  <div class="container-fluid">
    <button type="button" class="navbar-toggle collapsed pull-left" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="logo navbar-brand" href="#">Игровая платформа</a>
    <#if user??>
    <ul class="nav navbar-right header__info">
      <li class="header__user">
        <div class="user__avatar"><img src="<#if user.avatar??><@spring.url mediaUrl + user.avatarSmall/><#else><@spring.url "/img/person.jpg" /></#if>"></div>
        <div class="user__information">
          <div class="user__name">${user.fullName}</div>
        </div>
      </li>
      <li class="header__logout">
        <form class="JS-logout-form" action="<@spring.url "/logout" />" method="post" role="form">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <a class="JS-logout-button">
          <i class="icon icon-exit-default"></i>
        </a>
      </li>
    </ul>
    </#if>
  </div>
</nav>
<div class="content__layout">
  <div id="navbar" class="col-md-3 col-sm-4  col-xs-12 sidebar">
    <div class="menu">
      <ul class="menu__items">
        <li class="menu__item"><a class="menu__link" href="<@spring.url "/change" />"><i class="icon icon-teacher-default"></i><i class="icon icon_hover icon-teacher-hover"></i>Профиль</a></li>
        <li class="menu__item"><a class="menu__link" href="<@spring.url "/" />"><i class="icon icon-pupil-default"></i><i class="icon icon_hover icon-pupil-hover"></i>Мои приложения</a></li>
      </ul>
      <div class="menu__footer">
        <div class="menu__footer__info">
          <i class="icon icon-info-1"></i>${flatBlock("version")}
        </div>
      </div>
    </div>
  </div>
  <div class="col-md-9 col-md-offset-3 col-sm-8 col-sm-offset-4  main ">
    <@page_content />
  </div>
</div>
</#macro>