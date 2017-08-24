(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('AlertDetailController', AlertDetailController);

    AlertDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Alert', 'UserNotification'];

    function AlertDetailController($scope, $rootScope, $stateParams, previousState, entity, Alert, UserNotification) {
        var vm = this;

        vm.alert = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:alertUpdate', function(event, result) {
            vm.alert = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
