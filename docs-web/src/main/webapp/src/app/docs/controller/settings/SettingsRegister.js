/**
 * Settings register application page controller.
 */
angular.module('docs').controller('SettingsRegister', function(Restangular ,$scope, $state, $dialog, $translate){
    $scope.loadUsers = function(){
        Restangular.one("registerUser/list").get().then(function(data) {
            $scope.users = data.register_users;
        });
    };

    $scope.loadUsers();

    $scope.approve = function(user) {
        user.status = 1;
        user.operated_time = Date.now();
        $dialog.messageBox("Approved", "Approved", [
            {
                result: 'ok',
                label: "OK",
                cssClass: 'btn-primary'
            }
        ]);
    }
    $scope.reject = function(user) {
        user.status = 2;
        user.operated_time = Date.now();
        $dialog.messageBox("Rejected", "Rejected", [
            {
                result: 'ok',
                label: "OK",
                cssClass: 'btn-primary'
            }
        ]);
    };
});