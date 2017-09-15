(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramHistoryDetailController', ProgramHistoryDetailController);

    ProgramHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProgramHistory', 'ProgramLevel', 'ProgramStep', 'UserInfo'];

    function ProgramHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ProgramHistory, ProgramLevel, ProgramStep, UserInfo) {
        var vm = this;

        vm.programHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:programHistoryUpdate', function(event, result) {
            vm.programHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
