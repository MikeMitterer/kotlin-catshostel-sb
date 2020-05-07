<#-- @ftlvariable name="data" type="at.mikemitterer.catshostel.static.IndexData" -->
<html>
    <body>
        <ul>
        <#list data.items as item>
            <li>${item}</li>
        </#list>
        </ul>
    </body>
</html>
