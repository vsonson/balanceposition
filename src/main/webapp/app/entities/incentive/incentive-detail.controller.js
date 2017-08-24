(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveDetailController', IncentiveDetailController);

    IncentiveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Incentive', 'IncentiveAction'];

    function IncentiveDetailController($scope, $rootScope, $stateParams, previousState, entity, Incentive, IncentiveAction) {
        var vm = this;

        vm.incentive = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:incentiveUpdate', function(event, result) {
            vm.incentive = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
