(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserNotificationDetailController', UserNotificationDetailController);

    UserNotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserNotification', 'PathWay', 'Alert', 'UserInfo'];

    function UserNotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, UserNotification, PathWay, Alert, UserInfo) {
        var vm = this;

        vm.userNotification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:userNotificationUpdate', function(event, result) {
            vm.userNotification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
