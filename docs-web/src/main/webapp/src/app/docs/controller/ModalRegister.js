
'use strict';

/**
 * Modal register controller.
 */

angular.module('docs').controller('ModalRegister', function ($scope, $uibModalInstance) {
    // $scope.username = '';
    $scope.close = function(registerUser) {
        $uibModalInstance.close(registerUser);
    }
});