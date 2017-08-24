(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NotifcationTriggerDetailController', NotifcationTriggerDetailController);

    NotifcationTriggerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NotifcationTrigger', 'Trigger'];

    function NotifcationTriggerDetailController($scope, $rootScope, $stateParams, previousState, entity, NotifcationTrigger, Trigger) {
        var vm = this;

        vm.notifcationTrigger = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:notifcationTriggerUpdate', function(event, result) {
            vm.notifcationTrigger = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
