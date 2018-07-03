(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PerformanceDatumDetailController', PerformanceDatumDetailController);

    PerformanceDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PerformanceDatum', 'User'];

    function PerformanceDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, PerformanceDatum, User) {
        var vm = this;

        vm.performanceDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:performanceDatumUpdate', function(event, result) {
            vm.performanceDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
