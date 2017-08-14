<#import "/spring.ftl" as spring>
<#macro page_head>
</#macro>

<#macro page_title>
</#macro>

<#macro page_body>
</#macro>

<#macro display_page>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title><@page_title /></title>
  <link rel="stylesheet" href="<@spring.url "/minify/combined.min.css"/>">
  <script type="text/javascript" src="<@spring.url "/minify/combined.min.js"/>"></script>
  <@page_head />
</head>
<body>
  <@page_body />
</body>
</html>
</#macro>