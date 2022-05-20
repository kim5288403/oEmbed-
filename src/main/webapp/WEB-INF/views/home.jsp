<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="resources/css/home.css" type="text/css" />
  <script type="text/javascript" src="resources/js/home.js"></script>
  <title>Oembed Test Site</title>
</head>

  <body>
    <div class="container">
      <header>
        <div id="title">oEmbed Test</div>
        
        <div id="input_Box">
          <input type="text" placeholder="url을 입력해주세요" id="urlText"/>
          <input type="button" value="확인" onclick="requestUrl()"></input>
        </div>
      </header>
      <article>
        <div id="content_wrap">
          <div id="title_container"></div>
          <button type="button" onclick="foo()">확인</button>
          <ul id="responseDataInContainer"></ul>
        </div>
      </article>
    </div>

    <!-- /container -->
  </body>
</html>