<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <tiles:insertAttribute name="head"></tiles:insertAttribute>
</head>
<script type="text/javascript">
    function test(){
        $.ajax({
            url : 'searchs?query=최소',
            type : "GET",
            dataType : 'json'
        }).success(function(data) {
            console.log(data);
        });
    }
</script>
<body>

<input type="button"  onclick="test()"/>

</body>
</html>
