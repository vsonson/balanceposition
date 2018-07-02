(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('FocusDatumDetailController', FocusDatumDetailController);

    FocusDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FocusDatum', 'User'];

    function FocusDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, FocusDatum, User) {
        var vm = this;

        vm.focusDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:focusDatumUpdate', function(event, result) {
            vm.focusDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
