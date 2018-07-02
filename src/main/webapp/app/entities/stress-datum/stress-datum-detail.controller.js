(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('StressDatumDetailController', StressDatumDetailController);

    StressDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StressDatum', 'User'];

    function StressDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, StressDatum, User) {
        var vm = this;

        vm.stressDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:stressDatumUpdate', function(event, result) {
            vm.stressDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
