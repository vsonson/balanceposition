(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramLevelDetailController', ProgramLevelDetailController);

    ProgramLevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProgramLevel', 'Program', 'ProgramStep', 'ProgramHistory'];

    function ProgramLevelDetailController($scope, $rootScope, $stateParams, previousState, entity, ProgramLevel, Program, ProgramStep, ProgramHistory) {
        var vm = this;

        vm.programLevel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:programLevelUpdate', function(event, result) {
            vm.programLevel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
