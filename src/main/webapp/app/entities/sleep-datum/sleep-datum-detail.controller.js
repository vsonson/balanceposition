(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('SleepDatumDetailController', SleepDatumDetailController);

    SleepDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SleepDatum', 'User'];

    function SleepDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, SleepDatum, User) {
        var vm = this;

        vm.sleepDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:sleepDatumUpdate', function(event, result) {
            vm.sleepDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
