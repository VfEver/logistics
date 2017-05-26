/**
 * controller module
 */
angular.module('controllers', ['ngCookies'])
.config(function ($httpProvider) {
	$httpProvider.defaults.transformRequest = function(data){
        if (data === undefined) {
            return data;
        }
        return $.param(data);
    }
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] =  'application/x-www-form-urlencoded';
})
.controller('NavOperatorController', ['$scope', '$location', '$cookies', '$http', '$window', function($scope, $location, $cookies, $http, $window) {
	
	//开始状态为不在线
	$scope.online = false;

	$scope.username = $cookies.get("username");
	$scope.job = $cookies.get("job");
	$scope.online = $cookies.get("online");
	//激活状态更改
	$scope.changToTask = function () {
		$("li").removeClass("active");
		$location.path("index");
		$("#taskID").addClass("active");
	}
	
	$scope.changToSettlement = function () {
		$("li").removeClass("active");
		$location.path("settlement");
		$("#settlementID").addClass("active");
	}
	
	$scope.changToOperation = function () {
		$("li").removeClass("active");
		$location.path("operation");
		$("#operationID").addClass("active");
	}
	
	$scope.changToStatistics = function () {
		$("li").removeClass("active");
		$location.path("statistics");
		$("#statisticsID").addClass("active");
	}

	$scope.changToInformation = function () {
		$("li").removeClass("active");
		$location.path("information");
		$("#informationID").addClass("active");
	}
	
	
	//点击注册弹出注册模态框
	$scope.showRegisterModal = function () {
		$("#registerModal").modal("toggle");
	}
	
	//点击登录弹出登录模态框
	$scope.showLoginModal = function () {
		$("#loginModal").modal("toggle");
	}
	
	//两次密码是否一致
	$scope.comfirmPasswordInvalidate = false;
	//密码和确认密码是否一致
	$('.confirmPasswordInput').on('change', function () {
		
		if ($scope.comfirmPassword == undefined) {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = true;
			});
		}
		
		if ($scope.password === $scope.comfirmPassword) {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = false;
			});
		} else {
			$scope.$apply(function () {
				$scope.comfirmPasswordInvalidate = true;
			});
		}
	});
	
	$scope.accountInvalidate = false;//账号合法设置
	$scope.accountUsed = false;//账号是否使用过
	//验证账号是否合法
	$('.accountInput').on('blur', function() {
		if ($scope.account === undefined || $scope.account.trim() === '') {
			return ;
		}
		
		if ($scope.account.trim().length < 6 || $scope.account.trim().length > 20) {
			return ;
		} else {
			$scope.$apply(function () {
				$scope.accountInvalidate  = false;
			});
		}
		
		$http({
			method:'post',  
			url:'/logistics/user/accountused.do',
			data:{
				account: $scope.account
			}
		}).success(function(data){  
			if (data.used) {
				$scope.accountUsed = true;
				$scope.accountInvalidate = true;
			} else {
				$scope.accountUsed = false;
				$scope.accountInvalidate = false;
			}
		})
		
	});
	
	//点击注册发起请求
	$scope.register = function () {
		
		if ($scope.accountInvalidate || $scope.accountUsed) {
			alert("账号或者密码有问题");
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/user/register.do',
		    data:{
		    	account: $scope.account,
		    	password: $scope.password
		    }
		}).success(function(data){
			if (data.status === 200) {
				$scope.online = true;
				$scope.username = data.username;
				$scope.job = data.job;
				
				$cookies.put("username", data.username);
				$cookies.put("job", data.job);
				$cookies.put("userID", data.userID);
				$cookies.put("userType", data.userType);
				$cookies.put("online", true);
			} else {
				aler(data.reason);
			}
		})
	}
	
	//用户登录
	$scope.login = function () {
		
		$http({
		    method:'post', 
		    url:'/logistics/user/login.do',
		    data:{
		    	account: $scope.account,
		    	password: $scope.password
		    }
		}).success(function(data){
			if (data.status === 200) {
				$scope.online = true;
				$scope.username = data.username;
				$scope.job = data.job;
				
				$cookies.put("username", data.username);
				$cookies.put("job", data.job);
				$cookies.put("userID", data.userID);
				$cookies.put("userType", data.userType);
				$cookies.put("online", true);
			} else {
				alert(data.reason);
			}
		})
	}
	
	//退出登陆
	$scope.logout = function () {
		$scope.online = false;
		$cookies.remove("username");
		$cookies.remove("job");
		$cookies.remove("userID");
		$cookies.remove("userType");
		$cookies.remove("online");
	}
	
	//点击个人信息弹出个人信息模态框
	$scope.userInfoModalShow = function () {

		$("#userInfoModal").modal("toggle");
	}
	
	//点击完善个人信息
	$scope.updateUser = function () {
		
		$http({
		    method:'post', 
		    url:'/logistics/user/updateuser.do',
		    data:{
		    	password: $scope.updatePassword,
		    	username: $scope.updateUsername,
		    	IDcard: $scope.updateIDcard,
		    	telephone: $scope.updateTelephone,
		    	userID: $cookies.get("userID")
		    }
		}).success(function(data){
			if (data.status === 200) {
				alert("修改成功");
			} else {
				aler("修改信息出现问题");
			}
		})
	}
}])
.controller('InformationController', ['$scope', '$location', '$cookies', '$http', '$window', 'GetCarrierList', 'GetCarList', 'GetUserList', function($scope, $location, $cookies, $http, $window, GetCarrierList, GetCarList, GetUserList) {
	
	//操作用户权限管理
	function authority() {
		var user_id = $cookies.get("userID");
		if (user_id == undefined) {
			alert("您还未登录，请登录后操作");
			return false;
		}
		var userType = $cookies.get("userType");
		if (userType != "1" && userType != "3") {
			alert("您未有相应的权限，请联系管理员");
			return false; 
		}
		
		return true;
	}
	
	//查询出所有人员信息
	GetUserList.get().then(function(data) {
		$scope.userList = data.userList;
		$scope.userCurIndex = 0;
		$scope.userListNum = Math.ceil($scope.userList.length/5);
		$scope.userBtnNum = [];
		for (var i = 1; i <= $scope.userListNum; ++i) {
			$scope.userBtnNum.push(i);
		}
	});
	//点击进入相应页码界面
	$scope.jumpToUserFn = function (index) {
		$scope.userCurIndex = index;
	}
	
	//查询出所有车辆信息
	GetCarList.get().then(function(data) {
		$scope.carList = data.carList;
		$scope.carCurIndex = 0;
		$scope.carListNum = Math.ceil($scope.carList.length/5);
		$scope.carBtnNum = [];
		for (var i = 1; i <= $scope.carListNum; ++i) {
			$scope.carBtnNum.push(i);
		}
		
	});
	//点击进入相应页码界面
	$scope.jumpToCarFn = function (index) {
		$scope.carCurIndex = index;
	}

	//查询出所有承运商信息
	GetCarrierList.get().then(function(data) {
		$scope.carrierList = data.carrierList;
		$scope.carrierCurIndex = 0;
		$scope.carrierListNum = Math.ceil($scope.userList.length/5);
		$scope.carrierBtnNum = [];
		for (var i = 1; i <= $scope.carrierListNum; ++i) {
			$scope.carrierBtnNum.push(i);
		}
	});
	//点击进入响应页码界面
	$scope.jumpToCarrierFn = function (index) {
		$scope.carrierCurIndex = index;
	}
	
	//点击弹出添加车辆信息模态框
	$scope.carModalShow = function () {
		$("#carModal").modal("toggle");
	}
	
	$scope.selectCarrier = false;
	//点击承运商单选框
	$scope.showCarrier = function () {
		$scope.selectCarrier = true;
	}
	//点击承运商单选框
	$scope.hideCarrier = function () {
		$scope.selectCarrier = false;
	}
	
	//更新人员职业
	$scope.updateUserJob = function (userID, updateJob) {
		
		if (!authority()) {
			return ;
		}
		
		if ($cookies.get("userID") == userID) {
			alert("不能更改自己的职业信息");
			return ;
		}
		
		if ($cookies.get("userID") == 1) {
			alert("不能更改管理员权限");
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/user/updateuser.do',
		    data:{
    			userID: userID,
    			userType: updateJob
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetUserList.get().then(function(data) {
					$scope.userList = data.userList;
				});
				alert("更改职业成功");
			} else {
			}
		})
	}
	
	//添加车辆
	$scope.addCar = function () {
		
		if (!authority()) {
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/car/addcar.do',
		    data:{
		    	carNumber: $scope.carNumber,
		    	carType: $scope.selectedCarType,
		    	capacity: $scope.weight,
		    	belong: $scope.selfCar === undefined? $scope.carrierCar:$scope.selfCar,
    			carrierID: $scope.selectedCarrier === undefined? "-1" : $scope.selectedCarrier.carrierID,
		    	carrierName: $scope.selectedCarrier ===undefined? "" : $scope.selectedCarrier.carrierName
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarList.get().then(function(data) {
					$scope.carList = data.carList;
				});
			} else {
				alert("添加车辆信息失败");
			}
		})
	}
	
	//删除车辆信息
	$scope.deleteCar = function (carID) {
		
		if (!authority()) {
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/car/deletecar.do',
		    data:{
		    	carID: $scope.selectedCar.carID
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarList.get().then(function(data) {
					$scope.carList = data.carList;
				});
			} else {
				alert("删除车辆信息失败");
			}
		})
	}
	
	//点击添加承运商信息模态框
	$scope.carrierModalShow = function () {
		$("#carrierModal").modal("toggle");
	}
	
	//添加承运商信息
	$scope.addCarrier = function () {
		
		if (!authority()) {
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/carrier/addcarrier.do',
		    data:{
		    	carrierName: $scope.carrierName,
		    	carrierTelephone: $scope.carrierTelephone,
		    	carrierAddress: $scope.carrierProvince+$scope.carrierCity+$scope.carrierDistrict+$scope.carrierDetailAddress
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarrierList.get().then(function(data) {
					$scope.carrierList = data.carrierList;
				});
			} else {
				alert("添加承运商失败！");
			}
		})
	}
	
	//点击查看承运商弹出承运商详细信息框
	$scope.checkCarrierInfo = function (carrier) {
		$scope.selectCarrier = carrier;
		$("#carrierInfoModal").modal("toggle");
	}
	
	//根据承运商id删除承运商
	$scope.deleteCarrier = function () {
		
		if (!authority()) {
			return ;
		}
		
		if ($scope.selectCarrier != undefined || $scope.selectCarrier != null) {
			$http({
			    method:'post', 
			    url:'/logistics/carrier/deletecarrier.do',
			    data:{
			    	carrierID: $scope.selectCarrier.carrierID
			    }
			}).success(function(data){
				if (data.status === 200) {
					GetCarrierList.get().then(function(data) {
						$scope.carrierList = data.carrierList;
					});
				} else {
					alert("删除承运商信息失败");
				}
			})
		}
	}
	
	//更新承运商信息
	$scope.updateCarrier = function () {
		
		if (!authority()) {
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/carrier/updatecarrier.do',
		    data:{
		    	carrierID: $scope.selectCarrier.carrierID,
		    	carrierName: $scope.updateCarrierName,
		    	carrierTelephone: $scope.updateCarrierTelephone
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarrierList.get().then(function(data) {
					$scope.carrierList = data.carrierList;
				});
			} else {
				alert("删除承运商信息失败");
			}
		})
	}
	
	//点击查看车辆详细信息弹出车辆详细模态框
	$scope.checkCarInfo = function (car) {
		
		$scope.selectedCar = car;
		$("#carInfoModal").modal("toggle");
	}
	
	//更新车辆信息
	$scope.updateCar = function () {
		if (!authority()) {
			return ;
		}
		
		$http({
		    method:'post', 
		    url:'/logistics/car/updatecarinfo.do',
		    data:{
		    	carID: $scope.selectedCar.carID,
		    	weight: $scope.updateWeight,
		    	carType: $scope.updateCarType
		    }
		}).success(function(data){
			if (data.status === 200) {
				GetCarList.get().then(function(data) {
					$scope.carList = data.carList;
				});
			} else {
				alert("更新车辆信息失败");
			}
		})
	}
	
}])
.controller('OperationController', ['$scope', '$location', '$cookies', '$http', '$window', 'GetTaskList', function($scope, $location, $cookies, $http, $window, GetTaskList) {
	
	$scope.taskShow = false;
	
	//查询任务单
	$scope.searchTask = function () {
		$http({
		    method:'post', 
		    url:'/logistics/task/findtaskbyid.do',
		    data:{
		    	taskID: $scope.taskID
		    }
		}).success(function(data){
			if (data.status === 200) {
				if (data.task != undefined) {
					$scope.task = data.task;
					$scope.taskShow = true;
				} else {
					alert(data.info);
				}
			}
		})
	}
	
	GetTaskList.get().then(function(data) {
		$scope.taskList = data.taskList;
		$scope.allTaskList = data.taskList;
		$scope.taskCurIndex = 0;
		$scope.taskListNum = Math.ceil($scope.taskList.length/5);
		$scope.taskBtnNum = [];
		for (var i = 1; i <= $scope.taskListNum; ++i) {
			$scope.taskBtnNum.push(i);
		}
	});
	//点击进入响应页码界面
	$scope.jumpToTaskFn = function (index) {
		$scope.taskCurIndex = index;
	}
	
	//点击列表查看列表信息
	$scope.gotoTaskList = function () {
		$scope.taskShow = false;
		$scope.taskList = $scope.allTaskList;
		$scope.taskCurIndex = 0;
		$scope.taskListNum = Math.ceil($scope.taskList.length/5);
		$scope.taskBtnNum = [];
		for (var i = 1; i <= $scope.taskListNum; ++i) {
			$scope.taskBtnNum.push(i);
		}
	}
	
	//点击任务单查看详情
	$scope.checkTaskDetail = function (task) {
		
		$scope.task = task;
		$scope.taskShow = true;
	}
	
	//点击显示自营
	$scope.selfTask = function () {
		var arr = [];
		for (var taskIndex in $scope.allTaskList) {
			if ($scope.allTaskList[taskIndex].transType == 0) {
				arr.push($scope.allTaskList[taskIndex]);
			}
		}
		
		$scope.taskList = arr;
		$scope.taskCurIndex = 0;
		$scope.taskListNum = Math.ceil($scope.taskList.length/5);
		$scope.taskBtnNum = [];
		for (var i = 1; i <= $scope.taskListNum; ++i) {
			$scope.taskBtnNum.push(i);
		}
	}
	
	//点击显示承运商
	$scope.notSelfTask = function () {
		
		var arr = [];
		for (var taskIndex in $scope.allTaskList) {
			if ($scope.allTaskList[taskIndex].transType == 1) {
				arr.push($scope.allTaskList[taskIndex]);
			}
		}
		
		$scope.taskList = arr;
		$scope.taskCurIndex = 0;
		$scope.taskListNum = Math.ceil($scope.taskList.length/5);
		$scope.taskBtnNum = [];
		for (var i = 1; i <= $scope.taskListNum; ++i) {
			$scope.taskBtnNum.push(i);
		}
	}
	
}])
.controller('StatusticsController', ['$scope', '$location', '$cookies', '$http', '$window', 'GetChartData', 'GetDriverColumnData', 'GetCarColumnData', 'GetTypeColumnData', function($scope, $location, $cookies, $http, $window, GetChartData, GetDriverColumnData, GetCarColumnData, GetTypeColumnData) {
	
	GetChartData.get().then(function(data) {
		$scope.data = data;
		
		var chart = {
				type: 'spline'      
			}; 
		   var title = {
		      text: '近期收入折线图'   
		   };
		   var subtitle = {
		      text: '自有收入统计'
		   };
		   var xAxis = {
		      categories: $scope.data.incomeX
		   };
		   var yAxis = {
		      title: {
		         text: '收入'
		      },
		      labels: {
		         formatter: function () {
		            return this.value + '元';
		         }
		      },
		      lineWidth: 2
		   };
		   var tooltip = {
		      crosshairs: true,
		      shared: true
		   };
		   var plotOptions = {
		      spline: {
		         marker: {
		            radius: 4,
		            lineColor: '#666666',
		            lineWidth: 1
		         }
		      }
		   };
		   var series= [{
		         name: '自有',
		         marker: {
		            symbol: 'diamond'
		         },
		         data: $scope.data.incomeData
		      }
		   ];      
		      
		   var lineJson = {};
		   lineJson.chart = chart;
		   lineJson.title = title;
		   lineJson.subtitle = subtitle;
		   lineJson.tooltip = tooltip;
		   lineJson.xAxis = xAxis;
		   lineJson.yAxis = yAxis;  
		   lineJson.series = series;
		   lineJson.plotOptions = plotOptions;
		   $('#incomeLine').highcharts(lineJson);
		   
		   var chart = {
		       plotBackgroundColor: null,
		       plotBorderWidth: null,
		       plotShadow: false
		   };
		   var title = {
		      text: '各方收入饼图'
		   };      
		   var tooltip = {
		      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		   };
		   var plotOptions = {
		      pie: {
		         allowPointSelect: true,
		         cursor: 'pointer',
		         dataLabels: {
		            enabled: false           
		         },
		         showInLegend: true
		      }
		   };
		   var pieData = [];
		   for (var i = 0; i != $scope.data.incomeRatio.length; ++i) {
			   var temp = [];
			   temp.push($scope.data.incomeRatio[i].name,  $scope.data.incomeRatio[i].value);
			   pieData.push(temp);
		   }
		   
		   var series= [{
		      type: 'pie',
		      name: '收入',
		      data: 
		             pieData
		      
		   }];     
		      
		   var pieJson = {};   
		   pieJson.chart = chart; 
		   pieJson.title = title;     
		   pieJson.tooltip = tooltip;  
		   pieJson.series = series;
		   pieJson.plotOptions = plotOptions;
		   $('#pieChart').highcharts(pieJson);
	});
	
	GetDriverColumnData.get().then(function(data) {
		$scope.columnData = data;
		
		var chart = {
		      type: 'column'
		   };
		   var title = {
		      text: '驾驶员数据柱状图'   
		   };
		   var subtitle = {
		      text: '自有运输数据统计'  
		   };
		   var xAxis = {
		      categories: $scope.columnData.xName,
		      crosshair: true
		   };
		   var yAxis = {
		      min: 0,
		      title: {
		         text: '指标数据(Km，L，%)'
		      }      
		   };
		   var tooltip = {
		      headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		      pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		         '<td style="padding:0"><b>{point.y:.3f}</b></td></tr>',
		      footerFormat: '</table>',
		      shared: true,
		      useHTML: true
		   };
		   var plotOptions = {
		      column: {
		         pointPadding: 0.2,
		         borderWidth: 0
		      }
		   };  
		   var credits = {
		      enabled: false
		   };
		   
		   var series = $scope.columnData.series;     
		      
		   var json = {};   
		   json.chart = chart; 
		   json.title = title;   
		   json.subtitle = subtitle; 
		   json.tooltip = tooltip;
		   json.xAxis = xAxis;
		   json.yAxis = yAxis;  
		   json.series = series;
		   json.plotOptions = plotOptions;  
		   json.credits = credits;
		   $('#driverColumnDia').highcharts(json);
		
	});
	
	GetCarColumnData.get().then(function(data) {
		$scope.carColumnData = data;
		
		var chart = {
		      type: 'column'
		   };
		   var title = {
		      text: '车辆数据柱状图'
		   };
		   var subtitle = {
		      text: '自有运输数据统计'  
		   };
		   var xAxis = {
		      categories: $scope.carColumnData.xName,
		      crosshair: true
		   };
		   var yAxis = {
		      min: 0,
		      title: {
		         text: '指标数据(Km，L，%)'
		      }      
		   };
		   var tooltip = {
		      headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		      pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		         '<td style="padding:0"><b>{point.y:.3f}</b></td></tr>',
		      footerFormat: '</table>',
		      shared: true,
		      useHTML: true
		   };
		   var plotOptions = {
		      column: {
		         pointPadding: 0.2,
		         borderWidth: 0
		      }
		   };  
		   var credits = {
		      enabled: false
		   };
		   
		   var series = $scope.carColumnData.series;     
		      
		   var json = {};   
		   json.chart = chart; 
		   json.title = title;   
		   json.subtitle = subtitle; 
		   json.tooltip = tooltip;
		   json.xAxis = xAxis;
		   json.yAxis = yAxis;  
		   json.series = series;
		   json.plotOptions = plotOptions;  
		   json.credits = credits;
		   $('#carColumnDia').highcharts(json);
		
	});
	
	GetTypeColumnData.get().then(function(data) {
		$scope.typeColumnData = data;
		
		var chart = {
		      type: 'column'
		   };
		   var title = {
		      text: '车型数据柱状图'
		   };
		   var subtitle = {
		      text: '自有运输数据统计'  
		   };
		   var xAxis = {
		      categories: $scope.typeColumnData.xName,
		      crosshair: true
		   };
		   var yAxis = {
		      min: 0,
		      title: {
		         text: '指标数据(Km，L，%)'
		      }      
		   };
		   var tooltip = {
		      headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		      pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		         '<td style="padding:0"><b>{point.y:.3f}</b></td></tr>',
		      footerFormat: '</table>',
		      shared: true,
		      useHTML: true
		   };
		   var plotOptions = {
		      column: {
		         pointPadding: 0.2,
		         borderWidth: 0
		      }
		   };  
		   var credits = {
		      enabled: false
		   };
		   
		   var series = $scope.typeColumnData.series;     
		      
		   var json = {};
		   json.chart = chart;
		   json.title = title;
		   json.subtitle = subtitle;
		   json.tooltip = tooltip;
		   json.xAxis = xAxis;
		   json.yAxis = yAxis;
		   json.series = series;
		   json.plotOptions = plotOptions;
		   json.credits = credits;
		   $('#typeColumnDia').highcharts(json);
		
	});
	
}])
.controller('SettlementController', ['$scope', '$location', '$cookies', '$http', '$window', function($scope, $location, $cookies, $http, $window) {
	
	//操作用户权限管理
	function authority() {
		var user_id = $cookies.get("userID");
		if (user_id == undefined) {
			alert("您还未登录，请登录后操作");
			return false;
		}
		var userType = $cookies.get("userType");
		if (userType != "1" && userType != "2") {
			alert("您未有相应的权限，请联系管理员");
			return false; 
		}
		
		return true;
	}
	
	//获取未结算的任务单
	function getUnSettleTask() {

		$http({
		    method:'post', 
		    url:'/logistics/task/findstatustask.do',
		    data:{
		    	status: 0
		    }
		}).success(function(data){
			if (data.status === 200) {
				$scope.taskList = data.taskList;
			} else {
				alert("获取任务单失败");
			}
		})
		
	}
	getUnSettleTask();
	
	//点击结算弹出结算模态框
	$scope.taskDetailShow = function (task) {
		
		if (!authority()) {
			return ;
		}
		
		$scope.settleTaskID = task.taskID;
		if (task.transType == 0) {
			
			$scope.self = true;
		} else {
			$scope.self = false;
		}
		$scope.settleCarID = task.carID;
		$scope.settleDriverID = task.driverID;
		//显示模态框
		$("#taskModal").modal("toggle");
	}
	
	//点击结算此单进行结算
	$scope.settlementTask = function () {
		$http({
		    method:'post', 
		    url:'/logistics/task/updatetask.do',
		    data:{
		    	taskID: $scope.settleTaskID,
		    	miles: $scope.miles,
		    	oil: $scope.oil,
		    	transCost: $scope.transCost,
		    }
		}).success(function(data){
			if (data.status === 200) {
				getUnSettleTask();
				
			} else {
			}
		})
		
		//将司机空闲
		$http({
		    method:'post', 
		    url:'/logistics/user/updateuser.do',
		    data:{
		    	userID: $scope.settleDriverID,
		    	status: 1
		    }
		}).success(function(data){
		})
		
		//将货车空闲
		$http({
		    method:'post', 
		    url:'/logistics/car/updatecar.do',
		    data:{
		    	carID: $scope.settleCarID,
		    	status: 1
		    }
		}).success(function(data){
		})
	}
}])
.controller('TaskController', ['$scope', '$location', '$cookies', '$http', '$window', function($scope, $location, $cookies, $http, $window) {
	
	//操作用户权限管理
	function authority() {
		var user_id = $cookies.get("userID");
		if (user_id == undefined) {
			alert("您还未登录，请登录后操作");
			return false;
		}
		var userType = $cookies.get("userType");
		if (userType != "1" && userType != "2") {
			alert("您未有相应的权限，请联系管理员");
			return false; 
		}
		
		return true;
	}
	
	//是否显示驾驶人
	$scope.driverShow = true;
	
	
	//点击输入框显示模态框，查询所有车辆信息
	$scope.showCarList = function () {
		
		$http({
		    method:'post', 
		    url:'/logistics/car/getfreecar.do',
		    data:{
		    	status: 1
		    }
		}).success(function(data){

			if (data.status === 200) {
				$scope.carList = data.carList;
			}
		})
		
		$("#carModal").modal("toggle");
	}
	
	//选择车辆
	$scope.selectCar = function (car) {
		
		$scope.carInfo = car.carNumber + " " + car.carType + " " + car.belongType;
		$scope.selectedCar = car;
		if (car.belongType == "承运商") {
			$scope.driverShow = false;
		} else {
			$scope.driverShow = true;
		}
	}
	
	//显示用户列表模态框
	$scope.showUserList = function () {
		
		$http({
			method:'post', 
			url:'/logistics/user/findstatususer.do',
			data:{
				status: 1
			}
		}).success(function(data){
			
			if (data.status === 200) {
				$scope.userList = data.userList;
			}
		})
		
		$("#userModal").modal("toggle");
	}
	
	//选择驾驶人信息
	$scope.selectUser = function (user) {
		$scope.driverName = user.username + " " + user.telephone;
		$scope.selectedUser = user;
	}

	//提交任务单
	$scope.createTask = function () {
		
		if (!authority()) {
			return ;
		}
		
		if ($scope.driverShow) {
			//更新驾驶员信息
			$http({
				method:'post', 
				url:'/logistics/user/updateuser.do',
				data:{
					userID: $scope.selectedUser.userID,
					status: 0
				}
			}).success(function(data){
			})
		} else {
			$scope.selectedUser = null;
		}
		
		//更新车辆信息
		$http({
			method:'post', 
			url:'/logistics/car/updatecar.do',
			data:{
				carID: $scope.selectedCar.carID,
				status: 0
			}
		}).success(function(data){
		})
		
		//保存任务单信息
		$http({
			method:'post', 
			url:'/logistics/task/savetask.do',
			data:{
				sendName: $scope.sendName,
				sendTelephone: $scope.sendTelephone,
				sendAddress: $scope.sendProvince+$scope.sendCity+$scope.sendDistrict+$scope.sendDetailAddress,
				receiveName: $scope.reciveName,
				receiveTelephone: $scope.reciveTelephone,
				receiveAddress: $scope.reciveProvince+$scope.reciveCity+$scope.reciveDistrict+$scope.receiveDetailAddress,
				goodsType: $scope.goodsType,
				weight: $scope.weight,
				carID: $scope.selectedCar.carID,
				driverName: ($scope.selectedUser == undefined || $scope.selectedUser == null)? $scope.selectedCar.carrierName : $scope.selectedUser.username,
				driverID: ($scope.selectedUser == undefined || $scope.selectedUser == null)? $scope.selectedCar.carrierID : $scope.selectedUser.userID,
				transType: ($scope.selectedUser == undefined || $scope.selectedUser == null)? 1:0,
				realCost: $scope.cost
			}
		}).success(function(data){
			if (data.status === 200) {
				$scope.sendName= "";
				$scope.sendTelephone = "";
				$scope.reciveName = "";
				$scope.reciveTelephone = "";
				$scope.goodsType = "";
				$scope.weight =　"";
				$scope.cost =　"";
				$scope.selectedCar = null;
				$scope.selectedCar = null;
			}
		})
	}
}]);