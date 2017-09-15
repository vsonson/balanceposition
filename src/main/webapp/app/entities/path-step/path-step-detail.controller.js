(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathStepDetailController', PathStepDetailController);

    PathStepDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PathStep', 'PathAction', 'PathWay'];

    function PathStepDetailController($scope, $rootScope, $stateParams, previousState, entity, PathStep, PathAction, PathWay) {
        var vm = this;

        vm.pathStep = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:pathStepUpdate', function(event, result) {
            vm.pathStep = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
