(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveActionDetailController', IncentiveActionDetailController);

    IncentiveActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'IncentiveAction', 'Trigger', 'Incentive'];

    function IncentiveActionDetailController($scope, $rootScope, $stateParams, previousState, entity, IncentiveAction, Trigger, Incentive) {
        var vm = this;

        vm.incentiveAction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:incentiveActionUpdate', function(event, result) {
            vm.incentiveAction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
