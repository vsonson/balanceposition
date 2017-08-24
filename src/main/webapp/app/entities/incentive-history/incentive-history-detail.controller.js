(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveHistoryDetailController', IncentiveHistoryDetailController);

    IncentiveHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'IncentiveHistory', 'UserInfo'];

    function IncentiveHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, IncentiveHistory, UserInfo) {
        var vm = this;

        vm.incentiveHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:incentiveHistoryUpdate', function(event, result) {
            vm.incentiveHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
