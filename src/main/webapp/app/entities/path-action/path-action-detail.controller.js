(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathActionDetailController', PathActionDetailController);

    PathActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PathAction', 'PathStep', 'TrackMetric', 'Program'];

    function PathActionDetailController($scope, $rootScope, $stateParams, previousState, entity, PathAction, PathStep, TrackMetric, Program) {
        var vm = this;

        vm.pathAction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:pathActionUpdate', function(event, result) {
            vm.pathAction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
