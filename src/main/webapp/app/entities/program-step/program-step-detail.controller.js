(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramStepDetailController', ProgramStepDetailController);

    ProgramStepDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProgramStep', 'ProgramLevel', 'ProgramHistory'];

    function ProgramStepDetailController($scope, $rootScope, $stateParams, previousState, entity, ProgramStep, ProgramLevel, ProgramHistory) {
        var vm = this;

        vm.programStep = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:programStepUpdate', function(event, result) {
            vm.programStep = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
