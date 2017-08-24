(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TriggerDetailController', TriggerDetailController);

    TriggerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trigger', 'NotifcationTrigger', 'WellnessItem', 'IncentiveAction'];

    function TriggerDetailController($scope, $rootScope, $stateParams, previousState, entity, Trigger, NotifcationTrigger, WellnessItem, IncentiveAction) {
        var vm = this;

        vm.trigger = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:triggerUpdate', function(event, result) {
            vm.trigger = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
