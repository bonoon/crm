<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<title>CRM index</title>

<!-- Bootstrap -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet">
<style>
.test {
	border: 1px red solid;
}

bady div {
	margin: 0px;
	padding: 0px;
}

.div_weather {
	position: absolute;
	float: right;
	right: 20px;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

@media screen and (max-width:910px) and (min-width:480px) {
}

@media screen and (max-width:480px) {
	.div_weather {
		float: none;
		width: 116px;
		top: 20px;
		left: 50%;
		transform: translate(-50%, -50%);
	}
}
</style>
</head>
<body>
	<!-- 天气组件 -->
	<div class="div_weather ">
		<iframe name="weather_inc"
			src="http://i.tianqi.com/index.php?c=code&id=99" width="130"
			height="36" frameborder="0" marginwidth="0" marginheight="0"
			scrolling="no" disabled="disabled"></iframe>
		<div
			style="position: absolute; top: 0px; background: #6f5499; FILTER: alpha(opacity =   0); -moz-opacity: 0; opacity: 0; width: 100%; height: 100%; margin: 0px;"
			alt="遮罩层"></div>
	</div>

	<div class="test" style="width: 100%; height: 100px">
		<div class="jumbotron">
			<h1>Hello, world!</h1>
			<p>This is a simple hero unit, a simple jumbotron-style component for calling extra attention to featured content or information.</p>
			<p>
				<a class="btn btn-primary btn-lg" href="#" role="button">Learn
					more</a>
			</p>
		</div>
	</div>
</body>

<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.js"></script>

<script type="text/javascript">
	
</script>
</html>