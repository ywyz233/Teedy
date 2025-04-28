/**
 * User Activity Controller
 */
angular.module('docs').controller('SettingsUserActivity', function(Restangular ,$scope, $state, $dialog, $translate, $rootScope){

  $scope.init_func = function() {
    $scope.loginActivityScale = "monthly";
    $scope.registerActivityScale = "monthly";
    $scope.updateLoginActivityStartDatePicker();
    $scope.updateLoginActivityEndDatePicker();
    $scope.updateRegisterActivityStartDatePicker();
    $scope.updateRegisterActivityEndDatePicker();
  };

  $scope.loadLoginActivity = function(reload){
    if(reload){
      Restangular.one("user-activity-log/login-stat").get({
        scale: $scope.loginActivityScale,
        startTime: $scope.loginActivityStartDate,
        endTime: $scope.loginActivityEndDate
      }).then(
        function(data){
          $scope.loginActivityLabels = data.logs.map(item => item.date);
          $scope.loginActivityValues = data.logs.map(item => item.cnt);
          $scope.updateLoginActivityChart($scope.loginActivityLabels, $scope.loginActivityValues);
        }
      );
    } else {
      $scope.updateLoginActivityChart($scope.loginActivityLabels, $scope.loginActivityValues);
    }
  };

  $scope.updateLoginActivityChart = function(labels, counts) {
    if($scope.loginActivityChartInstance) {
      $scope.loginActivityChartInstance.destroy();
    }
    let y_max = Math.ceil(1.2*Math.max(...counts));
    if(y_max === 0){
      y_max = 1;
    }
    const y_step = Math.max(1, Math.floor(y_max / 10));
    const ctx = document.getElementById('loginActivityChart');
    $scope.loginActivityChartInstance =
      new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: $translate.instant('settings.user_activity.login_activity_ylabel'),
          data: counts,
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            min: 0,
            max: y_max,
            ticks: {
              stepSize: y_step
            }
          }
        },
        plugins:{
          title:{
            text: $translate.instant('settings.user_activity.login_activity_title'),
            display: true,
            font: {
              size: 20
            }
          }
        }
      }
    });
  };

  $scope.updateLoginActivityStartDatePicker = function(){
    $('#loginActivityStartDate').datepicker('destroy');
    let format = 'yyyy-mm-dd';
    let date = new Date();
    let defaultVal = '1970-01-01';
    let minViewMode;
    if($scope.loginActivityScale == 'daily'){
      format = 'yyyy-mm-dd';
      date.setDate(date.getDate() - 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
      minViewMode = 'days';
    } else if($scope.loginActivityScale === 'monthly'){
      format = 'yyyy-mm';
      date.setMonth(date.getMonth() - 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}`;
      minViewMode = 'months';
    } else if($scope.loginActivityScale === 'annually') {
      format = 'yyyy';
      date.setFullYear(date.getFullYear() - 1);
      defaultVal = `${date.getFullYear()}`;
      minViewMode = 'years';
    }
    $scope.loginActivityStartDate = defaultVal;
    $('#loginActivityStartDate').datepicker({
      format: format,
      minViewMode: minViewMode,
      autoclose: true,
      todayHighlight: true
    });
    $('#loginActivityStartDate').datepicker('setDate', defaultVal);
  };

  $scope.updateLoginActivityEndDatePicker = function(){
    $('#loginActivityEndDate').datepicker('destroy');
    let format = 'yyyy-mm-dd';
    let date = new Date();
    let defaultVal = '1970-01-01';
    let minViewMode;
    if($scope.loginActivityScale == 'daily'){
      format = 'yyyy-mm-dd';
      date.setDate(date.getDate() + 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
      minViewMode = 'days';
    } else if($scope.loginActivityScale === 'monthly'){
      format = 'yyyy-mm';
      date.setMonth(date.getMonth() + 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}`;
      minViewMode = 'months';
    } else if($scope.loginActivityScale === 'annually') {
      format = 'yyyy';
      date.setFullYear(date.getFullYear() + 1);
      defaultVal = `${date.getFullYear()}`;
      minViewMode = 'years';
    }
    $scope.loginActivityEndDate = defaultVal;
    $('#loginActivityEndDate').datepicker({
      format: format,
      minViewMode: minViewMode,
      autoclose: true,
      todayHighlight: true
    });
    $('#loginActivityEndDate').datepicker('setDate', defaultVal);
  };




  $scope.loadRegisterActivity = function(reload){
    if(reload){
      Restangular.one("user-activity-log/register-stat").get({
        scale: $scope.registerActivityScale,
        startTime: $scope.registerActivityStartDate,
        endTime: $scope.registerActivityEndDate
      }).then(
        function(data){
          $scope.registerActivityLabels = data.logs.map(item => item.date);
          $scope.registerActivityValues = data.logs.map(item => item.cnt);
          $scope.updateRegisterActivityChart($scope.registerActivityLabels, $scope.registerActivityValues);
        }
      );
    } else {
      $scope.updateRegisterActivityChart($scope.registerActivityLabels, $scope.registerActivityValues);
    }
  };

  $scope.updateRegisterActivityChart = function(labels, counts) {
    if($scope.registerActivityChartInstance) {
      $scope.registerActivityChartInstance.destroy();
    }
    let y_max = Math.ceil(1.2*Math.max(...counts));
    if(y_max === 0){
      y_max = 1;
    }
    const y_step = Math.max(1, Math.floor(y_max / 10));
    const ctx = document.getElementById('registerActivityChart');
    $scope.registerActivityChartInstance =
      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: $translate.instant('settings.user_activity.register_activity_ylabel'),
            data: counts,
            borderWidth: 1
          }]
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
              min: 0,
              max: y_max,
              ticks: {
                stepSize: y_step
              }
            }
          },
          plugins:{
            title:{
              text: $translate.instant('settings.user_activity.register_activity_title'),
              display: true,
              font: {
                size: 20
              }
            }
          }
        }
      });
  };

  $scope.updateRegisterActivityStartDatePicker = function(){
    $('#registerActivityStartDate').datepicker('destroy');
    let format = 'yyyy-mm-dd';
    let date = new Date();
    let defaultVal = '1970-01-01';
    let minViewMode;
    if($scope.registerActivityScale == 'daily'){
      format = 'yyyy-mm-dd';
      date.setDate(date.getDate() - 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
      minViewMode = 'days';
    } else if($scope.registerActivityScale === 'monthly'){
      format = 'yyyy-mm';
      date.setMonth(date.getMonth() - 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}`;
      minViewMode = 'months';
    } else if($scope.registerActivityScale === 'annually') {
      format = 'yyyy';
      date.setFullYear(date.getFullYear() - 1);
      defaultVal = `${date.getFullYear()}`;
      minViewMode = 'years';
    }
    $scope.registerActivityStartDate = defaultVal;
    $('#registerActivityStartDate').datepicker({
      format: format,
      minViewMode: minViewMode,
      autoclose: true,
      todayHighlight: true
    });
    $('#registerActivityStartDate').datepicker('setDate', defaultVal);
  };

  $scope.updateRegisterActivityEndDatePicker = function(){
    $('#registerActivityEndDate').datepicker('destroy');
    let format = 'yyyy-mm-dd';
    let date = new Date();
    let defaultVal = '1970-01-01';
    let minViewMode;
    if($scope.registerActivityScale == 'daily'){
      format = 'yyyy-mm-dd';
      date.setDate(date.getDate() + 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
      minViewMode = 'days';
    } else if($scope.registerActivityScale === 'monthly'){
      format = 'yyyy-mm';
      date.setMonth(date.getMonth() + 1);
      defaultVal = `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}`;
      minViewMode = 'months';
    } else if($scope.registerActivityScale === 'annually') {
      format = 'yyyy';
      date.setFullYear(date.getFullYear() + 1);
      defaultVal = `${date.getFullYear()}`;
      minViewMode = 'years';
    }
    $scope.registerActivityEndDate = defaultVal;
    $('#registerActivityEndDate').datepicker({
      format: format,
      minViewMode: minViewMode,
      autoclose: true,
      todayHighlight: true
    });
    $('#registerActivityEndDate').datepicker('setDate', defaultVal);
  };

  $scope.init_func();
  $scope.loadLoginActivity(true);
  $scope.loadRegisterActivity(true);

  $scope.$watch('loginActivityScale', function(newValue, oldValue){
    $scope.updateLoginActivityStartDatePicker();
    $scope.updateLoginActivityEndDatePicker();
    $scope.loadLoginActivity(true);
  });

  $scope.$watch('loginActivityStartDate', function(newValue, oldValue){
    $scope.loadLoginActivity(true);
  });

  $scope.$watch('loginActivityEndDate', function(newValue, oldValue){
    $scope.loadLoginActivity(true);
  });

  $scope.$watch('registerActivityScale', function(newValue, oldValue){
    $scope.updateRegisterActivityStartDatePicker();
    $scope.updateRegisterActivityEndDatePicker();
    $scope.loadRegisterActivity(true);
  });

  $scope.$watch('registerActivityStartDate', function(newValue, oldValue){
    $scope.loadRegisterActivity(true);
  });

  $scope.$watch('registerActivityEndDate', function(newValue, oldValue){
    $scope.loadRegisterActivity(true);
  });

  $rootScope.$on('$translateChangeSuccess', function(event, data){
    $scope.loadLoginActivity(true);
    $scope.loadRegisterActivity(true);
  });

});